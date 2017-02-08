package edu.asu.diging.gilesecosystem.freddie.config;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.repository.config.EnableSolrRepositories;

import edu.asu.diging.gilesecosystem.freddie.core.Properties;
import edu.asu.diging.gilesecosystem.util.properties.IPropertiesManager;

@Configuration
@EnableSolrRepositories(
  basePackages = "edu.asu.diging.gilesecosystem.freddie.core.repository",
  namedQueriesLocation = "classpath:solr-named-queries.properties")
@ComponentScan
public class SolrConfig {
    
    @Autowired
    private IPropertiesManager propertiesManager;

    @Bean
    public SolrClient solrClient() {
        return new HttpSolrClient(propertiesManager.getProperty(Properties.SOLR_HOST));
    }
 
    @Bean
    public SolrTemplate solrTemplate(SolrClient client) throws Exception {
        return new SolrTemplate(client, "documents");
    }
}
