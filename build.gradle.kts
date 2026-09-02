import multiloader.*

plugins {
    alias(libs.plugins.fabric.loom)
    alias(libs.plugins.publish)
    id("multiloader-extensions")
}

base {
    archivesName.set("${baseName}-${modVersion}+mc${libs.versions.minecraft.get()}-Fabric")
}

loom {
    accessWidenerPath = file("src/main/resources/${rootProject.property("mod_id")}.accesswidener")
}

repositories {
    mavenCentral()
    maven("https://maven.terraformersmc.com/") // Mod Menu
    maven("https://maven.bawnorton.com/releases") // MixinSquared extension for MixinExtras
    maven("https://maven.enjarai.dev/mirrors") // MixinSquared extension for MixinExtras
    maven("https://api.modrinth.com/maven")
    maven("https://maven.parchmentmc.org")
    maven("https://maven.lumynitystudios.net") // Core Libs
}

dependencies {
    minecraft(libs.minecraft.get())
    implementation(libs.fabric.loader.get())
    implementation(libs.fabric.api.get())

    // TODO: Uncomment when file becomes available
    //include(implementation("net.justmili:corelibs:${rootProject.property("corelibs")}")!!)

    implementation("com.terraformersmc:modmenu:${rootProject.property("mod_menu")}") // Mod menu
    implementation("maven.modrinth:lithium:${rootProject.property("lithium")}") // Just for performance
    //include(implementation(annotationProcessor("com.github.bawnorton.mixinsquared:mixinsquared-fabric:${libs.versions.mixinsquared.get()}")!!)!!)
}

tasks.processResources {
    filesMatching("fabric.mod.json") {
        expand(mapOf(
            "mod_id" to modId,
            "mod_name" to modName,
            "mod_version" to modVersion,
            "mod_description" to modDesc,
            "mod_authors" to modAuthor,
            "mod_license" to modLicense,
            "fabric_loader_version" to libs.versions.fabric.loader.get(),
            "fabric_api_version" to libs.versions.fabric.api.get(),
            "minecraft_version_constraint" to rootProject.property("minecraft_version_constraint")
        ))
    }
}

tasks.withType<JavaCompile>().configureEach {
    options.release = 25
}

java {
    sourceCompatibility = JavaVersion.VERSION_25
    targetCompatibility = JavaVersion.VERSION_25
}

tasks.jar {
    from("LICENSE") {
        rename { it }
    }
}

publishMods {
    file.set(tasks.jar.get().archiveFile)
    modLoaders.add("fabric")

    changelog = readChangelogFromBranch("origin/master", ".Informative/Changelogs/${modVersion}-Changelog.md")

    modrinth {
        accessToken = property("modrinth_token") as String
        projectId = "AvEXfaSD"

        minecraftVersions.add(mcVersion)
        environment = CLIENT_AND_SERVER
        // STABLE, BETA, ALPHA
        type = STABLE

        requires("fabric-api"/*, "millies-core-libs"*/)
    }
}