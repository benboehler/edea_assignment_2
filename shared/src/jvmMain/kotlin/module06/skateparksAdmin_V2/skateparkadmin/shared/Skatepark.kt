package module06.skateparksAdmin_V2.skateparkadmin.shared

import kotlin.random.Random

enum class SkateparkState(val description: String) {
    PROJECTED("geplant"),
    OPERATIONAL("in Betrieb"),
    UNDER_CONSTRUCTION("in Bau"),
    UNKNOWN("unbekannt")
}


fun String?.asSkateparkState() : SkateparkState = SkateparkState.entries.find { it.description == this } ?: SkateparkState.UNKNOWN
// hier wurde der description string in der Datenbank gespeichert, im Gegensatz zum Enum.name bei DefectStatus - je nach vorhandener Datenbank resp. Datengrundlage


data class Skatepark(
    val id: Int = Random.nextInt(),
    val name: String = "",
    val description: SkateparkState = SkateparkState.UNKNOWN, //Beschreibung des Skateparks Status: geplant, in Bau, in Betrieb
    val street: String = "",
    val municipality: String = "",
    val mapsUrl: String = "",
    val latitude: Double? = null,
    val longitude: Double? = null,
    val website: String = "",
    val imageUrl: String = "",
    val placeid: String = "",
)