package simulations
import io.gatling.core.Predef._
import io.gatling.core.structure.ChainBuilder
import io.gatling.http.Predef._

class ComputerDatabase extends Simulation {
  val httpProtocol = http
    .baseUrl("https://computer-database.gatling.io")
    .inferHtmlResources(BlackList(""".*\.js""", """.*\.css""", """.*\.gif""", """.*\.jpeg""", """.*\.jpg""", """.*\.ico""", """.*\.woff""", """.*\.woff2""", """.*\.(t|o)tf""", """.*\.png""", """.*detectportal\.firefox\.com.*"""), WhiteList())

  def getAllComputers() = {
    exec(http("get all computers")
      .get("/computers").check(status.is(200)))
      .pause(3)
  }

  def getToCreationPage(): ChainBuilder = {
    exec(http("get to creation page")
      .get("/computers/new").check(status.in(200 to 210)))
      .pause(2)
  }

  def createNewComputer(): ChainBuilder = {
    exec(http("create new computer")
      .post("/computers")
      .check(status.not(404), status.not(500))
      .check(bodyString.saveAs("responseBody"))
      .formParam("name", "TestQuan_" + Math.random())
      .formParam("introduced", "1982-12-21")
      .formParam("discontinued", "1990-01-01")
      .formParam("company", "1"))
  }


  val scn = scenario("ComputerDatabase")
    .exec(getAllComputers()).exec(getToCreationPage()).exec(createNewComputer())

  setUp(scn.inject(atOnceUsers(1))).protocols(httpProtocol)
}
