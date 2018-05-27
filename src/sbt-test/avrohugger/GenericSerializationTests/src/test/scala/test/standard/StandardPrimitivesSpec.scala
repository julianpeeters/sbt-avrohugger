import test._
import org.specs2.mutable.Specification
import com.sksamuel.avro4s._
import com.sksamuel.avro4s.ToSchema._
import java.time._
import scala.math.BigDecimal.RoundingMode
import java.nio.ByteBuffer
import org.apache.avro.Schema

object BigDecimalUtil {

  def bigDecimalToByte(num: BigDecimal): Array[Byte] = {
    val sig: Array[Byte] = num.bigDecimal.unscaledValue().toByteArray
    val scale: Int       = num.scale
    val byteScale: Array[Byte] =
      Array[Byte]((scale >>> 24).toByte, (scale >>> 16).toByte, (scale >>> 8).toByte, scale.toByte)
    byteScale ++ sig
  }

  def byteToBigDecimal(raw: Array[Byte]): BigDecimal = {
    val scale = (raw(0) & 0xFF) << 24 | (raw(1) & 0xFF) << 16 | (raw(2) & 0xFF) << 8 | (raw(3) & 0xFF)
    val sig   = new java.math.BigInteger(raw.drop(4))
    BigDecimal(sig, scale)
  }

}

class StandardPrimitivesSpec extends Specification {

  implicit object bigDecimalToSchema extends ToSchema[BigDecimal] {
    override val schema: Schema = Schema.create(Schema.Type.BYTES)
  }

  implicit object bigDecimalFromValue extends FromValue[BigDecimal] {
    def apply(value: Any, field: Schema.Field): BigDecimal =
      BigDecimalUtil.byteToBigDecimal(value.asInstanceOf[ByteBuffer].array())
  }

  implicit object bigDecimalToValue extends ToValue[BigDecimal] {
    override def apply(value: BigDecimal): ByteBuffer =
      ByteBuffer.wrap(BigDecimalUtil.bigDecimalToByte(value))
  }

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

  "A case class with `logicalType` fields and default values from .avdl" should {
    "deserialize correctly" in {
      val record1 = LogicalIdl()
      val record2 = LogicalIdl()
      val format = RecordFormat[LogicalIdl]
      val records = List(format.to(record1), format.to(record2))
      StandardTestUtil.verifyWriteAndRead(records)
    }
  }

  "A case class with `logicalType` fields and explicit values from .avdl" should {
    "deserialize correctly" in {
      val record1 = LogicalIdl(BigDecimal(10.0), LocalDateTime.MAX, LocalDate.MAX)
      val record2 = LogicalIdl(BigDecimal(10.0), LocalDateTime.MAX, LocalDate.MAX)
      val format = RecordFormat[LogicalIdl]
      val records = List(format.to(record1), format.to(record2))
      StandardTestUtil.verifyWriteAndRead(records)
    }
  }

  "A case class with a `logicalType` fields from .avsc" should {
    "deserialize correctly" in {
      val record1 = LogicalSc(BigDecimal(10.0), LocalDateTime.MAX, LocalDate.MAX)
      val record2 = LogicalSc(BigDecimal(10.0), LocalDateTime.MAX, LocalDate.MAX)
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

}
