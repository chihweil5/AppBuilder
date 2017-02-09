package com.appbuilder.model
import scala.reflect._
import java.io._
import scala.collection.mutable.ArrayBuffer
import scala.io.Source
import collection.JavaConversions._

class GithubInfoWrapper(var githubInfoList : java.util.List[GithubInfo], var msg : java.util.List[String], var status : java.util.List[String]) {

	def this (){
    	this(githubInfoList = ArrayBuffer[GithubInfo](), msg = ArrayBuffer[String](), status = ArrayBuffer[String]())
  	}

  	def getGithubInfoList() : java.util.List[GithubInfo] = githubInfoList

	def setGithubInfoList(githubInfoList : java.util.List[GithubInfo]) {
    	this.githubInfoList = githubInfoList
  	}

  	def getMsg() : java.util.List[String] = msg

  	def setMsg(msg : java.util.List[String]) {
  		this.msg = msg
  	}

  	def getStatus() : java.util.List[String] = status

  	def setStatus(status : java.util.List[String]) {
  		this.status = status
  	}

	def add(githubInfo: GithubInfo): Unit = {
		githubInfoList.append(githubInfo) 
	}

	override def toString(): String = {
		return "GithubInfoWrapper [githubInfoList=" + githubInfoList + " msg=" + msg + " status=" + status + "]"
	}
}

