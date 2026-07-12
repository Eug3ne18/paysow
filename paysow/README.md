# 💸 Paysow — Simple E-Wallet App

A GCash-style e-wallet built with **Spring Boot**, **Thymeleaf**, and **H2 Database**.

## Features
1. **Sign Up** — full name, email, phone number, 4-digit PIN
2. **Login** — phone number + 4-digit PIN
3. **Cash In / Cash Out** — top up your wallet, or send money to another Paysow phone number
4. **Transaction History** — full log of every cash in, cash out, and savings move
5. **Profile Updating** — edit name/email, change PIN
6. **Savings / Investment** — move money between your wallet and a separate savings balance

## Tech Stack
- Java 17
- Spring Boot 3 (Web, Thymeleaf, Spring Data JPA)
- H2 Database (file-based, no setup needed)
- Plain HTML/CSS (no frontend framework required)

---

## Where the 4 Pillars of OOP show up

| Pillar | Where |
|---|---|
| **Encapsulation** | `User.java` — fields are `private`; balance can only change through guarded methods like `credit()`, `debit()`, `moveToSavings()`. The raw PIN is never stored, only its hash. |
| **Abstraction** | `Transaction.java` (abstract class) and `UserService` / `WalletService` (interfaces) — callers depend on *what* something does, not *how*. |
| **Inheritance** | `CashInTransaction`, `CashOutTransaction`, `SavingsDepositTransaction`, `SavingsWithdrawTransaction` all extend `Transaction` and reuse its shared fields. |
| **Polymorphism** | `Transaction.getDescription()` / `getTypeLabel()` — each subclass overrides these differently. The history page loops over a `List<Transaction>` and calls `txn.getDescription()` without knowing which subclass it actually is; Java picks the right version automatically. |

---

## Step-by-Step: Push to GitHub & Run It (Error-Free)

### Part A — Get the code onto GitHub

1. **Install prerequisites** (skip any you already have):
   - [Java 17 JDK](https://adoptium.net/) — after installing, check it with `java -version`
   - [Git](https://git-scm.com/downloads) — check with `git --version`
   - A free [GitHub](https://github.com) account

2. **Unzip the project** you downloaded from this chat into a folder, e.g. `paysow/`.

3. **Create a new empty repository on GitHub**:
   - Go to github.com → click **+** (top right) → **New repository**
   - Name it `paysow`
   - Do **NOT** check "Add a README" (you already have one) — keep it empty
   - Click **Create repository**

4. **Open a terminal inside the `paysow` folder** and run these commands one by one:
   ```bash
   git init
   git add .
   git commit -m "Initial commit - Paysow e-wallet app"
   git branch -M main
   git remote add origin https://github.com/YOUR-USERNAME/paysow.git
   git push -u origin main
   ```
   Replace `YOUR-USERNAME` with your actual GitHub username. GitHub may ask you to log in or create a **Personal Access Token** (GitHub no longer accepts plain passwords for `git push`) — if so, go to GitHub → Settings → Developer settings → Personal access tokens → Generate new token, and paste the token in place of your password when prompted.

5. Refresh your GitHub repository page — your code is now online! ✅

### Part B — Run the project (2 ways)

**Option 1: Using an IDE (recommended for beginners)**
1. Download and open [IntelliJ IDEA Community Edition](https://www.jetbrains.com/idea/download/) (free) or VS Code with the "Extension Pack for Java" and "Spring Boot Extension Pack".
2. Choose **File → Open**, and select the `paysow` folder (or clone directly from GitHub: **File → New → Project from Version Control**, paste your repo URL).
3. Let the IDE download dependencies automatically (it reads `pom.xml`) — this needs an internet connection and may take a minute the first time.
4. Open `src/main/java/com/paysow/PaysowApplication.java` and click the green ▶️ **Run** button.
5. Once you see `Started PaysowApplication` in the console, open your browser to:
   ```
   http://localhost:8080
   ```

**Option 2: Using the command line (Maven Wrapper)**
1. Open a terminal in the `paysow` folder.
2. Run:
   ```bash
   ./mvnw spring-boot:run
   ```
   (On Windows, use `mvnw.cmd spring-boot:run` instead.)
3. Wait for `Started PaysowApplication`, then open `http://localhost:8080` in your browser.

> **Note:** This project doesn't ship the Maven Wrapper `.jar`/scripts by default in this download. If `./mvnw` doesn't exist or fails, just install [Apache Maven](https://maven.apache.org/download.cgi) and run `mvn spring-boot:run` instead, or use Option 1 (IDE) which handles this automatically.

### Common errors & fixes
| Error | Fix |
|---|---|
| `Port 8080 already in use` | Close whatever else is using that port, or change `server.port=8081` in `application.properties`. |
| `JAVA_HOME not set` / wrong Java version | Make sure Java **17 or newer** is installed and selected in your IDE's Project SDK settings. |
| Dependencies fail to download | Check your internet connection — Maven needs to download Spring Boot libraries the first time. |
| Blank/white page or 404 | Make sure you're going to `http://localhost:8080` (not https) and that the console shows "Started PaysowApplication" with no red errors above it. |
| Data disappears after restart | The app uses a file-based H2 database (`./data/paysowdb`) so your data *should* persist. If you want a totally fresh database every run instead, change the URL in `application.properties` to `jdbc:h2:mem:paysowdb`. |

### Viewing the database directly (optional)
While the app is running, visit `http://localhost:8080/h2-console`.
- **JDBC URL**: `jdbc:h2:file:./data/paysowdb` (or `jdbc:h2:mem:paysowdb` if you used the in-memory option)
- **Username**: `sa`
- **Password**: *(leave blank)*

---

## Project Structure
```
paysow/
├── pom.xml
├── src/main/java/com/paysow/
│   ├── PaysowApplication.java
│   ├── config/          → PasswordConfig, WebConfig
│   ├── interceptor/     → AuthInterceptor (guards logged-in pages)
│   ├── model/           → User, Transaction (+ 4 subclasses)
│   ├── repository/      → UserRepository, TransactionRepository
│   ├── service/         → UserService, WalletService (+ impl/)
│   ├── exception/       → custom exceptions
│   └── controller/      → Auth, Dashboard, Wallet, Savings, Profile
└── src/main/resources/
    ├── application.properties
    ├── templates/       → Thymeleaf HTML pages
    └── static/css/      → style.css
```
