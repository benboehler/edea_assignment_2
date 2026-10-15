package module06.xtracted.view.theme

import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import org.jetbrains.compose.resources.Font
import skateparks_v2.shared.generated.resources.Res
import skateparks_v2.shared.generated.resources.RotisSemiSansStd_Bold
import skateparks_v2.shared.generated.resources.RotisSemiSansStd_ExtraBold
import skateparks_v2.shared.generated.resources.RotisSemiSansStd_Italic
import skateparks_v2.shared.generated.resources.RotisSemiSansStd_Light
import skateparks_v2.shared.generated.resources.RotisSemiSansStd_LightIt
import skateparks_v2.shared.generated.resources.lato_bla
import skateparks_v2.shared.generated.resources.lato_bol
import skateparks_v2.shared.generated.resources.lato_extra_light
import skateparks_v2.shared.generated.resources.lato_hai
import skateparks_v2.shared.generated.resources.lato_lig
import skateparks_v2.shared.generated.resources.lato_reg

@Composable
private fun Rotis() = FontFamily(
        Font(resource = Res.font.RotisSemiSansStd_Light,     weight = FontWeight.Normal,     style = FontStyle.Normal),
        Font(resource = Res.font.RotisSemiSansStd_Light,     weight = FontWeight.Medium,     style = FontStyle.Normal),
        Font(resource = Res.font.RotisSemiSansStd_Light,     weight = FontWeight.Light,      style = FontStyle.Normal),
        Font(resource = Res.font.RotisSemiSansStd_Light,     weight = FontWeight.Thin,       style = FontStyle.Normal),
        Font(resource = Res.font.RotisSemiSansStd_Light,     weight = FontWeight.ExtraLight, style = FontStyle.Normal),
        Font(resource = Res.font.RotisSemiSansStd_Bold,      weight = FontWeight.Bold,       style = FontStyle.Normal),
        Font(resource = Res.font.RotisSemiSansStd_Bold,      weight = FontWeight.SemiBold,   style = FontStyle.Normal),
        Font(resource = Res.font.RotisSemiSansStd_ExtraBold, weight = FontWeight.ExtraBold,  style = FontStyle.Normal),
        Font(resource = Res.font.RotisSemiSansStd_ExtraBold, weight = FontWeight.Black,      style = FontStyle.Normal),
        Font(resource = Res.font.RotisSemiSansStd_Italic,    weight = FontWeight.Normal,     style = FontStyle.Italic),
        Font(resource = Res.font.RotisSemiSansStd_LightIt,   weight = FontWeight.Light,      style = FontStyle.Italic)
        )

@Composable
private fun Lato() = FontFamily(
        Font(Res.font.lato_extra_light, FontWeight.ExtraLight),
        Font(Res.font.lato_hai,         FontWeight.Thin),
        Font(Res.font.lato_lig,         FontWeight.Light),
        Font(Res.font.lato_reg,         FontWeight.Normal),
        Font(Res.font.lato_bol,         FontWeight.Bold),
        Font(Res.font.lato_bla,         FontWeight.Black)
                             )

@Composable
fun EdeaTypography(rotis: Boolean) = Typography().run {
    val fontFamily = if(rotis) Rotis() else Lato()
    copy(
            displayLarge = displayLarge.copy(fontFamily = fontFamily),
            displayMedium = displayMedium.copy(fontFamily = fontFamily),
            displaySmall = displaySmall.copy(fontFamily = fontFamily),
            headlineLarge = headlineLarge.copy(fontFamily = fontFamily),
            headlineMedium = headlineMedium.copy(fontFamily = fontFamily),
            headlineSmall = headlineSmall.copy(fontFamily = fontFamily),
            titleLarge = titleLarge.copy(fontFamily = fontFamily),
            titleMedium = titleMedium.copy(fontFamily = fontFamily),
            titleSmall = titleSmall.copy(fontFamily = fontFamily),
            bodyLarge = bodyLarge.copy(fontFamily = fontFamily),
            bodyMedium = bodyMedium.copy(fontFamily = fontFamily),
            bodySmall = bodySmall.copy(fontFamily = fontFamily),
            labelLarge = labelLarge.copy(fontFamily = fontFamily),
            labelMedium = labelMedium.copy(fontFamily = fontFamily),
            labelSmall = labelSmall.copy(fontFamily = fontFamily)
        )
}