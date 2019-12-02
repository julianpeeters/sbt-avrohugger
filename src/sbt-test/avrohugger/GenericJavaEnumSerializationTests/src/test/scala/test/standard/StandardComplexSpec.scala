import test._
import org.specs2.mutable.Specification
import com.sksamuel.avro4s.RecordFormat
import org.apache.avro.generic.GenericData
import org.apache.avro.generic.GenericData.EnumSymbol
import scala.collection.JavaConverters._

class StandardComplexTest extends Specification {

  "A case class with an empty `Option[String]` field" should {
    "serialize and deserialize correctly" in {
      val record1 = AvroTypeProviderTest07(None)
      val record2 = AvroTypeProviderTest07(None)
      val format = RecordFormat[AvroTypeProviderTest07]
      val genericRecord1 = new GenericData.Record(format.to(record1).getSchema)
      genericRecord1.put("x", record1.x.getOrElse(null))
      val genericRecord2 = new GenericData.Record(format.to(record2).getSchema)
      genericRecord2.put("x", record2.x.getOrElse(null))
      val records = List(genericRecord1, genericRecord2)
      StandardTestUtil.verifyWriteAndRead(records)
    }
  }

  "A case class with an empty `Option[Int]` field" should {
    "serialize and deserialize correctly" in {
      val record1 = AvroTypeProviderTest08(None)
      val record2 = AvroTypeProviderTest08(None)
      val format = RecordFormat[AvroTypeProviderTest08]
      val genericRecord1 = new GenericData.Record(format.to(record1).getSchema)
      genericRecord1.put("x", record1.x.getOrElse(null))
      val genericRecord2 = new GenericData.Record(format.to(record2).getSchema)
      genericRecord2.put("x", record2.x.getOrElse(null))
      val records = List(genericRecord1, genericRecord2)
      StandardTestUtil.verifyWriteAndRead(records)
    }
  }

  "A case class with an `List[String]` field" should {
    "serialize and deserialize correctly" in {
      val record1 = AvroTypeProviderTest10(List("head", "tail"))
      val record2 = AvroTypeProviderTest10(List("top", "bottom"))
      val format = RecordFormat[AvroTypeProviderTest10]
      val genericRecord1 = new GenericData.Record(format.to(record1).getSchema)
      genericRecord1.put("x", record1.x.asJava)
      val genericRecord2 = new GenericData.Record(format.to(record2).getSchema)
      genericRecord2.put("x", record2.x.asJava)
      val records = List(genericRecord1, genericRecord2)
      StandardTestUtil.verifyWriteAndRead(records)
    }
  }

  "A case class with an `List[Int]` field" should {
    "serialize and deserialize correctly" in {
      val record1 = AvroTypeProviderTest11(List(1, 2))
      val record2 = AvroTypeProviderTest11(List(3, 4))
      val format = RecordFormat[AvroTypeProviderTest11]
      val genericRecord1 = new GenericData.Record(format.to(record1).getSchema)
      genericRecord1.put("x", record1.x.asJava)
      val genericRecord2 = new GenericData.Record(format.to(record2).getSchema)
      genericRecord2.put("x", record2.x.asJava)
      val records = List(genericRecord1, genericRecord2)
      StandardTestUtil.verifyWriteAndRead(records)
    }
  }

  "A case class with an `Option[String]` field" should {
    "serialize and deserialize correctly" in {
      val record1 = AvroTypeProviderTest12(Some("I'm here"))
      val record2 = AvroTypeProviderTest12(Some("I'm there"))
      val format = RecordFormat[AvroTypeProviderTest12]
      val genericRecord1 = new GenericData.Record(format.to(record1).getSchema)
      genericRecord1.put("x", record1.x.getOrElse(null))
      val genericRecord2 = new GenericData.Record(format.to(record2).getSchema)
      genericRecord2.put("x", record2.x.getOrElse(null))
      val records = List(genericRecord1, genericRecord2)
      StandardTestUtil.verifyWriteAndRead(records)
    }
  }

  "A case class with an `Option[Int]` field" should {
    "serialize and deserialize correctly" in {
      val record1 = AvroTypeProviderTest13(Some(1))
      val record2 = AvroTypeProviderTest13(Some(2))
      val format = RecordFormat[AvroTypeProviderTest13]
      val genericRecord1 = new GenericData.Record(format.to(record1).getSchema)
      genericRecord1.put("x", record1.x.getOrElse(null))
      val genericRecord2 = new GenericData.Record(format.to(record2).getSchema)
      genericRecord2.put("x", record2.x.getOrElse(null))
      val records = List(genericRecord1, genericRecord2)
      StandardTestUtil.verifyWriteAndRead(records)
    }
  }
/*
Avro4s is used to convert to `GenericRecord` for testing, chokes on `Map`

  "A case class with a `Map[String, Int]` field" should {
    "serialize and deserialize correctly" in {
      val record1 = AvroTypeProviderTestMap01(Map("bongo"->2))
      val record2 = AvroTypeProviderTestMap01(Map("mongo"->3))
      val format = RecordFormat[AvroTypeProviderTestMap01]
      val genericRecord1 = new GenericData.Record(format.to(record1).getSchema)
genericRecord1.put("x", record1.x)
val genericRecord2 = new GenericData.Record(format.to(record2).getSchema)
genericRecord2.put("x", record2.x)
val records = List(genericRecord1, genericRecord2)
      StandardTestUtil.verifyWriteAndRead(records)
    }
  }

  "A case class with a `Map[String, String]` field" should {
    "serialize and deserialize correctly" in {
      val record1 = AvroTypeProviderTestMap02(Map("4"->"four"))
      val record2 = AvroTypeProviderTestMap02(Map("5"->"five"))
      val format = RecordFormat[AvroTypeProviderTestMap02]
      val genericRecord1 = new GenericData.Record(format.to(record1).getSchema)
genericRecord1.put("x", record1.x)
val genericRecord2 = new GenericData.Record(format.to(record2).getSchema)
genericRecord2.put("x", record2.x)
val records = List(genericRecord1, genericRecord2)
      StandardTestUtil.verifyWriteAndRead(records)
    }
  }

  "A case class with a `Map[String, List[Int]]` field" should {
    "serialize and deserialize correctly" in {
      val record1 = AvroTypeProviderTestMap03(Map("sherpa"->Some(List(5,6))))
      val record2 = AvroTypeProviderTestMap03(Map("autobus"->Some(List(8,9))))
      val format = RecordFormat[AvroTypeProviderTestMap03]
      val genericRecord1 = new GenericData.Record(format.to(record1).getSchema)
genericRecord1.put("x", record1.x)
val genericRecord2 = new GenericData.Record(format.to(record2).getSchema)
genericRecord2.put("x", record2.x)
val records = List(genericRecord1, genericRecord2)
      StandardTestUtil.verifyWriteAndRead(records)
    }
  }
  */

  "A case class with an enumeration field" should {
    "serialize and deserialize correctly" in {
      val record1 = Compass(Direction.NORTH)
      val record2 = Compass(Direction.SOUTH)
      val format = RecordFormat[Compass]
      val schema1 = format.to(record1).getSchema
      val schema2 = format.to(record2).getSchema
      val genericRecord1 = new GenericData.Record(format.to(record1).getSchema)
      genericRecord1.put("direction", new EnumSymbol(schema1.getField("direction").schema, record1.direction))
      val genericRecord2 = new GenericData.Record(format.to(record2).getSchema)
      genericRecord2.put("direction", new EnumSymbol(schema2.getField("direction").schema, record2.direction))
      val records = List(genericRecord1, genericRecord2)
      StandardTestUtil.verifyWriteAndRead(records)
    }
  }

}
