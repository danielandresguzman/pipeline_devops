def call(){
    env.TAREA = "Paso 1: Build && Test"
    stage("$env.TAREA"){
        sh "echo 'Build && Test!'"
        sh "gradle clean build"
        // code
    }
    env.TAREA="Paso 2: Sonar - Análisis Estático"
    stage("$env.TAREA"){
        sh "echo 'Análisis Estático!'"
        withSonarQubeEnv('sonarqube') {
            sh "echo 'Calling sonar by ID!'"
            // Run Maven on a Unix agent to execute Sonar.
            sh './gradlew sonarqube -Dsonar.projectKey=ejemplo-gradle -Dsonar.java.binaries=build'
        }
    }
    env.TAREA="Paso 3: Curl Springboot Gradle sleep 20"
    stage("$env.TAREA"){
    stage("Paso 3: Curl Springboot Gradle sleep 20"){
        sh "gradle bootRun&"
        sh "sleep 20 && curl -X GET 'http://localhost:8081/rest/mscovid/test?msg=testing'"
    }
    env.TAREA="Paso 3: Curl Springboot Gradle sleep 20"
    stage("$env.TAREA"){
    stage("Paso 4: Subir Nexus"){
        nexusPublisher nexusInstanceId: 'nexus',
        nexusRepositoryId: 'devops-usach-nexus',
        packages: [
            [$class: 'MavenPackage',
                mavenAssetList: [
                    [classifier: '',
                    extension: 'jar',
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
    env.TAREA="Paso 3: Curl Springboot Gradle sleep 20"
    stage("$env.TAREA"){
    stage("Paso 5: Descargar Nexus"){
        sh ' curl -X GET -u $NEXUS_USER:$NEXUS_PASS "http://nexus:8081/repository/devops-usach-nexus/com/devopsusach2020/DevOpsUsach2020/0.0.1/DevOpsUsach2020-0.0.1.jar" -O'
    }
    env.TAREA="Paso 3: Curl Springboot Gradle sleep 20"
    stage("$env.TAREA"){
    stage("Paso 6: Levantar Artefacto Jar"){
        sh 'nohup java -jar DevOpsUsach2020-0.0.1.jar & >/dev/null'
    }
    env.TAREA="Paso 3: Curl Springboot Gradle sleep 20"
    stage("$env.TAREA"){
    stage("Paso 7: Testear Artefacto - Dormir(Esperar 20sg) "){
        sh "sleep 20 && curl -X GET 'http://localhost:8081/rest/mscovid/test?msg=testing'"
    }
}
return this;