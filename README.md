# 📦 Gestione Ordini — API REST

API REST backend per la gestione di clienti, prodotti e ordini con metriche di vendita integrate.  
Sviluppata con **Java 21** e **Spring Boot 3**, seguendo un'architettura a strati pulita e professionale.

---

## 🛠️ Tecnologie utilizzate

![Java](https://img.shields.io/badge/Java_21-ED8B00?style=flat&logo=openjdk&logoColor=white)
![Spring Boot](https://img.shields.io/badge/Spring_Boot_3-6DB33F?style=flat&logo=spring-boot&logoColor=white)
![Spring Security](https://img.shields.io/badge/Spring_Security-6DB33F?style=flat&logo=spring-security&logoColor=white)
![Spring Data JPA](https://img.shields.io/badge/Spring_Data_JPA-6DB33F?style=flat&logo=spring&logoColor=white)
![MySQL](https://img.shields.io/badge/MySQL_8-4479A1?style=flat&logo=mysql&logoColor=white)
![Hibernate](https://img.shields.io/badge/Hibernate_6-59666C?style=flat&logo=hibernate&logoColor=white)
![Lombok](https://img.shields.io/badge/Lombok-BC4521?style=flat&logoColor=white)
![Maven](https://img.shields.io/badge/Maven-C71A36?style=flat&logo=apache-maven&logoColor=white)

---

## 📐 Architettura

Il progetto segue un'**architettura a strati** classica del mondo Spring Boot:

```
Controller → Service (interfaccia + implementazione) → Repository → Database
```

### Struttura dei package

```
com.hector.orders/
├── controller/          # Endpoints REST
├── service/             # Interfacce dei servizi
│   └── impl/            # Implementazioni dei servizi
├── repository/          # Accesso al database (Spring Data JPA)
├── model/               # Entità JPA
├── dto/
│   ├── request/         # Oggetti in entrata (validati con @Valid)
│   └── response/        # Oggetti in uscita
├── mapper/              # Conversione entità ↔ DTO
├── exception/           # Eccezioni personalizzate e handler globale
├── security/            # Configurazione Spring Security
└── util/                # Classi di utilità
```

### Decisioni di design

- **DTOs separati** per request e response — il modello di dominio non viene mai esposto direttamente
- **Mapper statici** per la conversione tra entità e DTOs
- **Interfacce per i servizi** — disaccoppiamento tra controller e implementazione
- **GlobalExceptionHandler** con `@RestControllerAdvice` — gestione centralizzata di tutti gli errori
- **Eccezioni personalizzate** per ogni entità (`CustomerNotFoundException`, `OrderNotFoundException`, ecc.)

---

## 🗄️ Modello dei dati

```
Customer (1) ──── (N) Order (1) ──── (N) OrderRegistration (N) ──── (1) Product
```

| Entità | Campi principali |
|---|---|
| `Customer` | id, name, idCard |
| `Product` | id, name, origin, price |
| `Order` | id, customerId |
| `OrderRegistration` | id, orderId, productId, quantity |
| `Account` | id, accountName, password, role |

---

## 🔒 Sicurezza

L'applicazione utilizza **Spring Security con Basic Auth** e gestione dei ruoli:

| Ruolo | Permessi |
|---|---|
| `USER` | Lettura e creazione di risorse |
| `ADMIN` | Accesso completo inclusa eliminazione |

Le password sono cifrate con **BCrypt**.

---

## 🌐 Endpoints

### 👤 Clienti — `/api/customers`

| Metodo | Endpoint | Descrizione | Stato |
|---|---|---|---|
| GET | `/api/customers` | Lista tutti i clienti | 200 |
| POST | `/api/customers` | Registra un cliente | 201 |
| GET | `/api/customers/{customerId}` | Cerca cliente per ID | 200 / 404 |
| PUT | `/api/customers/{customerId}` | Aggiorna un cliente | 200 / 404 |
| DELETE | `/api/customers/{customerId}` | Elimina un cliente | 204 |
| GET | `/api/customers/order/{orderId}` | Cliente di un ordine | 200 / 404 |
| GET | `/api/customers/product/{productId}` | Clienti che hanno acquistato un prodotto | 200 / 404 |

### 📦 Prodotti — `/api/products`

| Metodo | Endpoint | Descrizione | Stato |
|---|---|---|---|
| GET | `/api/products` | Lista tutti i prodotti | 200 |
| POST | `/api/products` | Registra un prodotto | 201 |
| GET | `/api/products/{productId}` | Cerca prodotto per ID | 200 / 404 |
| PUT | `/api/products/{productId}` | Aggiorna un prodotto | 200 / 404 |
| DELETE | `/api/products/{productId}` | Elimina un prodotto | 204 |
| GET | `/api/products/order/{orderId}` | Prodotti di un ordine | 200 / 404 |
| GET | `/api/products/customer/{customerId}` | Prodotti acquistati da un cliente | 200 / 404 |

### 🛒 Ordini — `/api/orders`

| Metodo | Endpoint | Descrizione | Stato |
|---|---|---|---|
| GET | `/api/orders` | Lista tutti gli ordini | 200 |
| POST | `/api/orders` | Crea un ordine | 201 |
| GET | `/api/orders/{orderId}` | Cerca ordine per ID | 200 / 404 |
| PUT | `/api/orders/{orderId}` | Aggiorna un ordine | 200 / 404 |
| DELETE | `/api/orders/{orderId}` | Elimina un ordine | 204 |
| GET | `/api/orders/cliente/{customerId}` | Ordini di un cliente | 200 / 404 |
| GET | `/api/orders/producto/{productId}` | Ordini che contengono un prodotto | 200 / 404 |

### 📋 Registrazioni — `/api/orders-registration`

| Metodo | Endpoint | Descrizione | Stato |
|---|---|---|---|
| GET | `/api/orders-registration` | Lista tutte le registrazioni | 200 |
| POST | `/api/orders-registration` | Aggiunge un prodotto a un ordine | 201 |
| GET | `/api/orders-registration/{registrationId}` | Cerca registrazione per ID | 200 / 404 |
| PUT | `/api/orders-registration/{registrationId}` | Aggiorna una registrazione | 200 / 404 |
| DELETE | `/api/orders-registration/{registrationId}` | Elimina una registrazione | 204 |
| GET | `/api/orders-registration/products-sold` | Prodotti venduti almeno una volta | 200 |
| GET | `/api/orders-registration/products-sold/{orderId}` | Prodotti venduti in un ordine | 200 / 404 |
| GET | `/api/orders-registration/products-sold/top/{positions}` | Top N prodotti più venduti | 200 |

### 💰 Metriche di vendita — `/api/orders-registration`

| Metodo | Endpoint | Descrizione | Stato |
|---|---|---|---|
| GET | `/api/orders-registration/total-sale` | Vendita totale globale | 200 |
| GET | `/api/orders-registration/total-sale/order/{orderId}` | Vendita totale per ordine | 200 / 404 |
| GET | `/api/orders-registration/total-sale/customer/{customerId}` | Vendita totale per cliente | 200 / 404 |
| GET | `/api/orders-registration/total-sale/product/{productId}` | Vendita totale per prodotto | 200 / 404 |
| GET | `/api/orders-registration/total-sale/average` | Media vendita per ordine | 200 |
| GET | `/api/orders-registration/total-units-sold` | Totale unità vendute | 200 |
| GET | `/api/orders-registration/total-units-sold/order/{orderId}` | Unità vendute per ordine | 200 / 404 |
| GET | `/api/orders-registration/total-units-sold/customer/{customerId}` | Unità vendute per cliente | 200 / 404 |
| GET | `/api/orders-registration/total-units-sold/product/{productId}` | Unità vendute per prodotto | 200 / 404 |

---

## ▶️ Come avviare il progetto

### Prerequisiti

- Java 21
- MySQL 8
- Maven

### Passi

**1. Clona il repository**
```bash
git clone https://github.com/HeFeBo/GestioneOrdini.git
cd GestioneOrdini
```

**2. Crea il database**
```sql
CREATE DATABASE order_system;
```

**3. Crea il file delle credenziali locali**

Crea il file `src/main/resources/application-local.properties` con i tuoi dati:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/order_system
spring.datasource.username=il_tuo_utente
spring.datasource.password=la_tua_password
```

> ⚠️ Questo file è escluso dal repository tramite `.gitignore` per motivi di sicurezza.

**4. Avvia l'applicazione**
```bash
./mvnw spring-boot:run -Dspring-boot.run.profiles=local
```

L'API sarà disponibile su `http://localhost:8080`.

---

## 📁 Esempi di request

### Registrare un cliente
```http
POST /api/customers
Content-Type: application/json
Authorization: Basic dXNlcjoxMjM0

{
  "name": "Mario Rossi",
  "idCard": "AB123456"
}
```

### Creare un ordine
```http
POST /api/orders
Content-Type: application/json
Authorization: Basic dXNlcjoxMjM0

{
  "customerId": 1
}
```

### Aggiungere un prodotto a un ordine
```http
POST /api/orders-registration
Content-Type: application/json
Authorization: Basic dXNlcjoxMjM0

{
  "orderId": 1,
  "productId": 2,
  "quantity": 3
}
```

---

## ⚠️ Gestione degli errori

Tutti gli errori vengono gestiti centralmente dal `GlobalExceptionHandler` (`@RestControllerAdvice`):

| Situazione | Codice HTTP |
|---|---|
| Risorsa non trovata | 404 Not Found |
| Dati non validi (`@Valid`) | 400 Bad Request |
| JSON malformato | 400 Bad Request |
| Tipo errato nel path | 400 Bad Request |
| Quantità superiore al limite | 404 Not Found |
| Errore interno generico | 500 Internal Server Error |

---

## 👤 Autore

**Hector Bohorquez Chavez**  
📧 hefebo82@gmail.com  
💼 [linkedin.com/in/hefebo](https://www.linkedin.com/in/hefebo)  
🐙 [github.com/HeFeBo](https://github.com/HeFeBo)