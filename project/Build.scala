import sbt._
import Keys._
import PlayProject._

object ApplicationBuild extends Build {
  val aetherVersion = "1.13.1"
  val mavenVersion = "3.0.3"
  val wagonVersion = "1.0"

  val aether = Seq(
    "org.sonatype.aether" % "aether-api" % aetherVersion,
    "org.sonatype.aether" % "aether-util" % aetherVersion,
    "org.sonatype.aether" % "aether-impl" % aetherVersion,
    "org.sonatype.aether" % "aether-connector-file" % aetherVersion,
    "org.sonatype.aether" % "aether-connector-asynchttpclient" % aetherVersion,
    "org.sonatype.aether" % "aether-connector-wagon" % aetherVersion,
    "org.apache.maven" % "maven-aether-provider" % mavenVersion,
    "org.apache.maven.wagon" % "wagon-ssh" % wagonVersion
  )

  val appName = "softwarekoch"
  val appVersion = "1.0"


  val appDependencies = Seq(
    // Add your project dependencies here,
    "org.scalaquery" % "scalaquery_2.9.0-1" % "0.9.5",
    "org.apache.ivy" % "ivy" % "2.2.0"
  )  ++ aether

  val main = PlayProject(appName, appVersion, appDependencies, mainLang = SCALA).settings(
    // Add your own project settings here
  )


}
