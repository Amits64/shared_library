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
                                -Dsonar.exclusions=**/*.java \
                                -Dsonar.sourceEncoding=UTF-8
                                """
                            }
                        }
                    }
                }
            }
    
            stage('Quality Gate') {
                steps {
                    script {
                        timeout(time: 5, unit: 'MINUTES') { // Increased timeout to 5 minutes
                            waitForQualityGate abortPipeline: true
                        }
                    }
                }
            }
    
            stage('Build Docker Image') {
                steps {
                    script {
                        docker.withRegistry("https://${registry}", REGISTRY_CREDENTIAL) {
                            def customDockerfile = 'Dockerfile'  // Adjust if Dockerfile has a different name or path
                            def dockerImage = docker.build("${registry}/${image}:${tag}", "-f ${customDockerfile} .")
                            dockerImage.push()
                        }
                    }
                }
            }
        }
        
        post {
            always {
                script {
                    echo "CI Pipeline executed for image ${registry}/${image}:${tag}"
                }
            }
        }
    }
}
