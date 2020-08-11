package uk.gov.justice.hmpps.architecture

import com.structurizr.model.DeploymentNode
import com.structurizr.model.Model

class AWS private constructor() {
  companion object {
    lateinit var london: DeploymentNode

    fun defineDeploymentNodes(model: Model) {
      val root = model.addDeploymentNode("Amazon Web Services", "AWS platform", "AWS")
      london = root.addDeploymentNode("London region (eu-west-2)")
    }
  }
}
