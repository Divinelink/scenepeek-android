val libs = extensions.getByType<VersionCatalogsExtension>().named("libs")
val ktlint by configurations.creating

dependencies {
  ktlint(libs.findLibrary("ktlint-cli").get())
}

tasks.register<JavaExec>("ktlintCheck") {
  group = "lint"
  description = "Check Kotlin code style."
  classpath = ktlint
  mainClass.set("com.pinterest.ktlint.Main")
  // see https://pinterest.github.io/ktlint/install/cli/#command-line-usage for more information
  args(
    "src/**/*.kt",
    "**.kts",
    "!**/build/**",
    "!**/generated/**",
    "!**/build/generated/compose/resourceGenerator/**"
  )
}

tasks.register<JavaExec>("ktlintFormat") {
  group = "lint"
  description = "Fix Kotlin code style deviations."
  classpath = ktlint
  mainClass.set("com.pinterest.ktlint.Main")
  jvmArgs("--add-opens=java.base/java.lang=ALL-UNNAMED")
  // see https://pinterest.github.io/ktlint/install/cli/#command-line-usage for more information
  args(
    "-F",
    "src/**/*.kt",
    "**.kts",
    "!**/build/**",
    "!**/generated/**",
    "!**/build/generated/compose/resourceGenerator/**"
  )
}
