package module06.xtracted.repository

import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardCopyOption
import java.sql.DriverManager
import java.sql.ResultSet
import java.sql.SQLException
import java.time.LocalDate
import java.util.logging.Logger
import kotlin.time.Duration

/*
Eine Sammlung von praktischen Funktionen für das DB-Handling
 */

fun <T> readFirst(url: String, table: String, columns: String = "*", where: String, map: ResultSet.() -> T?) : T? =
    DriverManager.getConnection(url)
        .use {
            val logger: Logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME)

            val sql = "SELECT $columns FROM $table WHERE $where"

            logger.info(sql)
            val resultSet = it.createStatement()
                .executeQuery(sql)

            if (resultSet.next()) {
                resultSet.map()
            } else {
                null
            }
        }

fun <T> readAll(url: String, table: String, columns: String = "*", where: String = "", orderBy : String = "ID", map: ResultSet.() -> T) : List<T> =
    DriverManager.getConnection(url)
        .use {
            val logger: Logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME)

            val sql = "SELECT $columns FROM $table ${if (where.isNotEmpty()) "WHERE $where" else ""} ORDER BY $orderBy DESC"

            logger.info(sql)
            val resultSet = it.createStatement()
                .executeQuery(sql)

            buildList {
                while (resultSet.next()) {
                    add(resultSet.map())
                }
            }
        }

fun update(url: String, table: String, id: Int, setStatement: String, idName: String = "ID") : Unit =
    DriverManager.getConnection(url)
        .use {
            val logger: Logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME)

            val sql = "UPDATE $table $setStatement WHERE $idName = $id"

            logger.info(sql)

            try {
                it.createStatement()
                    .executeUpdate(sql)
            } catch (e: SQLException) {
                logger.severe { e.localizedMessage }
            }
        }

fun count(url: String, table: String, idName: String = "ID") : Int =
    DriverManager.getConnection(url)
        .use {
            val sql = "SELECT COUNT($idName) FROM $table"

            val resultSet = it.createStatement()
                .executeQuery(sql)

            resultSet.next()

            resultSet.getInt(1)
        }

fun insertAndCreateKey(url: String, insertStmt: String) : Int =
    DriverManager.getConnection(url)
        .use {
            val logger: Logger = Logger.getLogger(Logger.GLOBAL_LOGGER_NAME)

            logger.info(insertStmt)

            try {
                val start = System.currentTimeMillis()

                val rs = it.createStatement().executeQuery(insertStmt)

                logger.info("""$insertStmt 
                    |   took ${System.currentTimeMillis() - start} msec""".trimMargin())

                return rs.getInt(1)
            } catch (e: SQLException) {
                logger.severe("FAIL: $insertStmt")
                logger.severe { e.localizedMessage }
                throw e
            }
        }

fun delete(url: String, table: String, id: Int) : Unit =
    DriverManager.getConnection(url)
        .use {
            val sql = "DELETE FROM $table WHERE ID = $id"

            it.createStatement()
                .execute(sql)
        }

fun deleteAll(url: String, table: String) : Unit =
    DriverManager.getConnection(url)
        .use {
            val sql = "DELETE FROM $table"

            it.createStatement()
                .execute(sql)
        }


// build folder - delivered sw (Cave: every build will override the existing DB)
fun String.urlFromResources(): String {
    val resourcePath = removePrefix("/")
    val fileName = resourcePath.substringAfterLast('/')

    val targetDirectory = Files.createDirectories(
        Path.of(System.getProperty("user.home"), ".edea", "database")
    )

    val targetFile = targetDirectory.resolve(fileName)

    val inputStream = object {}.javaClass.classLoader.getResourceAsStream(resourcePath)
        ?: error("Resource not found: $this")

    inputStream.use { input ->
        Files.copy(input, targetFile, StandardCopyOption.REPLACE_EXISTING)
    }

    return "jdbc:sqlite:${targetFile.toAbsolutePath()}"
}

// dev working dir - test data
fun String.urlFromWorkingDirectory(): String =
    "jdbc:sqlite:./shared/src/commonMain/resources$this"

fun String.asSql() = "'${this.replace("'", "''")}'"
fun LocalDate.asSql() = "'$this'"
fun Duration.asSql() = "'$this'"
fun Number?.asSql() = this?.let { "'$this'" }
