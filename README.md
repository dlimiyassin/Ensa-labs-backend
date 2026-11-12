# ENSA Labs — Backend

Spring Boot 4 (Java 21) REST API for the research labs platform of ENSA Béni Mellal:
labs, teams, members, publications, theses, collaborations, equipment and competences.
The Angular frontend lives in the `frontend` repository.

## Requirements

- Java 21
- PostgreSQL (a database named `ensa_labs` by default)

## Run

```bash
./mvnw spring-boot:run
```

The API listens on `http://localhost:8080`, Swagger UI on `http://localhost:8080/swagger-ui.html`.

On an **empty** database the seeders create the roles, the admin account and the LaRESI / LRSTA
data (members, teams, resources, collaborations, productions). On an existing database only the
roles and the admin account are checked: lab content is then managed from the dashboard and is
never overwritten on restart.

## Configuration

Every setting has a local default in `src/main/resources/application.yml` and can be overridden
with an environment variable:

| Variable | Default | Purpose |
|---|---|---|
| `DB_URL` | `jdbc:postgresql://localhost:5432/ensa_labs` | JDBC URL |
| `DB_USERNAME` / `DB_PASSWORD` | `postgres` / `1234` | Database credentials |
| `SERVER_PORT` | `8080` | HTTP port |
| `JWT_SECRET` | local development value | HMAC secret, **at least 32 characters; set it in every shared environment** |
| `JWT_ACCESS_EXPIRATION_MS` | `1800000` (30 min) | Access token lifetime |
| `JWT_REFRESH_EXPIRATION_MS` | `604800000` (7 days) | Refresh token lifetime |
| `CORS_ALLOWED_ORIGINS` | `http://localhost:4200` | Comma-separated frontend origins |
| `UPLOAD_DIR` | `uploads` | Folder for uploaded images (git-ignored) |
| `SECURITY_ENABLED` | `true` | `false` opens every endpoint (local debugging only) |

## Security model

Authentication is stateless JWT:

- `POST /api/auth/login` returns `{ accessToken, refreshToken, expiresIn }`.
- `POST /api/auth/refresh` exchanges a refresh token for a new pair.
- `GET /api/account/me` returns the account, its roles and its lab / team scope.

Four authorization roles (`User.roles`). The business role inside a lab stays on `Member.roleInLab`.
A user's lab / team scope comes from the member linked to the account.

| Role | Can do |
|---|---|
| `ROLE_SUPER_ADMIN` | Everything: labs, users, roles, research themes, all lab data |
| `ROLE_LAB_DIRECTOR` | In **his lab**: update the lab, manage teams, members, collaborations, resources, axes, publications, theses |
| `ROLE_TEAM_LEADER` | In **his lab**: create / update theses and productions; create / update publications of **his teams** |
| `ROLE_RESEARCHER` | Create publications in **his lab**, update **his own** publications |

All `GET` endpoints of the public website are anonymous. Role checks use `@PreAuthorize`
(`recherche/ws/Authorities.java`); lab / team ownership is enforced in the services by
`AccessControlService`. Errors come back as `{ message, details, errors? }` with 400 / 401 / 403 / 404 / 409.

### Seeded accounts (empty database)

| Account | Password | Roles |
|---|---|---|
| `admin` | `AA111` | Super admin |
| `allaoui.rabha` | `ALLAOUIR` | Lab director (LaRESI) + researcher |
| `soufiane.belhouideg` | `SOUFIANE` | Lab director (LRSTA) + researcher |
| every other seeded member | see below | Researcher; team responsables also get team leader |

Member accounts are named `firstname.lastname` (lower case, as in the seed data) and their initial
password is the first 8 letters of `FIRSTNAMELASTNAME` in upper case (the generated CIN).
**Change these passwords before any real deployment** (dashboard › Utilisateurs or Mon compte).

Databases created before this version are migrated on start: the old `ADMIN` / `RESEARCHER`
roles are renamed to `ROLE_SUPER_ADMIN` / `ROLE_RESEARCHER`, and lab directors and team
responsables receive their new role once.

## Main endpoints

| Resource | Path | Notes |
|---|---|---|
| Labs | `/api/labs` | `acronym/{acronym}`; `thematiquesRecherche` replaces `domainesRecherche` |
| Teams | `/api/equipes` | `lab/{acronym}` |
| Members | `/api/members` | `image` = uploaded picture path |
| Publications | `/api/publications` | attached automatically to the lab production (communications by type) |
| Theses | `/api/theses` | attached automatically to the lab production |
| Productions | `/api/productions` | `lab/{acronym}` returns an empty production when none exists |
| Collaborations | `/api/collaborations` | `academic`, `industrial`, `lab/{acronym}`; optional `logo` |
| Competences / Equipment | `/api/competences`, `/api/equipments` | `lab/{acronym}` |
| Research axes | `/api/axes-recherche` | linked to a lab or to a team |
| Research themes | `/api/thematiques-recherche` | shared catalogue (super admin) |
| Users / roles | `/api/users`, `/api/roles` | super admin |
| Account | `/api/account/me`, `/api/account/password`, `/api/account/stats` | authenticated |
| Files | `GET /api/files/get-image?path=`, `POST /api/files/image` (multipart `file`, `folder`) | paths are relative to `UPLOAD_DIR` |

## Tests

```bash
./mvnw test -Dtest='JWTHelperTest,AccessControlServiceTest'
```

These unit tests need no database. `LabsApplicationTests` starts the full context and needs PostgreSQL.
