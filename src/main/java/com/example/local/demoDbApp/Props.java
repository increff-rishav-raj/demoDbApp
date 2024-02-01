package com.example.local.demoDbApp;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties()
public class Props {
    @Value("${demo.url:URL}")
    private String url;
    @Value("${demo.password:PASS}")
    private String password;
    @Value("${auth.baseUrl:URL}")
    private String authBaseUrl;
    @Value("${auth.appToken:TOKEN}")
    private String authAppToken;

    @Value("${rest.max.connection.per.route:100}")
    private int maxConnectionsPerRoute;

    @Value("${rest.max.connection:200}")
    private int maxConnections;
}
