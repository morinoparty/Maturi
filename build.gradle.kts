import net.minecrell.pluginyml.bukkit.BukkitPluginDescription

plugins {
    id("java")
    id("com.github.johnrengelman.shadow") version "8.1.1"
    id("xyz.jpenilla.run-paper") version "2.3.0"
    id("net.minecrell.plugin-yml.paper") version "0.6.0"
}

repositories {
    mavenLocal()
    mavenCentral()
    maven("https://jitpack.io")
    maven("https://oss.sonatype.org/content/repositories/snapshots/")
    maven("https://s01.oss.sonatype.org/content/repositories/snapshots/")
    maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation("net.objecthunter", "exp4j", "0.4.8")
    testImplementation("io.papermc.paper", "paper-api", "1.20.6-R0.1-SNAPSHOT")

    // Paper
    compileOnly("io.papermc.paper", "paper-api", "1.20.6-R0.1-SNAPSHOT")

    // Plugin
    compileOnly("com.github.MilkBowl", "VaultAPI", "1.7")
    compileOnly(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar")))) // CommandItem

    // Config
    paperLibrary("org.spongepowered", "configurate-hocon", "4.2.0-SNAPSHOT")
    paperLibrary("net.kyori", "adventure-serializer-configurate4", "4.17.0")

    // Others
    paperLibrary("com.google.inject", "guice", "7.0.0")
    paperLibrary("net.objecthunter", "exp4j", "0.4.8")
    implementation("org.incendo.interfaces", "interfaces-paper", "1.0.0-SNAPSHOT")
    implementation("fr.mrmicky", "fastboard", "2.1.2")
}

version = "0.0.21-SNAPSHOT"

paper {
    authors = listOf("tyonakaisan")
    website = "https://github.com/tyonakaisan"
    apiVersion = "1.20"
    generateLibrariesJson = true
    foliaSupported = false

    val mainPackage = "github.tyonakaisan.maturi"
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
    compileJava {
        this.options.encoding = Charsets.UTF_8.name()
        options.release.set(21)
    }

    shadowJar {
        this.archiveClassifier.set(null as String?)
        mergeServiceFiles()
    }

    runServer {
        minecraftVersion("1.21")
        downloadPlugins {
            github("MilkBowl", "Vault", "1.7.3", "Vault.jar")
            github("jpenilla","TabTPS", "v1.3.24", "tabtps-spigot-1.3.24.jar")
        }
    }

    test {
        useJUnitPlatform()
    }
}