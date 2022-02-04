def call(){
  
  pipeline {
    agent any
    environment {
        NEXUS_USER      = credentials('NEXUS-USER')
        NEXUS_PASS = credentials('NEXUS-PASS')
        NOMBRE      ='Daniel Guzman'
    }
    parameters {
        choice(
            name:'compileTool',
            choices: ['Maven', 'Gradle'],
            description: 'Seleccione herramienta de compilacion'
        )
    }
    stages {
        stage("Pipeline"){
            steps {
                script{
                
	            //slackSend color: 'good', message: "[${NOMBRE}] [${JOB_NAME}] [${BUILD_TAG}] Ejecucion Exitosa", teamDomain: 'dipdevopsusac-tr94431', tokenCredentialId: 'tokenslack'
                //slackSend color: 'danger', message: "[${NOMBRE}] [${env.JOB_NAME}] [${BUILD_TAG}] Ejecucion fallida en stage [${env.TAREA}]", teamDomain: 'dipdevopsusac-tr94431', tokenCredentialId: 'tokenslack'
                  switch(params.compileTool)
                    {
                        case 'Maven':
                            def ejecucion = load 'maven.groovy'
                            slackSend color: 'good', message: "entre por maven[${NOMBRE}] [${JOB_NAME}] [${BUILD_TAG}] Ejecucion Exitosa", teamDomain: 'dipdevopsusac-tr94431', tokenCredentialId: 'tokenslack'    
                            ejecucion.call()
                        break;
                        case 'Gradle':
                            def ejecucion = load 'gradle.groovy'
                            slackSend color: 'good', message: "entre por gradle[${NOMBRE}] [${JOB_NAME}] [${BUILD_TAG}] Ejecucion Exitosa", teamDomain: 'dipdevopsusac-tr94431', tokenCredentialId: 'tokenslack'    
                            ejecucion.call()
                        break;



                    }
                }
            }
            post{
				success{
					slackSend color: 'good', message: "[Su Nombre] [${JOB_NAME}] [${BUILD_TAG}] Ejecucion Exitosa", teamDomain: 'dipdevopsusac-tr94431', tokenCredentialId: 'tokenslack'
				}
				failure{
					slackSend color: 'danger', message: "[Su Nombre] [${env.JOB_NAME}] [${BUILD_TAG}] Ejecucion fallida en stage [${env.TAREA}]", teamDomain: 'dipdevopsusac-tr94431', tokenCredentialId: 'tokenslack'
				}
			}
        }
    }
}

}

return this;