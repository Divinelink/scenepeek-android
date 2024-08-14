import com.divinelink.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

class AndroidKoinConventionPlugin : Plugin<Project> {
  override fun apply(target: Project) {
    with(target) {
      dependencies {
        "implementation"(libs.findLibrary("koin.android").get())
      }
    }
  }
}
