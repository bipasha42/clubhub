#ClubHub — Global University Clubs Hub

ClubHub is a Spring Boot 3.5.4 web application that aggregates university clubs from around the world. It provides a unified explore experience, social interactions (follow, like, comment), and admin workflows for clubs and universities. Built with Spring Boot, Thymeleaf, Spring Security, PostgreSQL, and Flyway.

Roles:
STUDENT (normal user)
CLUB_ADMIN
UNIVERSITY_ADMIN
Core flows:
Explore page (public)
Sign up as a student (generic signup)
Follow clubs, apply to join
Create posts with media URL
Like and comment on posts
ClubAdmin: create/edit/approve posts, delete comments, manage clubs
UniversityAdmin: assign club admins
Frontend: Thymeleaf templates with a modern, responsive UI
Security: Session-based authentication with role-based access control
Database: PostgreSQL 17.5
Migrations: Flyway
Table of Contents
Features
Architecture
Technologies
Project Structure
Prerequisites
Configuration
Run & Build
Database & Migrations
Security & Roles
Endpoints & API
Frontend
Testing
CI/CD & Deployment
Contributing
License
Notes for Reviewers / Questions
Features
Public Explore page to discover clubs and posts
Generic signup for students (no university email requirement)
Follow clubs and apply to join clubs
Create, edit, delete, and approve posts
Like and comment on posts (students and club admins)
ClubAdmin: create/edit clubs, approve posts, manage comments
UniversityAdmin: assign club admins, user administration
Role-based access control via Spring Security
Flyway-based database migrations with versioned scripts
Architecture
Frontend: Thymeleaf templates with optional Bootstrap styling
Backend (MVC):
Controllers for web pages
Services with business logic
Repositories (Spring Data JPA)
Entities: User, Role, University, Club, Post, Comment, Like, Notification, etc.
Security: Spring Security (session-based auth, RBAC)
Persistence: PostgreSQL
Migrations: Flyway
Build: Maven (Java 21)
Technologies
Java 21
Spring Boot 3.5.4
PostgreSQL 17.5
Thymeleaf
Spring Security (RBAC)
Flyway (db migrations)
Lombok (optional)
Bootstrap (optional for UI)
Project Structure
src/
main/
java/
com/example/clubhub/
config/ (SecurityConfig, etc.)
controller/ (PostPageController, ClubController, etc.)
model/ (User, Post, Club, University, Role, etc.)
repository/ (UserRepository, PostRepository, etc.)
service/ (PostService, UserService, etc.)
ClubhubApplication.java
resources/
templates/ (Thymeleaf templates: posts.html, create_post.html, approved_posts.html, etc.)
static/
css/ (styles.css)
js/ (clubhub.js or scripts.js)
db/migration/ (Flyway migrations: V1__init.sql, V2__add_tables.sql, etc.)
README.md
Prerequisites
JDK 21
Maven (or Maven Wrapper)
PostgreSQL 17.5
Git
Configuration
Copy and adapt application properties
File: src/main/resources/application.properties
example:

text

# Server
server.port=8080

# DataSource
spring.datasource.url=jdbc:postgresql://localhost:5432/clubhub
spring.datasource.username=clubhub_user
spring.datasource.password=your_password
spring.datasource.driver-class-name=org.postgresql.Driver

# JPA / Hibernate
spring.jpa.hibernate.ddl-auto=update
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect

# Flyway
spring.flyway.enabled=true
spring.flyway.locations=classpath:db/migration

# Security
# Actual security config is in SecurityConfig.java (RBAC)
Flyway migrations
Place your migrations under src/main/resources/db/migration
Example:
V1__init.sql
V2__add_tables.sql
V3__add_sample_data.sql
Security config (high level)
You will implement a SecurityConfig.java that defines:
UserDetailsService backed by DB
Password encoder
RBAC: ROLE_STUDENT, ROLE_CLUB_ADMIN, ROLE_UNIVERSITY_ADMIN
Endpoints access rules (examples below)
Example (simplified, put in src/main/java/com/example/clubhub/config/SecurityConfig.java):

Java

@EnableWebSecurity
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http
      .authorizeHttpRequests()
        .requestMatchers("/explore", "/posts", "/posts/approved", "/signup", "/css/**", "/js/**").permitAll()
        .antMatchers("/posts/new", "/posts", "/posts/edit/**", "/api/posts").hasAnyRole("STUDENT","CLUB_ADMIN")
        .antMatchers("/posts/approve/**", "/clubs/**/edit/**", "/clubs/**/delete/**").hasRole("CLUB_ADMIN")
        .antMatchers("/admin/**").hasRole("UNIVERSITY_ADMIN")
        .anyRequest().authenticated()
      .and()
      .formLogin()
        .loginPage("/login")
        .permitAll()
      .and()
      .logout()
        .permitAll();
  }

  // UserDetailsService and password encoding wired to DB-backed user store
}
Note: The exact endpoints you expose will drive your security rules.

Run & Build
Build:
mvn clean package
Run:
mvn spring-boot:run
or use the Maven wrapper:
./mvnw spring-boot:run
Access URLs:
Explore: http://localhost:8080/explore
All posts: http://localhost:8080/posts
Approved posts: http://localhost:8080/posts/approved
Database & Migrations
Flyway tracks migrations in public.flyway_schema_history.
If you encounter a checksum mismatch:
mvn flyway:repair
Ensure your migration scripts reflect your current schema, and keep them versioned in src/main/resources/db/migration.
API Endpoints (Example)
Public:
GET /explore
GET /api/clubs
GET /api/clubs/{id}/posts
Posts (MVC or REST):
GET /posts
GET /posts/approved
GET /posts/new
POST /posts
GET /posts/edit/{id}
POST /posts/edit/{id}
POST /posts/delete/{id}
POST /posts/approve/{id}
Admin actions (secured):
POST /api/posts
PUT /api/posts/{id}
DELETE /api/posts/{id}
POST /api/clubs
POST /api/clubs/{clubId}/assign-admin
Note: Adapt to your actual controllers and paths.

Frontend
Templates: src/main/resources/templates/
Global CSS: src/main/resources/static/css/styles.css
Global JS: src/main/resources/static/js/scripts.js
If you have a base layout, apply CSS/JS in the layout so all pages share the look.
Testing
Manual testing:
Sign up as a student
Explore (public)
Sign in as CLUB_ADMIN or UNIVERSITY_ADMIN
Create/approve posts, assign admins
Like/comment as student/admin
Automated tests (JUnit + Spring Test) can cover:
Public access vs. authenticated routes
Role-based access control
Post CRUD + approval flows
CI/CD & Deployment
GitHub Actions or similar:
mvn test
mvn clean package
Deploy to your target environment (on-prem, AWS, etc.)
Contributing
Fork the repo
Create a feature branch
Implement, write tests
Open a pull request with a clear description
License
[MIT or your chosen license]


