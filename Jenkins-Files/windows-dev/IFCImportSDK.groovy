node('windows-jobs'){
stage('service stop')
{
  node('master'){
    // Run the job in master jenkins
      stage('playbook-checkout')
      {
        // clean the current workspace and checkout the code from Devops repository
          cleanWs() 
          checkout([$class: 'SubversionSCM', additionalCredentials: [], excludedCommitMessages: '', excludedRegions: '', excludedRevprop: '', excludedUsers: '', filterChangelog: false, ignoreDirPropChanges: false, includedRegions: '', locations: [[cancelProcessOnExternalsFail: true, credentialsId: '585fa701-720a-4602-abe6-b977c8773a4c', depthOption: 'infinity', ignoreExternalsOption: true, local: '.', remote: 'https://192.168.1.200/svn/CloudBLM-DevOps/deployment/windows-playbook/Dev']], quietOperation: true, workspaceUpdater: [$class: 'UpdateUpdater']])
      }
      stage('Stop IIS website')
      {
          // Stop the IIS service via ansible
        sh 'ansible-playbook ifcimportsdk-Dev-stop.yml'
      }
  }
}
node('windows-jobs'){
  // Run the job in slave jenkins
stage('checkout from CBLM repo') {
  // clean the current workspace and checkout the code from CBLM repository
    cleanWs()
    checkout([$class: 'SubversionSCM', additionalCredentials: [], excludedCommitMessages: '', excludedRegions: '', excludedRevprop: '', excludedUsers: '', filterChangelog: false, ignoreDirPropChanges: false, includedRegions: '', locations: [[cancelProcessOnExternalsFail: true, credentialsId: '585fa701-720a-4602-abe6-b977c8773a4c', depthOption: 'infinity', ignoreExternalsOption: true, local: '.', remote: 'https://192.168.1.200/svn/API/Products/Cloud BLM/Application/branches/Experimental branch/BackEnd Services- Unified Database/IFCImportSDK']], quietOperation: true, workspaceUpdater: [$class: 'UpdateUpdater']])
}
stage('Remove obj')
{
//Remove the object file 
               bat 'del /Q IFC-DatabaseEntities\\obj'
               bat 'del /Q IFCImport_DatabaseLogic\\obj'
               bat 'del /Q IFCImport-BusinessLogic\\obj'
               bat 'del /Q IFCImport-DatabaseLogic\\obj'
               bat 'del /Q IFCImport-IBusinessLogic\\obj'
               bat 'del /Q IFCImport-IDatabaseLogic\\obj'
               bat 'del /Q IFCImportSDK\\obj'
               bat 'del /Q IFC-Models\\obj'
               bat 'del /Q IFCImportSDK\\obj'
               
}
stage('clean')
{
    bat 'dotnet clean'
}
stage('build')
{
    bat 'dotnet build'
}

stage('publish')
{
  //publish and delete the working dir in jenkins

   bat 'dotnet publish IFCImportSDK.sln -c Release -o C:\\inetpub\\wwwroot\\IFCImportSDK'
    deleteDir()
}
}

    stage('service start')
{
  node('master'){
    // Run the job in master jenkins
      stage('playbook-checkout')
      {
  
     // clean the current workspace and checkout the code from Devops repository

          cleanWs() 
          checkout([$class: 'SubversionSCM', additionalCredentials: [], excludedCommitMessages: '', excludedRegions: '', excludedRevprop: '', excludedUsers: '', filterChangelog: false, ignoreDirPropChanges: false, includedRegions: '', locations: [[cancelProcessOnExternalsFail: true, credentialsId: '585fa701-720a-4602-abe6-b977c8773a4c', depthOption: 'infinity', ignoreExternalsOption: true, local: '.', remote: 'https://192.168.1.200/svn/CloudBLM-DevOps/deployment/windows-playbook/Dev']], quietOperation: true, workspaceUpdater: [$class: 'UpdateUpdater']])
      }
      stage('Start IIS website')
      {
        // Start the IIS service via ansible delete the working dir in jenkins
        sh 'ansible-playbook ifcimportsdk-Dev-start.yml'
         deleteDir()
      }
  }


}

}