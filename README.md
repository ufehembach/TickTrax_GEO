Modul 3: Android
Abschlussaufgabe

Name
Fechner
Vorname
Uwe
Arbeitstitel der App
TickTrax-Geo


Zielsetzung
Ziel der Abschlussaufgabe ist es, das Gelernte innerhalb des Android-Moduls in einer eigenen App umzusetzen und diese App abschließend in einer Präsentation vorstellen und erklären zu können. Diese App soll anschließend als Teil eures Portfolios auf GitHub veröffentlicht werden.
Anforderungen an die App
    • Mindestens drei funktionsfähige Fragmente 
    • Navigation über Android Navigation Component
    • Beinhalten von mindestens einer RecyclerView
    • Beinhalten von mindestens einem Api Call (Firebase zählt nicht)
    • Datenbankanbindung (Room und/oder Firebase Datastore)
Pitch
Schreibe in diesem Feld 3-5 Sätze, welche beschreiben, welche Funktionalitäten deine App bieten wird und welchen Nutzen sie erfüllen soll. 
Zur lokationsbezogenen Zeiterfassung (Oberthema - TickTrax.de) muss das Thema GeoLocation und Geo Visualisierung verstanden und implementiert sein. In den kommenden Wochen möchte ich versuchen folgende punkte zu implementieren:

    1. Location Service auf Android implementieren( GPS, Funkzellen, Wifi)
    2. Umwandeln der Geo Daten mit Hilfe von OpenStreetMap in Adressen
    3. Abspeichern der Geo Daten in einer Datenbank (mindestens ROOM, besser FireBase, nochbesser geteilte FireBase)
* Historie der Location
* ggf. Definition der Location(keine Ahnung wie)
    4. Visualisierung der Location 
    5. ggf. Export der Daten auf die eine oder andere Art und Weise



Aufschlüsselung der Anforderungen
Schreibe hier in jedes Feld mit wenigen Sätzen, wie deine App die einzelnen Anforderungen erfüllen soll und welche Technologien du dafür verwenden möchtest.
Mindestens drei funktionsfähige Fragmente:
Home -> Geo Location History im Recycler mit Aggregierung (x mal am Standort gewesen)
Home-Detail -> details zur location mit stunden(Nice2Have) und Recyler mit den Besuchen
Geo Location Map
Settings
Export


Navigation:
Bottom Nav und Hamburger Mix


Anzeigen von Daten innerhalb einer RecyclerView:
Historie der GPS Daten / Historie der Locations


 API Call:
OpenStreetMap
Ggf. Google Bilder Suche


Abspeichern von Daten (Room / Firebase):
Geo Locations
Bereinigte Geo Locations 



Meilensteine
Hier soll festgelegt werden, welche Aufgaben zu welchem Meilenstein erledigt sein sollen. Die Meilensteine sind hierbei immer freitags während der Projektphase und ersetzen damit die Freitags-Aufgabe.
Es ist zwingend notwendig, dass zumindest jeden Freitag der aktuelle Stand der App auf Github gepusht wird! 

Freitag 15.09.23
Pflichtenheft ist ausgefüllt und abgegeben

Freitag 22.09.23
Locations in view model

Freitag 29.09.23
Geo Visualisierung im frontend

Freitag 06.10.23
Openstreetmap api

Freitag 13.10.23
Database Design

Freitag 20.10.23
Database Implementation

Freitag 03.11.23
Frontend Streamlining

Freitag 10.11.23
Export (Reserve Zeit)

Freitag 17.11.23
App ist fertiggestellt - Abschlusspräsentation



Bonus Features
Natürlich soll es möglich sein, über die harten funktionalen Anforderungen hinaus weitere Features in deine App einzubauen. 

Wir wollen euch aber stark dazu raten, an diesen Features erst zu arbeiten, wenn alle anderen Anforderungen erfüllt sind!

Feature
Erklärung
Firebase

Shared Firebase

Export

Location






