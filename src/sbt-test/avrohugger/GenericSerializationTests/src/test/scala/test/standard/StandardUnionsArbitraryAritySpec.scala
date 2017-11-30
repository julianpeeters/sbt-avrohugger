import test._
import example.idl._

import org.specs2.mutable.Specification
import com.sksamuel.avro4s.RecordFormat
import shapeless._

class StandardUnionsArbitraryAritySpec extends Specification {

  "Unions" should {
    "serialize and deserialize correctly in terms of `Option`" in {
      val record1 = ShouldRenderAsOption(Option(Event1()))
      val record2 = ShouldRenderAsOption(Option(Event1()))
      val format = RecordFormat[ShouldRenderAsOption]
      val records = List(format.to(record1), format.to(record2))
      StandardTestUtil.verifyWriteAndRead(records)
    }

    "serialize and deserialize correctly in terms of `Option(Either)`" in {
      val record1 = ShouldRenderAsOptionEither(Option(Right(Event2())))
      val record2 = ShouldRenderAsOptionEither(Option(Right(Event2())))
      val format = RecordFormat[ShouldRenderAsOptionEither]
      val records = List(format.to(record1), format.to(record2))
      StandardTestUtil.verifyWriteAndRead(records)
    }

    "serialize and deserialize correctly in terms of `Either`" in {
      val record1 = ShouldRenderAsEither(Left(Event1()))
      val record2 = ShouldRenderAsEither(Left(Event1()))
      val format = RecordFormat[ShouldRenderAsEither]
      val records = List(format.to(record1), format.to(record2))
      StandardTestUtil.verifyWriteAndRead(records)
    }

    "serialize and deserialize correctly in terms of `shapeless.Coproduct`" in {
      val record1 = ShouldRenderAsCoproduct(Coproduct(Event1()))
      val record2 = ShouldRenderAsCoproduct(Coproduct(Event1()))
      val format = RecordFormat[ShouldRenderAsCoproduct]
      val records = List(format.to(record1), format.to(record2))
      StandardTestUtil.verifyWriteAndRead(records)
    }

    "serialize and deserialize correctly in terms of `Option(shapeless.Coproduct)`" in {
      val record1 = ShouldRenderAsOptionCoproduct(Option(Coproduct(Event1())))
      val record2 = ShouldRenderAsOptionCoproduct(Option(Coproduct(Event1())))
      val format = RecordFormat[ShouldRenderAsOptionCoproduct]
      val records = List(format.to(record1), format.to(record2))
      StandardTestUtil.verifyWriteAndRead(records)
    }

  }

}
