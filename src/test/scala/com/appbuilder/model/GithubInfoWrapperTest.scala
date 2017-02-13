package com.appbuilder.model
import org.scalatest._
import org.scalamock.scalatest.MockFactory
import org.scalatest.Assertions._
import collection.JavaConversions._
import scala.collection.JavaConversions._
import java.util.Collection;
import scala.sys.process._


class GithubInfoWrapperTest extends FlatSpec with Matchers {

  "GithubInfoWrapper" should "be initialized with empty List of GithubInfo" in {
    val wrapper = new GithubInfoWrapper
    assert(wrapper.getGithubInfoList.size === 0)

  }

  it should "have List of size of 1 after adding a GithubInfo object to it" in {
    val wrapper = new GithubInfoWrapper
    wrapper.add(new GithubInfo)
    assert(wrapper.getGithubInfoList.size === 1)
  }

  it should "have List of size of 2 after adding two GithubInfo object to it" in {
    val wrapper = new GithubInfoWrapper
    wrapper.add(new GithubInfo)
    wrapper.add(new GithubInfo)
    assert(wrapper.getGithubInfoList.size === 2)
  }

}