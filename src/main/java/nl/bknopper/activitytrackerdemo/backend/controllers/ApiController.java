package nl.bknopper.activitytrackerdemo.backend.controllers;


import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.OAuth2RestOperations;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;

@RestController
public class ApiController {

    final static Logger LOG = Logger.getLogger(ApiController.class);

    @Autowired
    private OAuth2RestOperations oauth2RestTemplate;

    @RequestMapping("/user")
    public Principal user(Principal principal) {
        return principal;
    }


    @RequestMapping("/lifetime")
    public String lifetime() {
        String resourceUrl = "https://api.fitbit.com/1/user/-/activities.json";
        return getFitBitData(resourceUrl);
    }

    @RequestMapping("/summarytoday")
    public String todaysSummary() {
        final String today = getTodayAsString();

        final String resourceUrl = String.format("https://api.fitbit.com/1/user/-/activities/date/%s.json", today);
        return getFitBitData(resourceUrl);
    }

    @RequestMapping("/lastweek/steps")
    public String lastweekSteps() {
        final String today = getTodayAsString();

        final String resourceUrl = String.format("https://api.fitbit.com/1/user/-/activities/tracker/steps/date/%s/7d.json", today);
        return getFitBitData(resourceUrl);
    }

    @RequestMapping("/lastweek/distance")
    public String lastweekDistance() {
        final String today = getTodayAsString();

        final String resourceUrl = String.format("https://api.fitbit.com/1/user/-/activities/tracker/distance/date/%s/7d.json", today);
        return getFitBitData(resourceUrl);
    }

    @RequestMapping("/lastweek/floors")
    public String lastweekFloors() {
        final String today = getTodayAsString();

        final String resourceUrl = String.format("https://api.fitbit.com/1/user/-/activities/tracker/floors/date/%s/7d.json", today);
        return getFitBitData(resourceUrl);
    }

    @RequestMapping("/today/heartrate")
    public String heartRateToday() {
        final String today = getTodayAsString();

        final String resourceUrl = String.format("https://api.fitbit.com/1/user/-/activities/heart/date/%s/1d/1sec/time/00:00/23:59.json", today);
        return getFitBitData(resourceUrl);
    }

    private String getTodayAsString() {
        final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(Date.from(Instant.now()));
    }

    private String getFitBitData(String resourceUrl) {
        LOG.info(String.format("Performing FitBit API REST call for resource %s", resourceUrl));
        ResponseEntity<String> response = oauth2RestTemplate.getForEntity(
                resourceUrl,
                String.class);

        final String result = response.getBody().toString();
        LOG.info(String.format("Result: %s", result));

        return result;
    }
}
