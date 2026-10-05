# Docker Web Stack

Java (Servlet/JSP) + MySQL web app: register, login, session management, logout.
Built to be driven by the Jenkins -> SonarQube -> Nexus -> Docker Hub -> Swarm pipeline.

## Layout
- `src/main/java/...`   servlets, DAOs, password hashing, DB-backed sessions
- `src/main/webapp/`    HTML (JSP views), `css/style.css`, `js/app.js`
- `Docker-app/`         Tomcat image that serves the WAR
- `Docker-db/`          MySQL image
- `compose.yml`         Swarm stack (3 app replicas + 1 DB)
- `Jenkinsfile`         full pipeline

## Run locally (no pipeline)
```bash
mvn clean package                      # runs tests, builds target/vprofile-v2.war
docker network create appnet
docker build -t appstack:db  -f Docker-db/Dockerfile .
docker build -t appstack:app -f Docker-app/Dockerfile .
docker run -d --name devopsdb --network appnet appstack:db
docker run -d --name application --network appnet -p 1111:8080 appstack:app
# open http://localhost:1111   (wait ~30s for MySQL on first start)
```
