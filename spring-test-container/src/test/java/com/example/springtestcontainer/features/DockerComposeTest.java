package com.example.springtestcontainer.features;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.ClassPathResource;
import org.testcontainers.containers.ContainerState;
import org.testcontainers.containers.DockerComposeContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.io.File;
import java.io.IOException;

@Testcontainers
public class DockerComposeTest {

    @Container
    private static DockerComposeContainer DOCKER_COMPOSE_CONTAINER;
    static {
        try {
            File dockerComposeFile = new ClassPathResource("./docker/docker-compose.yml").getFile();
            DOCKER_COMPOSE_CONTAINER = new DockerComposeContainer(dockerComposeFile)
                    .withExposedService("redis", 6379)
                    .withExposedService("elasticsearch", 9200);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void dockerComposeTest(){

        ContainerState redis = (ContainerState) DOCKER_COMPOSE_CONTAINER.getContainerByServiceName("redis").get();
        ContainerState elasticsearch = (ContainerState) DOCKER_COMPOSE_CONTAINER.getContainerByServiceName("elasticsearch").get();

        Assertions.assertThat(redis.isRunning()).isTrue();
        Assertions.assertThat(elasticsearch.isRunning()).isTrue();

        int redisPort = redis.getMappedPort(6379);
        int elasticsearchPort = elasticsearch.getMappedPort(9200);
        System.out.println("redisPort = " + redisPort);
        System.out.println("elasticsearchPort = " + elasticsearchPort);
    }

}
