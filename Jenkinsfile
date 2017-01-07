
node {
 
  def mvnHome = tool 'maven3'

  stage('Clean') {
    checkout scm
    sh "${mvnHome}/bin/mvn -B clean"
  }

  stage('Build') {
    sh "${mvnHome}/bin/mvn -B  compile test-compile"
  }

  stage('Test') {
    sh "${mvnHome}/bin/mvn -B -Dmaven.test.failure.ignore verify"
    junit '**/target/surefire-reports/TEST-*.xml'
  }

  stage('Deploy') {
      sh "${mvnHome}/bin/mvn -B -Dmaven.test.failure.ignore deploy"
      step([$class: 'ArtifactArchiver', artifacts: '**/target/*.jar', fingerprint: true])
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
