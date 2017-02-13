package com.appbuilder.service

import java.io._
import scala.collection.mutable.ArrayBuffer
import collection.JavaConversions._
import org.springframework.stereotype.Service
import scala.sys.process._

@Service
class GradleService(var errorMsg: String) {
	
	def this (){
      		this("")
  	}

	def executeGradle(path : String) : Boolean = {
		/*System.out.println("Generating .apk file....");
		String dest = path;
		System.out.println("in the repository..." + dest);
		File projectDir = new File(dest);
		File gradlefile = new File("/usr/local/tomcat/gradle-3.3/daemon/3.3");
		ProjectConnection connection = GradleConnector.newConnector().useInstallation(gradlefile).forProjectDirectory(projectDir).connect();

		try {
			connection.newBuild().forTasks("assembleDebug").run();
			
		} catch(Exception e){
			System.out.println("ERROR: " + e);
			errorMsg = e;
			return false;
		} finally {
			connection.close();
		}*/

		try {
			val output = Seq("gradle", "-p", path, "assembleDebug").!!
			println(output)
			if(output.indexOf("FAILED") > -1) {
				errorMsg = "Gradle Build Failed"
				return false
			}
		} catch {
			case ex: Exception => { ex.printStackTrace(); ex.toString() }
			errorMsg = "Error Message: " + ex.toString
			return false
		} 
		return true
	}
	
	def getErrorMsg() : String = errorMsg

	
}
