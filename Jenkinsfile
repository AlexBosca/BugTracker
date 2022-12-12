pipeline {
    agent any
    node {
        stage('SCM') {
            git 'https://github.com/AlexBosca/BugTracker.git'
        }
        stage('SonarQube analysis') {
            withSonarQubeEnv(credentialsId: 'SONARQUBE_TOKEN', installationName: 'SonarQube') {
                bat 'mvn -f backend clean install -Dspring.profiles.active=dev sonar:sonar -Dsonar.sources=src/main/java/ -Dsonar.java.binaries=target/classes'
            }
        }
        stage('Archiving Artifacts') {
            archiveArtifacts artifacts: 'backend/target/*.jar, backend/target/*.war'
        }
    }
}