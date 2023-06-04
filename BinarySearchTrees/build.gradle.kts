plugins {
    id("org.jetbrains.kotlin.jvm") version "1.8.10"
    jacoco
}

repositories {
    mavenCentral()
}

dependencies {
    // Use the Kotlin JDK 8 standard library.
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

    testImplementation(platform("org.junit:junit-bom:5.9.2"))
    testImplementation("org.junit.jupiter:junit-jupiter")
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

    classDirectories.setFrom(files(classDirectories.files.map {
        fileTree(it) {
            exclude("**/binarysearchtrees/TreesKt.*")
        }
    }))

    reports {
        xml.required.set(false)
        html.required.set(true)
        html.outputLocation.set(file("${buildDir}/reports/jacoco"))
        csv.required.set(true)
        csv.outputLocation.set(file("${buildDir}/jacoco/report.csv"))
    }
}
