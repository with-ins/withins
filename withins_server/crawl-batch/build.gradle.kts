val awsSdkEcsVersion: String by project
val awsSdkS3Version: String by project
val awsSdkLambdaVersion: String = awsSdkS3Version

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-batch")
    implementation(project(":core"))

    implementation("software.amazon.awssdk:ecs:${awsSdkEcsVersion}")
    implementation("software.amazon.awssdk:s3:${awsSdkS3Version}")
    implementation("software.amazon.awssdk:lambda:${awsSdkLambdaVersion}")
    implementation("org.json:json:20230618")

    testImplementation(testFixtures(project(":core")))
    testImplementation("org.springframework.batch:spring-batch-test")

    // 지워야할 의존성
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    testImplementation("org.springframework.boot:spring-boot-starter-data-jpa")
    // 지워야할 의존성
}
