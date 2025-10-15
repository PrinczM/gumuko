# Amőba NxM Játék

Parancssoros Amőba játék Java nyelven.

## Leírás

Ez egy kétszemélyes stratégiai táblajáték egy NxM-es táblán (tipikusan 10x10).
A játékos célja, hogy függőlegesen, vízszintesen vagy átlósan kirakjon 4 jelet egymás mellé.

## Követelmények

- Java 21
- Maven 3.6+

## Futtatás
```bash
# Projekt fordítása és futtatható JAR létrehozása
mvn clean package

# Játék indítása
java -jar target/gomuko-1.0-SNAPSHOT-jar-with-dependencies.jar
```

## Tesztelés
```bash
# Unit tesztek futtatása
mvn test

# Teszt lefedettség report
mvn jacoco:report
```

## Fejlesztő

Princz Márió

## Licenc

Ez egy egyetemi projekt.