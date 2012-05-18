package model

import javax.xml.transform.OutputKeys
import anorm.Pk

/**
 *
 * Date: 18.05.12
 * Time: 20:55
 *
 * name                      varchar(255),
   description               clob,
   picture_url               varchar(255),
   preparation               varchar(255),
   complexity                integer,
 * @author Friderici
 */

case class Recipe(
  id: Long = -1,
  name: String,
  description: String,
  pictureUrl: String,
  preperation: String,
  categories: List[Category]) {
}

object Recipe {

  import play.api.db._
  import play.api.Play.current

  import anorm._
  import anorm.SqlParser._

  val simple = {
    get[Pk[Long]]("id") ~
    get[String]("name") ~
    get[String]("description") ~
    get[String]("pictureUrl") ~
    get[String]("pictureUrl") map {
      case =>

        ~
      get[String]("categorie")
    }
  }
}
