package nl.bknopper.activitytrackerdemo;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.http.ResponseEntity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.client.OAuth2RestOperations;
import org.springframework.security.web.csrf.CsrfFilter;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.security.web.csrf.CsrfTokenRepository;
import org.springframework.security.web.csrf.HttpSessionCsrfTokenRepository;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.WebUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.security.Principal;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;


@SpringBootApplication
@EnableOAuth2Sso
@RestController
public class ActivityTrackerDemo extends WebSecurityConfigurerAdapter {

	final static Logger LOG = Logger.getLogger(ActivityTrackerDemo.class);

	@Autowired
	private OAuth2RestOperations oauth2RestTemplate;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
				.antMatcher("/**")
				.authorizeRequests()
				.antMatchers("/", "/login**", "/webjars/**")
				.permitAll()
				.anyRequest()
				.authenticated()
				.and().logout().logoutSuccessUrl("/").permitAll()
				.and().csrf().csrfTokenRepository(csrfTokenRepository())
				.and().addFilterAfter(csrfHeaderFilter(), CsrfFilter.class);
	}

	private OncePerRequestFilter csrfHeaderFilter() {
		return new OncePerRequestFilter() {
			@Override
			protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
											FilterChain filterChain) throws ServletException, IOException {
				CsrfToken csrf = (CsrfToken) request.getAttribute(CsrfToken.class.getName());
				if (csrf != null) {
					Cookie cookie = WebUtils.getCookie(request, "XSRF-TOKEN");
					String token = csrf.getToken();
					if (cookie == null || token != null && !token.equals(cookie.getValue())) {
						cookie = new Cookie("XSRF-TOKEN", token);
						cookie.setPath("/");
						response.addCookie(cookie);
					}
				}
				filterChain.doFilter(request, response);
			}
		};
	}

	private CsrfTokenRepository csrfTokenRepository() {
		HttpSessionCsrfTokenRepository repository = new HttpSessionCsrfTokenRepository();
		repository.setHeaderName("X-XSRF-TOKEN");
		return repository;
	}

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

	public static void main(String[] args) {
		SpringApplication.run(ActivityTrackerDemo.class, args);
	}
}
