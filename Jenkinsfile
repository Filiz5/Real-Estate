pipeline {
    agent any

    environment {
        APP_REPO_NAME = "esenkaya123/real_estate-team1"
        IMAGE_TAG_FE = "${APP_REPO_NAME}:f.${BUILD_NUMBER}"
        IMAGE_TAG_BE = "${APP_REPO_NAME}:b.${BUILD_NUMBER}"
    }

    stages {
        stage('Build App Docker Images') {
            steps {
                echo 'Building App Dev Images'
                sh """
                    pwd
                    echo "FE Image: ${IMAGE_TAG_FE}"
                    echo "BE Image: ${IMAGE_TAG_BE}"
                    docker build --force-rm -t "${IMAGE_TAG_FE}" "${WORKSPACE}/True-Roots-Frontend"
                    docker build --force-rm -t "${IMAGE_TAG_BE}" "${WORKSPACE}/True-Roots-Backend"
                    docker image ls
                """
            }
        }

        stage('Push Images to Docker Hub') {
            steps {
                echo "Pushing App Images to Docker Hub"
                sh """
                    docker login -u esenkaya123 -p Canahmet63
                    docker push "${IMAGE_TAG_FE}"
                    docker push "${IMAGE_TAG_BE}"
                """
            }
        }

        stage('Run Docker Compose') {
            steps {
                echo 'Replacing variables in docker-compose file'
                sh """
                    export IMG_TAG_FE="${IMAGE_TAG_FE}"
                    export IMG_TAG_BE="${IMAGE_TAG_BE}"
                    envsubst < ${WORKSPACE}/docker-compose-template.yml > ${WORKSPACE}/docker-compose-n.yml
                    cat ${WORKSPACE}/docker-compose-n.yml  # Dosyanın içeriğini kontrol için
                    docker-compose -f ${WORKSPACE}/docker-compose-n.yml up -d --build
                """
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
        }

        failure {
            echo 'Pipeline failed.'
        }
    }
}
