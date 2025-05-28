rootProject.name = "withins"

include(
    "api",
    "core",
    "support:monitoring",
    "support:logging"
)

pluginManagement {
    val springBootVersion: String by settings
    val springDependencyManagement: String by settings

    resolutionStrategy {
        eachPlugin {
            when (requested.id.id) {
                "org.springframework.boot" -> useVersion(springBootVersion)
                "io.spring.dependency-management" -> useVersion(springDependencyManagement)
            }
        }
    }
}