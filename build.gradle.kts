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
        val isUnix = System.getProperty("os.name").toUpperCase().contains("MAC") || System.getProperty("os.name").toUpperCase().contains("LINUX")
        val buildDir = projectDir.resolve("src").resolve("main").resolve("cpp").resolve("build")
        if(isUnix) {
            buildDir.listFiles()?.filter { it.name.endsWith(".dll") || it.name.endsWith(".so") || it.name.endsWith(".dylib") }?.forEach {
                println("copy ${it.absolutePath}")
                it.copyTo(projectDir.resolve("src").resolve("main").resolve("resources").resolve(it.name), true)
            }
        } else {
            val windowsDebugDir = buildDir.resolve("Debug")
            windowsDebugDir.listFiles()?.filter { it.name.endsWith(".dll") || it.name.endsWith(".so") }?.forEach {
                println("copy ${it.absolutePath}")
                it.copyTo(projectDir.resolve("src").resolve("main").resolve("resources").resolve(it.name), true)
            }
        }
    }
}

tasks.withType<ProcessResources> {
    dependsOn("copyLibrary", "rustBuild")
}

tasks.create("rustBuild") {
    group = "rust"
    doLast {
        val isDebug = System.getenv("debug") != null
        val rustSourceDir = projectDir.resolve("src").resolve("main").resolve("rust")
        val cmd = if (isDebug) {
            "cargo build".split(" ").toTypedArray()
        } else {
            "cargo build --release".split(" ").toTypedArray()
        }
        val process = ProcessBuilder(*cmd).directory(rustSourceDir).start()
        val status = process.waitFor()
        if (status != 0) {
            throw RuntimeException("Rust build failed")
        }
        val rustBuildDir = rustSourceDir.resolve("target").resolve(if (isDebug) "debug" else "release")

        rustBuildDir.listFiles()?.forEach {
            if (it.name.endsWith(".dll") || it.name.endsWith(".so") || it.name.endsWith(".dylib")) {
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