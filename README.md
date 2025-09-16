# Plannly – Planner Web App

Plannly is a planner and notes web application. It lets you organize thoughts as bullet points inside lists, manage a daily list for every calendar day, and store everything in a SQL database so your data is available anywhere with an internet connection. The UI is built with Vaadin 24 and the backend with Spring Boot.

This project was built together with Matej Minar and Tim Kagerbauer as part of course work. More details (in German) can be found in the `sw-eng` folder.

## Highlights

- Accounts: users can register and log in; credentials are stored server-side.
- Lists: create, rename, delete lists; share lists, including public read access.
- Bullet points: small text pieces with position and indent; types: Dash, Bullet, To‑Do (checkable), Star (global bookmark). Type can be changed via UI or shortcuts. Bullets can be formatted, moved, or deleted.
- Daily lists: one list per day (auto-generated on first open), named by date; non-deletable and non-public. Navigate via arrows, month view, or mini calendar.
- Storage: all lists, bullets, and auth info are saved in a SQL database.

Nice-to-have (future):
- Real‑time collaboration on shared lists
- Global search across lists
- Dark theme
- Tags on bullets for cross-list categorization

## Product Scope

- Use cases: everyday note taking and to-do planning; optionally shared lists for groups.
- Target audience: anyone who wants structured lists and day-based planning via a calendar.
- Operations: server available 24/7 with short maintenance windows; admin may remove explicit content if required.

## Environment

Client
- Any modern web browser (PWA-capable for mobile usage)

Server
- Debian 10
- Spring Boot + Vaadin 24 packaged as a runnable JAR
- JDK 19 (or newer LTS that matches Spring Boot baseline)
- Tomcat (embedded via Spring Boot) and Nginx as reverse proxy
- Let’s Encrypt certificates via Certbot
- PostgreSQL (hosted on Supabase)

Development
- IntelliJ IDEA (Windows/macOS/Linux)
- JDK 19/20
- Apache Maven

## Architecture (high level)

The app follows the standard Vaadin architecture:
- Frontend and backend are integrated in a single Spring Boot app
- Client uses Vaadin’s UI components; server renders and handles events
- Data is persisted to PostgreSQL via JPA entities (the domain model maps to relational tables)

See `sw-eng/Ueberblick_Architektur.png` for an overview. The domain model includes classes for users, lists, bullets, etc.; many relationships are wired via dependency injection.

## Run locally

This is a standard Maven project.

1) Start the dev server:
  - Windows: `mvnw`
  - macOS/Linux: `./mvnw`

2) Open http://localhost:8080 in your browser.

You can also import it into your IDE like any Maven project. See Vaadin’s docs on importing to various IDEs.

## Production build

Create an optimized JAR with front-end resources:

- Windows: `mvnw clean package -Pproduction`
- macOS/Linux: `./mvnw clean package -Pproduction`

After the build, run the JAR from the `target` directory. The exact file name depends on your version; for example:

`java -jar target/plannerapp-0.1.0.jar`

## Localization

The UI is currently in English. Vaadin supports i18n, so additional languages (like German) can be added later.

## Related docs (German)

German engineering documents are stored under `sw-eng/`. See the short guide there for file descriptions.

## Useful links

- Vaadin docs: https://vaadin.com/docs
- Vaadin tutorial: https://vaadin.com/docs/latest/tutorial/overview
- Components: https://vaadin.com/docs/latest/components
- Examples & demos: https://vaadin.com/examples-and-demos
- CSS utilities: https://vaadin.com/docs/styling/lumo/utility-classes
- Cookbook: https://cookbook.vaadin.com/

## License

See `LICENSE` for details.
