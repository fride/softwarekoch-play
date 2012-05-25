package model

import org.codehaus.plexus.DefaultPlexusContainer
import org.sonatype.aether.{RepositorySystemSession, RepositorySystem}
import org.apache.maven.repository.internal.MavenRepositorySystemSession
import org.sonatype.aether.repository.{RemoteRepository, LocalRepository}
import org.sonatype.aether.graph.{DependencyNode, Dependency}
import org.sonatype.aether.util.artifact.DefaultArtifact
import org.sonatype.aether.collection.CollectRequest
import org.sonatype.aether.resolution.DependencyRequest
import org.sonatype.aether.util.graph.PreorderNodeListGenerator

/**
 *
 * Date: 25.05.12
 * Time: 14:24
 *
 * @author Friderici
 */

object MavenRepos {

  import scala.collection.JavaConversions._
  import scala.collection.JavaConverters._

  lazy val repos = new DefaultPlexusContainer().lookup(classOf[RepositorySystem])

  type DependencyGraph = Map[DependencyNode, List[DependencyNode]]
  /**
   * resolve the dependencies of given artifact.
   *
   * @param artifactId
   * @param groupId
   * @param version
   * @return
   */
  def resolve(artifactId: String, groupId: String, version: String):DependencyGraph = {
    val session = mavenSession(repos)

    val dependency = new Dependency(new DefaultArtifact("%s:%s:%s".format(groupId, artifactId, version)), "compile")
    val central = new RemoteRepository("central", "default", "http://repo1.maven.org/maven2/")

    val collectRequest = new CollectRequest()
    collectRequest.setRoot(dependency)
    collectRequest.addRepository(central)
    val node = repos.collectDependencies(session, collectRequest).getRoot
    val dependencyRequest = new DependencyRequest( node, null )
    repos.resolveDependencies( session, dependencyRequest  )

    val nlg = new PreorderNodeListGenerator()
    node.accept( nlg )
    val dependencies = nlg.getNodes.toList

    dependencies.foldLeft(Map[DependencyNode, List[DependencyNode]]()){(map, node) =>
      map + (node -> node.getChildren().toList)
    }
  }

  /**
   *
   * @param system
   * @return
   */
  def mavenSession(system: RepositorySystem): RepositorySystemSession = {
    val session = new MavenRepositorySystemSession()
    val localRepo = new LocalRepository("target/local-repo")
    session.setLocalRepositoryManager(system.newLocalRepositoryManager(localRepo))
    session
  }
}
