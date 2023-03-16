pipeline {
    agent {label 'slave1'}
    environment {
        branch="${env.gitlabSourceBranch}"
    }
    options {
        timestamps()    //设置在项目打印日志时带上对应时间
        disableConcurrentBuilds()   //不允许同时执行流水线，被用来防止同时访问共享资源等
        timeout(time: 20, unit: 'MINUTES')   // 设置流水线运行超过n分钟，Jenkins将中止流水线
//         buildDiscarder(logRotator(numToKeepStr: '20'))   // 表示保留n次构建历史
    }
    stages{
        stage('Print Message') {      //打印信息!
            steps {
                echo '打印信息'
                echo "workspace: ${WORKSPACE}"
                echo "${branch}"
           }
           post {
                failure {
                    updateGitlabCommitStatus name: 'Print Message', state: 'failed'
                }
            }
        }
        stage('Maven Build') {
            agent{
                docker {
                    image 'maven:3-alpine'
                    args '-v /root/.m2:/root/.m2'
                    reuseNode true
                }
            }
            steps {
                echo "maven build start"
                sh "ls -al"
                sh 'mvn --version'
                sh "mvn clean package jacoco:report -Dmaven.test.failure.ignore=true"
                sh "cd target && ls -l"
                jacoco()
                echo "maven build finished"
                // sh "cd target && ls -l"
            }
            post {
                failure {
                    updateGitlabCommitStatus name: 'Maven Build', state: 'failed'
                }
            }
        }
        stage('Run backend Shell On Service') {
            steps {
                echo "start run container"
                sh "bash backend.sh"
                echo "run container finished"
            }
            post {
                failure {
                    updateGitlabCommitStatus name: 'Run backend Shell On Service', state: 'failed'
                }
            }
        }
        stage('Run python Shell On Service'){
            steps {
                echo "start deal python"
                sh "sudo cp -r similarity-service /home/ubuntu"
                sh "cd similarity-service && bash run.sh"
                echo "copy and container finished"
            }
            post {
                failure {
                    updateGitlabCommitStatus name: 'Run python Shell On Service', state: 'failed'
                }
                success {
                    updateGitlabCommitStatus name:'build', state: 'success'
                }
            }
        }
    }
}