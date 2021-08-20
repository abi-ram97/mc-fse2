package com.academy.config;

import org.socialsignin.spring.data.dynamodb.repository.config.EnableDynamoDBRepositories;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.amazonaws.ClientConfiguration;
import com.amazonaws.ClientConfigurationFactory;
import com.amazonaws.Protocol;
import com.amazonaws.auth.AWSCredentialsProvider;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.client.builder.AwsClientBuilder;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder;

@Configuration
@EnableDynamoDBRepositories(basePackages = "com.academy.repository")
public class AwsDynamoDbConfig {
	
	@Value("${amazon.dynamodb.endpoint}")
	private String endpoint;
	
	@Value("${amazon.aws.secretKey}")
	private String secretKey;
	
	@Value("${amazon.aws.accessKey}")
	private String accessKey;
	
	@Value("${amazon.aws.region}")
	private String region;
	
	public AwsClientBuilder.EndpointConfiguration endpointConfiguration() {
		return new AwsClientBuilder.EndpointConfiguration(endpoint, region);
	}
	
	public AWSCredentialsProvider credentialsProvider() {
		return new AWSStaticCredentialsProvider(new BasicAWSCredentials(accessKey, secretKey));
	}
	
	public ClientConfiguration getClientConfiguration() {
		return new ClientConfigurationFactory().getConfig().withProtocol(Protocol.HTTP);
	}
	
	@Bean
	public AmazonDynamoDB amazonDynamoDB() {
		return AmazonDynamoDBClientBuilder.standard()
				.withCredentials(credentialsProvider())
				.withEndpointConfiguration(endpointConfiguration())
				.withClientConfiguration(getClientConfiguration())
				.build();
	}
}
