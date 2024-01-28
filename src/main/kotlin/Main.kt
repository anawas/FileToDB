import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.sql.Connection
import java.sql.DriverManager
import java.sql.PreparedStatement
import java.sql.ResultSet

/**
 * Wandelt ein Dokument in einen Bytestrem um und
 * speichert ein Dokument als BLOB in die Datenbank.
 * Argumente:
 * connection: die Verbindung zur Datenbank
 * filename: der Name der Datei auf dem Datenträger, welcher in die der DB gelesen werden soll
 */
fun saveDocumentToDb(connection: Connection, filename: String) {
    val file = File(filename)
    val fileInputStream = FileInputStream(file)
    val byteArray = ByteArray(file.length().toInt())
    fileInputStream.read(byteArray)
    fileInputStream.close()

    // SQL-Anweisung vorbereiten
    val preparedStatement: PreparedStatement =
        connection.prepareStatement("INSERT INTO Documents (filename, content) VALUES (?, ?)")
    preparedStatement.setString(1, filename)
    preparedStatement.setBytes(2, byteArray)

    // BLOB in die Datenbank einfügen
    preparedStatement.executeUpdate()
    preparedStatement.close()
    println("PDF-Datei erfolgreich in der Datenbank gespeichert.")

}

/**
 * Liest den Inhalt der Datei als BLOB aus der Datenbank aus,
 * und schreibt es als Bytestream in ein Dokument.
 * Argumente:
 * connection: die Verbindung zur Datenbank
 * filename: der Name der Datei, welcher aus der DB gelesen werden soll. Der Name
 * muss als Attribute in der Tabelle gespeichert sein. Das File wird dann unter
 * dem gleichen Namen auf der Festplatte gespeichert.
 */
fun getDocumentFromDb(connection: Connection, filename: String) {
    // SQL-Anweisung vorbereiten, um die BLOB-Daten abzurufen
    val preparedStatement: PreparedStatement =
        connection.prepareStatement("SELECT content FROM Documents WHERE filename = ?")
    preparedStatement.setString(1, filename)  // Setze die entsprechende Id deines Eintrags

    val resultSet: ResultSet = preparedStatement.executeQuery()

    if (resultSet.next()) {
        // BLOB-Daten aus dem ResultSet erhalten
        val blobData = resultSet.getBytes("content")

        // Datei auf dem Dateisystem speichern
        val outputFile = File(filename)
        val fileOutputStream = FileOutputStream(outputFile)
        fileOutputStream.write(blobData)
        fileOutputStream.close()
    }
    println("PDF-Datei erfolgreich aus der Datenbank geladen.")
}

fun main() {
    // Verbindung zur MySQL-Datenbank herstellen
    val url = "jdbc:mysql://localhost:3306/<database>"
    val user = "<username>"
    val password = "<password>"

    try {
        Class.forName("com.mysql.cj.jdbc.Driver")
        val connection: Connection = DriverManager.getConnection(url, user, password)

        // Datei in DB speichern
        saveDocumentToDb(connection, "name/der/datei.pdf")

        // Datei aus der DB lesen
        getDocumentFromDb(connection, "dateiname-in-db.pdf")
        connection.close()

    } catch (e: Exception) {
        e.printStackTrace()
    }
}
