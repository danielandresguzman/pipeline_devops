def call(){
    pipeline {
        agent any
        environment {
            NEXUS_USER         = credentials('NEXUS-USER')
            NEXUS_PASS     = credentials('NEXUS-PASS')
        }
        parameters {
            choice( name:'compileTool', choices: ['Maven', 'Gradle'], description: 'Seleccione herramienta de compilacion' )
        }
        stages {
            stage("Pipeline"){
                steps {
                    script{
                    def ci_or_cd = verifyBranchName()
                    figlet ci_or_cd
                    figlet params.compileTool
                    switch(params.compileTool)
                        {
                            case 'Maven':
                                figlet 'Ejecución con Maven'
                                maven.call(verifyBranchName())
                            break;
                            case 'Gradle':
                                figlet 'Ejecución con Gradle anterior'
                                gradle.call(verifyBranchName())
                            break;
                        }
                    }
                }
                post{
                    success{
                        slackSend color: 'good', message: "[Daniel Guzman] [${JOB_NAME}] [${BUILD_TAG}] Ejecucion Exitosa", teamDomain: 'dipdevopsusac-tr94431', tokenCredentialId: 'tokenslack'
                    }
                    failure{
                        slackSend color: 'danger', message: "[Daniel Guzman] [${env.JOB_NAME}] [${BUILD_TAG}] Ejecucion fallida en stage [${${env.DESCRTIPTION_STAGE}}]", teamDomain: 'dipdevopsusac-tr94431', tokenCredentialId: 'tokenslack'
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