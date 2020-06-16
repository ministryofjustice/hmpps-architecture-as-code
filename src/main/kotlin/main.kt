package uk.gov.justice.hmpps.architecture

import com.structurizr.Workspace
import com.structurizr.api.StructurizrClient
import com.structurizr.util.WorkspaceUtils
import java.io.File
import uk.gov.justice.hmpps.architecture.custody.*
import uk.gov.justice.hmpps.architecture.rehabilitation.*

object App {
  const val CUSTODY_WORKSPACE_ID: Long = 55246
  const val REHABILITATION_WORKSPACE_ID: Long = 54669

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
    client.putWorkspace(REHABILITATION_WORKSPACE_ID, rehabilitationWorkspace())
  }

  fun show() {
    WorkspaceUtils.saveWorkspaceToJson(custodyWorkspace(), File("custody-workspace.json"))
    WorkspaceUtils.saveWorkspaceToJson(rehabilitationWorkspace(), File("rehabilitation-workspace.json"))
  }

  fun custodyWorkspace(): Workspace {
    val workspace = Workspace("Custody systems", "Systems related to the confinement of offenders")

    custodyModel(workspace.model)
    custodyViews(workspace.model, workspace.views)

    return workspace
  }

  fun rehabilitationWorkspace(): Workspace {
    val workspace = Workspace("Rehabilitation systems", "Systems related to the rehabilitation of offenders")

    rehabilitationModel(workspace.model)
    rehabilitationViews(workspace.model, workspace.views)
    rehabilitationStyles(workspace.views.configuration.styles)

    return workspace
  }
}
