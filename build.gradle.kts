import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.7.20"
    application
    java
    id("net.freudasoft.gradle-cmake-plugin") version "0.0.2"
}

group = "me.sagiri"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}

application {
    mainClass.set("Hello")
}

cmake {
    val sppSourceDir = projectDir.resolve("src").resolve("main").resolve("cpp")
    workingFolder.set(sppSourceDir.resolve("build"))
    sourceFolder.set(sppSourceDir)
    buildConfig.set("release")
    buildTarget.set("install")
}

tasks.create("copyLibrary") {
    dependsOn("cmakeBuild")
    group = "cmake"
    doLast {
        val buildDir = projectDir.resolve("src").resolve("main").resolve("cpp").resolve("build").resolve("Debug")
        buildDir.listFiles()?.filter { it.isFile && it.name.endsWith(".dll") || it.name.endsWith(".so") }?.forEach {
            it.copyTo(projectDir.resolve("src").resolve("main").resolve("resources").resolve(it.name), true)
        }

    }
}

tasks.withType<ProcessResources> {
    dependsOn("copyLibrary", "rustBuild")
}

tasks.create("rustBuild") {
    group = "rust"
    doLast {
        val rustSourceDir = projectDir.resolve("src").resolve("main").resolve("rust")
        val process = ProcessBuilder("cargo", "build").directory(rustSourceDir).start()
        process.waitFor()
        val rustBuildDir = rustSourceDir.resolve("target").resolve("debug")

        rustBuildDir.listFiles()?.forEach {
            if (it.isFile && it.name.endsWith(".dll") || it.name.endsWith(".so")) {
                it.copyTo(projectDir.resolve("src").resolve("main").resolve("resources").resolve(it.name), true)
            }
        }
    }
}

tasks.create("rustClean") {
    group = "rust"
    doLast {
        val rustSourceDir = projectDir.resolve("src").resolve("main").resolve("rust")
        val process = ProcessBuilder("cargo", "clean").directory(rustSourceDir).start()
        process.waitFor()
    }
}