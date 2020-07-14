package uk.gov.justice.hmpps.architecture.prison

import com.structurizr.model.Enterprise
import com.structurizr.Workspace
import uk.gov.justice.hmpps.architecture.shared.cloudPlatform
import uk.gov.justice.hmpps.architecture.shared.styles

fun prisonWorkspace(): Workspace {
  val hmpps_prisons = Enterprise("HM Prison & Probation Service")
  val workspace = Workspace("Prison systems", "Systems related to the confinement of offenders").apply {
    model.setEnterprise(hmpps_prisons)
    setId(55246)
  }

  cloudPlatform(workspace.model)
  prisonModel(workspace.model)
  prisonViews(workspace.model, workspace.views)
  styles(workspace.views.configuration.styles)

  return workspace
}