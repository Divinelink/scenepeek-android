import com.android.build.gradle.LibraryExtension
import com.divinelink.buildScripts
import com.divinelink.configureKotlinAndroid
import com.divinelink.configureKotlinMultiplatform
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

class KotlinMultiplatformPlugin : Plugin<Project> {

  override fun apply(target: Project): Unit = with(target) {
    pluginManager.apply {
      apply("com.android.library")
      apply("org.jetbrains.kotlin.multiplatform")
      apply("org.jetbrains.kotlin.plugin.serialization")
      apply("org.jetbrains.kotlinx.kover")
    }

    extensions.configure<KotlinMultiplatformExtension>(::configureKotlinMultiplatform)
    extensions.configure<LibraryExtension>(::configureKotlinAndroid)
    apply("$buildScripts/ktlint.gradle.kts")
    apply("$buildScripts/detekt.gradle")
    apply("$buildScripts/kover.gradle")
  }
}
