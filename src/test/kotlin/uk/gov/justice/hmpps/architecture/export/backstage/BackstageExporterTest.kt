package uk.gov.justice.hmpps.architecture.export.backstage

import com.structurizr.util.WorkspaceUtils
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class BackstageExporterTest {

  val backstageExporter = BackstageExporter()

  @Test
  fun `Converts workspace to backstage yaml`() {
    val workspace = WorkspaceUtils.fromJson("/structurizr.json".readResourceAsText())
    val backstageExport = backstageExporter.export(workspace)

    assertThat(backstageExport).isEqualTo("/backstage.yaml".readResourceAsText())
  }

  fun String.readResourceAsText(): String {
    return BackstageExporterTest::class.java.getResource(this).readText()
  }
}
