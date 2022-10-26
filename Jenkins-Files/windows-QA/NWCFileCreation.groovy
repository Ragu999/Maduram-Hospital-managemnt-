node('windows-jobs'){
stage('service stop')
{
  node('master'){
    // Run the job in master jenkins
      stage('playbook-checkout')
      {
        // clean the current workspace and checkout the code from Devops repository
          cleanWs() 
          checkout([$class: 'SubversionSCM', additionalCredentials: [], excludedCommitMessages: '', excludedRegions: '', excludedRevprop: '', excludedUsers: '', filterChangelog: false, ignoreDirPropChanges: false, includedRegions: '', locations: [[cancelProcessOnExternalsFail: true, credentialsId: '585fa701-720a-4602-abe6-b977c8773a4c', depthOption: 'infinity', ignoreExternalsOption: true, local: '.', remote: 'https://192.168.1.200/svn/CloudBLM-DevOps/deployment/windows-playbook/QA']], quietOperation: true, workspaceUpdater: [$class: 'UpdateUpdater']])
      }
      stage('Stop IIS website')
      {
        // Stop the IIS service via ansible
        sh 'ansible-playbook NWC-QA-stop.yml'
      }
  }
}
node('windows-jobs'){
  // Run the job in slave jenkins
stage('checkout from CBLM repo') {
  // clean the current workspace and checkout the code from CBLM repository
    cleanWs()
checkout([$class: 'SubversionSCM', additionalCredentials: [], excludedCommitMessages: '', excludedRegions: '', excludedRevprop: '', excludedUsers: '', filterChangelog: false, ignoreDirPropChanges: false, includedRegions: '', locations: [[cancelProcessOnExternalsFail: true, credentialsId: '585fa701-720a-4602-abe6-b977c8773a4c', depthOption: 'infinity', ignoreExternalsOption: true, local: '.', remote: 'https://192.168.1.200/svn/API/Products/Cloud BLM/Application/branches/Experimental branch/BackEnd Services- Unified Database/CLoudBLM-NativeFilesService/NWC_Creation']], quietOperation: true, workspaceUpdater: [$class: 'UpdateUpdater']])
}
stage('Remove obj')
{ 
  //Remove the object file 

               bat 'del /Q CloudBLM-NativeFile-BusinessLogic\\obj'
               bat 'del /Q CloudBLM-NativeFile-BusinessLogicInterface\\obj'
               bat 'del /Q CloudBLM-NativeFile-DatabaseLogic\\obj'
               bat 'del /Q CloudBLM-NativeFile-DatabaseLogicInterface\\obj '
               bat 'del /Q CloudBLM-NativeFile-Entities\\obj '
               bat 'del /Q CloudBLM-NativeFile-Model\\obj'
               bat 'del /Q NWC_Creation\\obj'            
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
   bat 'dotnet publish NWC_Creation.sln -c Release -o C:\\inetpub\\wwwroot\\NWCFileCreationSDK-QA'
    deleteDir()
}
}
 stage('service stop')
{
  node('master'){
    // Run the job in master jenkins

      stage('playbook-checkout')
      {
        // clean the current workspace and checkout the code from Devops repository
          cleanWs() 
          checkout([$class: 'SubversionSCM', additionalCredentials: [], excludedCommitMessages: '', excludedRegions: '', excludedRevprop: '', excludedUsers: '', filterChangelog: false, ignoreDirPropChanges: false, includedRegions: '', locations: [[cancelProcessOnExternalsFail: true, credentialsId: '585fa701-720a-4602-abe6-b977c8773a4c', depthOption: 'infinity', ignoreExternalsOption: true, local: '.', remote: 'https://192.168.1.200/svn/CloudBLM-DevOps/deployment/windows-playbook/QA']], quietOperation: true, workspaceUpdater: [$class: 'UpdateUpdater']])
      }
      stage('Stop IIS website')
      {
        // Start the IIS service via ansible delete the working dir in jenkins
        sh 'ansible-playbook NWC-QA-stop.yml'
         deleteDir()
      }
  }
}

}