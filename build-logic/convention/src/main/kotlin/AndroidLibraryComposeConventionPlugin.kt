import com.android.build.gradle.LibraryExtension
import com.divinelink.configureAndroidCompose
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.getByType

class AndroidLibraryComposeConventionPlugin : Plugin<Project> {
  override fun apply(target: Project) {
    with(target) {
      with(pluginManager) {
        apply("com.android.library")
        apply("org.jetbrains.kotlinx.kover")
        apply("org.jetbrains.kotlin.plugin.compose")
        apply("com.android.compose.screenshot")
      }

      val extension = extensions.getByType<LibraryExtension>()
      configureAndroidCompose(extension)
    }
  }
}
