plugins {
	java
	jacoco
	id("org.springframework.boot") version "3.5.9"
	id("io.spring.dependency-management") version "1.1.7"
}

group = "tech.finaya"
version = "0.0.1-SNAPSHOT"
description = "Finaya Digital Wallet"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(21)
	}
}

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-actuator")
	implementation("org.springframework.retry:spring-retry:2.0.12")
	implementation("org.springframework.boot:spring-boot-starter-aop")
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.8.15")

	runtimeOnly("org.postgresql:postgresql")

	testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("org.springframework.boot:spring-boot-starter-webflux")
    testImplementation("org.testcontainers:junit-jupiter:1.19.0")
    testImplementation("org.testcontainers:postgresql:1.19.0")
	
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

sourceSets {
    val integrationTest by creating {
        java.srcDir("src/integrationTest/java")
        resources.srcDir("src/integrationTest/resources")
        compileClasspath += sourceSets["main"].output + sourceSets["test"].output + configurations["testRuntimeClasspath"]
        runtimeClasspath += output + compileClasspath
    }
}

configurations {
    val integrationTestImplementation by getting {
        extendsFrom(configurations["testImplementation"])
    }
    val integrationTestRuntimeOnly by getting {
        extendsFrom(configurations["testRuntimeOnly"])
    }
}

jacoco {
    toolVersion = "0.8.10"
}

tasks.test {
    useJUnitPlatform()
    include("**/*Test.class")
    systemProperty("spring.profiles.active", "test")
}

val integrationTest = tasks.register<Test>("integrationTest") {
    description = "Runs integration tests"
    group = "verification"

    useJUnitPlatform()
    include("**/*IT.class")
    shouldRunAfter(tasks.test)

    systemProperty("spring.profiles.active", "test")

    testClassesDirs = sourceSets["integrationTest"].output.classesDirs
    classpath = sourceSets["integrationTest"].runtimeClasspath

    extensions.configure<JacocoTaskExtension> {
        isEnabled = true
        destinationFile = layout.buildDirectory.file("jacoco/integrationTest.exec").get().asFile
    }
}

tasks.jacocoTestReport {
    dependsOn(tasks.test, integrationTest)

    executionData.setFrom(
        files(
            layout.buildDirectory.file("jacoco/test.exec"),
            layout.buildDirectory.file("jacoco/integrationTest.exec")
        )
    )

    reports {
        xml.required.set(true)
        csv.required.set(false)
        html.required.set(true)
        html.outputLocation.set(layout.buildDirectory.dir("jacocoHtml"))
    }
}

tasks.jacocoTestCoverageVerification {
    dependsOn(tasks.test, integrationTest)

    executionData.setFrom(
        files(
            layout.buildDirectory.file("jacoco/test.exec"),
            layout.buildDirectory.file("jacoco/integrationTest.exec")
        )
    )

    violationRules {
        rule {
            limit {
                minimum = 0.80.toBigDecimal()
            }
        }
    }
}

tasks.check {
    dependsOn(tasks.jacocoTestCoverageVerification)
}
