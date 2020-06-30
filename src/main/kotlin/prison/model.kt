package uk.gov.justice.hmpps.architecture.prison

import com.structurizr.model.CreateImpliedRelationshipsUnlessAnyRelationshipExistsStrategy
import com.structurizr.model.Model

fun prisonModel(model: Model) {
    model.setImpliedRelationshipsStrategy(CreateImpliedRelationshipsUnlessAnyRelationshipExistsStrategy())

    val aws = model.addSoftwareSystem("AWS", "Amazon Web Services").apply {
        addTags("External")
        addContainer("ElasticSearch", "Elastic Search Service", null)
    }

    HmmpsAuth(model)
    NOMIS(model)
    nDelius(model)
    NDH(model).system
    Pathfinder(model)
}
