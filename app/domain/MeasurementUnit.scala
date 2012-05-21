package domain

/**
 *
 * Date: 17.05.12
 * Time: 20:35
 *
 * @author Friderici
 */

sealed trait MeasurementUnit {

  type Unit <: Measurement

  def make(amount: Int): Unit

  def apply(amount: Int): Unit = make(amount)

  abstract class Measurement(val name: String) {
    def amount: Int
    def +(other: Unit) = make(amount + other.amount)
    def -(other: Unit) = make(amount - other.amount)
    def *(amount: Int) = make(this.amount * amount)
    def /(amount: Int) = make(this.amount / amount)
    override def equals(other: Any) = other match {
      case that:Unit => (that.name == this.name) && this.amount == that.amount
      case _ => false
    }
  }
}

object KG extends MeasurementUnit {
  type Unit = KGUnit
  class KGUnit extends MeasurementUnit("KG")
  def make(amount:Int) = new KGUnit(amaount)
}

object Liter extends MeasurementUnit {
  type Unit = LiterUnit
  class LiterUnit extends MeasurementUnit("Liter")
  def make(amount:Int) = new LiterUnit(amaount)
}
