package module06.skateparksAdmin_V2.defectsadmin.defecteditor


import androidx.compose.ui.window.application
import module06.skateparksAdmin_V2.defectsadmin.defecteditor.controller.DefectEditorController
import module06.skateparksAdmin_V2.defectsadmin.defecteditor.view.DefectEditorWindow
import module06.skateparksAdmin_V2.defectsadmin.shared.DefectRepository
import module06.xtracted.repository.urlFromResources

fun main() {
    val url = "/data/defectDB".urlFromResources()

    val repository = DefectRepository(url)
    val initialId = 14 // eine ID, die es in der DB gibt (demo purpose only, Editor wird später über Explorer geöffnet, also nur wenn es eine id gibt die man anzeigen kann)

    val controller = DefectEditorController(
        id            = initialId,
        repository    = repository,
        onFinished    = { println("finished editing")},
        onSkateparkSelected = { println("saved; return to skatepark $it") },
    )

    application {
            DefectEditorWindow(state     = controller.state,
                               trigger   = { controller.triggerAction(it) })
    }

}