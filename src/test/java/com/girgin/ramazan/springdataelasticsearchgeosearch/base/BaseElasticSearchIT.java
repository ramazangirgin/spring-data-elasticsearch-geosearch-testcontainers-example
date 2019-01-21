package com.girgin.ramazan.springdataelasticsearchgeosearch.base;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.source.ConfigurationPropertySources;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.testcontainers.elasticsearch.ElasticsearchContainer;

import java.util.stream.Stream;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(initializers = {BaseElasticSearchIT.Initializer.class})
public abstract class BaseElasticSearchIT {

    final protected static ElasticsearchContainer elasticSearchContainer = new ElasticsearchContainer("docker.elastic.co/elasticsearch/elasticsearch:6.5.1");
    @Autowired
    protected ConfigurableApplicationContext configurableApplicationContext;

    @BeforeClass
    public static void abstractBeforeClass() {
        startElasticSearch();
    }

    @AfterClass
    public static void abstractAfterClass() {
        stopElasticSearch();
    }

    private static void startElasticSearch() {
        try {
            if (!elasticSearchContainer.isRunning()) {
                elasticSearchContainer.start();
            }
        } catch (Exception exc) {
            exc.printStackTrace();
        }
    }

    private static void stopElasticSearch() {
        try {
            if (!elasticSearchContainer.isRunning()) {
                elasticSearchContainer.stop();
            }
        } catch (Exception exc) {
            exc.printStackTrace();
        }
    }

    public static class Initializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
        @Override
        public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
            System.setProperty("es.set.netty.runtime.available.processors", "false");
            startElasticSearch();
            TestPropertyValues.of(Stream.<String>builder()
                    .add("spring.data.elasticsearch.cluster-nodes=" + elasticSearchContainer.getTcpHost().getHostName() + ":" + elasticSearchContainer.getTcpHost().getPort())
                    .add("spring.data.elasticsearch.properties.client.transport.ignore_cluster_name=true")
                    .build()).applyTo(configurableApplicationContext);
            ConfigurationPropertySources.attach(configurableApplicationContext.getEnvironment());
        }
    }

}
