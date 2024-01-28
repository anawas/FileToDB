# FileToDB

## Zweck
Zeigt, wie mit Kotlin eine Datei als Blob in eine Datenbank gespeichert wird. Eine Testdatei ist hier: `documents/master.pdf`. 

Die Datenbank sollte die folgenden Tabelle enthalten. Einen Dump der Tabelle ist in der Datei `documents/dump-Database.sql`.

```
CREATE TABLE `Documents` (
`id` int NOT NULL AUTO_INCREMENT,
`filename` varchar(255) NOT NULL,
`content` longblob,
PRIMARY KEY (`id`)
```

## Vorbereiten
* Es wird laufende eine MySQL-Datenbankinstallation benötigt. 
* Der Code läuft auf Kotlin
* Das Projekt wurde mit IntellJ-Idea erstellt.

## Quelle
Der Code wurde mit ChatGPT erstellt. Die Anfrage war 
```
"Zeige mir den Code in Kotlin, wie man eine PDF-Datei in MySQL als BLOB speichert."
```