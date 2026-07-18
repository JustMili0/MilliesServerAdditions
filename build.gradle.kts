plugins {
    alias(libs.plugins.fabric.loom)
}

base {
    archivesName.set("${rootProject.property("archives_base_name")}-${rootProject.property("mod_version")}+mc${libs.versions.minecraft.get()}")
}

loom {
    accessWidenerPath = file("src/main/resources/${rootProject.property("mod_id")}.accesswidener")
}

repositories {
    maven("https://maven.terraformersmc.com/") // Mod Menu
    maven("https://maven.bawnorton.com/releases") // MixinSquared extension for MixinExtras
    maven("https://maven.enjarai.dev/mirrors") // MixinSquared extension for MixinExtras
    maven("https://api.modrinth.com/maven")
    maven("https://maven.parchmentmc.org")
}

dependencies {
    minecraft(libs.minecraft.get())
    mappings(loom.layered {
        officialMojangMappings()
        parchment("org.parchmentmc.data:parchment-${libs.versions.minecraft.get()}:${libs.versions.parchment.get()}@zip")
    })
    modImplementation(libs.fabric.loader.get())
    modImplementation(libs.fabric.api.get())

    //modImplementation("maven.modrinth:millies-core-libs:${rootProject.property("config_lib")}")

    modImplementation("com.terraformersmc:modmenu:${rootProject.property("mod_menu")}") // Mod menu
    modImplementation("maven.modrinth:lithium:mc1.21.11-0.21.4-fabric") // Just for performance
    //include(implementation(annotationProcessor("com.github.bawnorton.mixinsquared:mixinsquared-fabric:${libs.versions.mixinsquared.get()}")!!)!!)
}

tasks.processResources {
    filesMatching("fabric.mod.json") {
        expand(mapOf(
            "mod_id" to rootProject.property("mod_id"),
            "mod_name" to rootProject.property("mod_name"),
            "mod_version" to rootProject.property("mod_version"),
            "mod_description" to rootProject.property("mod_description"),
            "mod_authors" to rootProject.property("mod_authors"),
            "mod_license" to rootProject.property("mod_license"),
            "fabric_loader_version" to libs.versions.fabric.loader.get(),
            "fabric_api_version" to libs.versions.fabric.api.get(),
            "minecraft_version_constraint" to rootProject.property("minecraft_version_constraint")
        ))
    }
}

tasks.withType<JavaCompile>().configureEach {
    options.release = 21
}

java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}

tasks.jar {
    from("LICENSE") {
        rename { it }
    }
}