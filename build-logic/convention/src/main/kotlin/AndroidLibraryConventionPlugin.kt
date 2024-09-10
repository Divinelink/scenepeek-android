import com.android.build.gradle.LibraryExtension
import com.divinelink.configureKotlinAndroid
import com.divinelink.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies

class AndroidLibraryConventionPlugin : Plugin<Project> {
  override fun apply(target: Project) {
    with(target) {
      with(pluginManager) {
        apply("com.android.library")
        apply("org.jetbrains.kotlin.android")
        apply("org.jetbrains.kotlinx.kover")
      }

      extensions.configure<LibraryExtension> {
        configureKotlinAndroid(this)
        defaultConfig.targetSdk = 34
        testOptions.animationsDisabled = true
        // The resource prefix is derived from the module name,
        // so resources inside ":core:module1" must be prefixed with "core_module1_"
        resourcePrefix =
          path.split("""\W""".toRegex()).drop(1).distinct().joinToString(separator = "_")
            .lowercase() + "_"
      }

      dependencies {
        add("implementation", libs.findLibrary("androidx.tracing.ktx").get())
      }
    }
  }
}
