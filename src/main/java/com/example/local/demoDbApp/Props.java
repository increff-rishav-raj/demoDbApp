package com.example.local.demoDbApp;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties
public class Props {
    @Value("${demo.url}")
    private String url;
    @Value("${demo.password}")
    private String password;
    @Value("${auth.baseUrl}")
    private String authBaseUrl;
    @Value("${auth.appToken}")
    private String authAppToken;

    @Value("${rest.max.connection.per.route:100}")
    private int maxConnectionsPerRoute;

    @Value("${rest.max.connection:200}")
    private int maxConnections;
}
