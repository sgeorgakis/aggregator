package com.pollfish.consumer;

import com.datastax.driver.core.Cluster;
import com.datastax.driver.core.Session;
import org.apache.cassandra.exceptions.ConfigurationException;
import org.apache.thrift.transport.TTransportException;
import org.cassandraunit.CQLDataLoader;
import org.cassandraunit.dataset.cql.ClassPathCQLDataSet;
import org.cassandraunit.utils.EmbeddedCassandraServerHelper;
import org.junit.BeforeClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.cassandraunit.utils.EmbeddedCassandraServerHelper.getNativeTransportPort;

public class CassandraLoader {

    private static final long CASSANDRA_TIMEOUT = 30000L;
    private static final Logger LOG = LoggerFactory.getLogger(CassandraLoader.class);

    private static boolean started = false;

    @BeforeClass
    public static void startServer() throws InterruptedException, TTransportException, ConfigurationException, IOException, URISyntaxException {
        if (!started) {
            EmbeddedCassandraServerHelper.startEmbeddedCassandra(CASSANDRA_TIMEOUT);
            Cluster cluster = new Cluster.Builder()
                    .addContactPoints("localhost")
                    .withPort(getNativeTransportPort())
                    .withoutMetrics()
                    .build();

            Session session = cluster.connect();
            CQLDataLoader dataLoader = new CQLDataLoader(session);
            applyScripts(dataLoader, "config/cql/", "*.cql");
            started = true;
        }
    }

    private static void applyScripts(CQLDataLoader dataLoader, String cqlDir, String pattern) throws IOException, URISyntaxException {
        URL dirUrl = ClassLoader.getSystemResource(cqlDir);
        if (dirUrl == null) { // protect for empty directory
            LOG.error("dirUrl is null");
            return;
        }

        List<String> scripts = new ArrayList<>();
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(Paths.get(dirUrl.toURI()), pattern)) {
            for (Path entry : stream) {
                scripts.add(entry.getFileName().toString());
            }
        }
        Collections.sort(scripts);

        for (String fileName : scripts) {
            LOG.debug("execute script {}", cqlDir + fileName);
            dataLoader.load(new ClassPathCQLDataSet(cqlDir + fileName, false, false));
        }
    }
}
