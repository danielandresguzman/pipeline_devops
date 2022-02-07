
def call(String pipelineType){
    figlet pipelineType
    figlet "Laboratorio"
    runCI()
   /* if (pipelineType == 'CI') {
        runCI()
    } else {
        runCD()
    }
    */
}

def compile(){
    env.DESCRTIPTION_STAGE = 'compilar'
    stage("${env.DESCRTIPTION_STAGE}"){
        env.STAGE = "build - ${env.DESCRTIPTION_STAGE}"
        sh "echo  ${env.STAGE}"
        sh "mvn clean compile -e"
    }
}

def unitTest(){
    env.DESCRTIPTION_STAGE = 'testear'
    stage("${env.DESCRTIPTION_STAGE}"){
        env.STAGE = "${env.DESCRTIPTION_STAGE}"
        sh "echo  ${env.STAGE}"
        sh "mvn clean test -e"
    }
}

def jar(){
    env.DESCRTIPTION_STAGE = "jar"
    stage("${env.DESCRTIPTION_STAGE}"){
        env.STAGE = "empaquetar - ${DESCRTIPTION_STAGE}"
        sh "echo  ${env.STAGE}"
        sh 'mvn clean package -e'
    }
}

def sonar(){
    env.DESCRTIPTION_STAGE = "sonar"
    stage("${env.DESCRTIPTION_STAGE}"){
        env.STAGE = "sonar - ${DESCRTIPTION_STAGE}"
        withSonarQubeEnv('sonarqube') {
                  sh "echo  ${env.STAGE}"
          sh 'mvn clean verify sonar:sonar -Dsonar.projectKey=Laboratorio_mod3Grupo2 -Dsonar.java.binaries=build'
      }
    }
}

def nexusUpload(){
    env.DESCRTIPTION_STAGE = "nexusUpload"
    stage("${env.DESCRTIPTION_STAGE}"){
        nexusPublisher nexusInstanceId: 'nexus',
        nexusRepositoryId: 'devops-usach-nexus',
        packages: [
            [$class: 'MavenPackage',
                mavenAssetList: [
                    [classifier: '',
                    extension: '.jar',
                    filePath: 'build/DevOpsUsach2020-0.0.1.jar'
                ]
            ],
                mavenCoordinate: [
                    artifactId: 'DevOpsUsach2020',
                    groupId: 'com.devopsusach2020',
                    packaging: 'jar',
                    version: '0.0.1'
                ]
            ]
        ]
        env.STAGE = "upload_nexus - ${DESCRTIPTION_STAGE}"
        sh "echo  ${env.STAGE}"
    }
}

def gitCreateRelease(){
    env.DESCRTIPTION_STAGE = "gitCreateRelease"
    stage("${env.DESCRTIPTION_STAGE}"){
        env.STAGE = "gitCreateRelease - ${DESCRTIPTION_STAGE}"
        sh "echo  ${env.STAGE}"
    }
}

def gitCrearrama(){
    env.DESCRTIPTION_STAGE = "gitCrearrama"
    stage("${env.DESCRTIPTION_STAGE}"){
        env.STAGE = "gitCrearrama - ${DESCRTIPTION_STAGE}"
        sh "echo  ${env.STAGE}"
        withCredentials([gitUsernamePassword(credentialsId: 'usergithub', gitToolName: 'Default')]) {
            sh 'git checkout -b ramanueva'
            }
    }
}

def gitDiff(){
    env.DESCRTIPTION_STAGE = "gitDiff"
    stage("${env.DESCRTIPTION_STAGE}"){
        env.STAGE = "gitDiff - ${DESCRTIPTION_STAGE}"
        sh "echo  ${env.STAGE}"
        echo 'Diferencias con branch main'
        sh "git diff --name-only origin/main..${env.GIT_BRANCH}"
    }
}

def nexusDownload(){
    env.DESCRTIPTION_STAGE = "nexusDownload"
   stage("${env.DESCRTIPTION_STAGE}"){
        env.STAGE = "download_nexus - ${DESCRTIPTION_STAGE}"
        sh "echo  ${env.STAGE}"
        sh ' curl -X GET -u $NEXUS_USER:$NEXUS_PASS "http://nexus:8081/repository/devops-usach-nexus/com/devopsusach2020/DevOpsUsach2020/0.0.1/DevOpsUsach2020-0.0.1.jar" -O'
    }

}

def runartefacto(){
    env.DESCRTIPTION_STAGE = "run artefacto nexus"
   stage("${env.DESCRTIPTION_STAGE}"){
        env.STAGE = "${DESCRTIPTION_STAGE}"
        sh "echo  ${env.STAGE}"
        sh 'nohup bash java -jar DevOpsUsach2020-0.0.1.jar & >/dev/null'
    }

}

def test(){
    env.DESCRTIPTION_STAGE = "test servicios descargados"
   stage("${env.DESCRTIPTION_STAGE}"){
        env.STAGE = "${DESCRTIPTION_STAGE}"
        sh "echo  ${env.STAGE}"
        //sh "sleep 60 && curl -X GET 'http://localhost:8080/rest/mscovid/estadoMundial?msg=testing'"
        def continuar=true
        def intento=0
        def intentoMax=10
        while(continuar){
            intento++
            try {
                //sh "curl -X GET 'http://localhost:8080/rest/mscovid/test?msg=testing'  >> /dev/null "
                sh "curl -X GET 'http://localhost:8080/rest/mscovid/estadoMundial?msg=testing'"
                continuar=false
                sh "echo '#### ARRANCADO Intento:${intento}'"
            } catch (Exception e){
                sh "echo '#### AUN NO ARRANCA intento:${intento}'"
                if(intento>intentoMax){
                    continuar=false
                    sh "echo '#### Fail intento:${intento}'"
                    throw new Exception("Se demoro mucho en arrancar   :( ")
                } else {
                    sh "sleep 5"
                }
            }
        }

    }
}





def runCI(){
    gitCrearrama()
   compile()
   unitTest()
   jar()
   //sonar()
   nexusUpload()
   gitCreateRelease()
   gitDiff()
   nexusDownload()
   runartefacto()
   test()
  
}

def runCD(){

}



/*
def call(stages){
  stage("Paso 1: Compliar"){
    sh "mvn clean compile -e"
  }
  stage("Paso 2: Testear"){
    sh "mvn clean test -e"
  }
  stage("Paso 3: Build .Jar"){
    sh "mvn clean package -e"
  }
  stage("Paso 4: Sonar - Análisis Estático"){
      sh "echo 'Análisis Estático!'"
      withSonarQubeEnv('sonarqube') {
          sh 'mvn clean verify sonar:sonar -Dsonar.projectKey=ejemplo-gradle -Dsonar.java.binaries=build'
      }
  }
  stage("Paso 5: Levantar Springboot APP"){
        sh 'mvn spring-boot:run &'
  }
  stage("Paso 6: Subir Nexus"){
      nexusPublisher nexusInstanceId: 'nexus',
      nexusRepositoryId: 'devops-usach-nexus',
      packages: [
          [$class: 'MavenPackage',
              mavenAssetList: [
                  [classifier: '',
                  extension: '.jar',
                  filePath: 'build/DevOpsUsach2020-0.0.1.jar'
              ]
          ],
              mavenCoordinate: [
                  artifactId: 'DevOpsUsach2020',
                  groupId: 'com.devopsusach2020',
                  packaging: 'jar',
                  version: '0.0.1'
              ]
          ]
      ]
  }
  stage("Paso 7: Descargar Nexus"){
      sh ' curl -X GET -u $NEXUS_USER:$NEXUS_PASS "http://nexus:8081/repository/devops-usach-nexus/com/devopsusach2020/DevOpsUsach2020/0.0.1/DevOpsUsach2020-0.0.1.jar" -O'
  }
  stage("Paso 8: Levantar Artefacto Jar"){
      sh 'nohup bash java -jar DevOpsUsach2020-0.0.1.jar & >/dev/null'
  }
  stage("Paso 9: Testear Artefacto - Dormir(Esperar 20sg) "){
      sh "sleep 20 && curl -X GET 'http://localhost:8081/rest/mscovid/test?msg=testing'"
  }*/

return this;