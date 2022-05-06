/*pipeline {
    agent any
    stages {
        stage("Build Maven") {
            steps {
                sh 'mvn -B clean package'
            }
        }
        stage("Run Gatling") {
            steps {
                sh 'mvn gatling:test -Dgatling.simulationClass=simulations.ComputerDatabase'
            }
            post {
                always {
                    gatlingArchive()
                }
            }
        }
    }
}*/

pipeline {
    agent {
        docker {
          image 'chiquanhong/gatling-runner'
          }
     }
    stages {
        stage("Run Gatling from docker container") {
            steps {
                sh 'docker run gatling-runner -r test -DUSERS 5 -DRAMPUP 10 -DRAMPUP_DURATION 20 -DDURATION 30'
            }
            post {
                always {
                    gatlingArchive()
                }
            }
        }
    }