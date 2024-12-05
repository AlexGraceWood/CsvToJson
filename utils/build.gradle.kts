plugins {
    id("java")
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    // Для работы с CSV
    implementation("com.opencsv: opencsv: 5.1")
    // Для работы с JSON (json-simple)
    implementation("com.googlecode.json-simple:json-simple:1.1.1")

    // Для работы с JSON (Gson)
    implementation("com.google.code.gson:gson:2.8.2")

    implementation(project(":db"))
    implementation(project(":service"))

    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}