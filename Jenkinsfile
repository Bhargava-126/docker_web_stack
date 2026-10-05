pipeline {
    agent any

    tools {
        maven "mymaven3"   // Manage Jenkins > Tools > Maven name
    }

    environment {
        DOCKERHUB_USER = 'YOUR_DOCKERHUB_USER'          // <-- change to your Docker Hub username
        NEXUS_URL      = '<NEXUS-PUBLIC-IP>:8081'  // <-- change
    }

    stages {

        stage('Code Checkout') {
            steps {
                git 'https://github.com/DevOpsCloudZone/docker_web_stack.git'
            }
        }

        stage('Build and Test') {
            steps {
                sh 'mvn clean install'
            }
        }

        stage('Code Quality Analysis') {
            steps {
                withSonarQubeEnv('mysonarqube') {
                    sh "mvn clean verify org.sonarsource.scanner.maven:sonar-maven-plugin:sonar -Dsonar.projectKey=sonarproject -Dsonar.projectName='sonarproject'"
                }
            }
        }

        stage('Code Quality Gates') {
            steps {
                waitForQualityGate abortPipeline: true, credentialsId: 'sonarqube'
            }
        }

        stage('Artifact') {
            steps {
                nexusArtifactUploader(
                    artifacts: [[
                        artifactId: 'vprofile',
                        classifier: '',
                        file: 'target/vprofile-v2.war',
                        type: 'war'
                    ]],
                    credentialsId: 'nexusid',
                    groupId: 'com.visualpathit',
                    nexusUrl: "${NEXUS_URL}",
                    nexusVersion: 'nexus3',
                    protocol: 'http',
                    repository: 'nexusrepo',   // must be a maven2 (hosted) repo
                    version: 'v2'
                )
            }
        }

        stage('Docker Build') {
            steps {
                sh "docker build -t ${DOCKERHUB_USER}/appstack:app -f Docker-app/Dockerfile ."
                sh "docker build -t ${DOCKERHUB_USER}/appstack:db -f Docker-db/Dockerfile ."
            }
        }

        stage('Docker Push') {
            steps {
                withCredentials([
                    usernamePassword(
                        credentialsId: 'dockerhub_id',
                        usernameVariable: 'DOCKER_USERNAME',
                        passwordVariable: 'DOCKER_PASSWORD'
                    )
                ]) {
                    sh '''
                        echo "$DOCKER_PASSWORD" | docker login -u "$DOCKER_USERNAME" --password-stdin
                        docker push ''' + "${DOCKERHUB_USER}" + '''/appstack:app
                        docker push ''' + "${DOCKERHUB_USER}" + '''/appstack:db
                        docker logout
                    '''
                }
            }
        }

        stage('Deploy') {
            steps {
                sh 'docker stack deploy -c compose.yml appstack'
            }
        }
    }
}
