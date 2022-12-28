plugins {
  kotlin("jvm") version "1.7.22"
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
  implementation("com.structurizr:structurizr-client:1.16.2")
  implementation("com.structurizr:structurizr-core:1.16.2")
  implementation("com.structurizr:structurizr-documentation:1.0.2")
  implementation("com.structurizr:structurizr-graphviz:1.6.1")
  implementation("org.eclipse.jgit:org.eclipse.jgit:6.4.0.202211300538-r")
  implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-yaml:2.14.1")
  implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.14.1")

  testImplementation("org.assertj:assertj-core:3.23.1")
  testImplementation("org.junit.jupiter:junit-jupiter-api:5.9.1")
  testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.9.1")
}

val test by tasks.getting(Test::class) {
  useJUnitPlatform()
}
