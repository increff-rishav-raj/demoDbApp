package com.example.local.demoDbApp;

import co.elastic.clients.elasticsearch.ElasticsearchAsyncClient;
import co.elastic.clients.elasticsearch.core.IndexRequest;
import co.elastic.clients.elasticsearch.core.IndexResponse;
import co.elastic.clients.json.jackson.JacksonJsonpMapper;
import co.elastic.clients.transport.rest_client.RestClientTransport;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.increff.commons.springboot.server.DtoHelper;
import com.increff.commons.springboot.server.VaultConstants;
import lombok.extern.log4j.Log4j2;
import org.apache.http.Header;
import org.apache.http.HttpHost;
import org.apache.http.message.BasicHeader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.*;
import org.apache.logging.log4j.core.appender.AbstractAppender;
import org.apache.logging.log4j.core.config.Property;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.elasticsearch.client.RestClient;

import java.io.Serializable;
import java.io.StringReader;
import java.time.LocalDate;
import java.util.Base64;
import java.util.Objects;

@Plugin(name = "ElkAppender", category = Core.CATEGORY_NAME, elementType = Appender.ELEMENT_TYPE, printObject = true)
public class ElkAppender extends AbstractAppender {

//    private static final Logger log = (Logger) LogManager.getLogger(ElkAppender.class);

    private static String appName, esHost, esUsername, esPassword;
    private static Integer esPort;
    private static ElasticsearchAsyncClient client;

    protected ElkAppender(String name, Filter f) {
        super(name, f, null);
    }


    @PluginFactory
    public static ElkAppender initialize(@PluginAttribute("name") String name, @PluginElement("Filter") Filter f) {
        System.out.println("ElasticSearchAppender initialize");
        setELKConfigFromFile();

        HttpHost httpHost = new HttpHost(esHost, esPort, "http");
        RestClient httpClient = RestClient.builder(httpHost)
                .setDefaultHeaders(new Header[]{new BasicHeader("Authorization", getBasicAuth())})
                .build();
        RestClientTransport transport = new RestClientTransport(httpClient, new JacksonJsonpMapper(new ObjectMapper()));
        client = new ElasticsearchAsyncClient(transport);
        return new ElkAppender(name, f);
    }

    private static String getBasicAuth() {
        String encodedAuth = Base64.getEncoder().encodeToString(String.format("%s:%s", esUsername, esPassword).getBytes());
        return String.format("Basic %s", encodedAuth);
    }

    private static void setELKConfigFromFile() {
        esHost = System.getenv(VaultConstants.ELK_HOST);
        esUsername = System.getenv(VaultConstants.ELK_USERNAME);
        esPassword = System.getenv(VaultConstants.ELK_PASSWORD);
        String elkPort = System.getenv(VaultConstants.ELK_PORT);
        esPort = Objects.isNull(elkPort) ? 9200 : Integer.parseInt(elkPort);
    }

    private static void onResponse(IndexResponse response, Throwable exception) {
        System.out.println("here");
        if (exception != null) {
//            String errorStackTrace = DtoHelper.getErrorStackTraceString(exception);
//            log.error("EsClient:RuntimeException: Unable to connect/send message to ElasticSearch\n" + errorStackTrace);
        } else {
            System.out.println("Correct response");
        }
    }

    @Override
    public void append(LogEvent event) {
        System.out.println("Event Detected");
        System.out.println(event.getMessage().getFormattedMessage());
//        StringReader jsonStringReader = new StringReader(DtoHelper.convertToElasticSearchRequest(event, appName));
//        client.index(i -> {
//            IndexRequest.Builder<Object> document = i.index("demoapp")
//                    .withJson(jsonStringReader);
//            return document;
//        }).whenComplete(ElkAppender::onResponse);

//        IndexRequest request = DtoHelper.convertToElasticSearchRequest(event, appName);
//        log.info("Sending request to ELK: " + event.getLevel() + " " + event.getMessage().getFormattedMessage());
//        client.indexAsync(request, RequestOptions.DEFAULT, actionListener);
    }

}