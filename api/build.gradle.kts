plugins {
    id("java")
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.opencsv: opencsv: 5.1")
    // Для работы с JSON (json-simple)
    implementation("com.googlecode.json-simple:json-simple:1.1.1")

    // Для работы с JSON (Gson)
    implementation("com.google.code.gson:gson:2.8.2")

    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")

    implementation(project(":db"))
    implementation(project(":service"))
    implementation(project(":utils"))
}

tasks.test {
    useJUnitPlatform()
}