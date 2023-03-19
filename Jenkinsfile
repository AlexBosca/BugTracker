#!groovy
pipeline {
    agent any

    environment {
        MAIL = credentials('MAIL')
    }
    
    stages {
        stage('Pull GitHub Code') {
            steps {
                git branch: 'CU-85zrt8xa4_Add-credentials-using-in-Jenkinsfile',
                    url: 'https://github.com/AlexBosca/BugTracker.git'
            }
        }
        
        stage('Backend - Build') {
            steps {
                echo 'Build Backend'
            }
        }

        stage('Backend - Run Unit Tests') {
            steps {
                echo 'Run Backend Unit Tests'
                bat 'mvn -f backend test -Dspring.profiles.active=dev'
            }
        }

        stage('Backend - Run Integration Tests') {
            steps {
                echo 'Run Backend Integration Tests'
            }
        }

        stage('Backend - SonarQube Analysis') {
            steps {
                withSonarQubeEnv(credentialsId: 'SONARQUBE_TOKEN', installationName: 'SonarQube') {
                    bat 'mvn -f backend clean install -Dspring.profiles.active=dev sonar:sonar -Dsonar.sources=src/main/java/ -Dsonar.java.binaries=target/classes'
                }
            }
        }

        stage('Frontend - Build') {
            steps {
                echo 'Build Frontend'
            }
        }

        stage('Frontend - Run Unit Tests') {
            steps {
                echo 'Run Frontend Unit Tests'
            }
        }

        stage('Frontend - Run Integration Tests') {
            steps {
                echo 'Run Frontend Integration Tests'
            }
        }

        stage('Frontend - SonarQube Analysis') {
            steps {
                withSonarQubeEnv(credentialsId: 'SONARQUBE_TOKEN', installationName: 'SonarQube') {
                    bat 'mvn -f backend clean install -Dspring.profiles.active=dev sonar:sonar -Dsonar.sources=src/main/java/ -Dsonar.java.binaries=target/classes'
                }
            }
        }

        stage('System Tests') {
            steps {
                echo 'Run System Tests'
            }
        }

        stage('Build Docker Images') {
            steps {
                echo 'Build Docker Images'
            }
        }

        stage('Push Images on DockerHub') {
            steps {
                echo 'Push Images on DockerHub'
            }
        }

        stage('Archiving Artifacts') {
            steps {
                archiveArtifacts artifacts: 'backend/target/backend.jar, backend/target/backend.war'
            }
        }
    }
}
