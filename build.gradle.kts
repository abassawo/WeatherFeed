plugins {
    //trick: for the same plugin versions in all sub-modules
    id("com.android.application").version("7.4.0").apply(false)
    id("com.android.library").version("7.4.0").apply(false)
    kotlin("android").version("1.7.0").apply(false)
    kotlin("multiplatform").version("1.7.0").apply(false)
    id("org.jetbrains.kotlin.jvm") version "1.7.21" apply false
    id("com.google.dagger.hilt.android") version "2.44" apply false
}

buildscript {
    repositories {
        mavenLocal()
        google()
        mavenCentral()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    }
    dependencies {
        classpath("com.android.tools.build:gradle:7.4.2")
        val kotlinVersion = "1.6.21"
        classpath(kotlin("gradle-plugin", version = kotlinVersion))
        classpath(kotlin("serialization", version = kotlinVersion))
//        classpath("com.android.tools.build:gradle:4.2.0-alpha14")
        classpath("com.google.devtools.ksp:com.google.devtools.ksp.gradle.plugin:1.8.0-1.0.8")
//        classpath("com.rickclephas.kmp:kmp-nativecoroutines-gradle-plugin:${Versions.kmpNativeCoroutinesVersion}")
//        classpath("io.realm.kotlin:gradle-plugin:${Versions.realm}")
    }
}

allprojects {
    repositories {
        mavenLocal()
        google()
        mavenCentral()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
        maven("https://androidx.dev/storage/compose-compiler/repository")
    }
}


tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}
