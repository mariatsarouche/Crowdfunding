pipeline {

agent any

environment {
        DOCKER_TOKEN=credentials('docker-push-secret')
        DOCKER_USER='nikos-kaparos'
        DOCKER_SERVER='ghcr.io'
        DOCKER_PREFIX='ghcr.io/nikos-kaparos/crowdfunding'
    }


stages {


    stage('Test') {
        steps {
            dir('backend'){
                sh '''
                pwd
                echo "Start testing"
                mvn clean test
            '''
            }
        }
    }

    stage('Docker build and push') {
            steps {
                dir('backend'){
                    sh '''
                    HEAD_COMMIT=$(git rev-parse --short HEAD)
                    TAG=$HEAD_COMMIT-$BUILD_ID
                    docker build --rm -t $DOCKER_PREFIX:$TAG -t $DOCKER_PREFIX:latest -f Dockerfile .
                '''
                    sh '''
                    echo $DOCKER_TOKEN | docker login $DOCKER_SERVER -u $DOCKER_USER --password-stdin
                    docker push $DOCKER_PREFIX --all-tags
                '''
                }
            }
        }



    // stage('deploy to kubernetes') {
    //         steps {
    //             sh '''
    //                 HEAD_COMMIT=$(git rev-parse --short HEAD)
    //                 TAG=$HEAD_COMMIT-$BUILD_ID
    //                 export ANSIBLE_CONFIG=~/workspace/ansible/ansible.cfg
    //                 ansible-playbook -i ~/workspace/ansible/hosts.yaml -e new_image=$DOCKER_PREFIX:$TAG ~/workspace/ansible/playbooks/k8s-update-spring-deployment.yaml
    //             '''
    //         }
    //     }

}

}