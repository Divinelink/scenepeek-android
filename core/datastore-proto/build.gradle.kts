plugins {
  alias(libs.plugins.divinelink.android.library)
  alias(libs.plugins.protobuf)
}

protobuf {
  protoc {
    artifact = libs.protobuf.protoc.get().toString()
  }
  generateProtoTasks {
    all().forEach { task ->
      task.builtins {
        register("java") {
          option("lite")
        }
        register("kotlin") {
          option("lite")
        }
      }
    }
  }
}

dependencies {
  api(libs.protobuf.kotlin.lite)
}
