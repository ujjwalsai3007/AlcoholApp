plugins {
    kotlin("jvm")
    kotlin("plugin.serialization") version "1.9.21"
    application
}

group = "com.example"
version = "1.0-SNAPSHOT"

application {
    mainClass.set("com.example.alcoholapp.ApplicationKt")
}

dependencies {
    // Ktor server
    implementation("io.ktor:ktor-server-core:2.3.7")
    implementation("io.ktor:ktor-server-netty:2.3.7")
    implementation("io.ktor:ktor-server-content-negotiation:2.3.7")
    implementation("io.ktor:ktor-serialization-kotlinx-json:2.3.7")
    implementation("io.ktor:ktor-server-cors:2.3.7")
    implementation("io.ktor:ktor-server-status-pages:2.3.7")
    implementation("io.ktor:ktor-server-html-builder-jvm:2.3.7")
    implementation("io.ktor:ktor-server-resources:2.3.7")
    
    // Logging
    implementation("ch.qos.logback:logback-classic:1.4.11")
    
    // Serialization
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.2")
    
    // Database
    implementation("org.jetbrains.exposed:exposed-core:0.41.1")
    implementation("org.jetbrains.exposed:exposed-dao:0.41.1")
    implementation("org.jetbrains.exposed:exposed-jdbc:0.41.1")
    implementation("org.postgresql:postgresql:42.6.0")
    implementation("com.zaxxer:HikariCP:5.0.1")
    
    // Testing
    testImplementation("io.ktor:ktor-server-test-host:2.3.7")
    testImplementation("org.jetbrains.kotlin:kotlin-test:1.9.21")
}

kotlin {
    jvmToolchain(17)
}

tasks.withType<ProcessResources> {
    duplicatesStrategy = DuplicatesStrategy.INCLUDE
    from("src/main/resources") {
        include("**/*")
    }
}