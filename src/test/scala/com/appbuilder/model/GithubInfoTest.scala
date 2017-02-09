package com.appbuilder.model
import org.scalatest._
import org.scalamock.scalatest.MockFactory
import org.scalatest.Assertions._
import collection.JavaConversions._
import scala.collection.JavaConversions._
import java.util.Collection;
import scala.sys.process._


class GithubInfoTest extends FlatSpec with Matchers {
  "GithubInfo" should "store correct metadata if commit hash is provided" in {
    val github = new GithubInfo("chihweil5", "HelloWorldAndroid", "0cee20bb5ffe853d429debc88a97e4ea24580c3c")
    assert(github.getUsername === "chihweil5" && github.getReponame === "HelloWorldAndroid" && github.getTags === "0cee20bb5ffe853d429debc88a97e4ea24580c3c")
  }

  it should "store correct metadata if commit hash is not provided" in {
    val github = new GithubInfo("chihweil5", "HelloWorldAndroid")
    assert(github.getUsername === "chihweil5" && github.getReponame === "HelloWorldAndroid" && github.getTags === "No tags")
  }

  it should "return correct github url given the user name and repository name" in {
    val github = new GithubInfo("chihweil5", "HelloWorldAndroid")
    assert(github.getURL === "https://github.com/chihweil5/HelloWorldAndroid")
  }

}