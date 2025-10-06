plugins {
    `java-library`
    `maven-publish`
    id("io.github.goooler.shadow") version "8.1.8"
}

repositories {
    mavenLocal()
    maven {
        url = uri("https://repo.codemc.org/repository/maven-public/")
    }

    maven {
        url = uri("https://repo.papermc.io/repository/maven-public/")
    }

    maven {
        url = uri("https://oss.sonatype.org/content/groups/public/")
    }

    maven {
        url = uri("https://repo.maven.apache.org/maven2/")
    }
}

dependencies {
    api(libs.de.rapha149.signgui)
    api(libs.com.jeff.media.custom.block.data)
    api(libs.net.objecthunter.exp4j)
    implementation(libs.de.janschuri.lunaticlib.paper)
    compileOnly(libs.io.papermc.paper.paper.api)
}

group = "de.janschuri"
version = "1.1.0"
description = "LunaticDrops"
java.sourceCompatibility = JavaVersion.VERSION_16

java.sourceCompatibility = JavaVersion.VERSION_17
java.targetCompatibility = JavaVersion.VERSION_17

publishing {
    publications.create<MavenPublication>("maven") {
        from(components["java"])
    }
}

tasks {
    build {
        dependsOn(shadowJar)
    }
    jar {
        enabled = false
    }
    shadowJar {
        archiveBaseName.set("LunaticDrops")
        archiveClassifier.set("")
    }
}

tasks.withType<JavaCompile>() {
    options.encoding = "UTF-8"
}

tasks.withType<Javadoc>() {
    options.encoding = "UTF-8"
}

tasks.named<com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar>("shadowJar") {
    relocate("org.bstats", "de.janschuri.lunaticdrops.libs.bstats")
    relocate("de.janschuri.lunaticlib", "de.janschuri.lunaticdrops.libs.lunaticlib")
}