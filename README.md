# Amőba NxM Játék

Parancssoros Amőba (Gomoku) játék Java nyelven.

## 📋 Leírás

Ez egy kétszemélyes stratégiai táblajáték egy NxM-es táblán (alapból 10x10).

**Játékszabályok:**
- A játékos célja, hogy függőlegesen, vízszintesen vagy átlósan kirakjon **5 jelet** egymás mellé
- Az "X" jel a humán játékosé, az "O" a gépi játékosé
- A humán játékos kezd
- A lerakott jelnek legalább diagonálisan érintkeznie kell a már fennlévőkkel (kivéve az első lépés)
- A kezdő jel a tábla egyik középső mezőjére kerül; betöltött játék folytatásánál nincs középre kényszer

**Mentés/betöltés:**
- Fájlba mentés csak a játék menete közben érhető el (humán lépés előtt)
- A játék végén (győzelem/döntetlen) fájlmentés nem lehetséges (automatikusan csak High Score kerül mentésre)
- A mentés/betöltés formátuma: XML (ajánlott fájlnév: jatek.xml)

**High Score:**
- Az eredmények H2 adatbázisba mentődnek (lokális fájl)
- A kilistázás olvasható táblázatban, oszlopokkal: player name | table size | moves | res | date
- Dátum formátum: `yyyy.MM.dd. HH:mm` (pl. `2025.10.27. 19:53`)
- Rendezés: WIN előre, majd DRAW, majd LOSS; kevesebb "moves" előrébb; azonos moves esetén a korábbi (régebbi) teljesítés előrébb

## 🛠️ Technológiák

- Java 21
- Maven 3.6+
- JUnit 5 – unit tesztelés
- Mockito – mock objektumok
- Logback – naplózás
- H2 Database – beágyazott adatbázis (High Score)
- JaCoCo – kód lefedettség mérés
- Checkstyle – kód minőség ellenőrzés

## 🚀 Futtatás

### Fejlesztői környezetben (IntelliJ IDEA)

1. Nyisd meg a projektet IntelliJ IDEA-ban
2. Futtasd a `Main` osztályt

### JAR fájlból (Windows cmd)
```bat
mvn clean package
java -jar target\gumuko-1.0-SNAPSHOT-jar-with-dependencies.jar
```

## 🧪 Tesztelés és minőség
```bat
:: Unit tesztek
mvn test

:: Lefedettségi riport
mvn jacoco:report

:: Checkstyle (Google checks)
mvn checkstyle:check

:: Teljes build
mvn clean verify
```

### JaCoCo Report megtekintése

A teszt lefedettség report az alábbi helyen található build után:
```
target/site/jacoco/index.html
```

## 📝 Fejlesztési Ütemezés

### ✅ 1. védés (7. hét) – Alapok
- [x] Maven projekt setup
- [x] Git verziókezelés
- [x] Alapvető osztályok (Position, Player, Board)
- [x] Fájl mentés/olvasás
- [x] Unit tesztek

### ✅ 2. védés (13. hét) – Funkcionalitás
- [x] Teljes játéklogika (5 egymás mellett = győzelem)
- [x] AI ellenfél (alap random)
- [x] Adatbázis integráció (H2)
- [x] High Score tábla és rendezés
- [x] Betöltött játék folytatása (helyes kör, nincs középre kényszer)

## 👨‍💻 Fejlesztő

Princz Márió

## 📄 Licenc

Egyetemi projekt.

**Használat közben:**
- A humán játékosnál a lépés bevihető pl. `E5` formában
- Ha menteni szeretnél és visszalépni a főmenübe, egyszerűen írd be: `save`
- Mentés esetén a program rákérdez a fájlnévre (alap: `jatek.xml`), elmenti a táblát, majd visszalép a főmenübe
