plugins {
  kotlin("jvm") version "1.9.0"
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
  implementation("com.structurizr:structurizr-client:1.26.1")
  implementation("com.structurizr:structurizr-core:1.26.1")
  implementation("com.structurizr:structurizr-import:1.5.0")
  implementation("com.structurizr:structurizr-graphviz:2.2.0")
  implementation("org.eclipse.jgit:org.eclipse.jgit:6.6.0.202305301015-r")
  implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-yaml:2.15.2")
  implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.15.2")

  testImplementation("org.assertj:assertj-core:3.24.2")
  testImplementation("org.junit.jupiter:junit-jupiter-api:5.10.0")
  testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.10.0")
}

val test by tasks.getting(Test::class) {
  useJUnitPlatform()
}
