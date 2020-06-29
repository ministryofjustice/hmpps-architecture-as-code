plugins {
    kotlin("jvm") version "1.3.72"
    application
}

application {
    mainClass.set("uk.gov.justice.hmpps.architecture.App")
}

repositories {
    jcenter()
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation("com.structurizr:structurizr-client:1.4.5")
    implementation("com.structurizr:structurizr-core:1.4.5")
}
