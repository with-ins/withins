import org.springframework.boot.gradle.tasks.bundling.BootJar

plugins {
    id("com.github.node-gradle.node")
}

tasks.named<BootJar>("bootJar") {
    dependsOn("buildFrontend")
    enabled = true
    mainClass.set("com.withins.api.WithInsApplication")
}

tasks.named<Jar>("jar") {
    enabled = false
}

val restAssuredVersion: String by project

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
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

val nodeJsVersion: String by project
val npmversion: String by project
node {
    version.set(nodeJsVersion)
    npmVersion.set(npmversion)

    // Vue.js 프로젝트 경로 설정
    nodeProjectDir.set(file("${rootProject.projectDir}/../withins_vue"))

    // 위에서 설정한 Node.js와 npm 버전을 다운로드해서 사용
    download.set(true)
}

// Vue.js 빌드 태스크 정의
tasks.register<com.github.gradle.node.npm.task.NpmTask>("buildFrontend") {
    dependsOn("npmSetup", "npmInstall")
    npmCommand.set(listOf("run", "build"))

    /*
    false면 항상 빌드하도록 강제
    만약 로컬환경에서는 파일이 변경될때만 빌드하길 원한다면, 로컬에서만 설정하고 배포환경에서는 항상 빌드하도록 설정해야함
     */
    outputs.upToDateWhen { false }
}