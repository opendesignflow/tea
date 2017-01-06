node ('master'){
    stage('Build and Test') {
        properties([pipelineTriggers([[$class: 'GitHubPushTrigger']])])
        checkout scm
        env.PATH = "${tool 'maven3'}/bin:${env.PATH}"
        sh 'mvn clean package'
    }
}
