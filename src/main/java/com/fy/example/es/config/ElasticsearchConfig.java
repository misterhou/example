package com.fy.example.es.config;

import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ElasticsearchConfig {

    @Bean
    public RestHighLevelClient restHighLevelClient() {
        CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        credentialsProvider.setCredentials(AuthScope.ANY,
                new UsernamePasswordCredentials("elastic", "123456es"));
        return new RestHighLevelClient(
                RestClient.builder(new HttpHost("192.168.211.138", 9200, "http"))
                        .setHttpClientConfigCallback(
                                httpAsyncClientBuilder ->
                                        httpAsyncClientBuilder.setDefaultCredentialsProvider(credentialsProvider)));
    }
}
