package test.specific

import com.example.avrohugger.{NonNullUnion, NullableUnion}
import org.specs2.mutable.Specification
import test.SpecificTestUtil

class SpecificSpec extends Specification {

  "A case class with multiple non null types" should {
    "serialize and deserialize correctly" in {
      val record1 = NonNullUnion(true)
      val record2 = NonNullUnion(1)
      val record3 = NonNullUnion("abc")
      val records = List(record1, record2, record3)
      SpecificTestUtil.verifyWriteAndRead(records)
    }
  }

  "A case class with multiple nullable types" should {
    "serialize and deserialize correctly" in {
      val record1 = NullableUnion(Some(true))
      val record2 = NullableUnion(Some(1))
      val record3 = NullableUnion(Some("abc"))
      val record4 = NullableUnion(None)
      val records = List(record1, record2, record3, record4)
      SpecificTestUtil.verifyWriteAndRead(records)
    }
  }

}
