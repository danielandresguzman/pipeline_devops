
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

def compilar(){
    env.DESCRTIPTION_STAGE = 'compilar'
    stage("${env.DESCRTIPTION_STAGE}"){
        env.STAGE = "build - ${env.DESCRTIPTION_STAGE}"
        sh "echo  ${env.STAGE}"
        sh "mvn clean compile -e"
    }
}

def testear(){
    env.DESCRTIPTION_STAGE = 'testear'
    stage("${env.DESCRTIPTION_STAGE}"){
        env.STAGE = "${env.DESCRTIPTION_STAGE}"
        sh "echo  ${env.STAGE}"
        sh "mvn clean test -e"
    }
}

def runCI(){
   compilar()
   testear()
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