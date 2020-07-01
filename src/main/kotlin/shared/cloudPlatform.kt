package uk.gov.justice.hmpps.architecture.prison

import com.structurizr.model.DeploymentNode
import com.structurizr.model.Model

fun cloudPlatform(model: Model) {
  val cloudPlatform = model.addDeploymentNode("Cloud Platform", "AWS shared hosting platform", "AWS")
  cloudPlatform.addDeploymentNode("RDS", "AWS Relational Database Service database-as-a-service", "AWS")
  cloudPlatform.addDeploymentNode("S3", "AWS Simple Storage Service", "AWS")
  cloudPlatform.addDeploymentNode("Kubernetes", "The Cloud Platform Kubernetes cluster", "Kubernetes")
}
