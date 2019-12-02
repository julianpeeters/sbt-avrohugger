import org.specs2.mutable.Specification
import com.sksamuel.avro4s.RecordFormat
import org.apache.avro.generic.GenericData
import scala.collection.JavaConverters._

class StandardNoNamespaceTest extends Specification {

  "A case class with in the default package (i.e. without a namespace)" should {
    "serialize and deserialize correctly" in {
      val record1 = AvroTypeProviderTestNoNamespace(1)
      val record2 = AvroTypeProviderTestNoNamespace(2)
      val format = RecordFormat[AvroTypeProviderTestNoNamespace]
      val genericRecord1 = new GenericData.Record(format.to(record1).getSchema)
      genericRecord1.put("x", record1.x)
      val genericRecord2 = new GenericData.Record(format.to(record2).getSchema)
      genericRecord2.put("x", record2.x)
      val records = List(genericRecord1, genericRecord2)
      test.StandardTestUtil.verifyWriteAndRead(records)
    }
  }

}
