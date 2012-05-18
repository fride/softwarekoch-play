import java.sql.SQLException
import model.Category
import org.specs2.mutable._

import play.api.test._
import play.api.test.Helpers._

class CategorySpec extends Specification {

  "Category" should {
    "have an id after save" in {
      running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
        Category.create("Zucker") match {
          case Some(in) => in.id must not beNull
          case _ => failure("Ingredient has no id after saved")
        }
      }
    }

    "exist only once" in {
      running(FakeApplication(additionalConfiguration = inMemoryDatabase())) {
        {
          Category.create("Zucker")
          Category.create("Zucker")
        } must throwA[SQLException]
      }
    }
  }
}
