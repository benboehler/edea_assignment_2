package module06.skateparksAdmin_V2.skateparkadmin.skateparkeditor.model

import module06.skateparksAdmin_V2.skateparkadmin.shared.Skatepark
import module06.skateparksAdmin_V2.skateparkadmin.shared.SkateparkState
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class SkateparkFormModelTest {
    @Test
    fun testMapping(){
        //given
        val name         = "Tony Hawk Park"
        val description  = SkateparkState.UNDER_CONSTRUCTION
        val street       = "Parkstrasse 73"
        val municipality = "5210 Windisch"
        val mapsUrl      = ""
        val latitude     = 48.123
        val longitude    = null
        val website      = ""
        val imageUrl     = ""
        val placeid      = ""

        val skatepark = Skatepark(-1, name, description, street, municipality, mapsUrl, latitude, longitude, website, imageUrl, placeid)

        //when
        val mappedSkatepark = SkateparkFormModel(skatepark).asSkatepark()

        //then
        assertEquals(skatepark, mappedSkatepark)
    }

}