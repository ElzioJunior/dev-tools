package computerdatabase

import io.gatling.core.Predef._
import io.gatling.http.Predef._
import scala.concurrent.duration._


// Edit this Scala file to make your requests and then put it into gatlingFolder/user-files/simulations/computerdatabase and run ./gatling.sh

class InvoiceDetailsSimulation extends Simulation {
    
    val rampUpTimeSecs = 60
    val testTimeSecs = 60    
    val noOfUsers = 10000
    val minWaitMs = 1000 milliseconds
    val maxWaitMs = 5000 milliseconds
    
    val baseURL = "https://myHostAdress.com"
    val baseName = "webservice-call-greeting"
    val requestName = baseName + "-request"
    val scenarioName = baseName + "-test-performance"
    val URI = "/someApi"
    
    val httpConf = http
        .baseUrl(baseURL)
        .acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8") 
        .acceptLanguageHeader("en-US,en;q=0.5")
        .acceptEncodingHeader("gzip, deflate")
        .userAgentHeader("Mozilla/5.0 (Windows NT 5.1; rv:31.0) Gecko/20100101 Firefox/31.0")
        .headers(Map("accept" -> "application/json",
            "x-access-token" -> "eyJhbGciOiJSUzI1NiIsInppcCI6IkRFRiJ9.eNpsjVsOgyAURPdyv0UEynMR3QPgTUuNlgg2TYx7L03qXz_nTGbODqkUcHCvNRdHqb-l4Jepj8-5Dyt0ULbQ6oLrK0VsGd8ZHJPSCMs0Ex0kX3_AGPYFj5raAu2guOGcoLWaXEaGxGgdiBLayMjQBlTt7r-Xnj63n-qrn1uE4OOEy0j4QHzOcBwfAAAA__8.xD9FshB3ADnOS5iuXy5sbx1sLmqFD2Ekgj0gB90lP7jqJZkU3yvHJsMj0Vw5BnJp50QsJTvRjGMRqQ7de-uoTL2y5R01a7NNdiLtCoWjwJKxwnzhtax-KLbbvl1edgFm65EqUm_o2dfwO2gK0JrjW38HCfulxqhPdUACmv4tLfyhGyZq13Cmt8tVOryk3qRrXS9r8C8JovTJfJ8tEdPKm-Y-EF0VvUeNXI5N7ASlipy3eczFN8w8pF8MO33AuUeZM33ueIOZusblFMGseWJ1lZOHa-LnJOb8dvhHWbvUTk7X2DV8jbOkfIxGsKP4BUQJo06ItYo0fsJ9_-p-dAZqQA")
        )
    
    val scn = scenario(scenarioName)
        .during(testTimeSecs) {
            exec(
                http(requestName)
                 .get(URI)
                 .check(status.is(200))
            ).pause(minWaitMs, maxWaitMs)
        }
    
    setUp(
        scn.inject(rampUsers(noOfUsers) during (rampUpTimeSecs))
    ).protocols(httpConf)
}
