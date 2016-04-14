# activitytrackerdemo
Demo (for IoT Tech Day 2016) of reading data from the fitbit API and showing it in a dashboard.

Slides of the talk can be found here: https://speakerdeck.com/bknopper/iot-tech-day-2016-take-the-next-step-and-unleash-the-full-potential-of-your-activity-tracker

This demo deploys a Spring Boot backend and angularish Frontend (would definitely not code this for production! Demo!!) which uses the Fitbit API to show the data of the person logged in.

For the demo to work you only need to register an app at http://dev.fitbit.com and enter your `client id` en `client secret` in the `src/main/resources/application.properties` file.

Then it's just running the main class `ActivityTrackerDemo` and Spring Boot will do all the magic for you!

Navigate to 'http://localhost:8080/' and you're good to go... (unless you've defined a different port in 'application.properties' :-) ).
