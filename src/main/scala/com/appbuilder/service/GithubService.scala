package com.appbuilder.service

import java.io.File
import java.io.IOException
import java.io.PrintWriter
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL
import java.util.List
import org.eclipse.egit.github.core.RepositoryCommit
import org.eclipse.egit.github.core.service.CommitService
import org.eclipse.egit.github.core.service.RepositoryService
import org.eclipse.jgit.api.Git
import org.eclipse.jgit.api.errors.CheckoutConflictException
import org.eclipse.jgit.api.errors.GitAPIException
import org.eclipse.jgit.lib.TextProgressMonitor
import org.eclipse.jgit.lib.Repository
import org.eclipse.jgit.storage.file.FileRepositoryBuilder
import org.eclipse.jgit.lib.Ref
import org.eclipse.jgit.lib.Constants
import org.eclipse.jgit.lib.ObjectId
import org.eclipse.jgit.lib.ObjectLoader
import org.eclipse.jgit.revwalk.RevCommit
import org.eclipse.jgit.revwalk.RevTree
import org.eclipse.jgit.revwalk.RevWalk
import org.eclipse.jgit.treewalk.TreeWalk
import org.eclipse.jgit.treewalk.filter.PathFilter
import org.springframework.stereotype.Service
import com.appbuilder.model.GithubInfo
import scala.io.Source
import collection.JavaConversions._

@Service
class GithubService {

  def isRepoValid(url: String): Boolean = {
    //println("connecting github..." + url)
    HttpURLConnection.setFollowRedirects(false)
    val con: HttpURLConnection = new URL(url).openConnection().asInstanceOf[HttpURLConnection]
    con.setRequestMethod("HEAD")
    return (con.getResponseCode == HttpURLConnection.HTTP_OK)
  }

  def cloneRemoteRepository(g: GithubInfo): Unit = {
    // prepare a new folder for the cloned repository
    println(g.getLocalpath)
    val dir: File = new File(g.getLocalpath)
    println(dir.toString)
    val localPath: File = File.createTempFile(g.getReponame, "", dir)
    println("Temp file : " + localPath.getAbsolutePath())
    if (!localPath.delete()) {
      throw new IOException("Could not delete temporary file " + localPath)
    }
    // then clone
    println("Cloning from " + g.getURL + " to " + localPath)
    val result : Git = Git.cloneRepository().setProgressMonitor(new TextProgressMonitor(new PrintWriter(System.out))).setURI(g.getRepoURL).setDirectory(localPath).call().asInstanceOf[Git]
    try {
      g.setLocalpath(localPath.getAbsolutePath())
      println("Having repository: " + result.getRepository().getDirectory())
    } catch {
        case ex: Exception => println("Fail to have repository")
    } finally result.close()
  }

  def getCommitByTags(g: GithubInfo): String = {
    val service: RepositoryService = new RepositoryService()
    val repositories: java.util.List[org.eclipse.egit.github.core.Repository] = service.getRepositories(g.getUsername)
    val sha: String = g.getTags
    var ropo : org.eclipse.egit.github.core.Repository = null
    for(repo <- repositories) {
      if(repo.getName() == g.getReponame){ 
        //println("get commit in repository :" + repo.getName())
        val commitService: CommitService = new CommitService()
        try {
          val commit: RepositoryCommit = commitService.getCommit(repo, sha)
          //println(commit.getCommit.getMessage)
          return commit.getCommit.getMessage
        } catch {
          case e: Exception => //println("ERROR: " + e)
        }
      }
    }
    return "Commit Hash not found"
  }

  def getVersionByTags(g: GithubInfo): Unit = {
    val dir: File = new File(g.getLocalpath + "/.git")
    try {
      val repository : Repository = GithubHelper.openRepository(dir)
      val git: Git = new Git(repository)
      try {
        println("get Version " + g.getTags)
        git
          .checkout()
          .setCreateBranch(true)
          .setName("new-branch")
          .setStartPoint(g.getTags)
          .call()
        git.checkout().setName("new-branch").call()
      } finally git.close()
    }
  }

}

object GithubHelper {
  
  def openRepository(url : File) : Repository = {
    println("in helper " + url.toString)
    val builder : FileRepositoryBuilder = new FileRepositoryBuilder()
    val repository : Repository = builder.setGitDir(url)
                .readEnvironment() // scan environment GIT_* variables
                .findGitDir() // scan up the file system tree
                .build()
    return repository
  }
}