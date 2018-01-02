import test._
import org.specs2.mutable.Specification
import com.sksamuel.avro4s.RecordFormat
import shapeless.Inl

class StandardDefaultValuesSpec extends Specification {

  "A case class with default values" should {
    "deserialize correctly" in {
    
      val format = RecordFormat[DefaultTest]
      val record = DefaultTest()
      val avro = format.to(record)
      val sameRecord = format.from(avro)
      sameRecord.suit === DefaultEnum.SPADES
      sameRecord.number === 0
      sameRecord.str === "str"
      sameRecord.optionString === None
      sameRecord.optionStringValue === Some("default")
      sameRecord.embedded === Embedded(1)
      sameRecord.defaultArray === List(1,3,4,5)
      sameRecord.optionalEnum === None
      sameRecord.defaultMap === Map("Hello" -> "world", "Merry" -> "Christmas")
      sameRecord.byt === "\u00FF".getBytes
      sameRecord.defaultEither === Left(2)
      sameRecord.defaultCoproduct === Inl(3)
    }
  }

}
