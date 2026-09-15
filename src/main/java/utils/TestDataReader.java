package utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import config.ConfigReader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;

public class TestDataReader {

    private static final Logger log = LogManager.getLogger(TestDataReader.class);
    private static JsonNode root;

    static {
        try {
            String path = ConfigReader.get("testdata.path");
            root = new ObjectMapper().readTree(new File(path));
            log.info("Test data loaded from: {}", path);
        } catch (IOException e) {
            log.error("Failed to load test data JSON", e);
            throw new RuntimeException("Test data file not found or invalid", e);
        }
    }

    private TestDataReader() {}

    public static String get(String key) {
        JsonNode node = root.get(key);
        if (node == null) throw new RuntimeException("Missing test data key: " + key);
        return node.asText().trim();
    }
}
