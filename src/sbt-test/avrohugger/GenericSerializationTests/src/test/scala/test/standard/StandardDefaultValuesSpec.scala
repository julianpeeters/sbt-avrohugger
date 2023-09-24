import test._
import org.specs2.mutable.Specification
import java.io.File
import scala.collection.mutable.Buffer
import scala.collection.JavaConverters._

import org.apache.avro.file._
import org.apache.avro.generic._
import org.apache.avro._
class StandardDefaultValuesSpec extends Specification {

  "A case class with default values" should {
    "deserialize correctly" in {
      val record = DefaultTest()

      val enumSchemaString = """{"type":"enum","name":"DefaultEnum","symbols":["SPADES","DIAMONDS","CLUBS","HEARTS"]}"""
      val enumSchema = new Schema.Parser().parse(enumSchemaString)
      val genericEnum = new GenericData.EnumSymbol(enumSchema, record.suit.toString)
      
      val embeddedSchemaString = """{"type":"record","name":"Embedded","fields":[{"name":"inner","type":"int"}]}"""
      val embeddedSchema = new Schema.Parser().parse(embeddedSchemaString)
      val embeddedGenericRecord = new GenericData.Record(embeddedSchema)
      embeddedGenericRecord.put("inner", record.embedded.inner)

      val fixedSchemaString = """{"type":"fixed","name":"fix1","size":2}"""
      val fixedSchema = new Schema.Parser().parse(fixedSchemaString)
      val genericFixed = new GenericData.Fixed(fixedSchema, "ÿ".getBytes)

      val recordSchemaString = """{"type":"record","name":"DefaultTest","namespace":"test","fields":[{"name":"suit","type":{"type":"enum","name":"DefaultEnum","symbols":["SPADES","DIAMONDS","CLUBS","HEARTS"]},"default":"SPADES"},{"name":"number","type":"int","default":0},{"name":"str","type":"string","default":"str"},{"name":"optionString","type":["null","string"],"default":null},{"name":"optionStringValue","type":["string","null"],"default":"default"},{"name":"embedded","type":{"type":"record","name":"Embedded","fields":[{"name":"inner","type":"int"}]},"default":{"inner":1}},{"name":"defaultArray","type":{"type":"array","items":"int"},"default":[1,3,4,5]},{"name":"optionalEnum","type":["null","DefaultEnum"],"default":null},{"name":"defaultMap","type":{"type":"map","values":"string"},"default":{"Hello":"world","Merry":"Christmas"}},{"name":"byt","type":"bytes","default":"ÿ"},{"name":"fx", "type":{"type":"fixed","name":"fix1","size":2},"default":"ÿ"}, {"name":"defaultEither","type": ["int", "string"],"default":2}, {"name":"defaultCoproduct","type": ["int", "string", "boolean"],"default":3}]}"""
      val recordSchema = new Schema.Parser().parse(recordSchemaString)
      
      val genericRecord = new GenericData.Record(recordSchema)
      genericRecord.put("suit", genericEnum)
    	genericRecord.put("number", record.number)
    	genericRecord.put("str", record.str)
    	genericRecord.put("optionString", record.optionString.getOrElse(null))
      genericRecord.put("optionStringValue", record.optionStringValue.getOrElse(null))
      genericRecord.put("embedded", embeddedGenericRecord)
      genericRecord.put("defaultArray",record.defaultArray.asJava)
      genericRecord.put("optionalEnum", record.optionalEnum.getOrElse(null))
      genericRecord.put("defaultMap", record.defaultMap.asJava)
      genericRecord.put("byt", java.nio.ByteBuffer.wrap(record.byt))
      genericRecord.put("fx", genericFixed)
      genericRecord.put("defaultEither", record.defaultEither.fold(identity, identity))
      genericRecord.put("defaultCoproduct", record.defaultCoproduct.select[Int].getOrElse(0))
      val records = List(genericRecord)
      
      val fileName = s"${records.head.getClass.getName}"
      val fileEnding = "avro"
      val file = File.createTempFile(fileName, fileEnding)
      file.deleteOnExit()
      StandardTestUtil.write(file, records)

      var dummyRecord = new GenericDatumReader[GenericRecord]
      val schema = new DataFileReader(file, dummyRecord).getSchema
      val userDatumReader = new GenericDatumReader[GenericRecord](schema)
      val dataFileReader = new DataFileReader[GenericRecord](file, userDatumReader)
      // Adapted from: https://github.com/tackley/avrohugger-list-issue/blob/master/src/main/scala/net/tackley/Reader.scala
      // This isn't great scala, but represents how org.apache.avro.mapred.AvroInputFormat
      // (via org.apache.avro.file.DataFileStream) interacts with the StandardDatumReader.
      var sameRecord: GenericRecord = null.asInstanceOf[GenericRecord]
      while (dataFileReader.hasNext) {
        sameRecord = dataFileReader.next(sameRecord)
      }
      dataFileReader.close()

      sameRecord.get("suit").toString === DefaultEnum.SPADES.toString
      sameRecord.get("number") === 0
      sameRecord.get("str").toString === "str"
      sameRecord.get("optionString") === null
      sameRecord.get("optionStringValue").toString === "default"
      sameRecord.get("embedded").asInstanceOf[GenericRecord].get("inner") === 1
      sameRecord.get("defaultArray") === List(1,3,4,5).asJava
      sameRecord.get("optionalEnum") === null
      sameRecord.get("defaultMap").toString === "{Hello=world, Merry=Christmas}"
      sameRecord.get("byt") === java.nio.ByteBuffer.wrap("ÿ".getBytes)
      sameRecord.get("fx").asInstanceOf[GenericData.Fixed].bytes === "ÿ".getBytes
      sameRecord.get("defaultEither") === 2
      sameRecord.get("defaultCoproduct") === 3
    }

    "use full name when the field accessor is the same name as the field type" in {
      val record = example.Room()
      val recordSchemaString = """{"namespace":"example","name": "Room","type":"record","fields":[{"name":"door","default":"NORTH","type":{"name":"door","namespace":"example.types","type":"enum","symbols":["NORTH","SOUTH","EAST","WEST"]}}]}"""
      val recordSchema = new Schema.Parser().parse(recordSchemaString)

      val enumSchemaString = """{"type":"enum","name":"door","symbols":["NORTH","SOUTH","EAST","WEST"]}"""
      val enumSchema = new Schema.Parser().parse(enumSchemaString)
      val genericEnum = new GenericData.EnumSymbol(enumSchema, record.door.toString)
      val genericRecord = new GenericData.Record(recordSchema)
      genericRecord.put("door", genericEnum)
    	
      val records = List(genericRecord)
      
      val fileName = s"${records.head.getClass.getName}"
      val fileEnding = "avro"
      val file = File.createTempFile(fileName, fileEnding)
      file.deleteOnExit()
      StandardTestUtil.write(file, records)

      var dummyRecord = new GenericDatumReader[GenericRecord]
      val schema = new DataFileReader(file, dummyRecord).getSchema
      val userDatumReader = new GenericDatumReader[GenericRecord](schema)
      val dataFileReader = new DataFileReader[GenericRecord](file, userDatumReader)
      // Adapted from: https://github.com/tackley/avrohugger-list-issue/blob/master/src/main/scala/net/tackley/Reader.scala
      // This isn't great scala, but represents how org.apache.avro.mapred.AvroInputFormat
      // (via org.apache.avro.file.DataFileStream) interacts with the StandardDatumReader.
      var sameRecord: GenericRecord = null.asInstanceOf[GenericRecord]
      while (dataFileReader.hasNext) {
        sameRecord = dataFileReader.next(sameRecord)
      }
      dataFileReader.close()

      sameRecord.get("door").toString === example.types.door.NORTH.toString
    }
  }
}
