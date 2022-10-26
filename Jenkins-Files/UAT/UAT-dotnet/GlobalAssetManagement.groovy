 node('master') {
    try {
    	def dotnet = '/usr/bin/dotnet'
    	stage('Checkout') {
          cleanWs()
    	 checkout([$class: 'SubversionSCM', additionalCredentials: [], excludedCommitMessages: '', excludedRegions: '', excludedRevprop: '', excludedUsers: '', filterChangelog: false, ignoreDirPropChanges: false, includedRegions: '', locations: [[cancelProcessOnExternalsFail: true, credentialsId: '585fa701-720a-4602-abe6-b977c8773a4c', depthOption: 'infinity', ignoreExternalsOption: true, local: '.', remote: 'https://192.168.1.200/svn/api/Products/Cloud BLM/Application/branches/Experimental branch/BackEnd Services- Unified Database/CloudBLM-Global']], quietOperation: true, workspaceUpdater: [$class: 'UpdateUpdater']])
    	}
    		stage('Remove obj') {
    		sh  'rm -rf */obj'
    	}
    		stage('Clean') {
    		sh  'dotnet clean'
    	}
    	stage('Build') {
    		sh  'dotnet build --configuration Release'
    	}
    	stage('Stop Service') {
    		sh  'sudo systemctl stop cloudblm-globalasset-service.service'
    	}
    	stage('Publish') {
    		sh  'sudo dotnet publish -c Release --output /opt/apps/GlobalAssetManagementService'
    	}
    	stage('Start Service') {
    		sh  'sudo systemctl start cloudblm-globalasset-service.service'
			deleteDir()
    	}
     currentBuild.result = 'SUCCESS'
    } catch (Exception err) {
        currentBuild.result = 'FAILURE'
    }
		   stage('Notify') {
        if(currentBuild.result == 'FAILURE') {
         mail bcc: '', body: "Hi,\n Jenkins have some issues in  ${JOB_NAME} build #${BUILD_NUMBER} \n\n Build URL = ${BUILD_URL} \n\n Job URL = ${JOB_URL}", cc: '', from: 'sysadmin@srinsofttech.com', replyTo: '', subject: "Problem in ${JOB_NAME} build #${BUILD_NUMBER}", to: 'jagadeesan@srinsofttech.com'
        }
    }
}