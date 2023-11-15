# POC: TickTrax-Geo

Uwe Fechner; info@ticktrax.de, 2023-11


# Einleitung

Das Bundesarbeitsgericht (BAG) hat mit Beschluss vom 13. September 2022 (Az. 1 ABR 22/21), festgestellt, dass in Deutschland die gesamte Arbeitszeit der Arbeitnehmerinnen und Arbeitnehmer aufzuzeichnen ist. Arbeitgeber sind nach § 3 Abs. 2 Nr. 1 des Arbeitsschutzgesetzes (ArbSchG) - in unionskonformer Auslegung - verpflichtet, ein System einzuführen, mit dem die von den Arbeitnehmern geleistete Arbeitszeit erfasst werden kann. Damit hat das BAG verbindlich entschieden, dass das Urteil des Europäischen Gerichtshofs (EuGH) vom 14. Mai 2019 (EuGH Rs. 55/18 CCOO) auch von den deutschen Arbeitgebern zu beachten ist.
[Bundesministerium für Arbeit und Soziales](https://www.bmas.de/DE/Arbeit/Arbeitsrecht/Arbeitnehmerrechte/Arbeitszeitschutz/Fragen-und-Antworten/faq-arbeitszeiterfassung.html)

# Idee

Hier würde sich doch eine einfache und kostengünstige APP für Smartphone als Lösung für kleine Firmen und Arbeitsgruppen anbieten.  
  
Zeiterfassung durch  
* Button --> an / aus / pause  
* GEO daten auswerten --> wer ist wie lange an welchem Ort  
* RFID am Lager, Büro, LKW, Baustelle,.. 
 (für Leute die kein GPS nutzen wollen)  
  
Das die Gesamtlösung deutlich zu groß für einen Zeitraum von 8 Wochen ist, habe ich mich für einen POC zur Auswertung der Geo Daten entschieden -> TickTrax-GEO

# Disclaimer
Disclaimer:  
Es ist in keiner Weise gegeben, daß die App oder der POC Rechtlichen und Datenschutz Erfordernisse entspricht.  
Dies ist ein komplexes Gebiet und bis jetzt nicht behandelt.  
Use at your on risk!

# Location vs. Place
  
sagen wir mal wir haben lat und lon  
52,5247834, 13,4067613 (das ist eine Location)
dann sehen wir Folgendes mit  
https://www.openstreetmap.org/node/2089823517#map=19/52.52478/13.40676 
   
wenn wir dann einen reverse lookup damit machen:  
https://nominatim.openstreetmap.org/reverse?format=json&lat=52.52478&lon=13.40676
(das ist ein Place)

# Ressourcen  
  

 - OpenStreetMap frei verwendbar unter offener Lizenz   api: z.B.
   https://nominatim.openstreetmap.org/reverse?format=json&lat=52.52488282434212&lon=13.406595311907006
 - OSMDroid OSM Library für die Anzeige von Kartendaten 
   https://osmdroid.github.io/osmdroid/
 -Philipp Lackner -- How
   to Track Your Users Location in the Background in Android Studio Tutorial
    https://www.youtube.com/watch?v=Jj14sw4Yxk0   
     
   
 

 - Java Excel API - Java API to create Excel spreadsheets
 https://jexcelapi.sourceforge.net/

 - Kotlin

 - Android Studio Giraffe

# Test & Entwicklungs Umgebung

Google legt großen Wert auf den Schutz der Privatsphäre und hat Datenschutzrichtlinien für den Umgang mit Geodaten. In neueren Android-Versionen wurden verbesserte Datenschutzeinstellungen eingeführt, die es den Nutzern ermöglichen, den Zugriff von Apps auf Standortdaten genauer zu kontrollieren. Daher ist das Verhalten von Android im Bezug auf GeodDaten sehr Android Versions abhängig.  
  
DIe App wurde entwickelt mit: 
  

 - Pixel6a API 29 im Emulator
 - Sony xperia xa f3111 mit Android 7 / Nougat als Gerät

# What’s in it? (technically)  
  

 - multi lingual - en/de (wobei "de" ist der  englischer Text mit prefix “D-”)
 - location foreground service (ohne permission handling)
 - Bottom Nav
 - Hamburger Nav (noch ohne Inhalte für Lizenz, copyright, impressum)

  

 - Log Fenster zum Fehler finden unterwegs über Menue, mit fixer Anzahl Einträge um die ROOM DB zu schonen  
 - Kartenansicht mit OSM Droid im geo fragment  um karten fullscreen
   anzuzeigen  
    
 - Daten Export per E-Mail als json, excel und csv
 - Liste aller Locations der lon/lat GEO Koordinaten mit Aufenhaltsdauer
   (was keinen Sinn macht; s.o.)
 - Liste aller Places aus OSM mit Aufenhaltsdauer
 - Fragmente mit Recyclern im Recyclerdetail

# What is the App doing?  
Die App sammelt Location Daten und Places Daten und summiert die Anzahl der Minuten an den jeweiligen Orten auf. Auch mehrfach Besuche werden gelistet:

# Lessons learned  

  

 Do not underestimate to deal with Location Data

 - komplexes Thema (es handelt sich nicht um ein Kartesisches
   Koordinatensystem sondern um ein Geographisches).
 - Abfrage von Geo Location Data auf Android ist durch Google hoch
   reguliert und nicht einfach zu implementieren, User Permission
   notwendig
 - dazu kommt, daß Google in vielen Android Versionen die Handhabung der
   Geo Location Data verändert hat,  dies führt zu erhöhtem
   Iimplementations aufwand führt   hoher Testaufwand
 - gps simulation im emulator sehr instabil
 - gps simulation ergibt keine verlässlich replizierbare Daten
 - Geo Location Data Sammlung im Feld mit Smartphone sehr mühsam
	 - Debugging unterwegs schwierig -> Dafür Logfenster in der App
   eingebaut
	 - alles dauert, wenn man Zeit im Stundenbereich messen will 
	 -  mit jeder Datenbankänderung verliert man die Historie
# Next Steps
 - Logik und Berechnungen verbesern
 - Teile der Logik Parametrisieren (Abstände zwischen Places, Pausen   zwischen Einträgen,...)
 - Feld test mit echten Menschen und echten Geräten
 - Parameter configurierbar machen und in ROOM ablegen
 - Permission handling anschauen und verbessern
 - Export Parametrisieren (Zeitraum, Datentypen)
 - Reporting in der app: Tageweise, Wochenweise, Monatesweise,
   Datumsfenster,Arbeitszeit/Freizeit,sortieren
 - Lösch Strategie
 - Firebase anbindung
 - TickTrax Einbindung / Entwicklung

# Thanks!
Vielen Dank für Eure Aufmerksamkeit!  
  
An dieser Stelle noch ein recht herzliches Danke von mir an ALLE;  

 - die sich Zeit genommen haben,
 - Ideen gebracht haben,
 - mich aus meinem Tunnel geholt haben
 - und mich in jeder erdenklichen Seite unterstützt haben.

  
Bei Fragen und Anregungen und Unterstützung:  
  
info@ticktrax.de
