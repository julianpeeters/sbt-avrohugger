import java.io.{ByteArrayInputStream, ByteArrayOutputStream, ObjectInputStream, ObjectOutputStream}
import java.time._
import java.util.UUID

import org.specs2.mutable.Specification
import test._

class SpecificPrimitivesSpec extends Specification {

  private val zone: ZoneId = ZoneId.of("UTC")
  val instant = LocalDateTime.of(2018, 6, 12, 18, 0).atZone(zone).toInstant
  val clock = Clock.fixed(instant, zone)
  private val bigDecimal1 = BigDecimal(10.0)
  private val bigDecimal2 = BigDecimal(10.000000001)
  private val bigDecimal3 = BigDecimal(0.1)  
  private val bigDecimal4 = BigDecimal(0.2)  

  // java.time.Instant.MAX is a datetime so large that, expressed in milliseconds,
  // it exceeds the maximum Long Value available.
  private val topMillisInstant: Instant = Instant.ofEpochMilli(Long.MaxValue)

  "A case class with an `Int` field" should {
    "deserialize correctly" in {
      val record1 = AvroTypeProviderTest00(1)
      val record2 = AvroTypeProviderTest00(2)
      val records = List(record1, record2)
      SpecificTestUtil.verifyWriteAndRead(records)
    }
  }

  "A case class with an `Float` field" should {
    "deserialize correctly" in {
      val record1 = AvroTypeProviderTest01(1F)
      val record2 = AvroTypeProviderTest01(2F)
      val records = List(record1, record2)
      SpecificTestUtil.verifyWriteAndRead(records)
    }
  }

  "A case class with an `Long` field" should {
    "deserialize correctly" in {
      val record1 = AvroTypeProviderTest02(1L)
      val record2 = AvroTypeProviderTest02(2L)
      val records = List(record1, record2)
      SpecificTestUtil.verifyWriteAndRead(records)
    }
  }

  "A case class with an `Double` field" should {
    "deserialize correctly" in {
      val record1 = AvroTypeProviderTest03(1D)
      val record2 = AvroTypeProviderTest03(2D)
      val records = List(record1, record2)
      SpecificTestUtil.verifyWriteAndRead(records)
    }
  }

  "A case class with an `Boolean` field" should {
    "deserialize correctly" in {
      val record1 = AvroTypeProviderTest04(true)
      val record2 = AvroTypeProviderTest04(false)
      val records = List(record1, record2)
      SpecificTestUtil.verifyWriteAndRead(records)
    }
  }

  "A case class with an `String` field" should {
    "deserialize correctly" in {
      val record1 = AvroTypeProviderTest05("hello world")
      val record2 = AvroTypeProviderTest05("hello galaxy")
      val records = List(record1, record2)
      SpecificTestUtil.verifyWriteAndRead(records)
    }
  }

  "A case class with an `Null` field" should {
    "deserialize correctly" in {
      val record1 = AvroTypeProviderTest06(null)
      val record2 = AvroTypeProviderTest06(null)
      val records = List(record1, record2)
      SpecificTestUtil.verifyWriteAndRead(records)
    }
  }

  "A case class with `logicalType` fields and explicit values from .avdl" should {
    "deserialize correctly" in {
      val record1 = LogicalIdl(bigDecimal1, Some(bigDecimal1), topMillisInstant, LocalDate.now(clock))
      val record2 = LogicalIdl(bigDecimal2, Some(bigDecimal2), topMillisInstant, LocalDate.now(clock))
      val records = List(record1, record2)
      SpecificTestUtil.verifyWriteAndRead(records)
    }
  }

  "A case class with a `logicalType` fields from .avsc" should {
    "deserialize correctly" in {
      val record1 = LogicalSc(bigDecimal1, test.fxType(bigDecimal3), topMillisInstant, LocalDate.now(clock), UUID.randomUUID())
      val record2 = LogicalSc(bigDecimal2, test.fxType(bigDecimal4), topMillisInstant, LocalDate.now(clock), UUID.randomUUID())
      val records = List(record1, record2)
      SpecificTestUtil.verifyWriteAndRead(records)
    }
  }

  "A case class with an `Array[Bytes]` field" should {
    "deserialize correctly" in {
      val record1 = AvroTypeProviderTest69("hello world".getBytes)
      val record2 = AvroTypeProviderTest69("hello galaxy".getBytes)
      val records = List(record1, record2)
      SpecificTestUtil.verifyWriteAndRead(records)
    }
  }

  "A Specific Generator" should {
    "generate classes that are truly Externalizable" in {
      val record = BinarySc(Array(1.toByte, 2.toByte, 3.toByte, 4.toByte))

      val baos = new ByteArrayOutputStream()
      val oos = new ObjectOutputStream(baos)
      oos.writeObject(record)
      oos.close()

      val bais = new ByteArrayInputStream(baos.toByteArray)
      val ois = new ObjectInputStream(bais)
      val deserializedRecord = ois.readObject()
      ois.close()

      record === deserializedRecord
    }
  }
  
  "A case class with a reserved word for a field name" should {
    "deserialize correctly" in {
      val record1 = Test(true)
      val record2 = Test(false)
      val records = List(record1, record2)
      SpecificTestUtil.verifyWriteAndRead(records)
    }
  }

}
