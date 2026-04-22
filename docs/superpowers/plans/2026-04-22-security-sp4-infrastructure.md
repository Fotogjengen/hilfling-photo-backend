# Sub-project 4: Infrastructure — Implementation Plan

> **For agentic workers:** REQUIRED SUB-SKILL: Use superpowers:subagent-driven-development (recommended) or superpowers:executing-plans to implement this plan task-by-task. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** Update outdated CI/CD actions, harden Docker Compose, update the mockito-kotlin dependency, and add git SHA tagging to Docker image builds.

**Architecture:** Purely infrastructure changes — CI workflow files, Docker Compose, and pom.xml. No application code changes.

**Tech Stack:** GitHub Actions, Docker Compose, Maven

---

## File Structure

| Action | Path | Responsibility |
|--------|------|----------------|
| Modify | `.github/workflows/ci-cd.yml` | Update actions, add SHA tagging |
| Modify | `.github/workflows/codeql-analysis.yml` | Update setup-java action |
| Modify | `docker-compose.yml` | Remove exposed postgres port, disable Traefik insecure API |
| Modify | `pom.xml` | Update mockito-kotlin dependency |
| Modify | `src/test/kotlin/no/fg/hilflingbackend/dto/PhotoGangBangerDtoSpec.kt` | Update mockito import if needed |

---

### Task 1: Update CI/CD GitHub Actions

**Files:**
- Modify: `.github/workflows/ci-cd.yml`

- [ ] **Step 1: Update docker/build-push-action version**

In `.github/workflows/ci-cd.yml`, replace line 45:

```yaml
        uses: docker/build-push-action@v2.2.1 #very very very outdated latest as of 22.02.2026 is v6.19.2: https://github.com/docker/build-push-action/tree/v6.19.2
```

with:

```yaml
        uses: docker/build-push-action@v6
```

- [ ] **Step 2: Add git SHA tag to Docker image**

In `.github/workflows/ci-cd.yml`, replace lines 47-48:

```yaml
        with:
          tags: # optional
            fotogjengen/hilfling-backend:latest
```

with:

```yaml
        with:
          tags: |
            fotogjengen/hilfling-backend:latest
            fotogjengen/hilfling-backend:${{ github.sha }}
```

- [ ] **Step 3: Update other outdated actions**

In `.github/workflows/ci-cd.yml`, replace line 35:

```yaml
        uses: docker/setup-buildx-action@v1
```

with:

```yaml
        uses: docker/setup-buildx-action@v3
```

Replace line 38:

```yaml
        uses: docker/login-action@v1
```

with:

```yaml
        uses: docker/login-action@v3
```

- [ ] **Step 4: Commit**

```bash
git add .github/workflows/ci-cd.yml
git commit -m "fix: update CI/CD actions to current versions, add git SHA Docker tag"
```

---

### Task 2: Update CodeQL Workflow

**Files:**
- Modify: `.github/workflows/codeql-analysis.yml`

- [ ] **Step 1: Update setup-java action**

In `.github/workflows/codeql-analysis.yml`, replace line 41:

```yaml
      uses: actions/setup-java@v4
```

with:

```yaml
      uses: actions/setup-java@v5
```

- [ ] **Step 2: Commit**

```bash
git add .github/workflows/codeql-analysis.yml
git commit -m "fix: update actions/setup-java to v5 in CodeQL workflow"
```

---

### Task 3: Harden Docker Compose

**Files:**
- Modify: `docker-compose.yml`

- [ ] **Step 1: Remove postgres host port mapping**

In `docker-compose.yml`, delete lines 73-74:

```yaml
    ports:
      - "5432:5432"
```

The `expose: 5432` on lines 71-72 remains, allowing container-to-container communication.

- [ ] **Step 2: Disable Traefik insecure API**

In `docker-compose.yml`, replace line 18:

```yaml
      - "--api.insecure=true"
```

with:

```yaml
      - "--api.insecure=false"
```

- [ ] **Step 3: Commit**

```bash
git add docker-compose.yml
git commit -m "fix: remove exposed postgres port, disable Traefik insecure API"
```

---

### Task 4: Update mockito-kotlin Dependency

**Files:**
- Modify: `pom.xml`
- Modify: `src/test/kotlin/no/fg/hilflingbackend/dto/PhotoGangBangerDtoSpec.kt` (if it imports mockito)

- [ ] **Step 1: Update pom.xml dependency**

In `pom.xml`, replace lines 169-174:

```xml
		<dependency>
			<groupId>com.nhaarman.mockitokotlin2</groupId>
			<artifactId>mockito-kotlin</artifactId>
			<version>2.2.0</version>
			<scope>test</scope>
		</dependency>
```

with:

```xml
		<dependency>
			<groupId>org.mockito.kotlin</groupId>
			<artifactId>mockito-kotlin</artifactId>
			<version>5.4.0</version>
			<scope>test</scope>
		</dependency>
```

- [ ] **Step 2: Check if test file imports need updating**

Read `src/test/kotlin/no/fg/hilflingbackend/dto/PhotoGangBangerDtoSpec.kt` and check for `import com.nhaarman.mockitokotlin2`. If present, replace with `import org.mockito.kotlin`.

If no mockito imports are found, skip this step.

- [ ] **Step 3: Verify compilation**

Run: `mvn compile -pl . && mvn test-compile -pl .`
Expected: BUILD SUCCESS

- [ ] **Step 4: Commit**

```bash
git add pom.xml
git commit -m "fix: update mockito-kotlin from legacy 2.2.0 to 5.4.0"
```
