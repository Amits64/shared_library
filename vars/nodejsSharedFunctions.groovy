def ciPipeline(String registry, String registryCredential, String image, String tag, String sonarHostUrl, String repoUrl) {
    pipeline {
        agent any
        
        environment {
            SONAR_SCANNER_IMAGE = 'sonarsource/sonar-scanner-cli:latest'
            REGISTRY_CREDENTIAL = "${registryCredential}"
            SONARQUBE_ENV = 'sonarqube'
        }
        
        stages {
            stage('Git Checkout') {
                steps {
                    checkout([
                        $class: 'GitSCM',
                        branches: [[name: 'main']],
                        userRemoteConfigs: [[url: repoUrl]]
                    ])
                }
            }
    
            stage('Static Code Analysis') {
                steps {
                    script {
                        docker.image(SONAR_SCANNER_IMAGE).inside() {
                            withSonarQubeEnv(SONARQUBE_ENV) {
                                sh """
                                sonar-scanner \
                                -Dsonar.host.url=${sonarHostUrl} \
                                -Dsonar.projectKey=${image} \
                                -Dsonar.exclusions=**/*.java
                                """
                            }
                        }
                    }
                }
            }
    
            stage('Quality Gate') {
                steps {
                    script {
                        timeout(time: 1, unit: 'MINUTES') {
                            waitForQualityGate abortPipeline: true
                        }
                    }
                }
            }
    
            stage('Build Docker Image') {
                steps {
                    script {
                        docker.withRegistry("https://${registry}", REGISTRY_CREDENTIAL) {
                            docker.build("${registry}/${image}:${tag}", "-f Dockerfile .").push()
                        }
                    }
                }
            }
        }
    }
}
