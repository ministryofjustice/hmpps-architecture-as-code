package uk.gov.justice.hmpps.architecture.shared

import com.structurizr.model.DeploymentNode
import com.structurizr.model.Model
import com.structurizr.view.ViewSet

class CloudPlatform private constructor(model: Model) {
  val cloudPlatform: DeploymentNode
  val rds: DeploymentNode
  val s3: DeploymentNode
  val kubernetes: DeploymentNode

  init {
    cloudPlatform = model.addDeploymentNode("Cloud Platform", "AWS shared hosting platform", "AWS")
    rds = cloudPlatform.addDeploymentNode("RDS", "AWS Relational Database Service database-as-a-service", "AWS")
    s3 = cloudPlatform.addDeploymentNode("S3", "AWS Simple Storage Service", "AWS")
    kubernetes = cloudPlatform.addDeploymentNode("Kubernetes", "The Cloud Platform Kubernetes cluster", "Kubernetes")
  }

  companion object : SingletonHolder<CloudPlatform, Model>(::CloudPlatform)
}