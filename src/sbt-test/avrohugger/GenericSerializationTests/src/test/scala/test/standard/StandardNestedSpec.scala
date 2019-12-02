import test._

import org.specs2.mutable.Specification
import com.sksamuel.avro4s.RecordFormat
import org.apache.avro.generic.GenericData
import scala.collection.JavaConverters._

class StandardNestedSpec extends Specification {

  "A case class with a `List[List[String]]` field" should {
    "serialize and deserialize correctly" in {
      val record1 = AvroTypeProviderTest28(List(List("blackbird.grackle")))
      val record2 = AvroTypeProviderTest28(List(List("pheasant.turkey")))
      val format = RecordFormat[AvroTypeProviderTest28]
      val genericRecord1 = new GenericData.Record(format.to(record1).getSchema)
      genericRecord1.put("x", record1.x.map(_.asJava).asJava)
      val genericRecord2 = new GenericData.Record(format.to(record2).getSchema)
      genericRecord2.put("x", record2.x.map(_.asJava).asJava)
      val records = List(genericRecord1, genericRecord2)
      StandardTestUtil.verifyWriteAndRead(records)
    }
  }

  "A case class with a `List[List[Int]]` field" should {
    "serialize and deserialize correctly" in {
      val record1 = AvroTypeProviderTest29(List(List(1, 2)))
      val record2 = AvroTypeProviderTest29(List(List(3, 4)))
      val format = RecordFormat[AvroTypeProviderTest29]
      val genericRecord1 = new GenericData.Record(format.to(record1).getSchema)
      genericRecord1.put("x", record1.x.map(_.asJava).asJava)
      val genericRecord2 = new GenericData.Record(format.to(record2).getSchema)
      genericRecord2.put("x", record2.x.map(_.asJava).asJava)
      val records = List(genericRecord1, genericRecord2)
      StandardTestUtil.verifyWriteAndRead(records)
    }
  }

  "A case class with an `Option[List[String]]` field" should {
    "serialize and deserialize correctly" in {
      val record1 = AvroTypeProviderTest30(Some(List("starling.oriole")))
      val record2 = AvroTypeProviderTest30(Some(List("buzzard.hawk")))
      val format = RecordFormat[AvroTypeProviderTest30]
      val genericRecord1 = new GenericData.Record(format.to(record1).getSchema)
      genericRecord1.put("x", record1.x.map(_.asJava).getOrElse(null))
      val genericRecord2 = new GenericData.Record(format.to(record2).getSchema)
      genericRecord2.put("x", record2.x.map(_.asJava).getOrElse(null))
      val records = List(genericRecord1, genericRecord2)
      StandardTestUtil.verifyWriteAndRead(records)
    }
  }

  "A case class with an `Option[List[Int]]` field" should {
    "serialize and deserialize correctly" in {
      val record1 = AvroTypeProviderTest31(Some(List(5, 6)))
      val record2 = AvroTypeProviderTest31(Some(List(7, 8)))
      val format = RecordFormat[AvroTypeProviderTest31]
      val genericRecord1 = new GenericData.Record(format.to(record1).getSchema)
      genericRecord1.put("x", record1.x.map(_.asJava).getOrElse(null))
      val genericRecord2 = new GenericData.Record(format.to(record2).getSchema)
      genericRecord2.put("x", record2.x.map(_.asJava).getOrElse(null))
      val records = List(genericRecord1, genericRecord2)
      StandardTestUtil.verifyWriteAndRead(records)
    }
  }

  "A case class with a `List[Option[String]]` field" should {
    "serialize and deserialize correctly" in {
      val record1 = AvroTypeProviderTest32(List(Some("cowbird")))
      val record2 = AvroTypeProviderTest32(List(Some("cuckoo")))
      val format = RecordFormat[AvroTypeProviderTest32]
      val genericRecord1 = new GenericData.Record(format.to(record1).getSchema)
      genericRecord1.put("x", record1.x.map(_.getOrElse(null)).asJava)
      val genericRecord2 = new GenericData.Record(format.to(record2).getSchema)
      genericRecord2.put("x", record2.x.map(_.getOrElse(null)).asJava)
      val records = List(genericRecord1, genericRecord2)
      StandardTestUtil.verifyWriteAndRead(records)
    }
  }

  "A case class with a `List[Option[Int]]` field" should {
    "serialize and deserialize correctly" in {
      val record1 = AvroTypeProviderTest33(List(Some(1)))
      val record2 = AvroTypeProviderTest33(List(Some(2)))
      val format = RecordFormat[AvroTypeProviderTest33]
      val genericRecord1 = new GenericData.Record(format.to(record1).getSchema)
      genericRecord1.put("x", record1.x.map(_.getOrElse(null)).asJava)
      val genericRecord2 = new GenericData.Record(format.to(record2).getSchema)
      genericRecord2.put("x", record2.x.map(_.getOrElse(null)).asJava)
      val records = List(genericRecord1, genericRecord2)
      StandardTestUtil.verifyWriteAndRead(records)
    }
  }

  "A case class with a `Option[List[Option[String]]]` field" should {
    "serialize and deserialize correctly" in {
      val record1 = AvroTypeProviderTest34(Some(List(Some("cowbird"), None)))
      val record2 = AvroTypeProviderTest34(Some(List(Some("lark"), None)))
      val format = RecordFormat[AvroTypeProviderTest34]
      val genericRecord1 = new GenericData.Record(format.to(record1).getSchema)
      genericRecord1.put("x", record1.x.map(_.map(_.getOrElse(null)).asJava).getOrElse(null) )
      val genericRecord2 = new GenericData.Record(format.to(record2).getSchema)
      genericRecord2.put("x", record2.x.map(_.map(_.getOrElse(null)).asJava).getOrElse(null) )
      val records = List(genericRecord1, genericRecord2)
      StandardTestUtil.verifyWriteAndRead(records)
    }
  }

  "A case class with a `Option[List[Option[Int]]]` field" should {
    "serialize and deserialize correctly" in {
      val record1 = AvroTypeProviderTest35(Some(List(Some(1), None)))
      val record2 = AvroTypeProviderTest35(Some(List(Some(2), None)))
      val format = RecordFormat[AvroTypeProviderTest35]
      val genericRecord1 = new GenericData.Record(format.to(record1).getSchema)
      genericRecord1.put("x", record1.x.map(_.map(_.getOrElse(null)).asJava).getOrElse(null))
      val genericRecord2 = new GenericData.Record(format.to(record2).getSchema)
      genericRecord2.put("x", record2.x.map(_.map(_.getOrElse(null)).asJava).getOrElse(null))
      val records = List(genericRecord1, genericRecord2)
      StandardTestUtil.verifyWriteAndRead(records)
    }
  }


/*
Avro4s is used to convert to `GenericRecord` for testing, chokes on `Map`

  "A case class with a Map[Int, Map[Int, Int]] field" should {
    "serialize and deserialize correctly" in {
      val record1 = AvroTypeProviderTestMap07(Map("art"->Map("explode"->4)))
      val record2 = AvroTypeProviderTestMap07(Map("science"->Map("define"->4)))
      val format = RecordFormat[AvroTypeProviderTestMap07]
      val genericRecord1 = new GenericData.Record(format.to(record1).getSchema)
genericRecord1.put("x", record1.x)
val genericRecord2 = new GenericData.Record(format.to(record2).getSchema)
genericRecord2.put("x", record2.x)
val records = List(genericRecord1, genericRecord2)
      StandardTestUtil.verifyWriteAndRead(records)
    }
  }

  "A case class with a List[Map[String, Map[Int, String]]] field" should {
    "serialize and deserialize correctly" in {
      val record1 = AvroTypeProviderTestMap08(List(Map("hare"->Map("serpent"->"eagle"))))
      val record2 = AvroTypeProviderTestMap08(List(Map("snake"->Map("bear"->"deer"))))
      val format = RecordFormat[AvroTypeProviderTestMap08]
      val genericRecord1 = new GenericData.Record(format.to(record1).getSchema)
genericRecord1.put("x", record1.x)
val genericRecord2 = new GenericData.Record(format.to(record2).getSchema)
genericRecord2.put("x", record2.x)
val records = List(genericRecord1, genericRecord2)
      StandardTestUtil.verifyWriteAndRead(records)
    }
  }

  "A case class with a Option[Map[String, Option[List[String]]]] field" should {
    "serialize and deserialize correctly" in {
      val record1 = AvroTypeProviderTestMap09(Some(Map("Eje"->None)))
      val record2 = AvroTypeProviderTestMap09(Some(Map("Rayo"->None)))
      val format = RecordFormat[AvroTypeProviderTestMap09]
      val genericRecord1 = new GenericData.Record(format.to(record1).getSchema)
genericRecord1.put("x", record1.x)
val genericRecord2 = new GenericData.Record(format.to(record2).getSchema)
genericRecord2.put("x", record2.x)
val records = List(genericRecord1, genericRecord2)
      StandardTestUtil.verifyWriteAndRead(records)
    }
  }
  */
}
