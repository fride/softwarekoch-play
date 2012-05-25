package model

import org.apache.ivy.Ivy
import org.apache.ivy.core.settings.IvySettings
import org.apache.ivy.plugins.resolver.URLResolver
import org.apache.ivy.core.module.id.ModuleRevisionId
import java.io.File
import org.apache.ivy.plugins.parser.xml.XmlModuleDescriptorWriter
import org.apache.ivy.core.resolve.ResolveOptions
import org.apache.ivy.core.module.descriptor.{MDArtifact, DefaultModuleDescriptor, DefaultDependencyDescriptor}
import org.apache.ivy.core.report.ResolveReport
import xml.{NodeSeq, XML}


trait Resolvable {
  def resolve:ResolveReport
  def artifacts:List[MDArtifact]
}

object Resolvable {

  import scala.collection.JavaConversions._
  import scala.collection.JavaConverters._

  implicit def triple2Resolvable(tr: (String,String,String)) = new Resolvable {
    lazy val resolve:ResolveReport = Resolvable.resolve(tr._1, tr._2, tr._3)

    def artifacts = resolve.getArtifacts.toList.asInstanceOf[List[MDArtifact]]
  }

  /**
   * Shows NO transitive dependencies :-(
   * @param res
   * @return
   */
  def walkTree(res: ResolveReport) = {
    val artifacts = res.getArtifacts.toList.asInstanceOf[List[MDArtifact]]
    val deps = artifacts.headOption.map{ a =>
      artifacts.tail.map( a -> _)
    }
    deps
  }

  def resolveXml(r: ResolveReport, conf:String = "default"): NodeSeq = {
    val file = ivy.getResolutionCacheManager.getConfigurationResolveReportInCache(r.getResolveId, "default")
    val xml = XML.loadFile(file)

    val dependencies = for { dep <- xml \\ "dependencies"
          mod <- dep \ "module"
      revision <- mod \ "revision"
    } yield (mod \ "@name", mod \ "@organisation", revision \ "@name")
    println(dependencies)
    xml
  }

  /**
   *
   * @param groupId
   * @param artifactId
   * @param version
   * @return
   */
  def resolve(groupId: String, artifactId: String, version: String): ResolveReport = {
    val mod = moduleDesc(groupId, artifactId + "-caller", "working")
    mod.addDependency(dependency(groupId, artifactId, version))
    val ivyFile: File = new File("./test-ivy.xml")
    XmlModuleDescriptorWriter.write(mod, ivyFile)
    //ivy.resolve(md, new ResolveOptions().setConfs(Array("default"))) // does not work!
    val resolved = ivy.resolve(ivyFile.toURL, new ResolveOptions().setConfs(Array("default")))
    val cache: File = ivy.getResolutionCacheManager.getResolvedIvyFileInCache(resolved.getModuleDescriptor.getModuleRevisionId)
    println(
      ivy.getResolutionCacheManager.getConfigurationResolveReportInCache(resolved.getResolveId, "default")
    )
    println(cache)
    resolved
  }

  def moduleDesc(groupId: String, artifactId: String, version: String) =
    DefaultModuleDescriptor.newDefaultInstance(ModuleRevisionId.newInstance(groupId, artifactId, version))

  /**
   *
   * @param groupId
   * @param artifactId
   * @param version
   * @return
   */
  implicit def dependency(groupId: String, artifactId: String, version: String) =
    new DefaultDependencyDescriptor(
      moduleDesc(groupId, artifactId, version),
      ModuleRevisionId.newInstance(groupId, artifactId, version),
      false, false, true
    )

  lazy val ivy: Ivy = {
    val settings = new IvySettings()
    val resolver = new URLResolver()
    resolver.setM2compatible(true)
    resolver.setName("central")
    resolver.addArtifactPattern(
      "http://repo1.maven.org/maven2/[organisation]/[module]/[revision]/[artifact](-[revision]).[ext]")
    settings.addResolver(resolver)
    settings.setDefaultResolver(resolver.getName)
    Ivy.newInstance(settings)
  }

  def main(args: Array[String]) {
    ("junit", "junit", "4.10").artifacts
  }
}
