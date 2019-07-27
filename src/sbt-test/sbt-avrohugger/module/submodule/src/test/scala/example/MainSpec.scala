package example

import org.specs2.mutable.Specification
import org.specs2.ScalaCheck

class MainSpec extends Specification with ScalaCheck {
  "foo" should {
    "bar" in {
      1 must_== 1
    }
  }
}