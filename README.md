# Payment Optimizer

Aplikacja Java do optymalizacji metod płatności dla zamówień w sklepie internetowym, z uwzględnieniem promocji i rabatów.

---

## Zawartość projektu

```
payment-optimizer/
├── pom.xml                  # Konfiguracja Maven
├── src/
│   ├── main/
│   │   └── java/com/centrabiz/payment/
│   │       ├── PaymentOptimizerApp.java
│   │       ├── logic/Optimizer.java
│   │       ├── model/{Order, PaymentMethod}.java
│   │       └── util/JsonParser.java
│   └── test/
│       └── java/com/centrabiz/payment/OptimizerTest.java
```

---

## Wymagania

- Java 17 lub 21
- Maven 3.6+

---

## Budowanie projektu

### 1. Klonuj repozytorium lub rozpakuj ZIP
### 2. Zainstaluj zależności i zbuduj `fat-jar`:
```bash
mvn clean package
```

### 3. Po zakończeniu:
W katalogu `target/` znajdziesz:
```
payment-optimizer-1.0-SNAPSHOT.jar
```

---

## Testy

Plik testowy `OptimizerTest.java` znajduje się w:
```
src/test/java/com/centrabiz/payment/
```

Aby uruchomić testy jednostkowe:
```bash
mvn test
```

---

##  Uruchamianie aplikacji

Użyj dwóch plików `.json`:  
- `orders.json` – lista zamówień  
- `paymentmethods.json` – dostępne metody płatności

### Przykład:
```bash
java -jar target/payment-optimizer-1.0-SNAPSHOT.jar /ścieżka/orders.json /ścieżka/paymentmethods.json
```

---

## Dane wejściowe

### `orders.json`
```json
[
  { "id": "ORDER1", "value": "100.00", "promotions": ["mZysk"] },
  { "id": "ORDER2", "value": "200.00", "promotions": ["BosBankrut"] },
  { "id": "ORDER3", "value": "150.00", "promotions": ["mZysk", "BosBankrut"] },
  { "id": "ORDER4", "value": "50.00" }
]
```

### `paymentmethods.json`
```json
[
  { "id": "PUNKTY", "discount": "15", "limit": "100.00" },
  { "id": "mZysk", "discount": "10", "limit": "180.00" },
  { "id": "BosBankrut", "discount": "5", "limit": "200.00" }
]
```

---

## Przykład działania

```
mZysk 165.00
BosBankrut 190.00
PUNKTY 100.00
```

---

## Licencja

Projekt stworzony jako zadanie rekrutacyjne — tylko do użytku edukacyjnego/testowego.
