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
      apply("org.jetbrains.kotlin.multiplatform")
      apply("org.jetbrains.kotlin.plugin.serialization")
    }

    extensions.configure<KotlinMultiplatformExtension>(::configureKotlinMultiplatform)
    extensions.configure<ApplicationExtension> {
      configureKotlinAndroid(this)
      defaultConfig.targetSdk = libs.versions.target.sdk.get().toInt()
      testOptions.animationsDisabled = true
    }
  }
}
