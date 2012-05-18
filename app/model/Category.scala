package model


import play.api.db._
import play.api.Play.current

import anorm._
import anorm.SqlParser._

case class Category(id: Long, name: String) {
  import play.api.libs.json._

  def toJson = Json.toJson(Map(
    "id" -> Json.toJson(id),
    "name" -> Json.toJson(name)
  ))
}

object Category {

  var categoryParser = {
    get[Pk[Long]]("id") ~
    get[String]("name") map {
      case id ~name => Category(id.get, name)
    }

  }

  /**
   * Create a new Category.
   * @param name the categories name.
   * @return a new category.
   */
  def create(name: String):Option[Category] =
    DB.withConnection{ implicit connection =>
      SQL("insert into category(name) values({name})").on(
        'name -> name
      ).executeInsert() match {
        case Some(id) => println("Created " + id); Some(Category(id = id, name = name))
        case None => None
      }
    }

  /**
   * Find all categories.
   * @return all categories
   */
  def findAll():Seq[Category] =
    DB.withConnection{ implicit connection =>
      SQL("select * from category").as(categoryParser *)
    }
}
