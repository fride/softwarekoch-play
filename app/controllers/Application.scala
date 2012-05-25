package controllers

import play.api._
import play.api.mvc._
import model.MavenRepos

object Application extends Controller {
  
  def index = Action {
    Ok(views.html.index("Your new application is ready."))
  }

  def pom(artifactId: String, groupId: String, version:String) = Action {
    val deps =  MavenRepos.resolve(artifactId, groupId, version)

    val depsXml = deps.map{ p =>
      <node>{p._1.toString}</node>
      <children>
        {p._2.map(node => <child>{node.toString}</child>)}
      </children>
    }

    Ok(<dependencies>{depsXml}</dependencies>)
  }

}
