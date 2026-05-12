pluginManagement {
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

rootProject.name = "NEXORA"
include(":app")
include(":core")
include(":features:auth")
include(":features:chat")
include(":features:calls")
include(":features:stories")
include(":features:explore")
include(":features:profile")
include(":features:settings")
include(":shared")
