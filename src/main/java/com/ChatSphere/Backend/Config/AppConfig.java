package com.ChatSphere.Backend.Config;

import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.ssl.SSLContextBuilder;
import org.opensearch.client.RestClient;
import org.opensearch.client.json.jackson.JacksonJsonpMapper;
import org.opensearch.client.opensearch.OpenSearchClient;
import org.opensearch.client.transport.OpenSearchTransport;
import org.opensearch.client.transport.rest_client.RestClientTransport;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.s3.S3Client;

import javax.net.ssl.SSLContext;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

@Configuration
public class AppConfig {
    @Value("${OpenSearch.url}")
    String url;

    @Value("${OpenSearch.username}")
    String userName;

    @Value("${OpenSearch.password}")
    String password;


    @Value("${OpenSearch.port}")
    int port;

    @Value("${OpenSearch.protocol}")
    String protocol;

    @Value("${AWS.accessKeyId}")
    String accessKeyId;

    @Value("${AWS.secretAccessKey}")
    String secretAccessKey;

    @Value("${AWS.region}")
    String region;

    @Bean
    CredentialsProvider credentialsProvider() {
        BasicCredentialsProvider basicCredentialsProvider = new BasicCredentialsProvider();
        basicCredentialsProvider.setCredentials(AuthScope.ANY, new UsernamePasswordCredentials(userName, password));
        return basicCredentialsProvider;
    }

    @Bean
    HttpHost httpHost() {
        return new HttpHost(url, port, protocol);
    }

    @Bean
    SSLContext sslContext() throws NoSuchAlgorithmException, KeyManagementException {
        return SSLContextBuilder.create().build();
    }

    @Bean
    public RestClient restClient() {
        return RestClient.builder(
                        new HttpHost(httpHost()))
                .setHttpClientConfigCallback(httpClientBuilder ->
                {
                    try {
                        return httpClientBuilder
                                .setDefaultCredentialsProvider(credentialsProvider())
                                .setSSLContext(sslContext());
                    } catch (NoSuchAlgorithmException | KeyManagementException e) {
                        throw new RuntimeException(e);
                    }
                })
                .build();
    }

    @Bean
    OpenSearchTransport openSearchTransport() {
        return new RestClientTransport(restClient(), new JacksonJsonpMapper());
    }

    @Bean
    public OpenSearchClient openSearchClient() {
        return new OpenSearchClient(openSearchTransport());
    }

    @Bean
    public AwsCredentialsProvider awsCredentialsProvider() {
        return () -> AwsBasicCredentials.create(accessKeyId, secretAccessKey);
    }

    @Bean
    public S3Client s3Client() {
        return S3Client.builder().region(Region.of(region)).credentialsProvider(awsCredentialsProvider()).build();
    }
}
