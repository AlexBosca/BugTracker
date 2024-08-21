// #!groovy
// pipeline {
//     agent any

//     environment {
//         MAIL_CREDS = credentials('mail_credentials')
//     }
    
//     stages {
//         stage('Pull GitHub Code') {
//             steps {
//                 git branch: 'feature/CU-86c002z7k-CICD-Configuration', url: 'https://github.com/AlexBosca/BugTracker.git'
//             }
//         }
        
//         stage('Backend - Build') {
//             steps {
//                 echo 'Build Backend'
//             }
//         }

//         stage('Backend - Run Unit Tests') {
//             steps {
//                 echo 'Run Backend Unit Tests'
//                 bat 'mvn -f backend test -Dspring.profiles.active=dev'
//             }
//         }

//         stage('Backend - Run Integration Tests') {
//             steps {
//                 echo 'Run Backend Integration Tests'
//             }
//         }

//         stage('Backend - SonarQube Analysis') {
//             steps {
//                 withSonarQubeEnv(credentialsId: 'SONARQUBE_TOKEN', installationName: 'SonarQube') {
//                     bat 'mvn -f backend clean verify sonar:sonar -Dsonar.projectKey=ro.alexportfolio:backend -Dsonar.analysis.mode=publish'
//                 }
//             }
//         }
        
//         stage('Publish Code Coverage') {
//             steps {
//                 // Publish the JaCoCo coverage report
//                 jacoco execPattern: '**/target/jacoco.exec', 
//                        classPattern: '**/target/classes', 
//                        sourcePattern: '**/src/main/java', 
//                        exclusionPattern: '**/target/test-classes', 
//                        changeBuildStatus: true
//             }
//         }

//         stage('Frontend - Build') {
//             steps {
//                 echo 'Build Frontend'
//             }
//         }

//         stage('Frontend - Run Unit Tests') {
//             steps {
//                 echo 'Run Frontend Unit Tests'
//             }
//         }

//         stage('Frontend - Run Integration Tests') {
//             steps {
//                 echo 'Run Frontend Integration Tests'
//             }
//         }

//         stage('Frontend - SonarQube Analysis') {
//             steps {
//                 // withSonarQubeEnv(credentialsId: 'SONARQUBE_TOKEN', installationName: 'SonarQube') {
//                 //     bat 'mvn -f backend clean install -Dspring.profiles.active=dev sonar:sonar -Dsonar.sources=src/main/java/ -Dsonar.java.binaries=target/classes'
//                 // }

//                 echo 'Run SonarQube Analysis on Frontend'
//             }
//         }

//         stage('System Tests') {
//             steps {
//                 echo 'Run System Tests'
//             }
//         }

//         stage('Build Docker Images') {
//             steps {
//                 echo 'Build Docker Images'
//             }
//         }

//         stage('Push Images on DockerHub') {
//             steps {
//                 echo 'Push Images on DockerHub'
//             }
//         }

//         stage('Archiving Artifacts') {
//             steps {
//                 archiveArtifacts artifacts: 'backend/target/backend.jar, backend/target/backend.war'
//             }
//         }
//     }
    
//     post {
//         always {
//             // Archive the reports regardless of the build status
//             archiveArtifacts artifacts: 'backend/target/site/jacoco/*', allowEmptyArchive: true

//             // Publish the test results
//             junit 'backend/target/surefire-reports/*.xml'
            
//             recordCoverage(tools: [[parser: 'JACOCO', pattern: 'backend/target/site/jacoco/jacoco.xml']])
//         }
//     }
// }

#!groovy
pipeline {
    agent any

    environment {
        MAIL_CREDS = credentials('mail_credentials')
    }
    
    stages {
        // stage('Pull GitHub Code') {
        //     steps {
        //         git branch: 'feature/CU-86c002z7k-CICD-Configuration', url: 'https://github.com/AlexBosca/BugTracker.git'
        //     }
        // }
        
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
                    bat 'mvn -f backend clean verify sonar:sonar -Dspring.profiles.active=coverage -Dsonar.projectKey=ro.alexportfolio:backend -Dsonar.analysis.mode=publish'
                }
            }
        }
        
        // stage('Publish Code Coverage') {
        //     steps {
        //         // Publish the JaCoCo coverage report
        //         jacoco execPattern: '**/target/jacoco.exec', 
        //               classPattern: '**/target/classes', 
        //               sourcePattern: '**/src/main/java', 
        //               exclusionPattern: '**/target/test-classes', 
        //               changeBuildStatus: true
        //     }
        // }

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
                // withSonarQubeEnv(credentialsId: 'SONARQUBE_TOKEN', installationName: 'SonarQube') {
                //     bat 'mvn -f backend clean install -Dspring.profiles.active=dev sonar:sonar -Dsonar.sources=src/main/java/ -Dsonar.java.binaries=target/classes'
                // }

                echo 'Run SonarQube Analysis on Frontend'
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
    
    post {
        always {
            // Archive the reports regardless of the build status
            // archiveArtifacts artifacts: 'backend/target/jacoco/jacoco.xml', allowEmptyArchive: true

            // Publish the test results
            junit 'backend/target/surefire-reports/*.xml'
            
            recordCoverage(tools: [[parser: 'JACOCO', pattern: 'backend/target/site/jacoco/jacoco.xml']])
        }
    }
}

