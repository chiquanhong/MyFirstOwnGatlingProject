package simulations
import io.gatling.core.Predef._
import io.gatling.core.structure.ChainBuilder
import io.gatling.http.Predef._
import scala.language.postfixOps
import scala.concurrent.duration.{FiniteDuration, _}

class ComputerDatabase extends Simulation {

  private def getProperty(propertyName: String, defaultValue: String) = {
    Option(System.getenv(propertyName))
      .orElse(Option(System.getProperty(propertyName)))
      .getOrElse(defaultValue)
  }

  def userCount: Int = getProperty("USERS", "1").toInt
  def userRampUp: Int = getProperty("RAMPUP", "5").toInt
  def rampDuration: FiniteDuration = getProperty("RAMP_DURATION", "20").toInt seconds
  def testDuration: FiniteDuration = getProperty("DURATION", "30").toInt seconds

  before {
    println(s"Running test with ${userCount} users at once")
    println(s"Running test with ${userRampUp} ramped users")
    println(s"Ramping users over ${rampDuration} seconds")
    println(s"Total test duration: ${testDuration} seconds")
  }

  val httpProtocol = http
    .baseUrl("https://computer-database.gatling.io")

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

  //setUp(scn.inject(atOnceUsers(1))).protocols(httpProtocol)
  setUp(scn.inject(atOnceUsers(userCount), rampUsers(userRampUp) during (rampDuration))).protocols(httpProtocol).maxDuration(testDuration)
}
