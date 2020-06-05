package uk.gov.justice.hmpps.architecture

import java.util.Properties

import com.structurizr.Workspace
import com.structurizr.api.StructurizrClient
import com.structurizr.util.WorkspaceUtils

import uk.gov.justice.hmpps.architecture.custody.CustodyModel

object App {
  @JvmStatic
  fun main(@Suppress("UNUSED_PARAMETER") args: Array<String>) {
    val workspaceId: Long = 55246

    val client = StructurizrClient(System.getenv("STRUCTURIZR_API_KEY"), System.getenv("STRUCTURIZR_API_SECRET"))
    val workspace = client.getWorkspace(workspaceId).apply {
      name = "Custody systems"
      description = "Systems related to the confinement of offenders"
    }

    CustodyModel(workspace.model)

    client.putWorkspace(workspaceId, workspace)
  }
}
