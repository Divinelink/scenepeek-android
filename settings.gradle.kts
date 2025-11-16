pluginManagement {
  includeBuild("build-logic")
  repositories {
    google()
    mavenCentral()
    gradlePluginPortal()
  }
}

dependencyResolutionManagement {
  repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
  repositories {
    google()
    mavenCentral()
  }
}
rootProject.name = "ScenePeek"

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
include(":app")

include(":core:commons")
include(":core:data")
include(":core:database")
include(":core:datastore")
include(":core:designsystem")
include(":core:domain")
include(":core:model")
include(":core:network")
include(":core:ui")
include(":core:scaffold")

include(":core:testing")
include(":core:fixtures")

include(":feature:add-to-account")
include(":feature:credits")
include(":feature:details")
include(":feature:discover")
include(":feature:lists")
include(":feature:onboarding")
include(":feature:profile")
include(":feature:request-media")
include(":feature:requests")
include(":feature:search")
include(":feature:settings")
include(":feature:tmdb-auth")
include(":feature:user-data")
include(":feature:webview")
