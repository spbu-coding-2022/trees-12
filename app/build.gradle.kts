plugins {
    id("org.jetbrains.kotlin.jvm") version "1.8.10"
    id("org.jetbrains.kotlin.plugin.serialization") version "1.8.20"
    id("org.jetbrains.kotlin.plugin.noarg") version "1.8.20"
    id("org.jetbrains.compose") version "1.4.0"
    jacoco
    application
}

repositories {
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    google()
}

dependencies {
    // Use the Kotlin JDK 8 standard library.
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

    testImplementation(platform("org.junit:junit-bom:5.9.2"))
    testImplementation("org.junit.jupiter:junit-jupiter")

    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.5.0")
    implementation("org.neo4j:neo4j-ogm-core:4.0.5")
    implementation("org.neo4j:neo4j-ogm-bolt-driver:4.0.5")
    implementation("org.slf4j:slf4j-simple:2.0.0")

    implementation(compose.desktop.currentOs)
    implementation(compose.material3)

    implementation(project(":BinarySearchTrees"))
}

noArg {
    annotation("org.neo4j.ogm.annotation.NodeEntity")
    annotation("org.neo4j.ogm.annotation.RelationshipEntity")
}

tasks.test {
    finalizedBy("jacocoTestReport")
    useJUnitPlatform()
    maxHeapSize = "2G"
    testLogging {
        events("passed", "skipped", "failed")
    }
    reports.html.outputLocation.set(file("${buildDir}/reports/test"))
}

tasks.named<JacocoReport>("jacocoTestReport") {
    dependsOn(tasks.test)
    reports {
        xml.required.set(false)
        html.required.set(true)
        html.outputLocation.set(file("${buildDir}/reports/jacoco"))
        csv.required.set(true)
        csv.outputLocation.set(file("${buildDir}/jacoco/report.csv"))
    }
}

application {
    mainClass.set("app.AppKt")
}
