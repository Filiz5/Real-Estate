pipeline {
    agent any

    environment {
        APP_REPO_NAME = "esenkaya123/real_estate-team1"
        IMAGE_TAG_FE = "${APP_REPO_NAME}:f.${BUILD_NUMBER}"
        IMAGE_TAG_BE = "${APP_REPO_NAME}:b.${BUILD_NUMBER}"
    }

    stages {
        stage('Prepare Docker Compose File') {
            steps {
                echo 'Replacing environment variables in docker-compose-template.yml'
                sh """
                    export IMG_TAG_FE="${IMAGE_TAG_FE}"
                    export IMG_TAG_BE="${IMAGE_TAG_BE}"
                    envsubst < ${WORKSPACE}/docker-compose-template.yml > ${WORKSPACE}/docker-compose-n.yml
                    cat ${WORKSPACE}/docker-compose-n.yml  # Dosyanın içeriğini kontrol etmek için
                """
            }
        }

        stage('Build Docker Images') {
            steps {
                echo 'Building Docker Images'
                sh """
                    docker build --force-rm -t "${IMAGE_TAG_FE}" "${WORKSPACE}/True-Roots-Frontend"
                    docker build --force-rm -t "${IMAGE_TAG_BE}" "${WORKSPACE}/True-Roots-Backend"
                    docker image ls
                """
            }
        }

        stage('Push Images to Docker Hub') {
            steps {
                echo 'Logging into Docker Hub and pushing images'
                sh """
                    docker login -u esenkaya123 -p Canahmet63
                    docker push "${IMAGE_TAG_FE}"
                    docker push "${IMAGE_TAG_BE}"
                """
            }
        }

        stage('Stop and Remove Existing Containers') {
            steps {
                echo 'Stopping and removing any running containers'
                sh """
                    docker ps -q | xargs -r docker stop
                    docker ps -aq | xargs -r docker rm
                """
            }
        }

        stage('Run Docker Compose') {
            steps {
                echo 'Starting Docker Containers with Docker Compose'
                sh """
                    docker-compose -f ${WORKSPACE}/docker-compose-n.yml up -d --build
                """
            }
        }
    }

    post {
        always {
            echo 'Cleaning up unused Docker images'
            sh 'docker image prune -af'
        }

        success {
            echo 'Pipeline successfully completed.'
        }

        failure {
            echo 'Pipeline failed.'
        }
    }
}
