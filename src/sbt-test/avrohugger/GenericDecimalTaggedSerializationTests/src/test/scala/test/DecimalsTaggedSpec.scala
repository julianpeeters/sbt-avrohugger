package test

import java.nio.ByteBuffer
import example.idl._
import org.apache.avro.compiler.idl.Idl
import org.apache.avro.SchemaBuilder
import org.apache.avro.Schema.Field
import org.apache.avro.LogicalTypes.Decimal
import org.apache.avro.generic.{GenericData, GenericRecord}
import org.apache.avro.{Conversions, LogicalTypes, Schema}
import org.specs2.mutable.Specification
import shapeless._
import shapeless.tag.@@
import test._
import collection.JavaConverters._

class DecimalsTaggedSpec extends Specification {

  def bigDecimalToBytes(bd: BigDecimal, fieldSchema: Schema): ByteBuffer = {
    val decimalConversion = new Conversions.DecimalConversion
    val decimalType = fieldSchema.getLogicalType.asInstanceOf[Decimal]
    val scaled = bd.bigDecimal.setScale(decimalType.getScale)
    val bytes = decimalConversion.toBytes(scaled, fieldSchema, fieldSchema.getLogicalType).array
    ByteBuffer.wrap(bytes)
  }
  
  def maybeBigDecimalToBytes(fieldName: String, schema: Schema, bd: BigDecimal): Option[ByteBuffer] = {
    schema.getField(fieldName).schema.getTypes.asScala.toList
      .find(candidateSchema => candidateSchema.getType == Schema.Type.BYTES)
      .map(decimalSchema => bigDecimalToBytes(bd,decimalSchema))
  }

  def toDecimalTag[SP](bigDecimal: BigDecimal): BigDecimal @@ SP =
    shapeless.tag[SP][BigDecimal](bigDecimal)

  "A case class with `logicalType` fields and default values from .avdl" should {
    "deserialize correctly" in {
      val record1 = LogicalIdl(toDecimalTag(BigDecimal(10.6)), Some(toDecimalTag(BigDecimal(10.6))))
      val record2 = LogicalIdl(toDecimalTag(BigDecimal(14.6)), Some(toDecimalTag(BigDecimal(10.6))))
      val schemaFile = new java.io.File("src/main/avro/DecimalsTagged.avdl")
      val schema = (new Idl(schemaFile)).CompilationUnit().getType("example.idl.LogicalIdl")
      val genericRecord1 = new GenericData.Record(schema)
      val genericRecord2 = new GenericData.Record(schema)
      genericRecord1.put("dec", bigDecimalToBytes(record1.dec, schema.getField("dec").schema))
      genericRecord1.put("maybeDec", record1.maybeDec.flatMap(aDec => maybeBigDecimalToBytes("maybeDec", schema, aDec)).get)
      genericRecord1.put("decWithDefault", bigDecimalToBytes(record1.decWithDefault, schema.getField("decWithDefault").schema))
      genericRecord1.put("maybeDec2", record1.maybeDec2.select[(scala.math.BigDecimal @@ (shapeless.Nat._9, shapeless.Nat._2))].flatMap(aDec => maybeBigDecimalToBytes("maybeDec2",schema,aDec)).get)
      genericRecord2.put("dec", bigDecimalToBytes(record2.dec, schema.getField("dec").schema))
      genericRecord2.put("maybeDec", record2.maybeDec.flatMap(aDec => maybeBigDecimalToBytes("maybeDec", schema, aDec)).get)
      genericRecord2.put("decWithDefault", bigDecimalToBytes(record2.decWithDefault, schema.getField("decWithDefault").schema))
      genericRecord2.put("maybeDec2", record2.maybeDec2.select[(scala.math.BigDecimal @@ (shapeless.Nat._9, shapeless.Nat._2))].flatMap(aDec => maybeBigDecimalToBytes("maybeDec2",schema,aDec)).get)
      val records = List(genericRecord1,genericRecord2)
      StandardTestUtil.verifyWriteAndRead(records)
    }
  }

  "A case class with `logicalType` fields, big precision, and explicit values from .avdl" should {
    "deserialize correctly" in {
      val record1 = LogicalIdlBigPrecision(toDecimalTag(BigDecimal(10.6)), Some(toDecimalTag(BigDecimal(10.6))))
      val record2 = LogicalIdlBigPrecision(toDecimalTag(BigDecimal(14.6)), Some(toDecimalTag(BigDecimal(10.6))))
      val schemaFile = new java.io.File("src/main/avro/DecimalsTagged.avdl")
      val schema = (new Idl(schemaFile)).CompilationUnit().getType("example.idl.LogicalIdlBigPrecision")
      val genericRecord1 = new GenericData.Record(schema)
      val genericRecord2 = new GenericData.Record(schema)
      genericRecord1.put("dec", bigDecimalToBytes(record1.dec, schema.getField("dec").schema))
      genericRecord1.put("maybeDec", record1.maybeDec.flatMap(aDec => maybeBigDecimalToBytes("maybeDec", schema, aDec)).get)
      genericRecord1.put("decWithDefault", bigDecimalToBytes(record1.decWithDefault, schema.getField("decWithDefault").schema))
      genericRecord1.put("maybeDec2", record1.maybeDec2.select[(scala.math.BigDecimal @@ (shapeless.Nat._9, shapeless.Nat._2))].flatMap(aDec => maybeBigDecimalToBytes("maybeDec2",schema,aDec)).get)
      genericRecord2.put("dec", bigDecimalToBytes(record2.dec, schema.getField("dec").schema))
      genericRecord2.put("maybeDec", record2.maybeDec.flatMap(aDec => maybeBigDecimalToBytes("maybeDec", schema, aDec)).get)
      genericRecord2.put("decWithDefault", bigDecimalToBytes(record2.decWithDefault, schema.getField("decWithDefault").schema))
      genericRecord2.put("maybeDec2", record2.maybeDec2.select[(scala.math.BigDecimal @@ (shapeless.Nat._9, shapeless.Nat._2))].flatMap(aDec => maybeBigDecimalToBytes("maybeDec2",schema,aDec)).get)
      val records = List(genericRecord1,genericRecord2)
      StandardTestUtil.verifyWriteAndRead(records)
    }
  }
  
  "A case class with `logicalType` fields, big precision, big scale, and explicit values from .avdl" should {
    "deserialize correctly" in {
      val record1 = LogicalIdlBigPrecisionAndScale(toDecimalTag(BigDecimal(10.6)), Some(toDecimalTag(BigDecimal(10.6))))
      val record2 = LogicalIdlBigPrecisionAndScale(toDecimalTag(BigDecimal(14.6)), Some(toDecimalTag(BigDecimal(10.6))))
      val schemaFile = new java.io.File("src/main/avro/DecimalsTagged.avdl")
      val schema = (new Idl(schemaFile)).CompilationUnit().getType("example.idl.LogicalIdlBigPrecisionAndScale")
      val genericRecord1 = new GenericData.Record(schema)
      val genericRecord2 = new GenericData.Record(schema)
      genericRecord1.put("dec", bigDecimalToBytes(record1.dec, schema.getField("dec").schema))
      genericRecord1.put("maybeDec", record1.maybeDec.flatMap(aDec => maybeBigDecimalToBytes("maybeDec", schema, aDec)).get)
      genericRecord1.put("decWithDefault", bigDecimalToBytes(record1.decWithDefault, schema.getField("decWithDefault").schema))
      genericRecord1.put("maybeDec2", record1.maybeDec2.select[(scala.math.BigDecimal @@ (shapeless.Nat._9, shapeless.Nat._2))].flatMap(aDec => maybeBigDecimalToBytes("maybeDec2",schema,aDec)).get)
      genericRecord2.put("dec", bigDecimalToBytes(record2.dec, schema.getField("dec").schema))
      genericRecord2.put("maybeDec", record2.maybeDec.flatMap(aDec => maybeBigDecimalToBytes("maybeDec", schema, aDec)).get)
      genericRecord2.put("decWithDefault", bigDecimalToBytes(record2.decWithDefault, schema.getField("decWithDefault").schema))
      genericRecord2.put("maybeDec2", record2.maybeDec2.select[(scala.math.BigDecimal @@ (shapeless.Nat._9, shapeless.Nat._2))].flatMap(aDec => maybeBigDecimalToBytes("maybeDec2",schema,aDec)).get)
      val records = List(genericRecord1,genericRecord2)
      StandardTestUtil.verifyWriteAndRead(records)
    }
  }
  
  "A case class with coproduct `logicalType` fields and default values from .avdl" should {
    "deserialize correctly" in {
      val s = toDecimalTag(BigDecimal(10.6))
      val record1 = LogicalCoproductIdl(Coproduct[@@[scala.math.BigDecimal, (shapeless.Nat._9, shapeless.Nat._2)] :+: String :+: Boolean :+: CNil](shapeless.tag[(shapeless.Nat._9, shapeless.Nat._2)][scala.math.BigDecimal](scala.math.BigDecimal("9999.99"))))
      val record2 = LogicalCoproductIdl(Coproduct[@@[scala.math.BigDecimal, (shapeless.Nat._9, shapeless.Nat._2)] :+: String :+: Boolean :+: CNil](shapeless.tag[(shapeless.Nat._9, shapeless.Nat._2)][scala.math.BigDecimal](scala.math.BigDecimal("9999.99"))))
      val schemaFile = new java.io.File("src/main/avro/logical_coproduct.avdl")
      val schema = (new Idl(schemaFile)).CompilationUnit().getType("example.idl.LogicalCoproductIdl")
      val genericRecord1 = new GenericData.Record(schema)
      val genericRecord2 = new GenericData.Record(schema)
      genericRecord1.put("maybeDec", record1.maybeDec.select[(scala.math.BigDecimal @@ (shapeless.Nat._9, shapeless.Nat._2))].flatMap(aDec => maybeBigDecimalToBytes("maybeDec",schema,aDec)).get)
      genericRecord2.put("maybeDec", record2.maybeDec.select[(scala.math.BigDecimal @@ (shapeless.Nat._9, shapeless.Nat._2))].flatMap(aDec => maybeBigDecimalToBytes("maybeDec",schema,aDec)).get)
      val records = List(genericRecord1,genericRecord2)
      StandardTestUtil.verifyWriteAndRead(records)
    }
  }
  
  "A case class with either `logicalType` fields and default values from .avdl" should {
    "deserialize correctly" in {
      val record1 = LogicalEitherIdl(Left(toDecimalTag(BigDecimal(10.6))))
      val record2 = LogicalEitherIdl(Left(toDecimalTag(BigDecimal(10.6))))
      val schemaFile = new java.io.File("src/main/avro/logical_either.avdl")
      val schema = (new Idl(schemaFile)).CompilationUnit().getType("example.idl.LogicalEitherIdl")
      val genericRecord1 = new GenericData.Record(schema)
      val genericRecord2 = new GenericData.Record(schema)
      genericRecord1.put("maybeDec", record1.maybeDec.swap.toOption.flatMap(aDec => maybeBigDecimalToBytes("maybeDec",schema,aDec)).get)
      genericRecord2.put("maybeDec", record2.maybeDec.swap.toOption.flatMap(aDec => maybeBigDecimalToBytes("maybeDec",schema,aDec)).get)
      val records = List(genericRecord1,genericRecord2)
      StandardTestUtil.verifyWriteAndRead(records)
    }
  }
  
  "A case class with optional `logicalType` fields and default values from .avdl" should {
    "deserialize correctly" in {
      val record1 = LogicalOptionalIdl(Some(toDecimalTag(BigDecimal(10.6))))
      val record2 = LogicalOptionalIdl(Some(toDecimalTag(BigDecimal(10.6))))
      val schemaFile = new java.io.File("src/main/avro/logical_optional.avdl")
      val schema = (new Idl(schemaFile)).CompilationUnit().getType("example.idl.LogicalOptionalIdl")
      val genericRecord1 = new GenericData.Record(schema)
      val genericRecord2 = new GenericData.Record(schema)
      genericRecord1.put("maybeDec", record1.maybeDec.flatMap(aDec => maybeBigDecimalToBytes("maybeDec",schema,aDec)).get)
      genericRecord2.put("maybeDec", record2.maybeDec.flatMap(aDec => maybeBigDecimalToBytes("maybeDec",schema,aDec)).get)
      val records = List(genericRecord1,genericRecord2)
      StandardTestUtil.verifyWriteAndRead(records)
    }
  }

}
