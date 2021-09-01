package uk.gov.justice.hmpps.architecture

import com.structurizr.model.DeploymentNode
import com.structurizr.model.Model

class Azure private constructor() {
  companion object {
    lateinit var root: DeploymentNode
    lateinit var nomsdigitech: DeploymentNode
    lateinit var sequation: DeploymentNode

    fun defineDeploymentNodes(model: Model) {
      root = model.addDeploymentNode("Microsoft Azure", "Azure platform", "Azure")
      nomsdigitech = root.addDeploymentNode("NOMS Digital Studio")
      sequation = root.addDeploymentNode("Sequation")
    }
  }
}
