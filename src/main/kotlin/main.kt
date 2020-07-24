package uk.gov.justice.hmpps.architecture

import com.structurizr.Workspace
import com.structurizr.api.StructurizrClient
import com.structurizr.util.WorkspaceUtils
import uk.gov.justice.hmpps.architecture.prison.*
import uk.gov.justice.hmpps.architecture.probation.*
import java.io.File

object App {
  val EXPORT_LOCATION = File("./exports")

  @JvmStatic
  fun main(args: Array<String>) {
    val workspace = chooseWorkspace(args)
    if (args.contains("--push")) {
      pushToRemote(workspace)
    } else {
      writeToFile(workspace)
    }
  }

  fun pushToRemote(workspace: Workspace) {
    val client = StructurizrClient(System.getenv("STRUCTURIZR_API_KEY"), System.getenv("STRUCTURIZR_API_SECRET"))
    val workspaceId = System.getenv("STRUCTURIZR_WORKSPACE_ID")?.toLongOrNull() ?: workspace.id
    client.workspaceArchiveLocation = EXPORT_LOCATION

    workspace.version = System.getenv("BUILD_VERSION")
    client.putWorkspace(workspaceId, workspace)
  }

  fun writeToFile(workspace: Workspace) {
    val targetFile = File(EXPORT_LOCATION, "structurizr-${workspace.id}-local.json")
    WorkspaceUtils.saveWorkspaceToJson(workspace, targetFile)
    println("Wrote workspace to '$targetFile'")
  }

  fun chooseWorkspace(args: Array<String>): Workspace {
    if (args.contains("--prison")) {
      return prisonWorkspace()
    }
    if (args.contains("--probation")) {
      return probationWorkspace()
    }
    throw IllegalArgumentException("Please choose --prison or --probation")
  }
}
