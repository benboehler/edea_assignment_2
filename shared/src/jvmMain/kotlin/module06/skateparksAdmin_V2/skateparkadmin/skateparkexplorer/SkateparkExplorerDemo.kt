package module06.skateparksAdmin_V2.skateparkadmin.skateparkexplorer

import androidx.compose.ui.window.application
import module06.skateparksAdmin_V2.skateparkadmin.shared.SkateparkRepository
import module06.skateparksAdmin_V2.skateparkadmin.skateparkexplorer.controller.SkateparkExplorerController
import module06.skateparksAdmin_V2.skateparkadmin.skateparkexplorer.view.SkateparkExplorerWindow
import module06.xtracted.repository.urlFromResources


fun main() {
    val url = "/data/skateparkDB".urlFromResources()

    val repository = SkateparkRepository(url)
    val controller = SkateparkExplorerController(
        repository = repository,
        onCreate = { println("create triggered !") },
        onSkateparkSelected = { println("Skatepark with id $it selected") }
    )

      application {
          SkateparkExplorerWindow(
                  state        = controller.state,
                  trigger      = { controller.triggerAction(it) }
              )
      }

}