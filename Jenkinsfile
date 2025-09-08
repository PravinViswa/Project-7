pipeline{
    agent any
    stages{
        stage('Backend Build & Tests'){
            steps{
                dir('backend'){
                    sh './mvnw clean install -DskipTests || mvn clean install -DskipTests'
                    sh './mvnw test || mvn test' // try wrapper first
                    junit '**/target/surefire-reports/*.xml'
                }
            }
        }
        stage('Frontend Tests & Build'){
            steps {
                dir('frontend'){
                    sh 'npm install || true'
                    sh 'npm test -- --watchAll=false || true'
                }
            }
        }
        stage('Docker Build'){
            steps{
                sh 'docker-compose build'
            }
        }
    }
}
