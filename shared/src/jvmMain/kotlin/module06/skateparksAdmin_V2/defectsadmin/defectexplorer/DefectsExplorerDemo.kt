package module06.skateparksAdmin_V2.defectsadmin.defectexplorer

import androidx.compose.ui.window.application
import module06.skateparksAdmin_V2.defectsadmin.defectexplorer.controller.DefectExplorerController
import module06.skateparksAdmin_V2.defectsadmin.defectexplorer.view.DefectExplorerWindow
import module06.skateparksAdmin_V2.defectsadmin.shared.DefectRepository
import module06.xtracted.repository.urlFromResources


fun main() {
    val url = "/data/defectDB".urlFromResources()

    val repository = DefectRepository(url)
    val controller = DefectExplorerController(
        repository = repository,
        onCreate = { println("create triggered !") },
        onDefectSelected =  { println("Defect with id $it selected") }
    )

      application {
              DefectExplorerWindow(
                  state        = controller.state,
                  trigger      = { controller.triggerAction(it) }
              )
      }

}