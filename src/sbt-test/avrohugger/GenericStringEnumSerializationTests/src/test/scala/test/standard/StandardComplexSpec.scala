import test._
import org.specs2.mutable.Specification
import com.sksamuel.avro4s.RecordFormat
import org.apache.avro.generic.GenericData
import org.apache.avro.generic.GenericData.EnumSymbol
import scala.collection.JavaConverters._

class StandardComplexTest extends Specification {
 skipAll // fails on GenericRecord validation introduced in avro 1.9.1 (needs custom GenericRecord serialization to succeed)
  "A case class with an enumeration field" should {
    "serialize and deserialize correctly" in {
      val record1 = Compass("NORTH")
      val record2 = Compass("SOUTH")
      val format = RecordFormat[Compass]
      val schema1 = format.to(record1).getSchema
      val schema2 = format.to(record2).getSchema
      val genericRecord1 = new GenericData.Record(schema1)
      genericRecord1.put("direction", new EnumSymbol(schema1.getField("direction").schema, record1.direction.toString))
      val genericRecord2 = new GenericData.Record(schema2)
      genericRecord2.put("direction", new EnumSymbol(schema2.getField("direction").schema, record2.direction.toString))
      val records = List(genericRecord1, genericRecord2)
      StandardTestUtil.verifyWriteAndRead(records)
    }
  }

}
