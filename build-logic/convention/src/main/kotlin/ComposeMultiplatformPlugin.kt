import com.divinelink.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.jetbrains.kotlin.gradle.dsl.KotlinMultiplatformExtension

class ComposeMultiplatformPlugin : Plugin<Project> {
  override fun apply(target: Project) = with(target) {
    pluginManager.apply {
      apply("org.jetbrains.compose")
      apply("org.jetbrains.kotlin.plugin.compose")
    }

    extensions.configure<KotlinMultiplatformExtension> {
      sourceSets.apply {
        all {
          languageSettings.optIn("androidx.compose.ui.test.ExperimentalTestApi")
          languageSettings.optIn("kotlinx.coroutines.ExperimentalCoroutinesApi")
          languageSettings.optIn("kotlin.time.ExperimentalTime")
          languageSettings.optIn("androidx.compose.animation.ExperimentalSharedTransitionApi")
        }

        commonMain.dependencies {
          api(libs.compose.multiplatform.runtime)
          api(libs.compose.multiplatform.foundation)
          api(libs.compose.multiplatform.foundation.layout)
          api(libs.compose.multiplatform.material3)
          api(libs.compose.multiplatform.resources)
          api(libs.compose.multiplatform.ui.tooling.preview)
          api(libs.compose.multiplatform.material.icons)
          api(libs.compose.multiplatform.material3)
          api(libs.compose.multiplatform.material3.adaptive)

          implementation(libs.lifecycle.multiplatform.viewmodel)
          implementation(libs.lifecycle.multiplatform.viewmodel.compose)
          implementation(libs.lifecycle.multiplatform.runtime)
          implementation(libs.lifecycle.multiplatform.runtime.compose)

          api(libs.compose.multiplatform.navigation)
          implementation(libs.compose.multiplatform.backhandler)
        }

        androidMain.dependencies {
          implementation(libs.compose.multiplatform.ui.tooling)
        }
      }
    }
  }
}
