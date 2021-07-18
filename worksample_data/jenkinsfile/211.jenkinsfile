#!groovy

@Library("jenkinslib@master") _

def deploy = new org.devops.deploy()

String hosts = "${env.hosts}"
String func = "${env.func}"

pipeline{
    agent any

    stages{
        stage("deploy"){
            steps{
                script{
                    deploy.ansibleDeploy(hosts,func)
                }
            }
        }
    }
}
