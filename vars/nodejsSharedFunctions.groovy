def call(Map params) {
    pipeline {
        agent any

        stages {
            stage('Git Checkout') {
                steps {
                    checkout([
                        $class: 'GitSCM',
                        branches: [[name: 'main']],
                        userRemoteConfigs: [[url: 'https://github.com/Amits64/crud-app.git']]
                    ])
                }
            }

            stage('Static Code Analysis') {
                steps {
                    script {
                        def sonarScannerImage = 'sonarsource/sonar-scanner-cli:latest'
                        docker.image(sonarScannerImage).inside() {
                            withSonarQubeEnv('sonarqube') {
                                sh """
                                sonar-scanner \
                                -Dsonar.host.url=${params.sonarHostUrl} \
                                -Dsonar.projectKey="${params.image}" \
                                -Dsonar.exclusions=**/*.java
                                """
                            }
                        }
                    }
                }
            }

            stage('Build Docker Image') {
                steps {
                    script {
                        docker.build("${params.registry}/${params.image}:${params.tag}", "-f Dockerfile .")
                    }
                }
            }
        }
    }
}
