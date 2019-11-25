import test._
import example.idl._

import org.apache.avro.compiler.idl.Idl
import org.apache.avro.generic.{GenericData, GenericRecord}
import org.specs2.mutable.Specification
import com.sksamuel.avro4s.RecordFormat
import shapeless._
import collection.JavaConverters._


class AllUnionsWithShapelessCoproductSpec extends Specification {

  "Unions" should {
    "Not override `Option[A]` standard derivation with just `shapeless.Coproduct`" in {
      val record1 = ShouldRenderAsOptional(Option(Event1()))
      val record2 = ShouldRenderAsOptional(Option(Event1()))
      val format = RecordFormat[ShouldRenderAsOptional]
      val records = List(format.to(record1), format.to(record2))
      StandardTestUtil.verifyWriteAndRead(records)
    }
    
    "Not override `Option[A]` standard derivation with just `shapeless.Coproduct`" in {
      val record1 = ShouldRenderAsNullable(Option(Event1()))
      val record2 = ShouldRenderAsNullable(Option(Event1()))
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
    //   val schemaFile = new java.io.File("src/main/avro/UnionsWithOptionalShapelessCoproduct.avdl")
    //   val schema = (new Idl(schemaFile)).CompilationUnit().getType("example.idl.ShouldRenderAsCoproduct3")
    //   val genericRecord1 = new GenericData.Record(schema)
    //   val genericRecord2 = new GenericData.Record(schema)
    //   val d1: GenericRecord = new GenericData.Record(schema.getField("value").schema().getTypes.asScala.toList.find(_.getName == "Event2").get)
    //   val d2: GenericRecord = new GenericData.Record(schema.getField("value").schema().getTypes.asScala.toList.find(_.getName == "Event2").get)
    //   d1.put("index", new Integer(record1.value.select[Event2].get.index))
    //   d2.put("index",new Integer(record2.value.select[Event2].get.index))
    //   genericRecord1.put("value", d1)
    //   genericRecord2.put("value", d2)
    //   println("ERERERERE")
    //   val records = List(genericRecord1,genericRecord2)
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
      // case of nested records. As Strings they're shown that the values are equal
      //   val record = ShouldRenderAsCoproduct3(Coproduct(Event2()))
      //   val schemaFile = new java.io.File("src/main/avro/UnionsWithOptionalShapelessCoproduct.avdl")
      //   val schema = (new Idl(schemaFile)).CompilationUnit().getType("example.idl.ShouldRenderAsCoproduct3")
      //   val genericRecord1 = new GenericData.Record(schema)
      //   genericRecord1.put("value", new GenericData.Record(schema.getField("value").schema.getType("Event2")))
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
      // case of nested records. As Strings they're shown that the values are equal
      //   val record = ShouldRenderAsCoproduct3(Coproduct(Event2(99)))
      //   val format = RecordFormat[ShouldRenderAsCoproduct3]
      //   val avro = format.to(record)
      //   val sameRecord = format.from(avro)
      //   sameRecord.value === Coproduct(Event2(99))
      // }
    }

  }

}
