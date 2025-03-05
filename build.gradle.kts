// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    
    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.9.21")
        classpath("org.jetbrains.kotlin:kotlin-serialization:1.9.21")
        classpath("com.android.tools.build:gradle:8.2.0")
    }
}

plugins {
    id("org.jetbrains.kotlin.jvm") version "1.9.21" apply false
    id("org.jetbrains.kotlin.plugin.serialization") version "1.9.21" apply false
    id("org.springframework.boot") version "3.1.4" apply false
    id("io.spring.dependency-management") version "1.1.3" apply false
    id("com.android.application") version "8.2.0" apply false
    id("org.jetbrains.kotlin.android") version "1.9.21" apply false
    id("org.jetbrains.compose") version "1.5.11" apply false
}

allprojects {
    
}
