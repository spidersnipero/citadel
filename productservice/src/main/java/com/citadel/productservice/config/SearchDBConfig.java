package com.citadel.productservice.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

@Configuration
public class SearchDBConfig {

    private static final Logger log = LoggerFactory.getLogger(SearchDBConfig.class);

    @Bean
    public CommandLineRunner fullTextSearchInitializer(JdbcTemplate jdbcTemplate) {
        return args -> {
            log.info("Initializing Full-Text Search configuration...");

            try {
                // 1. Add a new column to store the search vector.
                log.info("Step 1: Adding 'search_vector' column to products table.");
                jdbcTemplate.execute("ALTER TABLE products ADD COLUMN IF NOT EXISTS search_vector tsvector");

                // 2. Create a GIN index for fast searching on the new column.
                log.info("Step 2: Creating GIN index on 'search_vector'.");
                jdbcTemplate.execute("CREATE INDEX IF NOT EXISTS search_vector_idx ON products USING GIN(search_vector)");

                // 3. Create a trigger function that will automatically update the search_vector column.
                log.info("Step 3: Creating trigger function to auto-update search vector.");
                jdbcTemplate.execute(
                        "CREATE OR REPLACE FUNCTION products_search_vector_update() RETURNS trigger AS $$ " +
                                "BEGIN " +
                                "   new.search_vector := " +
                                "       setweight(to_tsvector('pg_catalog.english', coalesce(new.name,'')), 'A') || " +
                                "       setweight(to_tsvector('pg_catalog.english', coalesce(new.description,'')), 'B'); " +
                                "   return new; " +
                                "END " +
                                "$$ LANGUAGE plpgsql;"
                );

                // 4. Attach the trigger to the products table.
                log.info("Step 4: Creating trigger on products table.");
                jdbcTemplate.execute(
                        "DO $$ " +
                                "BEGIN " +
                                "   IF NOT EXISTS (SELECT 1 FROM pg_trigger WHERE tgname = 'tsvectorupdate') THEN " +
                                "       CREATE TRIGGER tsvectorupdate BEFORE INSERT OR UPDATE " +
                                "       ON products FOR EACH ROW EXECUTE PROCEDURE products_search_vector_update(); " +
                                "   END IF; " +
                                "END; " +
                                "$$;"
                );

                log.info("Full-Text Search setup completed successfully.");

            } catch (Exception e) {
                log.error("Error during Full-Text Search initialization: ", e);
            }
        };
    }
}
