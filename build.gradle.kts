plugins {
  kotlin("jvm") version "1.4.20"
  application
}

application {
  mainClass.set("uk.gov.justice.hmpps.architecture.App")
}

repositories {
  jcenter()
}

dependencies {
  implementation(kotlin("stdlib"))
  implementation("com.structurizr:structurizr-client:1.4.5")
  implementation("com.structurizr:structurizr-core:1.4.5")
  implementation("com.structurizr:structurizr-adr-tools:1.3.6")
  implementation("org.eclipse.jgit:org.eclipse.jgit:5.10.0.202012080955-r")
}
