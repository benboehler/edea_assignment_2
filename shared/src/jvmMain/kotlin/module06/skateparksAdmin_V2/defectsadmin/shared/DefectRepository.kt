package module06.skateparksAdmin_V2.defectsadmin.shared

import java.sql.ResultSet
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import module06.xtracted.repository.*


class DefectRepository(val url: String) {

    enum class DefectColumn {
        ID, NAME, DESCRIPTION, STATUS, ISSUE_DATE, RESOLVE_DATE, PARK_ID;
    }

    private val table = "DEFECT"

    fun create(initial: Defect): Defect {
        val insertStmt = """INSERT INTO $table 
                            |            (${DefectColumn.NAME},    ${DefectColumn.DESCRIPTION},    ${DefectColumn.STATUS},                ${DefectColumn.ISSUE_DATE},   ${DefectColumn.RESOLVE_DATE},   ${DefectColumn.PARK_ID})
                            |    VALUES  (${initial.name.asSql()}, ${initial.description.asSql()}, ${initial.status.toString().asSql()},  ${initial.issueDate.asSql()}, ${initial.resolveDate.asSql()}, ${initial.parkId})
                            |    RETURNING ${DefectColumn.ID}
                            |    """.trimMargin()

        val id = insertAndCreateKey(url        = url,
                                    insertStmt = insertStmt)

        return initial.copy(id = id)
    }

    fun readDefect(id: Int): Defect?  =
        readFirst(url   = url,
                  table = table,
                  where = "${DefectColumn.ID} = $id",
                  map   = { asDefect() })


    fun readAllDefects(parkId: Int?): List<Defect> =
        readAll(url   = url,
                table = table,
                where = if (parkId != null) { "${DefectColumn.PARK_ID} = $parkId" } else { "" },
                map   = { asDefect() })

    fun updateDefect(defect: Defect) =
        update(url         = url,
              table        = table,
              id           = defect.id,
              setStatement = """
                                |SET ${DefectColumn.NAME}        = ${defect.name.asSql()}, 
                                |   ${DefectColumn.DESCRIPTION}  = ${defect.description.asSql()},
                                |   ${DefectColumn.STATUS}       = ${defect.status.asSql()},
                                |   ${DefectColumn.ISSUE_DATE}   = ${defect.issueDate.asSql()},        
                                |   ${DefectColumn.RESOLVE_DATE} = ${defect.resolveDate.asSql()},
                                |   ${DefectColumn.PARK_ID}      = ${defect.parkId}
                                |   """.trimMargin())

    fun deleteDefect(id: Int) =
        delete(url   = url,
               table = table,
               id    = id)

    fun deleteAllDefects() =
        deleteAll(url  = url,
                 table = table)


    fun countDefects(): Int =
        count(url   = url,
              table = table)

    private fun ResultSet.asDefect() =
        Defect(id           = getInt("${DefectColumn.ID}"),
               name         = getString("${DefectColumn.NAME}"),
               description  = getString("${DefectColumn.DESCRIPTION}"),
               status       = getString("${DefectColumn.STATUS}").asDefectsState(),
               issueDate    = getString("${DefectColumn.ISSUE_DATE}").asLocalDate()!!,
               resolveDate  = getString("${DefectColumn.RESOLVE_DATE}").asLocalDate(),
               parkId       = getInt("${DefectColumn.PARK_ID}"))

}

private val defectDateTimeFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS")

private fun String?.asLocalDate(): LocalDate? =
    this?.let {
        LocalDateTime.parse(it, defectDateTimeFormatter).toLocalDate()
    }

private fun LocalDate?.asSql(): String? =
    this?.let {
        "'${defectDateTimeFormatter.format(this.atStartOfDay())}'"
    }

private fun DefectState.asSql() : String = "'${this.name}'"

/*
Das ist die aktuelle DB-Definition

create table DEFECT
(
    ID                INTEGER          primary key  AUTOINCREMENT,
    NAME              VARCHAR(255)     NOT NULL,
    DESCRIPTION       VARCHAR(10000) ,
    STATUS            VARCHAR(50)    ,
    ISSUE_DATE        DATE           NOT NULL,      // Timestamp Datum Uhrzeit (YYYY-MM-DD HH:MM:SS.SSS)
    RESOLVE_DATE      DATE           ,
    PARK_ID           INTEGER        ,
);

create unique index DEFECT_ID
    on DEFECT (ID);

create index DEFECT_PARK_ID
    on DEFECT (PARK_ID);


INSERT INTO DEFECT (NAME, DESCRIPTION, ISSUE_DATE, PARK_ID)
VALUES
('Spielgerät defekt','die Rutsche am Spielgerät ist beschädigt','2025-08-12 11:00:00.000',5),
('Müll','der Skatepark ist zugemüllt','2025-08-18 13:33:00.000',7),
('Bank defekt','eine der Bänke ist kaputt','2025-09-05 15:15:00.000',3),
('Beleuchtung','die Beleuchtung im Park funktioniert nicht','2025-09-04 16:42:20.000',4),
('Wasserstelle','die Wasserstelle ist verstopft','2025-09-18 00:00:00.000',1),
('Ramp','die Ramp wackelt stark','2025-09-23 10:12:00.000',5);

 */