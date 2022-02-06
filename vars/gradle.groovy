/*
	forma de invocación de método call:
	def ejecucion = load 'script.groovy'
	ejecucion.call()

def call(String pipelineType){
    figlet pipelineType
    if (pipelineType == 'CI') {
        runCI()
    } else {
        runCD()
    }
}

def stageCleanBuildTest(){
    env.DESCRTIPTION_STAGE = 'Paso 1: Build - Test'
    stage("${env.DESCRTIPTION_STAGE}"){
        env.STAGE = "build - ${env.DESCRTIPTION_STAGE}"
        sh "echo  ${env.STAGE}"
        sh "gradle clean build"
    }
}

def stageSonar(){
    env.DESCRTIPTION_STAGE = "Paso 2: Sonar - Análisis Estático"
    stage("${env.DESCRTIPTION_STAGE}"){
        env.STAGE = "sonar - ${DESCRTIPTION_STAGE}"
        withSonarQubeEnv('sonarqube') {
            sh "echo  ${env.STAGE}"
            sh 'mvn clean verify sonar:sonar -Dsonar.projectKey=ejemplo-gradle -Dsonar.java.binaries=build'
        }
    }
}

def stageRunSpringCurl(){
    env.DESCRTIPTION_STAGE = "Paso 3: Curl Springboot Gralde sleep 20"
    stage("${env.DESCRTIPTION_STAGE}"){
        env.STAGE = "run_spring_curl - ${DESCRTIPTION_STAGE}"
        sh "echo  ${env.STAGE}"
        sh "gradle bootRun&"
        sh "sleep 20 && curl -X GET 'http://localhost:8081/rest/mscovid/test?msg=testing'"
    }
}

def stageUploadNexus(){
    env.DESCRTIPTION_STAGE = "Paso 4: Subir Nexus"
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

def stageDownloadNexus(){
    env.DESCRTIPTION_STAGE = "Paso 5: Descargar Nexus"
   stage("${env.DESCRTIPTION_STAGE}"){
        env.STAGE = "download_nexus - ${DESCRTIPTION_STAGE}"
        sh "echo  ${env.STAGE}"
        sh ' curl -X GET -u $NEXUS_USER:$NEXUS_PASSWORD "http://nexus:8081/repository/devops-usach-nexus/com/devopsusach2020/DevOpsUsach2020/0.0.1/DevOpsUsach2020-0.0.1.jar" -O'
    }
}

def stageRunJar(){
    env.DESCRTIPTION_STAGE = "Paso 6: Levantar Artefacto Jar"
    stage("${env.DESCRTIPTION_STAGE}"){
        env.STAGE = "run_jar - ${DESCRTIPTION_STAGE}"
        sh "echo  ${env.STAGE}"
        sh 'nohup bash java -jar DevOpsUsach2020-0.0.1.jar & >/dev/null'
    }
}

def stageCurlJar(){
    env.DESCRTIPTION_STAGE = "Paso 7: Testear Artefacto - Dormir Esperar 20sg "
    stage("${env.DESCRTIPTION_STAGE}"){
        env.STAGE = "curl_jar - ${DESCRTIPTION_STAGE}"
        sh "echo  ${env.STAGE}"
        sh "sleep 20 && curl -X GET 'http://localhost:8081/rest/mscovid/test?msg=testing'"
    }
}

def runCI(){
    stageCleanBuildTest()
    stageSonar()
    stageRunJar()
    stageUploadNexus()
}

def runCD(){
    stageDownloadNexus()
    stageRunJar()
    stageUploadNexus()
}

return this;*/
def call(){
    stage("Paso 1: Build && Test"){
        sh "gradle --version"
        sh "gradle clean build"
    }
   /* stage("Paso 2: Sonar - Análisis Estático"){
        sh "echo 'Análisis Estático!'"
        withSonarQubeEnv('sonarqube') {
            sh './gradlew sonarqube -Dsonar.projectKey=ejemplo-gradle -Dsonar.java.binaries=build'
        }
    }
    stage("Paso 3: Curl Springboot Gradle sleep 50"){
        sh "gradle bootRun&"
        sh "sleep 50 && curl -X GET 'http://localhost:8081/rest/mscovid/test?msg=testing'"
    }
    stage("Paso 4: Subir Nexus"){
        nexusPublisher nexusInstanceId: 'nexus',
        nexusRepositoryId: 'devops-usach-nexus',
        packages: [
            [$class: 'MavenPackage',
                mavenAssetList: [
                    [classifier: '',
                    extension: '.jar',
                    filePath: 'build/libs/DevOpsUsach2020-0.0.1.jar'
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
    stage("Paso 5: Descargar Nexus"){
        sh ' curl -X GET -u $NEXUS_USER:$NEXUS_PASS "http://nexus:8081/repository/devops-usach-nexus/com/devopsusach2020/DevOpsUsach2020/0.0.1/DevOpsUsach2020-0.0.1.jar" -O'
    }
    stage("Paso 6: Levantar Artefacto Jar"){
        sh 'nohup bash java -jar DevOpsUsach2020-0.0.1.jar & >/dev/null'
    }
    stage("Paso 7: Testear Artefacto - Dormir(Esperar 40sg) "){
        sh "sleep 40 && curl -X GET 'http://localhost:8081/rest/mscovid/test?msg=testing'"
    }*/
}
return this;