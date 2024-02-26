package com.example.local.demoDbApp;

import com.increff.account.client.AuthClient;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

@SpringBootApplication(exclude ={SecurityAutoConfiguration.class})
@EnableConfigurationProperties(Props.class)
@Log4j2
public class DemoDbAppApplication {

	@Autowired
	private Props props;


	public static void main(String[] args) {
		SpringApplication.run(DemoDbAppApplication.class, args);
	}

	@Bean
	public AuthClient authClient() {
		System.out.println("AuthBase URL: " + props.getAuthBaseUrl());
		System.out.println("Auth App Token: " + props.getAuthAppToken());
		System.out.println("Demo URL: " + props.getUrl());
		System.out.println("Auth Password: " + props.getPassword());
		return new AuthClient(props.getAuthBaseUrl(), props.getAuthAppToken());
	}

}
