## Routine quotidienne Git

Chaque membre de l’équipe travaille uniquement sur sa branche `feature/...`.

---

### 1. Commencer la journée

Avant de coder, récupérer les dernières modifications :

```bash
git checkout votre-branche
git pull origin votre-branche
git fetch origin
git merge origin/develop
````
```
---

## Branches du projet

| Branche                 | Rôle                        |
| ----------------------- | --------------------------- |
| `main`                  | Version stable du projet    |
| `develop`               | Branche d’intégration       |
| `feature/model`         | Modèle du jeu               |
| `feature/rules-engine`  | Moteur de règles et actions |
| `feature/ihm`           | Interface utilisateur       |
| `feature/ia`            | Intelligence artificielle   |
| `feature/tests`         | Tests                       |
| `feature/documentation` | Documentation               |

---
### 2. Sauvegarder son travail

Après avoir terminé une partie du travail :

```bash
git status
git add .
git commit -m "type: description courte"
git push origin votre-branche
```

Types de commits recommandés :

| Type       | Utilisation                |
| ---------- | -------------------------- |
| `feat`     | Nouvelle fonctionnalité    |
| `fix`      | Correction de bug          |
| `docs`     | Documentation              |
| `test`     | Tests                      |
| `refactor` | Réorganisation du code     |
| `style`    | Mise en forme ou interface |
| `chore`    | Tâche technique mineure    |

---

### 3. Créer une Pull Request

Quand la tâche est terminée, créer une Pull Request sur GitHub :

```text
base: develop
compare: votre-branche
```

Exemple :

```text
feature/ihm → develop
```

La Pull Request doit contenir :

```markdown
## Ce qui a été fait

- ...

## Comment tester

- ...

## Remarques

- ...
```


```
```
