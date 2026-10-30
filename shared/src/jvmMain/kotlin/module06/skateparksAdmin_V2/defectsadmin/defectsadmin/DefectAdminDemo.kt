package module06.skateparksAdmin_V2.defectsadmin.defectsadmin

import androidx.compose.ui.window.application
import module06.skateparksAdmin_V2.defectsadmin.defectsadmin.controller.DefectController
import module06.skateparksAdmin_V2.defectsadmin.defectsadmin.view.DefectWindow
import module06.skateparksAdmin_V2.defectsadmin.shared.DefectRepository
import module06.xtracted.repository.urlFromResources

fun main() {
    val url = "/data/defectDB".urlFromResources()

    val repository = DefectRepository(url)

    val controller = DefectController(
        repository = repository,
        onSkateparkSelected = { println("saved; return to skatepark $it") },
    )

    application {
        DefectWindow(state = controller.state)
    }

}