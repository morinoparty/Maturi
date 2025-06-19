import net.minecrell.pluginyml.bukkit.BukkitPluginDescription

plugins {
    id("java")
    alias(libs.plugins.shadow)
    alias(libs.plugins.run.paper)
    alias(libs.plugins.plugin.yml.paper)
}

group = rootProject.group
version = rootProject.version
val projectName = rootProject.name

repositories {
    mavenCentral()
    maven("https://jitpack.io")
    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://s01.oss.sonatype.org/content/repositories/snapshots/")
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation(libs.exp4j)

    // Paper
    compileOnly(libs.paper.api)

    // Plugin
    compileOnly(libs.vault)
    compileOnly(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar")))) // CommandItem

    // Config
    paperLibrary(libs.configurate.hocon)
    paperLibrary(libs.adventure.serializer.configurate4)

    // Others
    paperLibrary(libs.guice)
    paperLibrary(libs.exp4j)
    implementation(libs.interfaces)
    implementation(libs.fastboard)
}

paper {
    authors = listOf("tyonakaisan")
    website = "https://github.com/tyonakaisan"
    apiVersion = libs.versions.minecraft.get()
    generateLibrariesJson = true
    foliaSupported = false

    val mainPackage = "$group.${projectName.lowercase()}"
    main = "$mainPackage.Maturi"
    bootstrapper = "$mainPackage.MaturiBootstrap"
    loader = "$mainPackage.MaturiLoader"

    serverDependencies {
        register("Vault") {
            required = false
        }
        register("CommandItem") {
            required = false
        }
    }

    permissions {
        register("maturi.bypass.command") {
            description = "Bypass command use prohibitions."
            default = BukkitPluginDescription.Permission.Default.OP
        }
        register("maturi.bypass.money") {
            description = "Bypass the levy of money."
            default = BukkitPluginDescription.Permission.Default.OP
        }
    }
}

tasks {
    val javaVersion = "${property("java_version")}".toInt()
    compileJava {
        this.options.encoding = Charsets.UTF_8.name()
        options.release.set(javaVersion)
    }

    shadowJar {
        this.archiveClassifier.set(null as String?)
        mergeServiceFiles()
    }

    runServer {
        minecraftVersion(libs.versions.minecraft.get())
        downloadPlugins {
            github("MilkBowl", "Vault", "1.7.3", "Vault.jar")
            github("jpenilla","TabTPS", "v1.3.24", "tabtps-spigot-1.3.24.jar")
        }
    }

    test {
        useJUnitPlatform()
    }
}