import org.w3c.dom.Element
import systems.danger.kotlin.*
import java.io.File
import java.util.Locale
import javax.xml.parsers.DocumentBuilderFactory

val koverReport = "app/build/all-projects-reports/kover.xml"
val minLineCoverage = 60.0

data class FileCoverage(
  val name: String,
  val covered: Int,
  val missed: Int,
) {
  val total get() = covered + missed
  val percent get() = if (total == 0) 0.0 else covered * 100.0 / total
  val isEmpty get() = total == 0
}

data class ReportData(
  val total: FileCoverage,
  val byFile: Map<String, FileCoverage>, // key: "com/example/Foo.kt" style path
)

fun parseReport(path: String): ReportData? {
  val file = File(path)
  if (!file.exists()) return null

  val factory = DocumentBuilderFactory.newInstance().apply {
    setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false)
    isValidating = false
    isNamespaceAware = false
  }
  val doc = factory.newDocumentBuilder().parse(file)
  val report = doc.documentElement

  // --- total: direct <counter type="LINE"> children of <report> ---
  var totalCovered = 0
  var totalMissed = 0
  val children = report.childNodes
  for (i in 0 until children.length) {
    val node = children.item(i) as? Element ?: continue
    if (node.tagName == "counter" && node.getAttribute("type") == "LINE") {
      totalCovered = node.getAttribute("covered").toInt()
      totalMissed = node.getAttribute("missed").toInt()
      break
    }
  }

  val byFile = mutableMapOf<String, FileCoverage>()
  val packages = report.getElementsByTagName("package")
  for (p in 0 until packages.length) {
    val pkg = packages.item(p) as? Element ?: continue
    val pkgName = pkg.getAttribute("name")

    val sourceFiles = pkg.getElementsByTagName("sourcefile")
    for (s in 0 until sourceFiles.length) {
      val sf = sourceFiles.item(s) as? Element ?: continue
      val fileName = sf.getAttribute("name")
      val filePath = "$pkgName/$fileName"

      val counters = sf.getElementsByTagName("counter")
      for (c in 0 until counters.length) {
        val counter = counters.item(c) as? Element ?: continue
        if (counter.getAttribute("type") == "LINE") {
          val covered = counter.getAttribute("covered").toInt()
          val missed = counter.getAttribute("missed").toInt()
          byFile[filePath] = FileCoverage(filePath, covered, missed)
          break
        }
      }
    }
  }

  return ReportData(
    total = FileCoverage("total", totalCovered, totalMissed),
    byFile = byFile,
  )
}

fun String.toPackagePath(): String =
  replace("\\", "/")
    .split("/")
    .dropWhile { it != "kotlin" && it != "java" }
    .drop(1)
    .joinToString("/")

danger(args) {
  val report = parseReport(koverReport)

  if (report == null) {
    warn("No Kover report at `$koverReport` — did `./gradlew koverXmlReport` run?")
    return@danger
  }

  onGitHub {
    if (pullRequest.title.contains("WIP", ignoreCase = true)) {
      warn("PR is marked Work in Progress.")
    }
    val net = (pullRequest.additions ?: 0) - (pullRequest.deletions ?: 0)
    if (net > 500) {
      warn("Large PR (+$net lines net). Consider splitting it for easier review.")
    }
  }

  // total coverage summary
  val total = report.total
  val pct = total.percent
  val ok = pct >= minLineCoverage
  val pctStr = "%.2f".format(Locale.US, pct)

  markdown(
    """
        ## ${if (ok) "✅" else "⚠️"} Code coverage: $pctStr%

        | Metric | Covered | Missed | Total | Coverage |
        |--------|--------:|-------:|------:|---------:|
        | Lines  | ${total.covered} | ${total.missed} | ${total.total} | $pctStr% |

        <sub>Line coverage from unit tests (Kover). Threshold: ${minLineCoverage.toInt()}%.</sub>
        """.trimIndent(),
  )

  if (!ok) fail("Line coverage $pctStr% is below the required ${minLineCoverage.toInt()}%.")

  // changed files coverage
  val changedSourceFiles = (git.modifiedFiles + git.createdFiles)
    .filter { it.endsWith(".kt") }

  if (changedSourceFiles.isEmpty()) return@danger

  // match each changed file against the XML using the package path
  data class FileResult(val gitPath: String, val coverage: FileCoverage?)

  fun String.fileName() = substringAfterLast("/")

  val results = changedSourceFiles.map { gitPath ->
    val pkgPath = gitPath.toPackagePath()
    FileResult(gitPath, report.byFile[pkgPath])
  }

  val uncovered =
    results.filter { it.coverage != null && it.coverage.missed > 0 && it.coverage.percent < 100.0 }
  val notInReport = results.filter { it.coverage == null }
  val fullyCovered = results.filter { it.coverage != null && it.coverage.percent == 100.0 }

  if (uncovered.isEmpty() && notInReport.isEmpty()) return@danger

  val sb = StringBuilder()
  sb.appendLine("## 📂 Changed files coverage\n")
  sb.appendLine("| File | Covered | Missed | Coverage |")
  sb.appendLine("|------|--------:|-------:|---------:|")

  for (r in fullyCovered) {
    val c = r.coverage!!
    sb.appendLine("| [`${r.gitPath.fileName()}`](${r.gitPath}) | ${c.covered} | 0 | ✅ 100% |")
  }
  for (r in uncovered.sortedBy { it.coverage!!.percent }) {
    val c = r.coverage!!
    val filePct = "%.1f".format(Locale.US, c.percent)
    val emoji = if (c.percent < 50.0) "🔴" else "🟡"
    sb.appendLine("| [`${r.gitPath.fileName()}`](${r.gitPath}) | ${c.covered} | ${c.missed} | $emoji $filePct% |")
  }
  for (r in notInReport) {
    sb.appendLine("| [`${r.gitPath.fileName()}`](${r.gitPath}) | — | — | ⚪ not in report |")
  }

  markdown(sb.toString())

  if (uncovered.isNotEmpty()) {
    warn("${uncovered.size} changed file(s) have less than 100% line coverage.")
  }
}
