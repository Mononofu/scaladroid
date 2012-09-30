import org.furidamu.scaladroid
import org.scalatest.matchers.ShouldMatchers
import org.scalatest.FlatSpec

import scaladroid.JSON._
import scaladroid.JSONException

class JSONSpec extends FlatSpec with ShouldMatchers {
  val json = parseJSON("""{"name":"John","age":5}""")


  "json element access" should "return the expected values" in {
    json.name.toString should equal ("John")
    json.age.toInt should equal (5)
  }

  "json access" should "throw execptions when accessing invalid fields" in {
    evaluating { json.kids(5) } should produce [JSONException]
  }

  "makeJSON" should "convert data structures to JSON" in {
    makeJSON(List(1, 2, 3)) should equal ("[1,2,3]")
    makeJSON(Map("name" -> "tom", "devices" -> List(43, 23))) should equal ("""{"name":"tom","devices":[43,23]}""")
  }


}
