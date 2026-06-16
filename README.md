1. **Write Path (Shorten):** The user provides a long URL and an optional expiration duration. The system generates a clean, collision-free 7-character string using **Base62 encoding** ($[A-Z, a-z, 0-9]$). The mapping is persisted to PostgreSQL and simultaneously cached in Redis.
2. **Read Path (Redirect):** Incoming short URLs are looked up in Redis first. On a **Cache Hit**, a `302 Found` HTTP redirect is handled instantly. On a **Cache Miss**, the service queries PostgreSQL, back-populates Redis for subsequent traffic, and executes the redirect.

---

## 🚀 Key Architectural Decisions

### 1. Custom Base62 Encoding
Instead of utilizing bloated, non-URL-friendly UUIDs, the system leverages a custom **Base62 encoding** process. It yields a deterministic 62-character alphabet space, generating highly compact 7-character keys capable of representing over **3.5 trillion unique mappings** ($62^7$) without requiring special URL character escapes.

### 2. Time-To-Live (TTL) Cache Eviction
To optimize memory footprints, link expiration constraints chosen by the user are tied directly to dynamic **Redis TTL mechanics**. Expired keys are discarded out-of-memory automatically by Redis engine internals, entirely bypassing the need for expensive background database polling or batch deletion cron jobs.

### 3. $O(\log N)$ Database Indexing
To secure system integrity against *Cache Stampedes* (heavy traffic cascading to the database on a cold key look-up), a **Unique Index** is applied over the alphanumeric short keys in PostgreSQL. This bounds fallback database search operations to a shallow B-Tree traversal depth ($O(\log N)$), avoiding catastrophic sequential full-table scans.

---

## 💻 Technical Stack

* **Backend Framework:** Spring Boot 3.x (Spring Web, Spring Data JPA)
* **In-Memory Cache:** Redis (via `spring-boot-starter-data-redis`)
* **Primary Database:** PostgreSQL
* **Containerization & Deployment:** Docker, Docker Compose
* **Build Automation:** Maven / Gradle

---

## ⚡ API Specifications

### 1. Create Short URL
* **Endpoint:** `POST /api/v1/shorten`
* **Content-Type:** `application/json`
* **Payload:**
```json
{
  "longUrl": "[https://example-deep-link.com/modules/analytics/parameters?id=999823](https://example-deep-link.com/modules/analytics/parameters?id=999823)",
  "ttlInSeconds": 86400
}
