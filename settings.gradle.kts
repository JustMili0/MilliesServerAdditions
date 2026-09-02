pluginManagement {
    repositories {
        maven("https://maven.fabricmc.net/") {
            name = "Fabric"
        }
        gradlePluginPortal()
    }
}

includeBuild("build-logic")
rootProject.name = "Millie's Server Additions"