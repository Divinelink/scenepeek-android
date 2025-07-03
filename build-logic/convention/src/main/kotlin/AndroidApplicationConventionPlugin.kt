import com.android.build.api.dsl.ApplicationExtension
import com.divinelink.configureKotlinAndroid
import com.divinelink.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

class AndroidApplicationConventionPlugin : Plugin<Project> {
  override fun apply(target: Project) {
    with(target) {
      with(pluginManager) {
        apply("com.android.application")
        apply("org.jetbrains.kotlin.android")
        apply("org.jetbrains.kotlinx.kover")
      }

      extensions.configure<ApplicationExtension> {
        configureKotlinAndroid(this)
        defaultConfig.targetSdk = libs.findVersion("target-sdk").get().requiredVersion.toInt()
        @Suppress("UnstableApiUsage")
        testOptions.animationsDisabled = true
      }
    }
  }
}
