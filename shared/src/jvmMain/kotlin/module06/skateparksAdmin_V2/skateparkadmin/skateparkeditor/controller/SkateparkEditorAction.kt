package module06.skateparksAdmin_V2.skateparkadmin.skateparkeditor.controller

import androidx.compose.ui.graphics.vector.ImageVector
import module06.xtracted.controller.Action

sealed class SkateparkEditorAction(override val name:    String,
                                   override val icon:    ImageVector? = null,
                                   override val enabled: Boolean      = true) : Action {

    class SetName(val nameText: String) : SkateparkEditorAction("Set name")
    class SetLatitude(val latitudeText: String) : SkateparkEditorAction("Set latitude")
    class SetLongitude(val longitudeText: String) : SkateparkEditorAction("Set longitude")
    object Reload : SkateparkEditorAction("Reload")
    object Back : SkateparkEditorAction("Back")
}