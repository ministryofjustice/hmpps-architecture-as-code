package uk.gov.justice.hmpps.architecture

import com.structurizr.Workspace
import com.structurizr.api.StructurizrClient
import com.structurizr.graphviz.GraphvizAutomaticLayout
import com.structurizr.graphviz.RankDirection
import com.structurizr.util.WorkspaceUtils
import uk.gov.justice.hmpps.architecture.export.backstage.BackstageExporter
import java.io.File

object App {
  val EXPORT_LOCATION = File("./exports")
  val ACTIONS: Map<String, (Workspace) -> Unit> = mapOf(
    "--push" to ::pushToRemote,
    "--backstage" to ::exportToBackstage
  )

  @JvmStatic
  fun main(args: Array<String>) {
    val workspace = defineWorkspace()

    val graphviz = GraphvizAutomaticLayout(EXPORT_LOCATION)
    graphviz.setRankDirection(RankDirection.TopBottom)
    graphviz.setRankSeparation(400.0)
    graphviz.setNodeSeparation(300.0)
    graphviz.apply(workspace)

    if (args.isEmpty()) {
      writeToFile(workspace)
      return
    }

    args.forEach { arg -> ACTIONS.get(arg)?.invoke(workspace) }
  }

  private fun pushToRemote(workspace: Workspace) {
    val client = StructurizrClient(System.getenv("STRUCTURIZR_API_KEY"), System.getenv("STRUCTURIZR_API_SECRET"))
    val workspaceId = System.getenv("STRUCTURIZR_WORKSPACE_ID")?.toLongOrNull() ?: workspace.id
    client.workspaceArchiveLocation = EXPORT_LOCATION
    client.setMergeFromRemote(false)

    workspace.version = System.getenv("BUILD_VERSION")
    client.putWorkspace(workspaceId, workspace)
  }

  private fun exportToBackstage(workspace: Workspace) {
    val backstageExport = BackstageExporter().export(workspace)
    File(EXPORT_LOCATION, "backstage-${workspace.id}.yaml").writeText(backstageExport)
  }

  private fun writeToFile(workspace: Workspace) {
    val targetFile = File(EXPORT_LOCATION, "structurizr-${workspace.id}-local.json")
    WorkspaceUtils.saveWorkspaceToJson(workspace, targetFile)
    println("Wrote workspace to '$targetFile'")
  }
}
