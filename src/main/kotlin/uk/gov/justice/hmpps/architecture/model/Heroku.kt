package uk.gov.justice.hmpps.architecture.model

import com.structurizr.model.DeploymentNode
import com.structurizr.model.Model

class Heroku private constructor() {
  companion object {
    lateinit var dyno: DeploymentNode
    lateinit var database: DeploymentNode

    fun defineDeploymentNodes(model: Model) {
      val heroku = model.addDeploymentNode("Heroku", "Platform as a Service", "Heroku")
      dyno = heroku.addDeploymentNode(
        "Dyno",
        "Dynos are isolated, virtualized Linux containers that are designed to execute code based on a user-specified command",
        "Heroku"
      )
      database = heroku.addDeploymentNode("Managed Database", "Managed database services", "Heroku")
    }
  }
}
