package uk.gov.justice.hmpps.architecture.model

import com.structurizr.model.DeploymentNode
import com.structurizr.model.Model

class Azure private constructor() {
  companion object {
    lateinit var root: DeploymentNode
    lateinit var nomsdigitech: DeploymentNode
    lateinit var sequation: DeploymentNode
    lateinit var kubernetes: DeploymentNode

    fun defineDeploymentNodes(model: Model) {
      root = model.addDeploymentNode("Microsoft Azure", "Azure platform", "Azure")
      nomsdigitech = root.addDeploymentNode("NOMS Digital Studio")
      sequation = root.addDeploymentNode("Sequation")
      kubernetes = root.addDeploymentNode("Digital Studio AKS", "Digital Studio AKS Kubernetes Cluster", "Kubernetes")
    }
  }
}
