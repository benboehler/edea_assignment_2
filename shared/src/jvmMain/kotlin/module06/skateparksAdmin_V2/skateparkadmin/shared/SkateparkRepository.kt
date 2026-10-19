package module06.skateparksAdmin_V2.skateparkadmin.shared


import java.sql.ResultSet
import module06.xtracted.repository.asSql
import module06.xtracted.repository.count
import module06.xtracted.repository.delete
import module06.xtracted.repository.deleteAll
import module06.xtracted.repository.insertAndCreateKey
import module06.xtracted.repository.readAll
import module06.xtracted.repository.readFirst
import module06.xtracted.repository.update

class SkateparkRepository(val url: String) {

    enum class SkateparkColumn {
        ID,
        NAME,
        DESCRIPTION,
        STREET,
        MUNICIPALITY,
        MAPSURL,
        LATITUDE,
        LONGITUDE,
        WEBSITE,
        FEATUEREDIMAGE,
        PLACEID;
    }

    private val table = "SKATEPARK"

    fun create(initial: Skatepark): Skatepark {
        val insertStmt = """INSERT INTO $table 
                            |            (${SkateparkColumn.NAME}, ${SkateparkColumn.DESCRIPTION}, ${SkateparkColumn.STREET},
                            |             ${SkateparkColumn.MUNICIPALITY}, ${SkateparkColumn.MAPSURL}, ${SkateparkColumn.LATITUDE},
                            |             ${SkateparkColumn.LONGITUDE}, ${SkateparkColumn.WEBSITE}, ${SkateparkColumn.FEATUEREDIMAGE}, ${SkateparkColumn.PLACEID})
                            |    VALUES  (${initial.name.asSql()}, ${initial.description.description.asSql()}, ${initial.street.asSql()},
                            |             ${initial.municipality.asSql()}, ${initial.mapsUrl.asSql()}, ${initial.latitude.asSql()},
                            |             ${initial.longitude.asSql()}, ${initial.website.asSql()}, ${initial.imageUrl.asSql()}, ${initial.placeid.asSql()})
                            |    RETURNING ${SkateparkColumn.ID}
                            |    """.trimMargin()

        val id = insertAndCreateKey(url        = url,
                                    insertStmt = insertStmt)

        return initial.copy(id = id)
    }

    fun readSkatepark(id: Int): Skatepark? =
        readFirst(url   = url,
                  table = table,
                  where = "${SkateparkColumn.ID} = $id",
                  map   = { asSkatepark() })


    fun readAllSkateparks(): List<Skatepark> =
        readAll(url     = url,
                table   = table,
                orderBy = SkateparkColumn.MUNICIPALITY.name,
                map     = { asSkatepark() })

    fun updateSkatepark(skatepark: Skatepark) =
        update(url          = url,
               table        = table,
               id           = skatepark.id,
               setStatement = """
                                  |SET ${SkateparkColumn.NAME}          = ${skatepark.name.asSql()},
                                  |   """.trimMargin()
        )


    fun deleteSkatepark(id: Int) =
        delete(url   = url,
               table = table,
               id    = id)

    fun deleteAllSkateparks() =
        deleteAll(url   = url,
                  table = table)

    fun countSkateparks(): Int =
        count(url  = url,
             table = table)

    private fun ResultSet.asSkatepark() =
        Skatepark(id           = getInt(SkateparkColumn.ID.name),
                  name         = getString(SkateparkColumn.NAME.name) ?: "",
                  description  = getString(SkateparkColumn.DESCRIPTION.name).asSkateparkState(),
                  street       = getString(SkateparkColumn.STREET.name) ?: "",
                  municipality = getString(SkateparkColumn.MUNICIPALITY.name) ?: "",
                  mapsUrl      = getString(SkateparkColumn.MAPSURL.name) ?: "",
                  latitude     = getString(SkateparkColumn.LATITUDE.name)?.toDoubleOrNull(),
                  longitude    = getString(SkateparkColumn.LONGITUDE.name)?.toDoubleOrNull(),
                  website      = getString(SkateparkColumn.WEBSITE.name) ?: "",
                  imageUrl     = getString(SkateparkColumn.FEATUEREDIMAGE.name) ?: "",
                  placeid      = getString(SkateparkColumn.PLACEID.name) ?: "")


}


/*
Das ist die aktuelle DB-Definition

Name,Description,Fulladdress,Street,Municipality,Categories,Time Zone,Amenities,Phone,Phones,Claimed,Review Count,Average Rating,Review URL,Google Maps URL,Latitude,Longitude,Website,Domain,Opening Hours,Featured Image,Cid,Fid,Place Id
"BADEN, Skate Park",in Betrieb,"Im Langacker 17, 5405 Baden",Im Langacker 17,5405 Baden,Skatepark,Europe/Zurich,,,,YES,75,4.5,https://search.google.com/local/reviews?placeid=ChIJbwaslFETkEcRVeCVJfLPzrE&q=skateparks&authuser=0&hl=de&gl=CH,https://www.google.com/maps?cid=12812406628835254357,47.4586397,8.2867355,https://www.baden.ch/skatepark,baden.ch,"Sonntag: [08:00-23:00], Montag: [08:00-23:00], Dienstag: [08:00-23:00], Mittwoch: [08:00-23:00], Donnerstag: [08:00-23:00], Freitag: [08:00-23:00], Samstag: [08:00-23:00]",https://lh3.googleusercontent.com/gps-cs-s/AC9h4nr6mJJr3-MnT_Vy-IfTnAAD8bK5pD_0yRsZcuHWDXJcU7x8cB01RbIuHOPs6eNMebPyDrYa-BXQkUJ3os5YNYDGoxy8AsddDALjXm7QDzLnnCZrmlAyKRimbA8RP4zbSbk_ezZAaw=w191-h92-k-no,1.28124066288353E+019,0x4790135194ac066f:0xb1cecff22595e055,ChIJbwaslFETkEcRVeCVJfLPzrE


create table SKATEPARK
(
    ID             INTEGER        primary key  AUTOINCREMENT,
    NAME           VARCHAR(255)   ,
    DESCRIPTION    VARCHAR(255)   ,
    STREET         VARCHAR(255)   ,
    MUNICIPALITY   VARCHAR(255)   ,
    MAPSURL        VARCHAR(255)   ,
    LATITUDE       VARCHAR(255)   ,
    LONGITUDE      VARCHAR(255)   ,
    WEBSITE        VARCHAR(255)   ,
    FEATUEREDIMAGE VARCHAR(255)   ,
    PLACEID        VARCHAR(255)
);

create unique index SKATEPARK_ID
    on SKATEPARK (ID);




INSERT INTO SKATEPARK (NAME, DESCRIPTION, STREET, MUNICIPALITY, MAPSURL, LATITUDE, LONGITUDE, WEBSITE, FEATUEREDIMAGE, PLACEID)
VALUES
('BADEN, Skate Park','in Betrieb','Im Langacker 17','5405 Baden','https://www.google.com/maps?cid=12812406628835254357','47.4586397','8.2867355','https://www.baden.ch/skatepark','https://lh3.googleusercontent.com/gps-cs-s/AC9h4nr6mJJr3-MnT_Vy-IfTnAAD8bK5pD_0yRsZcuHWDXJcU7x8cB01RbIuHOPs6eNMebPyDrYa-BXQkUJ3os5YNYDGoxy8AsddDALjXm7QDzLnnCZrmlAyKRimbA8RP4zbSbk_ezZAaw=w191-h92-k-no','ChIJbwaslFETkEcRVeCVJfLPzrE');
... weitere Einträge ...
 */
