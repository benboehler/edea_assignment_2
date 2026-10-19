package module06.skateparksAdmin_V2.skateparkadmin.skateparkeditor.controller

import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.RemoveCircle
import androidx.compose.material.icons.filled.Sync
import module06.xtracted.controller.Action

sealed class SkateparkEditorAction(override val name:    String,
                                   override val icon:    ImageVector? = null,
                                   override val enabled: Boolean      = true) : Action {

    class SetName(val nameText: String) : SkateparkEditorAction("Set name")
    class SetDescription(val descriptionText: String) : SkateparkEditorAction("Set status")
    class SetStreet(val streetText: String) : SkateparkEditorAction("Set street")
    class SetMunicipality(val municipalityText: String) : SkateparkEditorAction("Set municipality")
    class SetWebsite(val websiteText: String) : SkateparkEditorAction("Set website")
    class SetImageUrl(val imageUrlText: String) : SkateparkEditorAction("Set image URL")
    class SetLatitude(val latitudeText: String) : SkateparkEditorAction("Set latitude")
    class SetLongitude(val longitudeText: String) : SkateparkEditorAction("Set longitude")
    object Reload : SkateparkEditorAction("Reload", Icons.Filled.Sync)
    object Back : SkateparkEditorAction("Back", Icons.AutoMirrored.Filled.ArrowBack)
    object Update : SkateparkEditorAction("Save")
    object Delete : SkateparkEditorAction("Delete", Icons.Filled.RemoveCircle)
}