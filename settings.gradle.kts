pluginManagement {
    repositories {
        google()
        // Google-hosted Maven Central mirror helps avoid transient Maven Central 429 responses.
        maven { url = uri("https://maven-central.storage-download.googleapis.com/maven2/") }
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        // Fallback mirror for dependencies normally downloaded from repo.maven.apache.org.
        maven { url = uri("https://maven-central.storage-download.googleapis.com/maven2/") }
        mavenCentral()
    }
}

rootProject.name = "RobloxStudioMobile"
include(":app")
