package com.appbuilder.service
import org.scalatest._
import org.scalamock.scalatest.MockFactory
import org.scalatest.Assertions._
import collection.JavaConversions._
import scala.collection.JavaConversions._
import java.util.Collection;
import scala.sys.process._

import com.appbuilder.model._


class GithubServiceTest extends FlatSpec with Matchers {
  // tests go here...
  val service = new GithubService
  val gradleservice = new GradleService
  
  "Server" should "check the validation of the url" in {
  	val github = new GithubInfo("chihweil5", "HelloWorldAndroid")
  	service.isRepoValid(github.getURL) should equal (true)
  	val github1 = new GithubInfo("chihweil5", "123")
  	service.isRepoValid(github1.getURL) should equal (false)
  }

  it should "throw MalformedURLException given an empty url string" in {
  	assertThrows[java.net.MalformedURLException] {
      service.isRepoValid("")
    }
  }

  it should "get the commit description given the correct commit hash" in {
  	val github = new GithubInfo("chihweil5", "HelloWorldAndroid", "0cee20bb5ffe853d429debc88a97e4ea24580c3c") 
  	service.getCommitByTags(github) should equal ("Initial commit")
  }

  it should "shows \"Commit Hash not found\" given the incorrect commit hash" in {
  	val github = new GithubInfo("chihweil5", "HelloWorldAndroid", "0bb5ffe853d429debc88a97e4ea24580c3c") 
  	service.getCommitByTags(github) should equal ("Commit Hash not found")
  }

  it should "shows \"Commit Hash not found\" given the invalid github username" in {
  	val github = new GithubInfo("chihweil", "HelloWorldAndroid", "0bb5ffe853d429debc88a97e4ea24580c3c") 
  	service.getCommitByTags(github) should equal ("Commit Hash not found")
  }

  it should "shows \"Commit Hash not found\" given the invalid github repository name" in {
  	val github = new GithubInfo("chihweil5", "helloWorldAndroid", "0bb5ffe853d429debc88a97e4ea24580c3c") 
  	service.getCommitByTags(github) should equal ("Commit Hash not found")
  }

  it should "throw IllegalArgumentException given the empty github input" in {
  	assertThrows[java.lang.IllegalArgumentException]{
  		val github = new GithubInfo()
  		service.getCommitByTags(github)
  	}
  }

  it should "clone the remote repository given the valid github information" in {
  	val github = new GithubInfo("chihweil5", "HelloWorldAndroid")
  	val output = Seq("ls", System.getProperty("user.dir")).!!
    assert(output.indexOf(github.getReponame) < 0 && service.isRepoValid(github.getURL))
  }

  it should "check out the cloned repository to any valid commit hash" in {
  	val github = new GithubInfo("chihweil5", "HelloWorldAndroid", "0cee20bb5ffe853d429debc88a97e4ea24580c3c") 
  	service.getCommitByTags(github) should equal ("Initial commit")
  }

}