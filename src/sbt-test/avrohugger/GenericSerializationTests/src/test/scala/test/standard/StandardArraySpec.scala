package test.standard

import com.sksamuel.avro4s._
import org.specs2.mutable.Specification
import test._

class StandardArraySpec extends Specification {

  "A case class with an `array` field" should {
    "serialize and deserialize correctly" in {
      val record1 = ArrayIdl(Seq(1,2,3))
      val record2 = ArrayIdl(Seq(2,3,4))
      val format = RecordFormat[ArrayIdl]
      val records = List(format.to(record1), format.to(record2))
      StandardTestUtil.verifyWriteAndRead(records)
    }
  }


}
