plugins {
  kotlin("jvm") version "1.6.10"
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
  implementation("com.structurizr:structurizr-client:1.12.1")
  implementation("com.structurizr:structurizr-core:1.12.1")
  implementation("com.structurizr:structurizr-documentation:1.0.1")
  implementation("com.structurizr:structurizr-graphviz:1.6.1")
  implementation("org.eclipse.jgit:org.eclipse.jgit:6.0.0.202111291000-r")
  implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-yaml:2.13.1")
  implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.13.1")

  testImplementation("org.assertj:assertj-core:3.22.0")
  testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.2")
  testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.2")
}

val test by tasks.getting(Test::class) {
  useJUnitPlatform()
}
