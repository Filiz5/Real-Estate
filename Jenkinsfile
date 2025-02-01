pipeline {
    agent any

    environment {
        APP_REPO_NAME = "esenkaya123/real_estate-team1"
    }

    stages {
        stage('Prepare Tags for Docker Images') {
            steps {
                echo 'Preparing Tags for Docker Images'
                script {
                    env.IMAGE_TAG_FE = "${APP_REPO_NAME}:f.${BUILD_NUMBER}"
                    env.IMAGE_TAG_BE = "${APP_REPO_NAME}:b.${BUILD_NUMBER}"
                }
            }
        }

        stage('Build App Docker Images') {
            steps {
                echo 'Building App Dev Images'
                sh """
                    pwd
                    docker build --force-rm -t "${IMAGE_TAG_FE}" "${WORKSPACE}/Real-Estate-Team1/True-Roots-Frontend"
                    docker build --force-rm -t "${IMAGE_TAG_BE}" "${WORKSPACE}/Real-Estate-Team1/True-Roots-Backend"
                    docker image ls
                """
            }
        }

        stage('Push Images to Docker Hub') {
            steps {
                echo "Pushing App Images to Docker Hub"
                sh """
                    docker push "${IMAGE_TAG_FE}"
                    docker push "${IMAGE_TAG_BE}"
                """
            }
        }


        stage('Run Docker Compose') {
            steps {
                echo 'Running Docker Compose'
                sh """
                    docker-compose -f ${WORKSPACE}/docker-compose.yml up -d --build
                """
                // Eğer logları görmek isterseniz aşağıdaki satırı ekleyebilirsiniz:
                // sh "docker-compose -f ${WORKSPACE}/docker-compose.yml logs -f"
            }
        }
    }

    post {
        always {
            echo 'Deleting all local images'
            sh 'docker image prune -af'
        }

        success {
            echo 'Pipeline successfully completed.'
            timeout(time: 5, unit: 'DAYS') {
                input message: 'Pipeline tamamlandı. Sonlandırmak istiyor musunuz?'
            }
            echo 'Pipeline sonlandırıldı.'
        }

        failure {
            echo 'Pipeline failed.'
            timeout(time: 5, unit: 'DAYS') {
                input message: 'Pipeline başarısız oldu. Sonlandırmak istiyor musunuz?'
            }
            echo 'Pipeline sonlandırıldı.'
        }
    }
}
