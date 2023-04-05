plugins {
  kotlin("jvm") version "1.8.20"
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
  implementation("com.structurizr:structurizr-client:1.24.1")
  implementation("com.structurizr:structurizr-core:1.24.1")
  implementation("com.structurizr:structurizr-documentation:1.1.1")
  implementation("com.structurizr:structurizr-graphviz:1.8.1")
  implementation("org.eclipse.jgit:org.eclipse.jgit:6.5.0.202303070854-r")
  implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-yaml:2.14.2")
  implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.14.2")

  testImplementation("org.assertj:assertj-core:3.24.2")
  testImplementation("org.junit.jupiter:junit-jupiter-api:5.9.2")
  testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.9.2")
}

val test by tasks.getting(Test::class) {
  useJUnitPlatform()
}
