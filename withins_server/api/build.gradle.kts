import org.springframework.boot.gradle.tasks.bundling.BootJar

tasks.named<BootJar>("bootJar") {
    enabled = true
    mainClass.set("com.withins.api.WithInsApplication")
}

tasks.named<Jar>("jar") {
    enabled = false
}

val restAssuredVersion: String by project

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
    implementation("org.springframework.boot:spring-boot-starter-security")
    implementation("org.springframework.boot:spring-boot-starter-oauth2-client")
    implementation(project(":core"))
    implementation(project(":support:monitoring"))
    implementation(project(":support:logging"))

    testImplementation("io.rest-assured:rest-assured:${restAssuredVersion}")
    testImplementation("org.springframework.boot:spring-boot-starter-data-jpa")
    testImplementation("org.springframework.security:spring-security-test")
    testImplementation(testFixtures(project(":core")))
}
