import com.android.build.api.dsl.ApplicationExtension
import com.divinelink.configureAndroidCompose
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.getByType

class AndroidApplicationComposeConventionPlugin : Plugin<Project> {
  override fun apply(target: Project) {
    with(target) {
      with(pluginManager) {
        apply("com.android.application")
        apply("org.jetbrains.kotlinx.kover")
        apply("org.jetbrains.kotlin.plugin.compose")
      }

      val extension = extensions.getByType<ApplicationExtension>()
      configureAndroidCompose(extension)
    }
  }
}
