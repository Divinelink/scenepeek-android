import com.divinelink.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.getByType
import org.jetbrains.compose.ComposeExtension
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

class ComposeMultiplatformPlugin : Plugin<Project> {
  override fun apply(target: Project) = with(target) {
    with(pluginManager) {
      apply(libs.findPlugin("compose").get().get().pluginId)
      apply(libs.findPlugin("compose-multiplatform").get().get().pluginId)
    }

    val composeDeps = extensions.getByType<ComposeExtension>().dependencies

    extensions.configure<KotlinMultiplatformExtension> {
      sourceSets.apply {
        all {
          languageSettings.optIn("androidx.compose.ui.test.ExperimentalTestApi")
          languageSettings.optIn("kotlinx.coroutines.ExperimentalCoroutinesApi")
          languageSettings.optIn("kotlin.time.ExperimentalTime")
          languageSettings.optIn("androidx.compose.animation.ExperimentalSharedTransitionApi")
        }

        commonMain.dependencies {
          implementation(composeDeps.runtime)
          implementation(composeDeps.foundation)
          implementation(composeDeps.material3)
          implementation(composeDeps.material3AdaptiveNavigationSuite)
          implementation(composeDeps.materialIconsExtended)
          implementation(composeDeps.components.resources)
          implementation(composeDeps.components.uiToolingPreview)

          implementation(libs.findLibrary("compose-backhandler").get())
          implementation(libs.findLibrary("compose-navigation").get())
        }

        androidMain.dependencies {
          implementation(composeDeps.uiTooling)
        }
      }
    }
  }
}
