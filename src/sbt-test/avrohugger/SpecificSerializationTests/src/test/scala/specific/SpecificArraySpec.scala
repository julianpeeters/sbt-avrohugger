import test._
import org.specs2.mutable.Specification

class SpecificArraySpec extends Specification {

  "A case class with an `array` field" should {
    "serialize and deserialize correctly" in {
      val record1 = ArrayIdl(Seq(1,2,3))
      val record2 = ArrayIdl(Seq(2,3,4))
      val records = List(record1, record2)
      SpecificTestUtil.verifyWriteAndRead(records)
    }
  }


}
