package uk.gov.justice.hmpps.architecture

import com.structurizr.Workspace
import com.structurizr.api.StructurizrClient
import com.structurizr.util.WorkspaceUtils
import java.io.File
import uk.gov.justice.hmpps.architecture.custody.*

object App {
  const val CUSTODY_WORKSPACE_ID: Long = 55246

  @JvmStatic
  fun main(args: Array<String>) {
    if (args.contains("--push")) {
      push()
    } else {
      show()
    }
  }

  fun push() {
    val client = StructurizrClient(System.getenv("STRUCTURIZR_API_KEY"), System.getenv("STRUCTURIZR_API_SECRET"))
    client.putWorkspace(CUSTODY_WORKSPACE_ID, custodyWorkspace())
  }

  fun show() {
    WorkspaceUtils.saveWorkspaceToJson(custodyWorkspace(), File("custody-workspace.json"))
  }

  fun custodyWorkspace(): Workspace {
    val workspace = Workspace("Custody systems", "Systems related to the confinement of offenders")

    custodyModel(workspace.model)
    custodyViews(workspace.model, workspace.views)

    return workspace
  }
}
