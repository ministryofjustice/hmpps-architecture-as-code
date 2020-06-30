package uk.gov.justice.hmpps.architecture

import com.structurizr.Workspace
import com.structurizr.api.StructurizrClient
import com.structurizr.util.WorkspaceUtils
import java.io.File
import uk.gov.justice.hmpps.architecture.prison.*
import uk.gov.justice.hmpps.architecture.probation.*

object App {

  private const val WORKSPACE_ID: Long = 55600
  private const val API_KEY = "0c84d004-deaa-4bad-b586-a092d396e2c9"
  private const val API_SECRET = "671e7e9b-d226-4ede-8d96-02510e39f820"

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
    val client = StructurizrClient(API_KEY, API_SECRET)
    client.putWorkspace(WORKSPACE_ID, workspace)
  }

  fun writeToFile(workspace: Workspace) {
    val targetFile = File("structurizr-${workspace.id}-local.json")
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
