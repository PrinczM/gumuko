# Amőba NxM Játék

Parancssoros Amőba (Gomoku) játék Java nyelven.

## 📋 Leírás

Ez egy kétszemélyes stratégiai táblajáték egy NxM-es táblán (tipikusan 10x10).

**Játékszabályok:**
- A játékos célja, hogy függőlegesen, vízszintesen vagy átlósan kirakjon **4 jelet** egymás mellé
- Az "X" jel a humán játékosé, az "O" a gépi játékosé
- A humán játékos kezd
- A lerakott jelnek legalább diagonálisan érintkeznie kell a már fennlévőkkel
- A kezdő jel a tábla egyik középső mezőjére kerül

## 🛠️ Technológiák

- **Java 21**
- **Maven 3.6+**
- **JUnit 5** - unit tesztelés
- **Mockito** - mock objektumok
- **Logback** - naplózás
- **H2 Database** - beágyazott adatbázis
- **JaCoCo** - kód lefedettség mérés
- **Checkstyle** - kód minőség ellenőrzés

## 🚀 Futtatás

### Fejlesztői környezetben (IntelliJ IDEA)

1. Nyisd meg a projektet IntelliJ IDEA-ban
2. Futtasd a `Main` osztályt (`Shift+F10`)

### JAR fájlból
```bash
# Projekt fordítása és futtatható JAR létrehozása
mvn clean package

# Játék indítása
java -jar target/gumuko-1.0-SNAPSHOT-jar-with-dependencies.jar
```

## 🧪 Tesztelés
```bash
# Unit tesztek futtatása
mvn test

# Teszt lefedettség report generálása
mvn jacoco:report

# Checkstyle ellenőrzés
mvn checkstyle:check

# Teljes build
mvn clean install
```

### JaCoCo Report megtekintése

A teszt lefedettség report az alábbi helyen található build után:
```
target/site/jacoco/index.html
```

## 📁 Projekt Struktúra
```
gumuko/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── projekt/
│   │   │       └── progtech/
│   │   │           └── Main.java
│   │   └── resources/
│   │       └── logback.xml
│   └── test/
│       └── java/
│           └── projekt/
│               └── progtech/
│                   └── AppTest.java
├── .gitignore
├── pom.xml
└── README.md
```

## 📝 Fejlesztési Ütemezés

### ✅ 1. védés (7. hét) - Alapok
- [x] Maven projekt setup
- [x] GitHub repository
- [x] Alapvető osztályok
- [ ] Value Objects (Position, Player, Cell, Move)
- [ ] Board osztály
- [ ] Fájl-ba mentés/olvasás
- [ ] 80% teszt lefedettség

### ⏳ 2. védés (13. hét) - Teljes funkcionalitás
- [ ] Teljes játéklogika
- [ ] AI ellenfél
- [ ] Adatbázis integráció
- [ ] High score tábla
- [ ] (Opcionális) XML mentés/betöltés

## 👨‍💻 Fejlesztő

Princz Márió - Programozási Technológiák Beadandó 2025

## 📄 Licenc

Ez egy egyetemi projekt.
```