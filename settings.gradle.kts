pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {

        google()
        mavenCentral()
        maven { url = uri("https://jitpack.io") }
        maven { url = uri("https://groovy.jfrog.io/artifactory/libs-release/") }
        maven { url = uri("https://eu.livotov.com/releases/") }
    }
}

rootProject.name = "Grocio"
include(":app")
 