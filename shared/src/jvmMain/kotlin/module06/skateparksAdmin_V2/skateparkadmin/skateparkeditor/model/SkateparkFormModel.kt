package module06.skateparksAdmin_V2.skateparkadmin.skateparkeditor.model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import module06.skateparksAdmin_V2.skateparkadmin.shared.Skatepark
import module06.xtracted.data.Attribute

class SkateparkFormModel(skatepark: Skatepark){
    var description  by mutableStateOf(value = Attribute(caption ="Status",       value = skatepark.description,     valueAsText = skatepark.description.description))

    var id           by mutableStateOf(value = Attribute(caption ="Id",           value = skatepark.id,              valueAsText = skatepark.id.toString()))
    var name         by mutableStateOf(value = Attribute(caption ="Name",         value = skatepark.name,            valueAsText = skatepark.name))
    var street       by mutableStateOf(value = Attribute(caption ="Street",       value = skatepark.street,          valueAsText = skatepark.street))
    var municipality by mutableStateOf(value = Attribute(caption ="Municipality", value = skatepark.municipality,    valueAsText = skatepark.municipality))
    var mapsUrl      by mutableStateOf(value = Attribute(caption ="MapsUrl",      value = skatepark.mapsUrl,         valueAsText = skatepark.mapsUrl))
    var latitude     by mutableStateOf(value = Attribute(caption ="Latitude",     value = skatepark.latitude?: 0.0,  valueAsText = skatepark.latitude?.toString() ?: ""))
    var longitude    by mutableStateOf(value = Attribute(caption ="Longitude",    value = skatepark.longitude?: 0.0, valueAsText = skatepark.longitude?.toString() ?: ""))
    var website      by mutableStateOf(value = Attribute(caption ="Website",      value = skatepark.website,         valueAsText = skatepark.website))
    var imageUrl     by mutableStateOf(value = Attribute(caption ="ImageUrl",     value = skatepark.imageUrl,        valueAsText = skatepark.imageUrl))
    var placeid      by mutableStateOf(value = Attribute(caption ="Placeid",      value = skatepark.placeid,         valueAsText = skatepark.placeid))

    val isValid: Boolean
        get() = listOf(id, name, description, street, municipality, mapsUrl,
                       latitude, longitude, website, imageUrl, placeid).all { it.isValid }

    fun asSkatepark(): Skatepark =
        Skatepark(id.value,
                  name.value,
                  description.value,
                  street.value,
                  municipality.value,
                  mapsUrl.value,
                  if(latitude.value == 0.0) null else latitude.value,
                  if(longitude.value == 0.0) null else longitude.value,
                  website.value,
                  imageUrl.value,
                  placeid.value)
}