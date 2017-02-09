package com.appbuilder.controller;

import com.appbuilder.model._
import com.appbuilder.service._

import scala.collection.mutable.ArrayBuffer
import java.io._
import scala.io.Source
import collection.JavaConversions._
import scala.collection.JavaConversions._
import util.control.Breaks._
import scala.sys.process._
import java.net.MalformedURLException;

import javax.servlet.ServletException;
import javax.servlet._
import javax.servlet.annotation._
import javax.servlet.http._

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.ModelMap
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestParam

@Controller
class BuilderController @Autowired()(githubService: GithubService, gradleService: GradleService){

	private var githubInfoList : java.util.List[GithubInfo] = ArrayBuffer[GithubInfo]()
	private var status : java.util.List[String] = ArrayBuffer[String]()
	private var msg : java.util.List[String] = ArrayBuffer[String]()

	@RequestMapping(value = Array("/appbuilder"), method = Array(RequestMethod.GET))
	def showProjectNumPage : String = {
		return "appbuilder"
	}

	@RequestMapping(value = Array("/appbuilder"), method = Array(RequestMethod.POST))
	def setProjectNum(model : ModelMap, @RequestParam numOfProj : String) : String = {
		//println("Project num: " + numOfProj)
		model.addAttribute("numOfProj", numOfProj)
        return "redirect:projectform"
	}

	@RequestMapping(value = Array("/projectform"), method = Array(RequestMethod.GET))
    def showProjectFormPage(model: ModelMap, @RequestParam numOfProj : String) : String = {
		val githubInfoWrapper : GithubInfoWrapper = new GithubInfoWrapper
		var i = 0
		for(i <- 0 to numOfProj.toInt-1) {
			val githubInfo = new GithubInfo
			githubInfoWrapper.add(githubInfo)
		}
		println("GithubInfoWrapper size: " + githubInfoWrapper.getGithubInfoList.size)
		model.addAttribute("githubInfoWrapper", githubInfoWrapper)
        return "projectform"
    }


    @RequestMapping(value = Array("/projectform"), method = Array(RequestMethod.POST))
    def HandleCloneAndBuild(model : ModelMap, githubInfoWrapper : GithubInfoWrapper) : Unit = {
		println(githubInfoWrapper.getGithubInfoList.size)
		println(githubInfoWrapper.toString)
		githubInfoList = githubInfoWrapper.getGithubInfoList
		status = ArrayBuffer.fill(githubInfoList.length)("Not Scheduled")
		msg = ArrayBuffer.fill(githubInfoList.length)("")

		var i = 0
		for(i <- 0 until githubInfoList.length){
	
			breakable {
				status(i) = "Scheduled"

				// check the repository
				println("checking the user name and repository name")
				if (!githubService.isRepoValid(githubInfoList(i).getURL)) {
					println("--------------------------------------------------")
					println(githubInfoList(i).toString)
					println("errorMsg: Invalid user name or repository name")
					println("--------------------------------------------------")
					msg(i) = "Invalid user name or repository name"
					status(i) = "Failed"
					githubInfoList(i).setLocalpath("")
					break
				}

				println("Start checking out the commit....")
				// check the commit hash and clone
				if(githubInfoList(i).getTags != "") {
					val commit : String = githubService.getCommitByTags(githubInfoList.get(i))
					println("Commit: " + commit)

					if(commit == "Commit Hash not found") {
						println("--------------------------------------------------")
						println(githubInfoList(i).toString + "TAG: " + githubInfoList(i).getTags)
						println("errorMsg: Error message: Invalid SHA tag" );
						println("--------------------------------------------------")
						
						msg(i) = "Invalid SHA tag"
						status(i) = "Failed"
						githubInfoList(i).setLocalpath("")
						break
					}
					else {
						println("--------------------------------------------------")
						println("Building Version: " + commit + "(" + githubInfoList(i).getTags + ")" )
						println("Start cloning....")
						println("--------------------------------------------------")
						
						githubService.cloneRemoteRepository(githubInfoList(i))
						githubService.getVersionByTags(githubInfoList.get(i))
						msg(i) = "Building Version: " + commit + "(" + githubInfoList(i).getTags + ")"
					}
				}
				else {
					println("--------------------------------------------------")
					println(githubInfoList(i).toString + ": Building the latest Version" )
					println("Start cloning latest version....")
					println("--------------------------------------------------")
					
					githubService.cloneRemoteRepository(githubInfoList(i))
					msg(i) = "Building the latest Version"
				}


				System.out.println("Start Building....");
			
				// build the project
				if(!gradleService.executeGradle(githubInfoList(i).getLocalpath)) {
					println("--------------------------------------------------")
					println(githubInfoList(i).toString + ": Fail to build the app project")
					println("Error message: " + gradleService.getErrorMsg)
					println("--------------------------------------------------")
					msg(i) = gradleService.getErrorMsg()
					status(i) = "Failed"
					break
				}
				status(i) = "Built"
			}
		}

		githubInfoWrapper.setMsg(msg)
		githubInfoWrapper.setStatus(status)
		println("--------------------------------------------------")
		println("projects are completed")
		println(githubInfoWrapper.toString)
		println("--------------------------------------------------")
	}

	@RequestMapping(value = Array("/result"), method = Array(RequestMethod.GET))
	def showResultPage(model : ModelMap, @RequestParam(value = "select1", required = false) select1: String, @RequestParam(value = "select2", required = false) select2 : String) : String = {
		//println(select1 + " " + select2)
		val num = githubInfoList.size
		var _githubInfoList : java.util.List[GithubInfo] = ArrayBuffer[GithubInfo]()
		var _msg : java.util.List[String] = ArrayBuffer[String]()
		var _status : java.util.List[String] = ArrayBuffer[String]()
		
		val repoList = createRepoNameList(num-1)
		
		//repoList.foreach(println)
		
		model.addAttribute("repoList", repoList)

		if(select1 != null && select2 != null) {
			showResultTable(_githubInfoList, _msg, _status, select1, select2, num-1)
		}
		//_githubInfoList.foreach(println)
		model.addAttribute("_githubInfoList", _githubInfoList)
		model.addAttribute("_Msg", _msg)
		model.addAttribute("_Status", _status)
		
		return "result"
	}

	@RequestMapping(value = Array("/dataquery"), method = Array(RequestMethod.GET))
	def showDataQueryPage(model:ModelMap, @RequestParam(value = "select1", required = false) select1 : String, @RequestParam(value = "select2", required = false) select2 : String) : String = {
		//System.out.println(select1 + " " + select2);
		val num = githubInfoList.size
		var _githubInfoList : java.util.List[GithubInfo] = ArrayBuffer[GithubInfo]()
		var _msg : java.util.List[String] = ArrayBuffer[String]()
		var _status : java.util.List[String] = ArrayBuffer[String]()

		val repoList = createRepoNameList(num-1)
		
		model.addAttribute("repoList", repoList)

		if(select1 != null && select2 != null) {
			showResultTable(_githubInfoList, _msg, _status, select1, select2, num-1)
		}
		//_githubInfoList.foreach(println)
		model.addAttribute("_githubInfoList", _githubInfoList)
		model.addAttribute("_Status", _status)
		
		return "dataquery"
	}

	@RequestMapping(value = Array("/download/{id}"), method = Array(RequestMethod.GET))
    def doDownload(request : HttpServletRequest, response : HttpServletResponse, @PathVariable("id") id: Int) : String = {
		
		println("downloading apk files from " + githubInfoList(id).getReponame + "...");
		println(id)
		val filePaths = findApkFile(githubInfoList(id).getLocalpath)
		println(filePaths)
		println(filePaths.length)
        val context : ServletContext = request.getSession().getServletContext()
        var i = 0
        for(i <- 0 until filePaths.length) {
        	val downloadFile : File = new File(filePaths(i))
            val inputStream : FileInputStream = new FileInputStream(downloadFile)
             
            // get MIME type of the file
            var mimeType : String = context.getMimeType(filePaths(i))
            if (mimeType == null) {
                // set to binary type if MIME mapping not found
                mimeType = "application/octet-stream"
            }
            //System.out.println("MIME type: " + mimeType);
     
            // set content attributes for the response
            response.setContentType(mimeType)
            response.setContentLength(downloadFile.length.asInstanceOf[Int])
     
            // set headers for the response
            val headerKey : String = "Content-Disposition"
            val headerValue : String = String.format("attachment; filename=\"%s\"", downloadFile.getName())
            response.setHeader(headerKey, headerValue)
     
            // get output stream of the response
            val outStream : OutputStream = response.getOutputStream()

     		val byteArray = Stream.continually(inputStream.read).takeWhile(-1 !=).map(_.toByte).toArray
    		outStream.write(byteArray)
        	inputStream.close()
            outStream.close()      
        }
        return "redirect:dataquery"
    }


	def createRepoNameList(num : Int) : java.util.List[String] = {
		var i = 0
		var name : String = " "
		var repoList : java.util.List[String] = ArrayBuffer[String]()
		for(i <- 0 to num) {
			val tmpName : String = githubInfoList(i).getUsername + "/" + githubInfoList(i).getReponame
			var j = 0
			breakable {
				for(name <- repoList) {
					if(tmpName == name) {
						break
					}
					else {
						j+=1
					}
				}
			}
			if(j == repoList.length) {
				repoList.add(tmpName)
			}
		}
		return repoList
	}
	
	def showResultTable(t_githubInfoList : java.util.List[GithubInfo], t_msg: java.util.List[String], t_status: java.util.List[String], select1: String , select2: String , num: Int) : Unit = {
		var i = 0
		
		if(select1 == "Name") {
			for(i <- 0 to num) {
				val tmpName : String = githubInfoList(i).getUsername() + "/" + githubInfoList(i).getReponame()
				if(tmpName == select2) {
					t_githubInfoList.add(githubInfoList(i))
					t_msg.add(msg(i))
					t_status.add(status(i))
				}
			}
		}
		else if(select1 == "Status") {
			for(i <- 0 to num) {
				if(status(i) == select2) {
					t_githubInfoList.add(githubInfoList(i))
					t_msg.add(msg(i))
					t_status.add(status(i))
				}
			}
		}
		else {
			for(i <- 0 to num) {
				t_githubInfoList.add(githubInfoList(i))
				t_msg.add(msg(i))
				t_status.add(status(i))
			}
		}
	}

	def findApkFile(path: String) : ArrayBuffer[String] = {
		var filePath = ArrayBuffer[String]()
		try {
			val output = Seq("find", path, "-name", "*.apk").!!
			println(output)
			val f = output.split("\n")
			filePath ++= f
			
        } catch {
        	case ex: Exception => { ex.printStackTrace(); ex.toString() }
        }
        return filePath
	}
}