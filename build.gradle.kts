plugins {
    id("java")
    id("com.github.johnrengelman.shadow") version "8.1.1"
    id("xyz.jpenilla.run-paper") version "2.2.2"
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
    testImplementation("io.papermc.paper", "paper-api", "1.20.4-R0.1-SNAPSHOT")

    // Paper
    compileOnly("io.papermc.paper", "paper-api", "1.20.4-R0.1-SNAPSHOT")

    // Config
    paperLibrary("org.spongepowered", "configurate-hocon", "4.1.2")
    paperLibrary("net.kyori", "adventure-serializer-configurate4", "4.12.0")

    // Others
    paperLibrary("cloud.commandframework", "cloud-paper", "1.8.3")
    paperLibrary("com.google.inject", "guice", "7.0.0")
}

version = "1.0.0"

paper {
    authors = listOf("tyonakaisan")
    website = "https://github.com/tyonakaisan"
    apiVersion = "1.20"
    generateLibrariesJson = true
    foliaSupported = false

    val mainPackage = "github.tyonakaisan.example"
    main = "$mainPackage.Example"
    bootstrapper = "$mainPackage.ExampleBootstrap"
    loader = "$mainPackage.ExampleLoader"

    serverDependencies {
    }
}

tasks {
    compileJava {
        this.options.encoding = Charsets.UTF_8.name()
        options.release.set(17)
    }

    shadowJar {
        this.archiveClassifier.set(null as String?)
    }

    runServer {
        minecraftVersion("1.20.4")
    }

    test {
        useJUnitPlatform()
    }
}