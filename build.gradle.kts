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
  implementation("com.squareup.okhttp3:okhttp:4.9.2")
  implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-yaml:2.13.0")
  implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.13.0")

  testImplementation("org.assertj:assertj-core:3.19.0")
  testImplementation("org.junit.jupiter:junit-jupiter-api:5.5.2")
  testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.5.2")
}

val test by tasks.getting(Test::class) {
  useJUnitPlatform()
}
