package example

import org.apache.avro.SchemaBuilder

object Main extends App {
  override def main(args: Array[String]): Unit = {
    println(SchemaBuilder
     .record("HandshakeRequest").namespace("org.apache.avro.ipc")
     .fields()
       .name("clientHash").`type`().fixed("MD5").size(16).noDefault()
       .name("clientProtocol").`type`().nullable().stringType().noDefault()
     .endRecord().toString)
  }
}