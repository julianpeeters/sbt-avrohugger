import test._
import example.idl._

import org.specs2.mutable.Specification
import com.sksamuel.avro4s.RecordFormat
import shapeless._

class AllUnionsWithShapelessCoproductSpec extends Specification {

  "Unions" should {
    "Override `Option[A]` standard derivation with just `shapeless.Coproduct`" in {
      val record1 = ShouldRenderAsCoproduct(Option(Coproduct(Event1())))
      val record2 = ShouldRenderAsCoproduct(Option(Coproduct(Event1())))
      val format = RecordFormat[ShouldRenderAsCoproduct]
      val records = List(format.to(record1), format.to(record2))
      StandardTestUtil.verifyWriteAndRead(records)
    }

    "Override `Option[Either[A, B]]` standard derivation with just `shapeless.Coproduct`" in {
      val record1 = ShouldRenderAsCoproduct3(Option(Coproduct(Event1())))
      val record2 = ShouldRenderAsCoproduct3(Option(Coproduct(Event1())))
      val format = RecordFormat[ShouldRenderAsCoproduct3]
      val records = List(format.to(record1), format.to(record2))
      StandardTestUtil.verifyWriteAndRead(records)
    }

    "Override `Either[A, B]` standard derivation with just `shapeless.Coproduct`" in {
      val record1 = ShouldRenderAsCoproduct9(Coproduct(Event1()))
      val record2 = ShouldRenderAsCoproduct9(Coproduct(Event1()))
      val format = RecordFormat[ShouldRenderAsCoproduct9]
      val records = List(format.to(record1), format.to(record2))
      StandardTestUtil.verifyWriteAndRead(records)
    }

    "Continue using `A :+: B :+: :+: Cnil` with `shapeless.Coproduct`" in {
      val record1 = ShouldRenderAsCoproduct9(Coproduct(Event1()))
      val record2 = ShouldRenderAsCoproduct9(Coproduct(Event1()))
      val format = RecordFormat[ShouldRenderAsCoproduct9]
      val records = List(format.to(record1), format.to(record2))
      StandardTestUtil.verifyWriteAndRead(records)
    }

    "Continue using `Option[A :+: B :+: :+: Cnil]` with `shapeless.Coproduct`" in {
      val record1 = ShouldRenderAsCoproduct6(Option(Coproduct(Event1())))
      val record2 = ShouldRenderAsCoproduct6(Option(Coproduct(Event1())))
      val format = RecordFormat[ShouldRenderAsCoproduct6]
      val records = List(format.to(record1), format.to(record2))
      StandardTestUtil.verifyWriteAndRead(records)
    }

  }

}
