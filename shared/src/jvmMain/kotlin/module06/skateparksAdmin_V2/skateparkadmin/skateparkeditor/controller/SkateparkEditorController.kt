package module06.skateparksAdmin_V2.skateparkadmin.skateparkeditor.controller

import java.net.URI
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.ImageBitmap

import module06.skateparksAdmin_V2.skateparkadmin.shared.SkateparkRepository
import module06.skateparksAdmin_V2.skateparkadmin.skateparkeditor.model.SkateparkFormModel
import module06.xtracted.controller.Controller
import module06.xtracted.repository.urlFromResources
import org.jetbrains.compose.resources.decodeToImageBitmap
import skateparks_v2.shared.generated.resources.Res

class SkateparkEditorController(id: Int,
                                val repository: SkateparkRepository,
                                val onFinished: () -> Unit     = {},
                                val onNewDefect: (Int) -> Unit = {},
                                val onDefectSelected: (Int) -> Unit = {} ) : Controller<SkateparkEditorAction> {


    val url = "/data/defectDB".urlFromResources()
    val defaultImageBitmap = getImageBitmapFromResources("module06/skatepark-default.jpg")

    val controllerScope = CoroutineScope(Job() + Dispatchers.IO)




    var state by mutableStateOf(SkateparkEditorState(title        = "Skatepark Editor Demo",
                                                     formModel    = SkateparkFormModel(repository.readSkatepark(id)!!),
                                                      imageBitmap  = defaultImageBitmap))



    init {
      //  loadImageBitmap(state.formModel.imageUrl.value)
    }

    override fun triggerAction(action: SkateparkEditorAction) {
        if(!action.enabled){
            return
        }
        when(action){

            else -> {}
        }
    }



    fun loadImageBitmap(url: String) {
        controllerScope.launch {
            state = state.copy(imageBitmap = getImageBitmapFromUrl(url))
        }
    }

    private fun getImageBitmapFromResources(fileName: String) : ImageBitmap {
        var img: ImageBitmap?
        runBlocking {
            img =  Res.readBytes("files/$fileName")
                .decodeToImageBitmap()
        }
        return img!!
    }

    private fun getImageBitmapFromUrl(url: String): ImageBitmap {
        return try {
            URI(url).toURL().openStream().readAllBytes().decodeToImageBitmap()
        } catch (e: Exception) {
            defaultImageBitmap
        }
    }

}