package com.appbuilder.model
import scala.reflect._
import java.io._
import scala.collection.mutable.ArrayBuffer
import scala.io.Source

class GithubInfo (var username: String, var reponame: String, var tags: String, var localpath: String){
	this.setLocalpath(System.getProperty("user.dir") + "/" + reponame)

	val id = GithubInfo.newIdNum

  def this (username: String, reponame: String, tags: String){
      this("No username", "No reponame", "No tags", "No localpath")
      this.setUsername(username)
      this.setReponame(reponame)
      this.setTags(tags)
      this.setLocalpath(System.getProperty("user.dir") + "/" + reponame)
    }
	
	def this (username: String, reponame: String){
    	this("No username", "No reponame", "No tags", "No localpath")
    	this.setUsername(username)
    	this.setReponame(reponame)
    	this.setLocalpath(System.getProperty("user.dir") + "/" + reponame)
  	}

	def this (){
      this("", "", "", "")
    	this.setLocalpath(System.getProperty("user.dir") + "/" + reponame)
  	}

	def getUsername() : String = username
	def getReponame() : String = reponame
	def getTags() : String = tags
	def getLocalpath() : String = localpath

	def setUsername(username : String) {
    	this.username = username
  	}

  	def setReponame(reponame : String) {
    	this.reponame = reponame
  	}

  	def setTags(tags : String) {
    	this.tags = tags
  	}

  	def setLocalpath(localpath : String) {
    	this.localpath = localpath
  	}


	def getURL(): String = "https://github.com/" + username + "/" + reponame

	def getRepoURL(): String = "https://github.com/" + username + "/" + reponame + ".git"

	/*override def toString(): String = {
		return "GithubInfo [userName=" + username + ", repoName=" + reponame + ", tags=" + tags + ", localpath=" + localpath + "]"
	}*/

  override def toString(): String = {
    return "Github : " + username + "/" + reponame
  }
}

object GithubInfo {
  private var idNumber = 0
  private def newIdNum = { idNumber += 1; idNumber }
}

