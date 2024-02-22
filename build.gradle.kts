plugins {
  kotlin("jvm") version "1.9.22"
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
  implementation("com.structurizr:structurizr-client:2.0.0")
  implementation("com.structurizr:structurizr-core:1.29.0")
  implementation("com.structurizr:structurizr-import:1.7.0")
  implementation("com.structurizr:structurizr-graphviz:2.2.2")
  implementation("org.eclipse.jgit:org.eclipse.jgit:6.8.0.202311291450-r")
  implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-yaml:2.16.1")
  implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.16.1")

  testImplementation("org.assertj:assertj-core:3.25.3")
  testImplementation("org.junit.jupiter:junit-jupiter-api:5.10.2")
  testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.10.2")
}

val test by tasks.getting(Test::class) {
  useJUnitPlatform()
}
