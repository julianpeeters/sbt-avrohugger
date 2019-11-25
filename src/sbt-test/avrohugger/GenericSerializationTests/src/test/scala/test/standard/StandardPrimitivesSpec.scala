import test._
import org.specs2.mutable.Specification
import com.sksamuel.avro4s._
import com.sksamuel.avro4s.ToSchema._
import org.apache.avro.{LogicalTypes, Schema}
import java.time.{Instant, LocalDate}
import java.util.UUID
import scala.math.BigDecimal.RoundingMode


class StandardPrimitivesSpec extends Specification {

  "A case class with an `Int` field" should {
    "deserialize correctly" in {
      val record1 = AvroTypeProviderTest00(1)
      val record2 = AvroTypeProviderTest00(2)
      val format = RecordFormat[AvroTypeProviderTest00]
      val records = List(format.to(record1), format.to(record2))
      StandardTestUtil.verifyWriteAndRead(records)
    }
  }

  "A case class with an `Float` field" should {
    "deserialize correctly" in {
      val record1 = AvroTypeProviderTest01(1F)
      val record2 = AvroTypeProviderTest01(2F)
      val format = RecordFormat[AvroTypeProviderTest01]
      val records = List(format.to(record1), format.to(record2))
      StandardTestUtil.verifyWriteAndRead(records)
    }
  }

  "A case class with an `Long` field" should {
    "deserialize correctly" in {
      val record1 = AvroTypeProviderTest02(1L)
      val record2 = AvroTypeProviderTest02(2L)
      val format = RecordFormat[AvroTypeProviderTest02]
      val records = List(format.to(record1), format.to(record2))
      StandardTestUtil.verifyWriteAndRead(records)
    }
  }

  "A case class with an `Double` field" should {
    "deserialize correctly" in {
      val record1 = AvroTypeProviderTest03(1D)
      val record2 = AvroTypeProviderTest03(2D)
      val format = RecordFormat[AvroTypeProviderTest03]
      val records = List(format.to(record1), format.to(record2))
      StandardTestUtil.verifyWriteAndRead(records)
    }
  }

  "A case class with an `Boolean` field" should {
    "deserialize correctly" in {
      val record1 = AvroTypeProviderTest04(true)
      val record2 = AvroTypeProviderTest04(false)
      val format = RecordFormat[AvroTypeProviderTest04]
      val records = List(format.to(record1), format.to(record2))
      StandardTestUtil.verifyWriteAndRead(records)
    }
  }

  "A case class with an `String` field" should {
    "deserialize correctly" in {
      val record1 = AvroTypeProviderTest05("hello world")
      val record2 = AvroTypeProviderTest05("hello galaxy")
      val format = RecordFormat[AvroTypeProviderTest05]
      val records = List(format.to(record1), format.to(record2))
      StandardTestUtil.verifyWriteAndRead(records)
    }
  }

  implicit object instantToSchema extends ToSchema[Instant] {
    override val schema: Schema =
      LogicalTypes.timestampMillis.addToSchema(
        Schema.create(Schema.Type.LONG)
      )
  }

  implicit object instantFromValue extends FromValue[Instant] {
    override def apply(value: Any, field: Schema.Field): Instant =
      Instant.ofEpochMilli(value.asInstanceOf[Long])
  }

  implicit object instantToValue extends ToValue[Instant] {
    override def apply(value: Instant): Long = value.toEpochMilli
  }

  // java.time.Instant.MAX is a datetime so large that, expressed in milliseconds,
  // it exceeds the maximum Long Value available.
  private val topMillisInstant: Instant = Instant.ofEpochMilli(Long.MaxValue)

  // "A case class with `logicalType` fields and default values from .avdl" should {
  //   "deserialize correctly" in {
  // this is commented out because equality of Generic Records fails in the
  // case of nested records. As Strings they're shown to be equal
  //     implicit val sp: ScaleAndPrecisionAndRoundingMode = ScaleAndPrecisionAndRoundingMode(8, 20, RoundingMode.HALF_UP)
  //     val record1 = LogicalIdl()
  //     val record2 = LogicalIdl()
  //     val format = RecordFormat[LogicalIdl]
  //     val records = List(format.to(record1), format.to(record2))
  //     StandardTestUtil.verifyWriteAndRead(records)
  //   }
  // }

  // "A case class with `logicalType` fields and explicit values from .avdl" should {
  //   "deserialize correctly" in {
  // this is commented out because equality of Generic Records fails in the
  // case of nested records. As Strings they're shown to be equal
  //     implicit val sp: ScaleAndPrecisionAndRoundingMode = ScaleAndPrecisionAndRoundingMode(8, 20, RoundingMode.HALF_UP)
  //     val record1 = LogicalIdl(BigDecimal(10.0), Some(BigDecimal(10.0)), topMillisInstant, LocalDate.MAX)
  //     val record2 = LogicalIdl(BigDecimal(10.0), Some(BigDecimal(10.0)), topMillisInstant, LocalDate.MAX)
  //     val format = RecordFormat[LogicalIdl]
  //     val records = List(format.to(record1), format.to(record2))
  //     StandardTestUtil.verifyWriteAndRead(records)
  //   }
  // }

  "A case class with a `logicalType` fields from .avsc" should {
    "deserialize correctly" in {

      implicit val sp: ScaleAndPrecisionAndRoundingMode = ScaleAndPrecisionAndRoundingMode(8, 20, RoundingMode.HALF_UP)
      val record1 = LogicalSc(BigDecimal(10.0), topMillisInstant, LocalDate.MAX, UUID.randomUUID())
      val record2 = LogicalSc(BigDecimal(10.0), topMillisInstant, LocalDate.MAX, UUID.randomUUID())
      val format = RecordFormat[LogicalSc]
      val records = List(format.to(record1), format.to(record2))
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
      val records = List(format.to(record1), format.to(record2))
      StandardTestUtil.verifyWriteAndRead(records)
    }
  }
*/
  "A case class with an `Array[Bytes]` field" should {
    "deserialize correctly" in {
      val record1 = AvroTypeProviderTest69("hello world".getBytes)
      val record2 = AvroTypeProviderTest69("hello galaxy".getBytes)
      val format = RecordFormat[AvroTypeProviderTest69]
      val records = List(format.to(record1), format.to(record2))
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
