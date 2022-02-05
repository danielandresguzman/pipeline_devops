def call(){
  pipeline {
      agent any
      environment {
          NEXUS_USER         = credentials('NEXUS-USER')
          NEXUS_PASS     = credentials('NEXUS-PASS')
      }
      figlet 'Ejecución'
      parameters {
          choice choices: ['maven', 'gradle'], description: 'Seleccione una herramienta para preceder a compilar', name: 'compileTool'
      }
      stages {
          stage("Pipeline"){
              steps {
                  script{
                    
                    switch(params.compileTool)
                        {
                            case 'Maven':
                                maven.call(verifyBranchName())
                            break;
                            case 'Gradle':
                                echo 'Ejecución con Gradle'
                                figlet 'Ejecución con Gradle'
                                gradle.call(verifyBranchName())
                            break;
                        }
                    }
              }
              post{
          success{
            slackSend color: 'good', message: "[Mentor] [${JOB_NAME}] [${BUILD_TAG}] Ejecucion Exitosa", teamDomain: 'dipdevopsusac-tr94431'
          }
          failure{
            slackSend color: 'danger', message: "[Mentor] [${env.JOB_NAME}] [${BUILD_TAG}] Ejecucion fallida en stage [${env.TAREA}]", teamDomain: 'dipdevopsusac-tr94431'
          }
        }
          }
      }
  }
}

def verifyBranchName(){
	if(env.GIT_BRANCH.contains('feature-') || env.GIT_BRANCH.contains('develop')) {
		return 'CI'
	} else {
		return 'CD'
	}
}
return this;