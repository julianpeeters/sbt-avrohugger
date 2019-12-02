package test.standard

import com.sksamuel.avro4s._
import org.specs2.mutable.Specification
import test._
import org.apache.avro.generic.GenericData
import scala.collection.JavaConverters._

class StandardArraySpec extends Specification {

  "A case class with an `array` field" should {
    "serialize and deserialize correctly" in {
      val record1 = ArrayIdl(Seq(1,2,3))
      val record2 = ArrayIdl(Seq(2,3,4))
      val format = RecordFormat[ArrayIdl]
      val genericRecord1 = new GenericData.Record(format.to(record1).getSchema)
      genericRecord1.put("data", record1.data.asJava)
      val genericRecord2 = new GenericData.Record(format.to(record2).getSchema)
      genericRecord2.put("data", record2.data.asJava)
      val records = List(genericRecord1, genericRecord2)
      StandardTestUtil.verifyWriteAndRead(records)
    }
  }


}
