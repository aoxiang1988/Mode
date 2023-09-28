plugins {
    id("java")
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    //compileClasspath("org.jetbrains.kotlin:kotlin-script-runtime:1.6.10")
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    implementation(kotlin("script-runtime:1.6.10"))
}

tasks.test {
    useJUnitPlatform()
}