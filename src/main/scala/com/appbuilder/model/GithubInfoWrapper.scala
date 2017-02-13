package com.appbuilder.model
import scala.reflect._
import java.io._
import scala.collection.mutable.ArrayBuffer
import scala.io.Source
import collection.JavaConversions._

class GithubInfoWrapper(var githubInfoList : java.util.List[GithubInfo]) {

	def this (){
    	this(githubInfoList = ArrayBuffer[GithubInfo]())
  	}

  	def getGithubInfoList() : java.util.List[GithubInfo] = githubInfoList

	def setGithubInfoList(githubInfoList : java.util.List[GithubInfo]) {
    		this.githubInfoList = githubInfoList
  	}

	def add(githubInfo: GithubInfo): Unit = {
		githubInfoList.append(githubInfo) 
	}

	override def toString(): String = {
		return "GithubInfoWrapper [githubInfoList=" + githubInfoList + "]"
	}
}

