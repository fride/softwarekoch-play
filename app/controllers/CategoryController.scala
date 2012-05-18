package controllers

/**
 *
 * Date: 18.05.12
 * Time: 20:06
 *
 * @author Friderici
 */

import play.api._
import play.api.mvc._
import db.DB
import play.api.libs.json._
import model.Category


import play.api.data.Form
import play.api.data.Forms.{single, nonEmptyText}

object CategoryController extends Controller {

  import play.api.Play.current

  val barForm = Form(
      single("name" -> nonEmptyText)
  )
  def initCategories = Action {
      DB.withTransaction { conn =>
        Category.create("Salz")
        Category.create("Zucker")
        Category.create("Honig")
        conn.commit()
      }
      Redirect(routes.CategoryController.listCategories())
  }

  def createCategory = Action {
    implicit request =>
      barForm.bindFromRequest.fold(
        errors => BadRequest,
        {
          case (name) =>
          Category.create(name)
          Redirect(routes.CategoryController.listCategories())
        }
      )
  }
  def listCategories = Action {
    Ok(Json.toJson(
      Category.findAll().map(_.toJson)
    ))
  }
}
