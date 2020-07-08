package uk.gov.justice.hmpps.architecture.shared

import com.structurizr.model.DeploymentNode
import com.structurizr.model.Model
import com.structurizr.view.ViewSet

class CloudPlatform private constructor(model: Model) {
  val cloudPlatform: DeploymentNode
  val rds: DeploymentNode
  val s3: DeploymentNode
  val sns: DeploymentNode
  val sqs: DeploymentNode
  val elasticsearch: DeploymentNode
  val kubernetes: DeploymentNode

  init {
    cloudPlatform = model.addDeploymentNode("Cloud Platform", "AWS shared hosting platform", "AWS")
    rds = cloudPlatform.addDeploymentNode("RDS", "AWS Relational Database Service database-as-a-service", "AWS")
    s3 = cloudPlatform.addDeploymentNode("S3", "AWS Simple Storage Service", "AWS")
    sns = cloudPlatform.addDeploymentNode("SNS", "AWS Simple Notification Service", "AWS")
    sqs = cloudPlatform.addDeploymentNode("SQS", "AWS Simple Queue Service", "AWS")
    elasticsearch = cloudPlatform.addDeploymentNode("ElasticSearch", "AWS ElasticSearch Service", "AWS")
    kubernetes = cloudPlatform.addDeploymentNode("Kubernetes", "The Cloud Platform Kubernetes cluster", "Kubernetes")
  }

  companion object : SingletonHolder<CloudPlatform, Model>(::CloudPlatform)
}
