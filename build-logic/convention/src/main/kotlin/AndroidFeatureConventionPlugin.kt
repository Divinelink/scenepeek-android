import com.android.build.gradle.LibraryExtension
import com.divinelink.configureKotlinAndroid
import com.divinelink.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure

class AndroidFeatureConventionPlugin : Plugin<Project> {
  override fun apply(target: Project) {
    with(target) {
      with(pluginManager) {
        apply("com.android.library")
        apply("org.jetbrains.kotlin.android")
        apply("org.jetbrains.kotlinx.kover")
      }

      extensions.configure<LibraryExtension> {
        configureKotlinAndroid(this)
        defaultConfig.targetSdk = libs.versions.target.sdk.get().toInt()
        testOptions.animationsDisabled = true
      }
    }
  }
}
