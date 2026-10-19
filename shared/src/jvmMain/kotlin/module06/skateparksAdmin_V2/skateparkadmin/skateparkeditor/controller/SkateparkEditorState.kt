package module06.skateparksAdmin_V2.skateparkadmin.skateparkeditor.controller

import androidx.compose.ui.graphics.ImageBitmap
import module06.skateparksAdmin_V2.defectsadmin.shared.Defect
import module06.skateparksAdmin_V2.skateparkadmin.skateparkeditor.model.SkateparkFormModel

data class SkateparkEditorState(val title : String,
                                val formModel: SkateparkFormModel,
                                val imageBitmap: ImageBitmap,
                                val parkDefects: List<Defect> = emptyList()
)