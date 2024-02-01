package com.example.local.demoDbApp;

import com.increff.account.client.AuthClient;
import com.increff.commons.springboot.common.JsonUtil;
import com.increff.commons.springboot.server.VaultPropertyLocator;
import jakarta.annotation.PostConstruct;
import lombok.extern.log4j.Log4j2;
import org.apache.hc.client5.http.ConnectionKeepAliveStrategy;
import org.apache.hc.client5.http.classic.HttpClient;
import org.apache.hc.client5.http.impl.classic.HttpClients;
import org.apache.hc.client5.http.impl.io.PoolingHttpClientConnectionManager;
import org.apache.hc.core5.http.HeaderElement;
import org.apache.hc.core5.http.HeaderElements;
import org.apache.hc.core5.http.HttpResponse;
import org.apache.hc.core5.http.io.SocketConfig;
import org.apache.hc.core5.http.message.MessageSupport;
import org.apache.hc.core5.http.protocol.HttpContext;
import org.apache.hc.core5.util.TimeValue;
import org.apache.hc.core5.util.Timeout;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.autoconfigure.security.servlet.UserDetailsServiceAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.io.support.PropertySourceFactory;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.Iterator;

@SpringBootApplication(exclude = SecurityAutoConfiguration.class)
@EnableConfigurationProperties(Props.class)
@Log4j2
@PropertySources({
		@PropertySource("file:./application.properties"),
		@PropertySource(name = "VaultPropertyLocatorName",value = "VaultPropertyLocator", factory = VaultPropertyLocator.class)
})
public class DemoDbAppApplication {

	@Autowired
	private Props props;

	@Autowired
	private DataSource dataSource;


	public static void main(String[] args) {
		SpringApplication.run(DemoDbAppApplication.class, args);
//		LogManager.getLogger("elasticsearch").log(Level.ERROR,"Help me");
	}

//	@Bean
//	public PropertySourceFactory getPropertySourceLocator() {
//		VaultPropertyLocator factory = new VaultPropertyLocator();
//		factory.setPath("kv/data/internal/demoDbApp");
//		return factory;
//	}

	@PostConstruct
	public void printDataSourceProps() throws SQLException {
		System.out.println("DataSource Name :" + JsonUtil.serialize(dataSource.getClass().getName()));
		System.out.println("DataSource Metadata :" + dataSource.getConnection().getMetaData());
	}

	@Bean
	public AuthClient authClient() {
		System.out.println("AuthBase URL: " + props.getAuthBaseUrl());
		System.out.println("Auth App Token: " + props.getAuthAppToken());
		return new AuthClient(props.getAuthBaseUrl(), props.getAuthAppToken(), new RestTemplate(getRequestFactory()));
	}


	private ClientHttpRequestFactory getRequestFactory() {

		/* HttpClient by default uses BasicHttpClientConnectionManager which uses single connection object and hence
		 * is not preferred to be used in case the application is heavy on rest call.
		 * Also we must provide correct value for max total and max per route as the default value for these are
		 * just 20 and 2 respectively. This means at a time only 2 parallel request can be processed for a particular host.
		 * In application like proxy, we do have a lot of parallel requests going for same host and hence this value
		 * should be higher. Also the container tomcat has 200 default number of threads configured. As in proxy all the
		 * requests are supposed to make external calls, we have use these decisively.
		 * Suggested value is 100 for a heavy proxy and 50 for light weight proxy*/
		PoolingHttpClientConnectionManager connManager = new PoolingHttpClientConnectionManager();
		connManager.setDefaultMaxPerRoute(props.getMaxConnectionsPerRoute());
		connManager.setMaxTotal(props.getMaxConnections());
		connManager.setDefaultSocketConfig(
				SocketConfig.custom()
						.setSoTimeout(Timeout.ofSeconds(25)) // Set your desired socket read timeout
						.build()
		);

		final ConnectionKeepAliveStrategy myStrategy = new ConnectionKeepAliveStrategy() {
			@Override
			public TimeValue getKeepAliveDuration(HttpResponse httpResponse, HttpContext httpContext) {
				final Iterator<HeaderElement> it = MessageSupport.iterate(httpResponse, HeaderElements.KEEP_ALIVE);
				final HeaderElement he = it.next();
				final String param = he.getName();
				final String value = he.getValue();
				if (value != null && param.equalsIgnoreCase("timeout")) {
					try {
						return TimeValue.ofSeconds(Long.parseLong(value));
					} catch (final NumberFormatException ignore) {
					}
				}
				return TimeValue.ofSeconds(30);
			}
		};

		HttpClient httpClient = HttpClients.custom().setConnectionManager(connManager).setKeepAliveStrategy(myStrategy).build();

		/* HttpComponentsClientHttpRequestFactory is being used as this gives more flexibility around timeouts.
		 * Also one must be aware that when ever HttpComponentsClientHttpRequestFactory is used, default connection
		 * manager is PoolingHttpClientConnectionManager. And in case the default one is used, the connections configurations
		 * are too small as mentioned above. So when using HttpComponentsClientHttpRequestFactory, one must properly
		 * configure PoolingHttpClientConnectionManager else problems are expected at scale*/
		HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory(httpClient);
		factory.setConnectionRequestTimeout(15 * 1000);
		factory.setConnectTimeout(15 * 1000);

		return factory;
	}
}
