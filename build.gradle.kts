plugins {
    kotlin("jvm") version "1.6.0"
}

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
    testImplementation(kotlin("test-junit5"))
    testImplementation("org.junit.jupiter:junit-jupiter:5.8.2")
}

tasks {
    sourceSets {
        main {
            java.srcDirs("src")
        }
        test {
            java.srcDirs("test")
        }
    }

    wrapper {
        gradleVersion = "7.3"
    }

    test {
        useJUnitPlatform()
    }
}