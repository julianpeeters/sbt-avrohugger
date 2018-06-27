package scavro

import org.specs2.mutable.Specification

import org.oedura.scavro.{AvroReader, AvroWriter}

import scavrotest._

class ScavroArraySpec extends Specification {
  "A generated case class with an `array` field" should {
    "serialize and deserialize correctly" in {

      val filename = "AvroArrayType.avro"

      val record = ArrayIdl(Seq(1,2,3))

      val records = record :: Nil

      // Convert to json
      records.foreach(f => println(f.toJson))

      // Write the avro file
      val writer = AvroWriter[ArrayIdl](filename)
      writer.write(records)

      // Read the avro file and do some processing
      val reader: AvroReader[ArrayIdl] =
        AvroReader[ArrayIdl]
      val sameRecords = reader.read(filename)

      sameRecords must ===(records)

    }
  }
}

