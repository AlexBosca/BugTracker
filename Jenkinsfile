#!groovy
pipeline {
    agent any
    stages {
        stage('SCM') {
            steps {
                git 'https://github.com/AlexBosca/BugTracker.git'
            }
        }
        stage('SonarQube analysis') {
            steps {
                withSonarQubeEnv(credentialsId: 'SONARQUBE_TOKEN', installationName: 'SonarQube') {
                    bat 'mvn -f backend clean install -Dspring.profiles.active=dev sonar:sonar -Dsonar.sources=src/main/java/ -Dsonar.java.binaries=target/classes'
                }
            }
        }
        stage('Archiving Artifacts') {
            steps {
                archiveArtifacts artifacts: 'backend/target/backend.jar, backend/target/backend.war'
            }
        }
    }
}
