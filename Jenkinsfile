
node {
  git url: 'https://github.com/jglick/simple-maven-project-with-tests.git'
  def mvnHome = tool 'maven3'

  stage('Clean') {
    sh "${mvnHome}/bin/mvn clean"
  }

  stage('Build') {
    sh "${mvnHome}/bin/mvn compile test-compile"
  }

  stage('Test') {
    sh "${mvnHome}/bin/mvn verify"
    junit 'reports/**/*.xml'
  }

  stage('Deploy') {
  
  }

  //step([$class: 'ArtifactArchiver', artifacts: '**/target/*.jar', fingerprint: true])
  //step([$class: 'JUnitResultArchiver', testResults: '**/target/surefire-reports/TEST-*.xml'])
}

/*node ('master'){
    stage('Build and Test') {
        properties([pipelineTriggers([[$class: 'GitHubPushTrigger']])])
        checkout scm
        env.PATH = "${tool 'maven3'}/bin:${env.PATH}"
        sh 'mvn clean package'
    }
}*/
