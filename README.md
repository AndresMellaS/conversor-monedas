<h1 align="center"> Conversor de Monedas  </h1> 

Proyecto desarrollado como parte del Challenge de Alura — Backend con Java.

<h4 align="center">
:construction: Proyecto en construcción :construction:
</h4>

Aplicación de conversión de divisas en tiempo real con dos modos de ejecución: consola interactiva y frontend web, ambos conectados al mismo backend con persistencia de historial.

---

 <em>**Funcionalidades**</em>

- Conversión entre 23 monedas con tasas en tiempo real via API
- Historial de todas las conversiones realizadas
- Marca de tiempo en cada conversión usando `java.time.LocalDateTime`
- Persistencia del historial en `historial.json` — sobrevive reinicios
- API REST con Spring Boot — endpoints `/api/convertir` y `/api/historial`
- Dos modos: consola interactiva y frontend web

---

<em> **Modo Consola** </em>

Menú principal

![Menú consola](screenshots/01-menu-consola.png)

Realizando una conversión

![Conversión en consola](screenshots/02-conversion-consola.png)

Historial con marcas de tiempo

![Historial consola](screenshots/03-historial-consola.png)

---

<em> Su título aquí </em>

Servidor Spring Boot activo

![Spring Boot](screenshots/04-spring-boot.png)

Frontend en el navegador

![Frontend](screenshots/05-frontend.png)

---

<em> **Estructura del proyecto** </em>

```
src/main/
├── java/com/daniels/conversor/
│   ├── Main.java
│   ├── model/
│   │   ├── Moneda.java
│   │   └── Conversion.java
│   ├── service/
│   │   ├── ApiService.java
│   │   └── ConversorService.java
│   ├── repository/
│   │   └── HistorialRepository.java
│   ├── controller/
│   │   └── ConversorController.java
│   └── util/
│       └── Menu.java
└── resources/
    ├── static/
    │   └── index.html
    └── application.properties
```

---

<em> Cómo ejecutar </em>

Requisitos
- Java 17 o superior
- Maven

Modo Web
```bash
mvn spring-boot:run
```
Abre `http://localhost:8081` en el navegador.

Modo Consola

Configura una ejecución con el argumento `--consola` en IntelliJ, o ejecuta:
```bash
mvn spring-boot:run -Dspring-boot.run.arguments=--consola
```

---

<em>**API REST**</em>

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| `POST` | `/api/convertir` | Realiza una conversión |
| `GET` | `/api/historial` | Obtiene el historial completo |
| `DELETE` | `/api/historial` | Limpia el historial |

Ejemplo de request
```json
POST /api/convertir
{
  "desde": "USD",
  "hasta": "BRL",
  "monto": 100
}
```

Ejemplo de response
```json
{
  "desde": "USD",
  "hasta": "BRL",
  "monto": 100.0,
  "resultado": 527.0,
  "tasa": 5.27,
  "timestamp": "2026-03-08T10:38:33.123"
}
```

---

<em> **Monedas disponibles** </em>

| Código | Moneda |
|--------|--------|
| USD | Dólar estadounidense |
| EUR | Euro |
| BRL | Real brasileño |
| ARS | Peso argentino |
| CLP | Peso chileno |
| MXN | Peso mexicano |
| COP | Peso colombiano |
| PEN | Sol peruano |
| UYU | Peso uruguayo |
| BOB | Boliviano |
| GBP | Libra esterlina |
| JPY | Yen japonés |
| CNY | Yuan chino |
| CAD | Dólar canadiense |
| AUD | Dólar australiano |
| CHF | Franco suizo |
| INR | Rupia india |
| KRW | Won surcoreano |
| SEK | Corona sueca |
| NOK | Corona noruega |
| NZD | Dólar neozelandés |
| SGD | Dólar de Singapur |
| HKD | Dólar de Hong Kong |

---

<em>**Tecnologías**</em> 

- Java 17+ — Lógica de negocio y backend
- Spring Boot 3.2.3 — Servidor web y API REST
- Gson 2.10.1 — Parseo de JSON y persistencia
- HttpClient — Consumo de API externa
- java.time — Marcas de tiempo
- HTML / CSS / JS — Frontend sin frameworks

---

<em> **API de tasas de cambio** </em>

Este proyecto usa [ExchangeRate API](https://www.exchangerate-api.com/) en su versión gratuita — no requiere API key.

---
Autor: Daniel Sepúlveda M.
*Challenge Alura — One Oracle Next Education*
