import java.nio.ByteBuffer

import com.sksamuel.avro4s._
import com.sksamuel.avro4s.ToSchema._
import example.idl._
import org.apache.avro.Schema.Field
import org.apache.avro.{Conversions, LogicalTypes, Schema}
import org.specs2.mutable.Specification
import shapeless.Nat
import shapeless.ops.nat.ToInt
import shapeless.tag.@@
import test._

import scala.math.BigDecimal.RoundingMode

class DecimalsTaggedSpec extends Specification {

  def toDecimalTag[SP](bigDecimal: BigDecimal): BigDecimal @@ SP =
    shapeless.tag[SP][BigDecimal](bigDecimal)

  implicit val roundingMode: RoundingMode.RoundingMode = RoundingMode.UNNECESSARY

  def bigDecimalToSchema[A, B](precision: Int, scale: Int): ToSchema[BigDecimal @@ (A, B)] =
    new ToSchema[BigDecimal @@ (A, B)] {
      protected val schema: Schema = {
        val schema = Schema.create(Schema.Type.BYTES)
        LogicalTypes.decimal(precision, scale).addToSchema(schema)
        schema
      }
    }

  def bigDecimalFromValue[A, B](precision: Int, scale: Int): FromValue[BigDecimal @@ (A, B)] = {
    new FromValue[BigDecimal @@ (A, B)] {
      val decimalConversion = new Conversions.DecimalConversion
      val decimalType = LogicalTypes.decimal(precision, scale)
      override def apply(value: Any, field: Field): BigDecimal @@ (A, B) =
          toDecimalTag[(A, B)](
            decimalConversion
              .fromBytes(value.asInstanceOf[ByteBuffer], null, decimalType))
    }
  }

  def bigDecimalToValue[A, B](precision: Int, scale: Int)(
      implicit roundingMode: RoundingMode.RoundingMode)
    : ToValue[BigDecimal @@ (A, B)] = {
    val decimalConversion = new Conversions.DecimalConversion
    val decimalType = LogicalTypes.decimal(precision, scale)
    new ToValue[BigDecimal @@ (A, B)] {
      override def apply(value: BigDecimal @@ (A, B)): ByteBuffer = {
        val scaledValue = value.setScale(scale, roundingMode)
        decimalConversion.toBytes(scaledValue.bigDecimal, null, decimalType)
      }
    }
  }

  implicit def bigDecimalToSchemaSimple[A <: Nat, B <: Nat](
      implicit toIntNA: ToInt[A],
      toIntNB: ToInt[B]): ToSchema[BigDecimal @@ (A, B)] =
    bigDecimalToSchema[A, B](Nat.toInt[A], Nat.toInt[B])

  implicit def bigDecimalToSchemaBigPrecision[A <: Nat, B <: Nat, C <: Nat](
      implicit toIntNA: ToInt[A], toIntNB: ToInt[B], toIntNC: ToInt[C]): ToSchema[BigDecimal @@ ((A, B), C)] =
    bigDecimalToSchema[(A, B), C](Nat.toInt[A] * 10 + Nat.toInt[B], Nat.toInt[C])

  implicit def bigDecimalToSchemaBigPrecisionScale[A <: Nat, B <: Nat, C <: Nat, D <: Nat](
      implicit toIntNA: ToInt[A], toIntNB: ToInt[B], toIntNC: ToInt[C], toIntND: ToInt[D]): ToSchema[BigDecimal @@ ((A, B), (C, D))] =
    bigDecimalToSchema[(A, B), (C, D)](Nat.toInt[A] * 10 + Nat.toInt[B], Nat.toInt[C] * 10 + Nat.toInt[D])

  implicit def bigDecimalFromValueSimple[A <: Nat, B <: Nat](
      implicit toIntNA: ToInt[A],
      toIntNB: ToInt[B]): FromValue[BigDecimal @@ (A, B)] =
    bigDecimalFromValue[A, B](Nat.toInt[A], Nat.toInt[B])

  implicit def bigDecimalFromValueBigPrecision[A <: Nat, B <: Nat, C <: Nat](
      implicit toIntNA: ToInt[A], toIntNB: ToInt[B], toIntNC: ToInt[C]): FromValue[BigDecimal @@ ((A, B), C)] =
    bigDecimalFromValue[(A, B), C](Nat.toInt[A] * 10 + Nat.toInt[B], Nat.toInt[C])

  implicit def bigDecimalFromValueBigPrecisionScale[A <: Nat, B <: Nat, C <: Nat, D <: Nat](
      implicit toIntNA: ToInt[A], toIntNB: ToInt[B], toIntNC: ToInt[C], toIntND: ToInt[D]): FromValue[BigDecimal @@ ((A, B), (C, D))] =
    bigDecimalFromValue[(A, B), (C, D)](Nat.toInt[A] * 10 + Nat.toInt[B], Nat.toInt[C] * 10 + Nat.toInt[D])

  implicit def bigDecimalToValueSimple[A <: Nat, B <: Nat](
      implicit toIntNA: ToInt[A],
      toIntNB: ToInt[B]): ToValue[BigDecimal @@ (A, B)] =
    bigDecimalToValue[A, B](Nat.toInt[A], Nat.toInt[B])

  implicit def bigDecimalToValueBigPrecision[A <: Nat, B <: Nat, C <: Nat](
      implicit toIntNA: ToInt[A], toIntNB: ToInt[B], toIntNC: ToInt[C]): ToValue[BigDecimal @@ ((A, B), C)] =
    bigDecimalToValue[(A, B), C](Nat.toInt[A] * 10 + Nat.toInt[B], Nat.toInt[C])

  implicit def bigDecimalToValueBigPrecisionScale[A <: Nat, B <: Nat, C <: Nat, D <: Nat](
      implicit toIntNA: ToInt[A], toIntNB: ToInt[B], toIntNC: ToInt[C], toIntND: ToInt[D]): ToValue[BigDecimal @@ ((A, B), (C, D))] =
    bigDecimalToValue[(A, B), (C, D)](Nat.toInt[A] * 10 + Nat.toInt[B], Nat.toInt[C] * 10 + Nat.toInt[D])


  "A case class with `logicalType` fields and default values from .avdl" should {
    "deserialize correctly" in {
      val record1 = LogicalIdl(toDecimalTag(BigDecimal(10.6)), Some(toDecimalTag(BigDecimal(10.6))))
      val record2 = LogicalIdl(toDecimalTag(BigDecimal(14.6)), Some(toDecimalTag(BigDecimal(10.6))))
      val format = RecordFormat[LogicalIdl]
      val records = List(format.to(record1), format.to(record2))
      StandardTestUtil.verifyWriteAndRead(records)
    }
  }

  "A case class with `logicalType` fields, big precision, and explicit values from .avdl" should {
    "deserialize correctly" in {
      val record1 = LogicalIdlBigPrecision(toDecimalTag(BigDecimal(10.6)), Some(toDecimalTag(BigDecimal(10.6))))
      val record2 = LogicalIdlBigPrecision(toDecimalTag(BigDecimal(14.6)), Some(toDecimalTag(BigDecimal(10.6))))
      val format = RecordFormat[LogicalIdlBigPrecision]
      val records = List(format.to(record1), format.to(record2))
      StandardTestUtil.verifyWriteAndRead(records)
    }
  }

  "A case class with `logicalType` fields, big precision, big scale, and explicit values from .avdl" should {
    "deserialize correctly" in {
      val record1 = LogicalIdlBigPrecisionAndScale(toDecimalTag(BigDecimal(10.6)), Some(toDecimalTag(BigDecimal(10.6))))
      val record2 = LogicalIdlBigPrecisionAndScale(toDecimalTag(BigDecimal(14.6)), Some(toDecimalTag(BigDecimal(10.6))))
      val format = RecordFormat[LogicalIdlBigPrecisionAndScale]
      val records = List(format.to(record1), format.to(record2))
      StandardTestUtil.verifyWriteAndRead(records)
    }
  }

}
