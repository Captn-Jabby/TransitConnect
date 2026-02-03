#  TransitConnect

**Find the routes locals actually take.**

TransitConnect is a community-driven, full-stack web application designed to help users discover and share local transit routes. Unlike generic mapping services, TransitConnect focuses on the "hidden" connections and neighborhood shortcuts—the **routes that locals actually use**—to provide a more authentic and efficient travel experience.

 Live Demo
React App :[https://chimerical-wisp-0e018b.netlify.app/](https://transitfrontend.netlify.app)

 *Note*: The backend is hosted on a free tier, so the first request may take a few seconds to “wake up” the server.

##  The "Best Suit" Advantage

TransitConnect knows that the "best" route depends on your priorities. The platform analyzes a network of local **"Stops"** and **"Hops"** (connections) to find the path that best suits your day:

*  **The Fastest:** When every minute counts and you need the most efficient connection.
*  **The Cheapest:** Designed for students and budget-conscious travelers to minimize fare costs.
*  **The Shortest:** For those who prefer the most direct physical path between two locations.

By mapping individual connections as reported by the community, TransitConnect captures localized transit data that official maps often overlook.

---

##  Tech Stack

### **Frontend**

* **React.js:** Functional components and Hooks for dynamic state management.
* **Axios:** For asynchronous API communication and interceptors.
* **React Router:** For seamless Single Page Application (SPA) navigation.
* **Tailwind CSS:** For a modern, responsive user interface.

### **Backend**

* **Java 17 & Spring Boot 3:** Robust core application framework.
* **Spring Security:** Stateless authentication using **JWT (JSON Web Tokens)**.
* **Spring Data JPA:** For efficient ORM and data persistence.
* **PostgreSQL:** Relational database for storing complex transit nodes and relationships.

---

##  Key Architectural Features

* **Community-Driven Data:** A "Stops and Hops" architecture that allows the database to grow through user contributions.
* **Secure Stateless Auth:** JWT-based security ensures that user data is protected while maintaining high performance.
* **Dynamic CORS Handling:** Configured to support secure communication across various cloud environments and Netlify deploy previews.
* **SPA-Optimized Routing:** Custom configurations to handle browser refreshes and direct URL navigation without server-side errors.

---

##  Project Structure

```text
TransitConnect/
├── backend/
│   ├── src/main/java/com/connect/transitconnect/
│   │   ├── config/          # Security & CORS Configuration
│   │   ├── controller/      # REST API Controllers
│   │   ├── security/        # JWT Filters & Auth Utilities
│   │   └── service/         # Routing Algorithms & Business Logic
│   └── pom.xml
└── frontend/
    ├── public/              # Redirects & static assets
    ├── src/
    │   ├── api/             # API services & Axios configuration
    │   ├── components/      # UI Components & Layouts
    │   └── services/        # Auth & State helpers
    └── package.json

```

---

##  Local Setup

1. **Clone the repository:**
```bash
git clone https://github.com/Monishrajpalanivelu/TransitConnect.git

```


2. **Backend Setup:**
* Ensure Java 17 and PostgreSQL are installed.
* Update `application.properties` with your local database credentials.
* Run the application via your IDE or `./mvnw spring-boot:run`.


3. **Frontend Setup:**
```bash
cd frontend
npm install
npm start
