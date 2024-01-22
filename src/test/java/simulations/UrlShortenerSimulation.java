package simulations;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.gatling.core.json.JsonParsers;
import io.gatling.javaapi.core.ScenarioBuilder;
import io.gatling.javaapi.core.Simulation;
import io.gatling.javaapi.http.HttpProtocolBuilder;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.*;

public class UrlShortenerSimulation extends Simulation {

    HttpProtocolBuilder httpProtocol = http
            .baseUrl("http://localhost:8080")
            .userAgentHeader("Gatling Load Test");

    ScenarioBuilder scnPublicShortener = scenario("Anonymous User | Shorten a URL and maybe access it.")
            .feed(tsv("user-files/url-100.tsv").random())
            .exec(
                    http("reducto")
                            .post("/api/reducto")
                            .header("Content-Type", "application/json")
                            .body(StringBody("#{payload}"))
                            .check(status().in(201, 422, 400))
                            .check(status().saveAs("httpStatus"))
                            .checkIf(session -> "201".equals(session.getString("httpStatus")))
                            .then(jsonPath("$.shortenedUrl").saveAs("location"))
            )
            .pause(Duration.ofMillis(1), Duration.ofMillis(30))
            .doIf(session -> session.contains("location")).then(
                    exec(http("access")
                            .get("#{location}")
                            .check(status().is(307))
                            .disableFollowRedirect())
            );

    ScenarioBuilder scnLoggedShortener = scenario("Logged User | Shorten a URL and maybe access it.")
            .feed(csv("user-files/users.csv").random())
            .feed(tsv("user-files/url-100.tsv").transform((key, value) -> {
                JsonNode jsonNode = new JsonParsers().parse(value);
                if (jsonNode.hasNonNull("expiresIn")) {
                    int chronoIndex = (int) Math.floor(Math.random() * (7 - 4 + 1) + 4);
                    int random = (int) Math.floor(Math.random() * 24);
                    ChronoUnit chronoUnit = ChronoUnit.values()[chronoIndex];

                    ((ObjectNode) jsonNode).put("expiresIn", LocalDateTime.now().plus(random, chronoUnit).toString());
                }
                return jsonNode.toString();
            }).random())
            .exec(
                    http("shortener")
                            .post("/api/shortener")
                            .header("Content-Type", "application/json")
                            .basicAuth("#{username}", "#{password}")
                            .body(StringBody("#{payload}"))
                            .check(status().in(201))
                            .check(status().saveAs("httpStatus"))
                            .checkIf(session -> "201".equals(session.getString("httpStatus")))
                            .then(jsonPath("$.shortenedUrl").saveAs("location"))
            )
            .exec(addCookie(Cookie("JSESSIONID", CookieKey("JSESSIONID").toString())))
            .pause(Duration.ofMillis(5), Duration.ofMillis(40))
            .doIf(session -> session.contains("location")).then(
                    exec(http("access")
                            .get("#{location}")
                            .check(status().is(307))
                            .disableFollowRedirect())
            );

//    ScenarioBuilder scnAccess = scenario("Access URLs Shortened");

    {
        setUp(
               scnPublicShortener.injectOpen(
                       atOnceUsers(5),
                       constantUsersPerSec(2).during(10),
                       constantUsersPerSec(5).during(15),
                       rampUsersPerSec(1).to(300).during(Duration.ofMinutes(3)).randomized()
               ),
                scnLoggedShortener.injectOpen(
                        atOnceUsers(10)
                        ,
                        constantUsersPerSec(1).during(10),
                        constantUsersPerSec(3).during(15)
                       ,
                       rampUsersPerSec(2).to(100).during(Duration.ofMinutes(3)).randomized()
                )
        ).protocols(httpProtocol);
    }
}
