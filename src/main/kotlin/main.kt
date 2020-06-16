package uk.gov.justice.hmpps.architecture

import com.structurizr.Workspace
import com.structurizr.api.StructurizrClient
import uk.gov.justice.hmpps.architecture.custody.*

object App {
  const val CUSTODY_WORKSPACE_ID: Long = 55246

  @JvmStatic
  fun main(@Suppress("UNUSED_PARAMETER") args: Array<String>) {
    val client = StructurizrClient(System.getenv("STRUCTURIZR_API_KEY"), System.getenv("STRUCTURIZR_API_SECRET"))
    val workspace = Workspace("Custody systems", "Systems related to the confinement of offenders")

    custodyModel(workspace.model)
    custodyViews(workspace.model, workspace.views)

    client.putWorkspace(CUSTODY_WORKSPACE_ID, workspace)
  }
}
