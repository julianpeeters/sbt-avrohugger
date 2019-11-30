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
    
    // "Continue using `A :+: B :+: C :+: Cnil` with `shapeless.Coproduct`" in {
    // this is commented out because equality of Generic Records fails in the
    // case of nested records. As Strings they're shown to be equal
    //   val record1 = ShouldRenderAsCoproduct3(Coproduct(Event2()))
    //   val record2 = ShouldRenderAsCoproduct3(Coproduct(Event2()))
    //   val format = RecordFormat[ShouldRenderAsCoproduct3]
    //   val records = List(format.to(record1), format.to(record2))
    //   StandardTestUtil.verifyWriteAndRead(records)
    // }

    "Not override `Option[A]` standard derivation with just `shapeless.Coproduct`" in {
      val record = ShouldRenderAsOptional()
      val format = RecordFormat[ShouldRenderAsOptional]
      val records = List(format.to(record))
      StandardTestUtil.verifyWriteAndRead(records)
    }

    "A Union class with default values" should {
      "deserialize correctly in nullable position" in {
        val record = ShouldRenderAsOptionalCoproduct2(Option(Coproduct(Event1())))
        val format = RecordFormat[ShouldRenderAsOptionalCoproduct2]
        val avro = format.to(record)
        val sameRecord = format.from(avro)
        sameRecord.value === Option(Coproduct(Event1(1)))
      }

      "deserialize correctly with default values" in {
        val record = ShouldRenderAsOptionalCoproduct2()
        val format = RecordFormat[ShouldRenderAsOptionalCoproduct2]
        val avro = format.to(record)
        val sameRecord = format.from(avro)
        sameRecord.value === None
      }

      // "deserialize correctly in non-nullable position" in {
      // this is commented out because equality of Generic Records fails in the
      // case of nested records. As Strings they're shown to be equal
      //   val record = ShouldRenderAsCoproduct3(Coproduct(Event2()))
      //   val format = RecordFormat[ShouldRenderAsCoproduct3]
      //   val avro = format.to(record)
      //   val sameRecord = format.from(avro)
      //   sameRecord.value === Coproduct(Event2(10))
      // }

      "deserialize correctly when instantiated empty" in {
        val record = ShouldRenderAsOptionalCoproduct2(None)
        val format = RecordFormat[ShouldRenderAsOptionalCoproduct2]
        val avro = format.to(record)
        val sameRecord = format.from(avro)
        sameRecord.value === None
      }

      // "deserialize correctly when default value is overriden" in {
      // this is commented out because equality of Generic Records fails in the
      // case of nested records. As Strings they're shown to be equal
      //   val record = ShouldRenderAsCoproduct3(Coproduct(Event2(99)))
      //   val format = RecordFormat[ShouldRenderAsCoproduct3]
      //   val avro = format.to(record)
      //   val sameRecord = format.from(avro)
      //   sameRecord.value === Coproduct(Event2(99))
      // }
    }

  }

}
