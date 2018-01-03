import test._
import example.idl._

import org.specs2.mutable.Specification
import com.sksamuel.avro4s.RecordFormat
import shapeless._

class AllUnionsWithShapelessCoproductSpec extends Specification {

  "Unions" should {
    "Not override `Option[A]` standard derivation with just `shapeless.Coproduct`" in {
      val record1 = ShouldRenderAsOptional(Option(Coproduct(Event1())))
      val record2 = ShouldRenderAsOptional(Option(Coproduct(Event1())))
      val format = RecordFormat[ShouldRenderAsOptional]
      val records = List(format.to(record1), format.to(record2))
      StandardTestUtil.verifyWriteAndRead(records)
    }
    
    "Not override `Option[A]` standard derivation with just `shapeless.Coproduct`" in {
      val record1 = ShouldRenderAsNullable(Option(Coproduct(Event1())))
      val record2 = ShouldRenderAsNullable(Option(Coproduct(Event1())))
      val format = RecordFormat[ShouldRenderAsNullable]
      val records = List(format.to(record1), format.to(record2))
      StandardTestUtil.verifyWriteAndRead(records)
    }

    "Override `Option[Either[A, B]]` standard derivation with just `Option[shapeless.Coproduct]`" in {
      val record1 = ShouldRenderAsOptionalCoproduct(Option(Coproduct(Event1())))
      val record2 = ShouldRenderAsOptionalCoproduct(Option(Coproduct(Event1())))
      val format = RecordFormat[ShouldRenderAsOptionalCoproduct]
      val records = List(format.to(record1), format.to(record2))
      StandardTestUtil.verifyWriteAndRead(records)
    }
    
    "Override `Option[Either[A, B]]` standard derivation with just `Option[shapeless.Coproduct]`" in {
      val record1 = ShouldRenderAsNullableCoproduct(Option(Coproduct(Event1())))
      val record2 = ShouldRenderAsNullableCoproduct(Option(Coproduct(Event1())))
      val format = RecordFormat[ShouldRenderAsNullableCoproduct]
      val records = List(format.to(record1), format.to(record2))
      StandardTestUtil.verifyWriteAndRead(records)
    }

    "Override `Either[A, B]` standard derivation with just `shapeless.Coproduct`" in {
      val record1 = ShouldRenderAsCoproduct(Coproduct(Event1()))
      val record2 = ShouldRenderAsCoproduct(Coproduct(Event1()))
      val format = RecordFormat[ShouldRenderAsCoproduct]
      val records = List(format.to(record1), format.to(record2))
      StandardTestUtil.verifyWriteAndRead(records)
    }

    "Continue using `Option[A :+: B :+: C :+: Cnil]` with `Option[shapeless.Coproduct]` and override default args" in {
      val record1 = ShouldRenderAsOptionalCoproduct2(Option(Coproduct(Event1(5))))
      val record2 = ShouldRenderAsOptionalCoproduct2(Option(Coproduct(Event1(5))))
      val format = RecordFormat[ShouldRenderAsOptionalCoproduct2]
      val records = List(format.to(record1), format.to(record2))
      StandardTestUtil.verifyWriteAndRead(records)
    }
    
    "Continue using `Option[A :+: B :+: C :+: Cnil]` with `Option[shapeless.Coproduct]`" in {
      val record1 = ShouldRenderAsNullableCoproduct3(Option(Coproduct(Event1())))
      val record2 = ShouldRenderAsNullableCoproduct3(Option(Coproduct(Event1())))
      val format = RecordFormat[ShouldRenderAsNullableCoproduct3]
      val records = List(format.to(record1), format.to(record2))
      StandardTestUtil.verifyWriteAndRead(records)
    }
    
    "Continue using `Option[A :+: B :+: C :+: Cnil]` with `Option[shapeless.Coproduct]`" in {
      val record1 = ShouldRenderAsNullableCoproduct4(Option(Coproduct(Event1())))
      val record2 = ShouldRenderAsNullableCoproduct4(Option(Coproduct(Event1())))
      val format = RecordFormat[ShouldRenderAsNullableCoproduct4]
      val records = List(format.to(record1), format.to(record2))
      StandardTestUtil.verifyWriteAndRead(records)
    }

    "Continue using `A :+: B :+: C :+: Cnil` with `shapeless.Coproduct`" in {
      val record1 = ShouldRenderAsCoproduct2(Coproduct(Event1()))
      val record2 = ShouldRenderAsCoproduct2(Coproduct(Event1()))
      val format = RecordFormat[ShouldRenderAsCoproduct2]
      val records = List(format.to(record1), format.to(record2))
      StandardTestUtil.verifyWriteAndRead(records)
    }
    
    "Continue using `A :+: B :+: C :+: Cnil` with `shapeless.Coproduct`" in {
      val record1 = ShouldRenderAsCoproduct3(Coproduct(Event2()))
      val record2 = ShouldRenderAsCoproduct3(Coproduct(Event2()))
      val format = RecordFormat[ShouldRenderAsCoproduct3]
      val records = List(format.to(record1), format.to(record2))
      StandardTestUtil.verifyWriteAndRead(records)
    }

    "Not override `Option[A]` standard derivation with just `shapeless.Coproduct`" in {
      val record = ShouldRenderAsOptional()
      val format = RecordFormat[ShouldRenderAsOptional]
      val records = List(format.to(record))
      StandardTestUtil.verifyWriteAndRead(records)
    }

  }

}
