# 💡 Overview

Skool.ly ist eine auf Java gecodete Applikation für das Notenmanagement von Schulen. Es ist ausgestattet mit Admin-, Lehrkraft- und Schüleransichten, und hat eine einfache Bedienung. 

# ✨ Features
Skool.ly bietet eine Vielzahl an Features, welches folgend alle aufgelistet werden:

## 🎓 Für SchülerInnen (Students)
- **Dashboard-Übersicht**:
    - Anzeige des **Gesamtdurchschnitts** über alle belegten Kurse hinweg (in Echtzeit berechnet).
    - Boxen-Ansicht aller eingeschriebenen Kurse.

 - **Kurs-Ansicht**:
    - Anzeige von Kursname und der unterrichtenden Lehrkraft.
    - **Aktueller Kursdurchschnitt:** Berechnung der aktuellen Durchschnittsnote im Kurs basierend auf den eingetragenen Noten und deren Gewichtung.
    - **Verständlichkeit der Gewichtungen:** Anzeige, wie viel Prozent verschiedene Notentypen (z.B. Mündlich, Schriftlich etc.) zählen.
    - **Notenübersicht:** Detaillierte Ansicht aller erhaltenen Noten in dem Kurs mit Datum, Notenwert (in Punkten) und Beschreibung.
  
## 👨‍🏫 Für Lehrkräfte (Teachers)
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
 
 ## 👨‍💻 Für Administratoren (Admins)
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
  

       


