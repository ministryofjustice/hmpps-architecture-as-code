plugins {
  kotlin("jvm") version "2.0.20"
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
  implementation("com.structurizr:structurizr-client:1.29.0")
  implementation("com.structurizr:structurizr-core:1.29.0")
  implementation("com.structurizr:structurizr-import:1.7.0")
  implementation("com.structurizr:structurizr-graphviz:2.2.2")
  implementation("org.eclipse.jgit:org.eclipse.jgit:7.0.0.202409031743-r")
  implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-yaml:2.18.0")
  implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.18.0")

  testImplementation("org.assertj:assertj-core:3.26.3")
  testImplementation("org.junit.jupiter:junit-jupiter-api:5.11.1")
  testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.11.1")
}

val test by tasks.getting(Test::class) {
  useJUnitPlatform()
}
