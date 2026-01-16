# Übersicht

Skool.ly ist eine auf Java gecodete Applikation für das Notenmanagement von Schulen. Es ist ausgestattet mit Admin-, Lehrkraft- und Schüleransichten, und hat eine einfache Bedienung. 

# Features
Skool.ly bietet eine Vielzahl an Features, welches folgend alle aufgelistet werden:

## Für SchülerInnen (Students)
- **Dashboard-Übersicht**:
    - Anzeige des **Gesamtdurchschnitts** über alle belegten Kurse hinweg (in Echtzeit berechnet).
    - Boxen-Ansicht aller eingeschriebenen Kurse.

 - **Kurs-Ansicht**:
    - Anzeige von Kursname und der unterrichtenden Lehrkraft.
    - **Aktueller Kursdurchschnitt:** Berechnung der aktuellen Durchschnittsnote im Kurs basierend auf den eingetragenen Noten und deren Gewichtung.
    - **Verständlichkeit der Gewichtungen:** Anzeige, wie viel Prozent verschiedene Notentypen (z.B. Mündlich, Schriftlich etc.) zählen.
    - **Notenübersicht:** Detaillierte Ansicht aller erhaltenen Noten in dem Kurs mit Datum, Notenwert (in Punkten) und Beschreibung.
  
## Für Lehrkräfte (Teachers)
- **Lehrer-Dashboard:**
  - Eine Übersicht aller Kurs, die die Lehrkraft unterrichtet.
    
- **Kursverwaltung:**
  - **Management von Gewichtungen:**
    - Erstellen und Bearbeiten von Gewichtungen für verschiedene Notentypen (Schriftlich, Mündlich, Fachpraktisch, Test).
    - Löschen von Notentypen (inkl. Sicherheitswarnung, falls zu dem Typ bereits Noten existieren).
      
  - **Schülerliste:** Übersicht aller im Kurs eingeschriebenen Schüler mit ihren Namen.
  - **Kursdurchschnitt:** Anzeige des insegsamten Notendurchschnitts aller Schüler im gesamten Kurs.

- **Notenvergabe:**
  - Auswahl eines spezifischen Schülers aus dem Kurs möglich.
  - Detailierte Anzeige des aktuellen Schnitts des ausgewählten Schülers mit einer Auflistung der erzielten Noten des Schülers im jeweiligen Kurs.
  - **Note eintragen:** Hinzufügen einer Note mit Punktwert, Notentyp (Dropdown basierend auf im Kurs bereits definierte Gewichtungen) und Beschreibung.
  - **Notenverwaltung:** Löschen von fehlerhaft eingetragenen Noten einfach gestaltet möglich.
 
 ## Für Administratoren (Admins)
- **Benutzerverwaltung:**
  - **Erstellen neuer Benutzer:** Anlegen von Schülern. Lehrkräften und weiteren Admins.
        
- **Kursverwaltung:**
  - Erstellen neuer Kurse mit Name und Beschreibung.
  - Direkte Zuweisung eines Lehrers zum Kurs.
        
- **Einschreibung eines Schülers:**
  - Einschreiben von Schülern in existierende Kurse.
  - Prüfung auf bereits bestehende Einschreibungen, um Datenbank-Fehler vorzeitig zu vermeiden.
        
- **Passwort-Reset:** Zurücksetzen von Passwörtern für andere Benutzer (Schutzmechanismus: Admins können ihr eigenes Passwort hier nicht ändern, um mehr Sicherheit zu gewährleisten).

## Allgemeine App-Features und UI
- **Modernes UI:**
  - Verwendung von FlatLaf für ein modernes Look & Feel.
  - **Dark Mode / Light Mode:** Umschaltbar über ein Seitenmeü.
  - Flüssiger Wechsel zwischen Ansichten, ohne neue Fenster öffnen zu müssen.
- **Seitenmenü**
  - Ausklappbare Sidebar für generelle Aktionen.
  - **Refresh-Button:** Manuelles Neuladen der Daten aus der Datenbank.
  - **Logout:** Abmelden und Rückkehr zum Login-Screen.
  - **Passwort ändern:** Eingeloggte Benutzer können ihr eigenes Passwort ändern. Dafür ist die Prüfung des alten Passwort für Sicherheit erforderlich.
 
## Logik & Berechnung (Backend)
- **Notenberechnung (GradeCalc):**
  - **Dynamische Gewichtsanpassung:** Wenn in einem Kurs für einen bestimmten Notentyp (z.B: Mündlich) noch keine Note eingetragen wurde, wird dessen Gewichtung ignoriert und die Anteile der anderen Typen werden proportional angepasst. Folgende Formel gilt für Gewichte, die angepasst werden:
    ```
    neues Gewicht = altes Gewicht / Summe aller verbleibenden Gewichte
    ```
  - **Keine fest definierte Notenskala:** Es wird keine fest definierte Notenskala verwendet. Jedoch müssen Zahlen als Noten verwendet werden, da die Berechnung auf diese Zahlen ausgelegt ist. Amerikanische Noten (A-F) sind nicht möglich.
  - **Automatische Ausfilterung:** Wenn ein Kurs oder ein Kursteilnehmer keine Noten hat, werden diese nicht in die Durchschnittsberechnung einbezogen.

## Sicherheit und Datenbank
- **Authentifizierung/Login:**
  - Login-System mit Benutzername und Passwort.
  - Verwenden des SHA-512 Hashingalgorithmus mit einem zufälligen Salt für gehobene Sicherheit.
    
- **Datenbank-Verbindung:**
    - Einsatz von HikariCP zur effizienten Nutzung der Datenbankverbindungen (hält Verbindungen offen -> neue Verbindungen nicht nötig).
    - Verhinderung von SQL-Injections mit ```PreparedStatement```.

# Datenbankstruktur
Folgend wird die Struktur der genutzten Datenbank mithilfe eines Entity-Relationship Diagramms veranschaulicht:
![alt text](https://github.com/Adv35/Skool.ly/blob/master/ER_Diagramm.png?raw=true)

# Klassenstruktur des Projekts
Folgend wird die Struktur des Projekts mithilfe eines UML-Diagramms veranschaulicht. Wichtig ist hierbei, dass aufgrund von Verständnis- und Überblicksgründen einige Klassen, darunter auch die Mehrheit der Panels ausgelassen wurde.

![alt text](https://github.com/Adv35/Skool.ly/blob/master/umlCoreFunctions2.jpg?raw=true)


# Wahl der Programmiersprache & Projektzeitraum
- Die Programmiersprache Java wurde einerseits wegen der Anforderung, aber auch wegen der Objektorientierung gewählt. 
- Der Zeitraum für das Projekt erstreckt sich von ca. Anfang September bis Mitte Januar, also circa 5 Monate, wobei manchmal Pausen aufgrund von Klausuren eingelegt wurden.


# Eigenreflektion
## Komplexe Situationen / Schwierigkeiten
- Schwierigkeiten hatte ich auf jeden Fall bei der Laufzeit bzw. dabei, die App flüssig zum laufen zu bringen, da eine große Verzögerung zwischen Datenbank und Applikation vorhanden war. Ich versuchte daher erstmal SQL-Anfragen durch JOINs und LEFT JOINs zu minimieren, was etwas geholfen hatte, aber trotzdem nicht gereicht hatte. Später kam ich auf die Idee, Connection-Pooling zu verwenden, was sich als die perfekte Lösung für mein Problem darstellte.
- Auch die Notenberechnung hatte mir ein Paar Kopfschmerzen bereitet. Da Standardisierungen vorher nicht getroffen wurden (wie z.B. was passieren soll wenn jemand keine Noten hat bzw. wenn ein Gewicht keine Noten eingetragen hat), habe ich eigene Regeln entworfen, die in meinem Szenario nach meinem Gewissen am sinnvollsten erscheinten.
- Weiterhin war auch die UI/UX eine Herausforderung, da ich von Java Swing kaum Erfahrung hatte. Glücklicherweise habe ich mich mit YouTube-Videos, Stackoverflow und der Hilfe von Freunden gut orientieren können.

## Zeitmanagement
Ich hatte viel Zeit zur Verfügung und habe diese mal effizienter, mal ineffizienter genutzt. Insgesamt habe ich gelernt, früh ein Dashboard/Plan zu machen, bis wann man was fertig zu haben hat, da ich v.a. in September sehr entspannt an die Sache ranging und im Dezember panisch agiert habe. Ich hätte mir die Zeit also besser einteilen können, aber letztendlich habe ich das Projekt so geschafft, dass es meinen Erwartungen entsprochen hat.
