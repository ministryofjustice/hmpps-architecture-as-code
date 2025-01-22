plugins {
  kotlin("jvm") version "2.1.0"
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
  implementation("org.eclipse.jgit:org.eclipse.jgit:7.1.0.202411261347-r")
  implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-yaml:2.18.2")
  implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.18.2")

  testImplementation("org.assertj:assertj-core:3.27.3")
  testImplementation("org.junit.jupiter:junit-jupiter-api:5.11.4")
  testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.11.4")
}

val test by tasks.getting(Test::class) {
  useJUnitPlatform()
}
