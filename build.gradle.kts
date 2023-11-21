plugins {
  kotlin("jvm") version "1.9.20"
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
  implementation("com.structurizr:structurizr-client:1.28.0")
  implementation("com.structurizr:structurizr-core:1.28.0")
  implementation("com.structurizr:structurizr-import:1.7.0")
  implementation("com.structurizr:structurizr-graphviz:2.2.2")
  implementation("org.eclipse.jgit:org.eclipse.jgit:6.7.0.202309050840-r")
  implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-yaml:2.16.0")
  implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.16.0")

  testImplementation("org.assertj:assertj-core:3.24.2")
  testImplementation("org.junit.jupiter:junit-jupiter-api:5.10.1")
  testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.10.1")
}

val test by tasks.getting(Test::class) {
  useJUnitPlatform()
}
