import test._
import example.idl._

import org.specs2.mutable.Specification
import com.sksamuel.avro4s.RecordFormat
import shapeless._
import org.apache.avro.generic.GenericData
import scala.collection.JavaConverters._

class StandardUnionsArbitraryAritySpec extends Specification {
  skipAll
  "Unions" should {
    "serialize and deserialize correctly in terms of `Option`" in {
      val record1 = ShouldRenderAsOption(Option(Event1()))
      val record2 = ShouldRenderAsOption(Option(Event1()))
      val format = RecordFormat[ShouldRenderAsOption]
      val genericRecord1 = new GenericData.Record(format.to(record1).getSchema)
      genericRecord1.put("value", record1.value.getOrElse(null))
      val genericRecord2 = new GenericData.Record(format.to(record2).getSchema)
      genericRecord2.put("value", record2.value.getOrElse(null))
      val records = List(genericRecord1, genericRecord2)
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
      val genericRecord1 = new GenericData.Record(format.to(record1).getSchema)
      genericRecord1.put("value", record1.value match {
        case Left(l) => l
        case Right(r) => r})
      val genericRecord2 = new GenericData.Record(format.to(record2).getSchema)
      genericRecord2.put("value", record2.value match {
        case Left(l) => l
        case Right(r) => r}
      )
      val records = List(genericRecord1, genericRecord2)
      StandardTestUtil.verifyWriteAndRead(records)
    }

    // "serialize and deserialize correctly in terms of `shapeless.Coproduct`" in {
    //   val record1 = ShouldRenderAsCoproduct(Coproduct(Event1()))
    //   val record2 = ShouldRenderAsCoproduct(Coproduct(Event1()))
    //   val format = RecordFormat[ShouldRenderAsCoproduct]
    //   val records = List(format.to(record1), format.to(record2))
    //   StandardTestUtil.verifyWriteAndRead(records)
    // }

    // "serialize and deserialize correctly in terms of `Option(shapeless.Coproduct)`" in {
    //   val record1 = ShouldRenderAsOptionCoproduct(Option(Coproduct(Event1())))
    //   val record2 = ShouldRenderAsOptionCoproduct(Option(Coproduct(Event1())))
    //   val format = RecordFormat[ShouldRenderAsOptionCoproduct]
    //   val records = List(format.to(record1), format.to(record2))
    //   StandardTestUtil.verifyWriteAndRead(records)
    // }

    // error: magnolia.Deferred is used for derivation of recursive typeclasses
    // "serialize and deserialize correctly in terms of `shapeless.Coproduct(shapeless.Coproduct)`" in {
    //   val record1 = ShouldRenderAsCoproductOfCoproduct(Coproduct(CopX(Coproduct(Event1()))))
    //   val record2 = ShouldRenderAsCoproductOfCoproduct(Coproduct(CopX(Coproduct(Event1()))))
    //   val format = RecordFormat[ShouldRenderAsCoproductOfCoproduct]
    //   val records = List(format.to(record1), format.to(record2))
    //   StandardTestUtil.verifyWriteAndRead(records)
    // }

  }

}
