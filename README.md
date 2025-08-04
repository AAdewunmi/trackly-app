# 👷UNDER CONSTRUCTION🚧

```markdown
# 🎯 Trackly — Job Application Tracker

Trackly is a web-based application that helps job seekers track, manage, and analyze their job applications.

Built with Java, Spring Boot, and Thymeleaf, it provides a clean interface to log job applications,

manage stages like “Applied” or “Interviewing”, and visualize job search progress with charts and insights.

---

## 📌 Features (MVP)

- ✅ User Registration & Login (Spring Security)
- ✅ Add/Edit/Delete Job Applications
- ✅ Status Pipeline: Applied → Interviewing → Offer → Rejected
- ✅ Filter/Search by status, company, or role
- ✅ 📊 **Insights Dashboard** — View total applications, status breakdown, and weekly progress
- 🌓 Responsive UI with Dark Mode (optional)
- 🚀 Deployed to [Your Host] (e.g., Render, Railway, etc.)

---

## 📸 Screenshots

| Dashboard View                        | Job Entry Form                        |
|--------------------------------------|--------------------------------------|
| _To be added after Week 5_           | _To be added after Week 3_           |

---

## 🛠️ Tech Stack

**Backend**
- Java 17
- Spring Boot 3.x
- Spring MVC, Spring Security, Spring Data JPA

**Frontend**
- Thymeleaf 3
- Bootstrap 5
- JQuery
- Chart.js

**Database**
- PostgreSQL (or MySQL)
- Flyway for DB migrations

**Dev Tools**
- Maven
- GitHub Projects
- Render / Railway (Deployment)

---

## 🔄 Project Structure

```trackly/
├── src/
│   ├── main/java/
│   │   └── com.trackly/
│   │       ├── controller/
│   │       ├── model/
│   │       ├── repository/
│   │       ├── service/
│   │       └── TracklyApplication.java
│   ├── main/resources/
│   │   ├── templates/
│   │   ├── static/
│   │   └── application.properties
│   └── test/

````

---

## ⚙️ Setup Instructions

1. **Clone the repository**
   ```bash
   git clone https://github.com/yourusername/trackly.git
   cd trackly
````

2. **Configure your database**
   Update `application.properties` or use `application-dev.properties`:

   ```properties
   spring.datasource.url=jdbc:postgresql://localhost:5432/trackly
   spring.datasource.username=youruser
   spring.datasource.password=yourpass
   spring.jpa.hibernate.ddl-auto=validate
   ```

3. **Run migrations**

   ```bash
   ./mvnw flyway:migrate
   ```

4. **Start the server**

   ```bash
   ./mvnw spring-boot:run
   ```

5. **Visit**

   ```
   http://localhost:8080
   ```

---

## ✍️ Developer Diary

> 📅 Week 1: Setup and ideation
> 📅 Week 2: Auth system and DB config
> 📅 Week 3: CRUD implementation
> 📅 Week 4: Status pipeline + filters
> 📅 Week 5: Insights dashboard
> 📅 Week 6: Final polish + deployment

Check out [`/docs`](./docs) for full PRD, dev logs, ERD, and reflections.

---

## 🚀 Deployment

Deployed here: [https://trackly.example.com](https://trackly.example.com)

> Uses PostgreSQL on [Render.com](https://render.com)
> Secrets managed via environment variables

---

## 🧠 Lessons Learned (To be completed)

* How to integrate Spring Security with Thymeleaf
* How to structure CRUD with relational data in JPA
* Using Chart.js for backend-driven analytics
* Product planning & documentation in a solo project

---

## 📃 License

MIT License
© 2025 [Your Name](https://github.com/yourusername)

```

---

