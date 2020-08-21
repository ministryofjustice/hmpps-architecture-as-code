package uk.gov.justice.hmpps.architecture

import com.structurizr.model.DeploymentNode
import com.structurizr.model.Model

class CloudPlatform private constructor() {
  companion object {
    lateinit var rds: DeploymentNode
    lateinit var s3: DeploymentNode
    lateinit var sns: DeploymentNode
    lateinit var sqs: DeploymentNode
    lateinit var elasticache: DeploymentNode
    lateinit var elasticsearch: DeploymentNode
    lateinit var kubernetes: DeploymentNode

    fun defineDeploymentNodes(@Suppress("UNUSED_PARAMETER") model: Model) {
      var cloudPlatform = AWS.london.addDeploymentNode("Cloud Platform account", "AWS shared hosting platform", "AWS")
      rds = cloudPlatform.addDeploymentNode("RDS", "AWS Relational Database Service database-as-a-service", "AWS")
      s3 = cloudPlatform.addDeploymentNode("S3", "AWS Simple Storage Service", "AWS")
      sns = cloudPlatform.addDeploymentNode("SNS", "AWS Simple Notification Service", "AWS")
      sqs = cloudPlatform.addDeploymentNode("SQS", "AWS Simple Queue Service", "AWS")
      elasticache = cloudPlatform.addDeploymentNode("ElastiCache", "Managed in-memory data store and cache service", "AWS")
      elasticsearch = cloudPlatform.addDeploymentNode("ElasticSearch", "AWS ElasticSearch Service", "AWS")
      kubernetes = cloudPlatform.addDeploymentNode("Kubernetes", "The Cloud Platform Kubernetes cluster", "Kubernetes")
    }
  }
}
