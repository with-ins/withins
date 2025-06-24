val awsSdkEcsVersion: String by project
val awsSdkS3Version: String by project
val awsSdkLambdaVersion: String = awsSdkS3Version
val jacksonVersion: String by project

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-batch")
    implementation(project(":core"))
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")

    implementation("software.amazon.awssdk:ecs:${awsSdkEcsVersion}")
    implementation("software.amazon.awssdk:s3:${awsSdkS3Version}")
    implementation("software.amazon.awssdk:lambda:${awsSdkLambdaVersion}")

    //TODO org.json:json 라이브러리는 jackson 라이브러리로 대체 예정
    implementation("org.json:json:20230618")
    implementation("com.fasterxml.jackson.core:jackson-databind:${jacksonVersion}")

    testImplementation(testFixtures(project(":core")))
    testImplementation("org.springframework.batch:spring-batch-test")

}
