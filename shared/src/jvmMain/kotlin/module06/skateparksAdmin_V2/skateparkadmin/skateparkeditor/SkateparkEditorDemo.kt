package module06.skateparksAdmin_V2.skateparkadmin.skateparkeditor


import androidx.compose.ui.window.application
import module06.skateparksAdmin_V2.skateparkadmin.shared.SkateparkRepository
import module06.skateparksAdmin_V2.skateparkadmin.skateparkeditor.controller.SkateparkEditorController
import module06.skateparksAdmin_V2.skateparkadmin.skateparkeditor.view.SkateparkEditorWindow
import module06.xtracted.repository.urlFromResources

fun main() {

    // der Skatepark Editor startet erst, wenn das Skatepark Repository implementiert ist

    val url = "/data/skateparkDB".urlFromResources()

    val repository = SkateparkRepository(url)
    val initialId  = 5 // eine ID, die es in der DB gibt (demo purpose only, Editor wird später über Explorer geöffnet, also nur wenn es eine id gibt die man anzeigen kann)

    val controller = SkateparkEditorController(id               = initialId,
                                               repository       = repository,
                                               onNewDefect      = { println("new defect for skatepark $it")},
                                               onDefectSelected = { println("defect selected: $it")},
                                               onFinished       = { println("finished editing") })

    application {
            SkateparkEditorWindow(state         = controller.state,
                                  trigger       = { controller.triggerAction(it)},
                                 )
    }

}