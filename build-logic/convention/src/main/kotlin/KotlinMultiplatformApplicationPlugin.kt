import com.android.build.api.dsl.ApplicationExtension
import com.divinelink.configureKotlinAndroid
import com.divinelink.configureKotlinMultiplatform
import com.divinelink.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

class KotlinMultiplatformApplicationPlugin : Plugin<Project> {

  override fun apply(target: Project): Unit = with(target) {
    pluginManager.apply {
      apply("com.android.application")
      apply(libs.findPlugin("kotlin.multiplatform").get().get().pluginId)
      apply(libs.findPlugin("kotlin.cocoapods").get().get().pluginId)
      apply(libs.findPlugin("kotlin.serialization").get().get().pluginId)
    }

    extensions.configure<KotlinMultiplatformExtension>(::configureKotlinMultiplatform)
    extensions.configure<ApplicationExtension> {
      configureKotlinAndroid(this)
      defaultConfig.targetSdk = libs.findVersion("target-sdk").get().requiredVersion.toInt()
      testOptions.animationsDisabled = true
    }
  }
}
