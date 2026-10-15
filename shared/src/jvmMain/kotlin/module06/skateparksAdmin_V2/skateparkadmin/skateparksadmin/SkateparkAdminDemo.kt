package module06.skateparksAdmin_V2.skateparkadmin.skateparksadmin

import androidx.compose.ui.window.application
import module06.skateparksAdmin_V2.skateparkadmin.shared.SkateparkRepository
import module06.skateparksAdmin_V2.skateparkadmin.skateparksadmin.controller.SkateparkController
import module06.skateparksAdmin_V2.skateparkadmin.skateparksadmin.view.SkateparkWindow
import module06.xtracted.repository.urlFromResources

fun main() {
    val url = "/data/skateparkDB".urlFromResources()

    val repository = SkateparkRepository(url)

    val controller = SkateparkController(repository       = repository,
                                         onNewDefect      = { println("create new defect for skatepark $it")},
                                         onDefectSelected = { println ("Defect selected: $it")})

    application {
        SkateparkWindow(state = controller.state)
    }

}