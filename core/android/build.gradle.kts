import com.android.build.api.variant.BuildConfigField
import java.util.Properties

plugins {
  alias(libs.plugins.divinelink.android.feature)
  alias(libs.plugins.secrets)
}

android {
  buildFeatures {
    buildConfig = true
  }

  defaultConfig {
    buildConfigField("Integer", "VERSION_CODE", libs.versions.version.code.get())
    buildConfigField("String", "VERSION_NAME", "\"${libs.versions.version.name.get()}\"")

    // NOTE: The 'flavor' property used below must be passed via the command line
    // (e.g., -Pflavor=fdroid) or through custom tasks like 'assembleDebugFdroid'.
    // If no property is provided, it defaults to 'secrets.full.properties'.
    val flavorFileName = project.findProperty("flavor")?.let {
      "secrets.$it.properties"
    } ?: "secrets.full.properties"

    androidComponents {
      onVariants { variant ->
        loadProperties(flavorFileName).forEach { (key, value) ->
          variant.buildConfigFields?.put(
            key.toString(),
            BuildConfigField(
              type = "String",
              value = "$value",
              comment = "Generated from $flavorFileName",
            ),
          )
        }
      }
    }
  }
}

private fun loadProperties(fileName: String): Properties {
  val props = Properties()
  val file = rootProject.file(fileName)
  if (file.exists()) props.load(file.inputStream())
  return props
}

secrets {
  defaultPropertiesFileName = "secrets.defaults.properties"
}
