# MyFirstOwnGatlingProject
first own gatling project

## create docker image
docker build -t gatling-runner .

## run created docker image
docker run gatling-runner -r <name of the report bucket>

## run tests via docker command line with parameters
docker run gatling-runner -r test -DUSERS 5 -DRAMPUP 10 -DRAMPUP_DURATION 20 -DDURATION 30

## run tests via gatling command line
mvn gatling:test -Dgatling.simulationClass=simulations.ComputerDatabase
  
## to run in jenkins
  - create a jenkins pipeline (not freestyle)
  - connect to repo
  - link the "Jenkinsfile" in it
