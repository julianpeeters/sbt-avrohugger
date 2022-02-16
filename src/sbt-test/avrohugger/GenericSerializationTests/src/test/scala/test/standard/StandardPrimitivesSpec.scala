import test._
import org.specs2.mutable.Specification
import com.sksamuel.avro4s._
import org.apache.avro.{Conversions, LogicalTypes, Schema}
import org.apache.avro.data.TimeConversions
import org.apache.avro.generic.GenericData
import java.time.{Instant, LocalDate}
import java.util.UUID
import scala.math.BigDecimal.RoundingMode


class StandardPrimitivesSpec extends Specification {

  "A case class with an `Int` field" should {
    "deserialize correctly" in {
      val record1 = AvroTypeProviderTest00(1)
      val record2 = AvroTypeProviderTest00(2)
      val format = RecordFormat[AvroTypeProviderTest00]
      val genericRecord1 = new GenericData.Record(format.to(record1).getSchema)
      genericRecord1.put("x", record1.x)
      val genericRecord2 = new GenericData.Record(format.to(record2).getSchema)
      genericRecord2.put("x", record2.x)
      val records = List(genericRecord1, genericRecord2)
      StandardTestUtil.verifyWriteAndRead(records)
    }
  }

  "A case class with an `Float` field" should {
    "deserialize correctly" in {
      val record1 = AvroTypeProviderTest01(1F)
      val record2 = AvroTypeProviderTest01(2F)
      val format = RecordFormat[AvroTypeProviderTest01]
      val genericRecord1 = new GenericData.Record(format.to(record1).getSchema)
      genericRecord1.put("x", record1.x)
      val genericRecord2 = new GenericData.Record(format.to(record2).getSchema)
      genericRecord2.put("x", record2.x)
      val records = List(genericRecord1, genericRecord2)
      StandardTestUtil.verifyWriteAndRead(records)
    }
  }

  "A case class with an `Long` field" should {
    "deserialize correctly" in {
      val record1 = AvroTypeProviderTest02(1L)
      val record2 = AvroTypeProviderTest02(2L)
      val format = RecordFormat[AvroTypeProviderTest02]
      val genericRecord1 = new GenericData.Record(format.to(record1).getSchema)
      genericRecord1.put("x", record1.x)
      val genericRecord2 = new GenericData.Record(format.to(record2).getSchema)
      genericRecord2.put("x", record2.x)
      val records = List(genericRecord1, genericRecord2)
      StandardTestUtil.verifyWriteAndRead(records)
    }
  }

  "A case class with an `Double` field" should {
    "deserialize correctly" in {
      val record1 = AvroTypeProviderTest03(1D)
      val record2 = AvroTypeProviderTest03(2D)
      val format = RecordFormat[AvroTypeProviderTest03]
      val genericRecord1 = new GenericData.Record(format.to(record1).getSchema)
      genericRecord1.put("x", record1.x)
      val genericRecord2 = new GenericData.Record(format.to(record2).getSchema)
      genericRecord2.put("x", record2.x)
      val records = List(genericRecord1, genericRecord2)
      StandardTestUtil.verifyWriteAndRead(records)
    }
  }

  "A case class with an `Boolean` field" should {
    "deserialize correctly" in {
      val record1 = AvroTypeProviderTest04(true)
      val record2 = AvroTypeProviderTest04(false)
      val format = RecordFormat[AvroTypeProviderTest04]
      val genericRecord1 = new GenericData.Record(format.to(record1).getSchema)
      genericRecord1.put("x", record1.x)
      val genericRecord2 = new GenericData.Record(format.to(record2).getSchema)
      genericRecord2.put("x", record2.x)
      val records = List(genericRecord1, genericRecord2)
      StandardTestUtil.verifyWriteAndRead(records)
    }
  }

  "A case class with an `String` field" should {
    "deserialize correctly" in {
      val record1 = AvroTypeProviderTest05("hello world")
      val record2 = AvroTypeProviderTest05("hello galaxy")
      val format = RecordFormat[AvroTypeProviderTest05]
      val genericRecord1 = new GenericData.Record(format.to(record1).getSchema)
      genericRecord1.put("x", record1.x)
      val genericRecord2 = new GenericData.Record(format.to(record2).getSchema)
      genericRecord2.put("x", record2.x)
      val records = List(genericRecord1, genericRecord2)
      StandardTestUtil.verifyWriteAndRead(records)
    }
  }

  "A case class with a reserved word for a field name" should {
    "serialize correctly" in {
      val format = RecordFormat[Test]

      val schema = format.to(Test(true, true)).getSchema
      val record1 = new GenericData.Record(schema)
      record1.put("public", true)
      record1.put("protected", true)
      val record2 = new GenericData.Record(schema)
      record2.put("public", false)
      record2.put("protected", false)

      StandardTestUtil.verifyWriteAndRead(List(record1, record2))
    }
  }

  // java.time.Instant.MAX is a datetime so large that, expressed in milliseconds,
  // it exceeds the maximum Long Value available.
  private val topMillisInstant: Instant = Instant.ofEpochMilli(Long.MaxValue)

  // this is commented out because avro4s
  // "A case class with `logicalType` fields and default values from .avdl" should {
  //   "deserialize correctly" in {
  //     implicit val sp: ScaleAndPrecisionAndRoundingMode = ScaleAndPrecisionAndRoundingMode(8, 20, RoundingMode.HALF_UP)
  //     val record1 = LogicalIdl()
  //     val record2 = LogicalIdl()
  //     val format = RecordFormat[LogicalIdl]
  //     val genericRecord1 = new GenericData.Record(format.to(record1).getSchema)
// val genericRecord2 = new GenericData.Record(format.to(record2).getSchema)
// val records = List(genericRecord1, genericRecord2)
  //     StandardTestUtil.verifyWriteAndRead(records)
  //   }
  // }
  // 
  // "A case class with `logicalType` fields and explicit values from .avdl" should {
  //   "deserialize correctly" in {
  //     implicit val sp: ScaleAndPrecisionAndRoundingMode = ScaleAndPrecisionAndRoundingMode(8, 20, RoundingMode.HALF_UP)
  //     val record1 = LogicalIdl(BigDecimal(10.0), Some(BigDecimal(10.0)), topMillisInstant, LocalDate.MAX)
  //     val record2 = LogicalIdl(BigDecimal(10.0), Some(BigDecimal(10.0)), topMillisInstant, LocalDate.MAX)
  //     val format = RecordFormat[LogicalIdl]
  //     val genericRecord1 = new GenericData.Record(format.to(record1).getSchema)
  // val genericRecord2 = new GenericData.Record(format.to(record2).getSchema)
  // val records = List(genericRecord1, genericRecord2)
  //     StandardTestUtil.verifyWriteAndRead(records)
  //   }
  // }

  "A case class with a `logicalType` fields from .avsc" should {
    "deserialize correctly" in {
      implicit val sp: ScalePrecision = ScalePrecision(8, 20)
      implicit val roundingMode = BigDecimal.RoundingMode.HALF_EVEN
      val record1 = LogicalSc(BigDecimal(10.00000000), topMillisInstant, LocalDate.MAX, UUID.randomUUID())
      val record2 = LogicalSc(BigDecimal(10.000000001), topMillisInstant, LocalDate.MAX, UUID.randomUUID())
      val format = RecordFormat[LogicalSc]
      val decimalConverter = new Conversions.DecimalConversion()
      val timestampConverter = new TimeConversions.TimestampMillisConversion()
      val dateConverter = new TimeConversions.DateConversion()
      val uuidConverter = new Conversions.UUIDConversion()

      val schema1 = format.to(record1).getSchema
      val decimalSchema1 = schema1.getField("data").schema
      val timestampSchema1 = schema1.getField("ts").schema
      val dateSchema1 = schema1.getField("dt").schema
      val uuidSchema1 = schema1.getField("uuid").schema

      val schema2 = format.to(record2).getSchema
      val decimalSchema2 = schema2.getField("data").schema
      val timestampSchema2 = schema2.getField("ts").schema
      val dateSchema2 = schema2.getField("dt").schema
      val uuidSchema2 = schema2.getField("uuid").schema
      
      val genericRecord1 = new GenericData.Record(schema1)
      genericRecord1.put("data", decimalConverter.toBytes(record1.data.setScale(8, BigDecimal.RoundingMode.HALF_EVEN).bigDecimal, decimalSchema1, decimalSchema1.getLogicalType))
      genericRecord1.put("ts", timestampConverter.toLong(record1.ts, timestampSchema1, timestampSchema1.getLogicalType))
      genericRecord1.put("dt", dateConverter.toInt(record1.dt, dateSchema1, dateSchema1.getLogicalType))
      genericRecord1.put("uuid", uuidConverter.toCharSequence(record1.uuid, uuidSchema1, uuidSchema1.getLogicalType))
      val genericRecord2 = new GenericData.Record(schema2)
      genericRecord2.put("data", decimalConverter.toBytes(record2.data.setScale(8, BigDecimal.RoundingMode.HALF_EVEN).bigDecimal, decimalSchema2, decimalSchema2.getLogicalType))
      genericRecord2.put("ts", timestampConverter.toLong(record2.ts, timestampSchema2, timestampSchema2.getLogicalType))
      genericRecord2.put("dt", dateConverter.toInt(record2.dt, dateSchema2, dateSchema2.getLogicalType))
      genericRecord2.put("uuid", uuidConverter.toCharSequence(record2.uuid, uuidSchema2, uuidSchema2.getLogicalType))
      val records = List(genericRecord1, genericRecord2)
      StandardTestUtil.verifyWriteAndRead(records)
    }
  }


/*
  Avro4s is used to convert to `GenericRecord` for testing, chokes on `null`
  "A case class with an `Null` field" should {
    "deserialize correctly" in {
      val record1 = AvroTypeProviderTest06(null)
      val record2 = AvroTypeProviderTest06(null)
      val format = RecordFormat[AvroTypeProviderTest06]
      val genericRecord1 = new GenericData.Record(format.to(record1).getSchema)
val genericRecord2 = new GenericData.Record(format.to(record2).getSchema)
val records = List(genericRecord1, genericRecord2)
      StandardTestUtil.verifyWriteAndRead(records)
    }
  }
*/
  "A case class with an `Array[Bytes]` field" should {
    "deserialize correctly" in {
      val record1 = AvroTypeProviderTest69("hello world".getBytes)
      val record2 = AvroTypeProviderTest69("hello galaxy".getBytes)
      val format = RecordFormat[AvroTypeProviderTest69]
      val genericRecord1 = new GenericData.Record(format.to(record1).getSchema)
      genericRecord1.put("x", java.nio.ByteBuffer.wrap(record1.x))
      val genericRecord2 = new GenericData.Record(format.to(record2).getSchema)
      genericRecord2.put("x", java.nio.ByteBuffer.wrap(record2.x))
      val records = List(genericRecord1, genericRecord2)
      StandardTestUtil.verifyWriteAndRead(records)
    }
  }
  
  // skip until spark catalyst 2.13 is released
  // "A case class' field names" should {
  //   "be reflectable by spark even when they are reserved keywords" in {
  //     import org.apache.spark.sql.catalyst.expressions._
  //     import org.apache.spark.sql.catalyst.ScalaReflection
  //     import org.apache.spark.sql.types._
  //     val inputObject = BoundReference(0, ObjectType(classOf[Test]), nullable = true)
  //     ScalaReflection.serializerFor[Test](inputObject) must not(throwAn[java.lang.UnsupportedOperationException])
  //   }
  // }

}
