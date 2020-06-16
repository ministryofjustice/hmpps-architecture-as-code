package uk.gov.justice.hmpps.architecture

import com.structurizr.Workspace
import com.structurizr.api.StructurizrClient
import com.structurizr.util.WorkspaceUtils
import java.io.File
import uk.gov.justice.hmpps.architecture.custody.*
import uk.gov.justice.hmpps.architecture.rehabilitation.*

object App {
  @JvmStatic
  fun main(args: Array<String>) {
    val workspace = chooseWorkspace(args)
    if (args.contains("--push")) {
      push(workspace)
    } else {
      show(workspace)
    }
  }

  fun push(workspace: Workspace) {
    val client = StructurizrClient(System.getenv("STRUCTURIZR_API_KEY"), System.getenv("STRUCTURIZR_API_SECRET"))
    client.putWorkspace(workspace.id, workspace)
  }

  fun show(workspace: Workspace) {
    val targetFile = File("structurizr-${workspace.id}-local.json")
    WorkspaceUtils.saveWorkspaceToJson(workspace, targetFile)
    println("Wrote workspace to '$targetFile'")
  }

  fun chooseWorkspace(args: Array<String>): Workspace {
    if (args.contains("--custody")) {
      return custodyWorkspace()
    }
    if (args.contains("--rehabilitation")) {
      return rehabilitationWorkspace()
    }
    throw IllegalArgumentException("Please choose --custody or --rehabilitation")
  }
}
