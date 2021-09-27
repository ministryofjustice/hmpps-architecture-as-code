plugins {
  kotlin("jvm") version "1.5.0"
  application
}

application {
  mainClass.set("uk.gov.justice.hmpps.architecture.App")
}

repositories {
  mavenCentral()
}

dependencies {
  implementation(kotlin("stdlib"))
  implementation("com.structurizr:structurizr-client:1.9.3")
  implementation("com.structurizr:structurizr-core:1.9.3")
  implementation("com.structurizr:structurizr-adr-tools:1.3.7")
  implementation("org.eclipse.jgit:org.eclipse.jgit:5.11.0.202103091610-r")
}
