def ciPipeline(String registry, String registryCredential, String image, String tag, String sonarHostUrl, String repoUrl) {
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
                    def sonarScannerImage = 'sonarsource/sonar-scanner-cli:latest'
                    docker.image(sonarScannerImage).inside() {
                        withSonarQubeEnv('sonarqube') {
                            sh """
                            sonar-scanner \
                            -Dsonar.host.url=${sonarHostUrl} \
                            -Dsonar.projectKey="${image}" \
                            -Dsonar.exclusions=**/*.java
                            """
                        }
                    }
                }
            }
        }

        stage('Quality Gate') {
            steps {
                waitForQualityGate abortPipeline: true
            }
        }

        stage('Build Docker Image') {
            steps {
                script {
                    docker.build("${registry}/${image}:${tag}", "-f Dockerfile .")
                }
            }
        }
    }
}
