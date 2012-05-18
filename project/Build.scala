import sbt._
import Keys._
import PlayProject._

object ApplicationBuild extends Build {

    val appName         = "softwarekoch"
    val appVersion      = "1.0"

    val appDependencies = Seq(
      // Add your project dependencies here,
      "org.scalaquery" % "scalaquery_2.9.0-1" % "0.9.5"
    )

    val main = PlayProject(appName, appVersion, appDependencies, mainLang = SCALA).settings(
      // Add your own project settings here      
    )

}
