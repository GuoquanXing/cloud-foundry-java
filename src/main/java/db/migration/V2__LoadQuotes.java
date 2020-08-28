package db.migration;

import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.PreparedStatement;

import org.flywaydb.core.api.migration.BaseJavaMigration;
import org.flywaydb.core.api.migration.Context;
import org.springframework.util.ResourceUtils;

public class V2__LoadQuotes extends BaseJavaMigration {

    @Override
    public void migrate(Context context) throws Exception {
        URI uri = ResourceUtils.getFile("classpath:fortunes.txt").toURI();
        String contents = new String(Files.readAllBytes(Paths.get(uri)));
        String[] quotes = contents.split("\n~~~~~\n");
        String sql = "INSERT INTO quote (quote) VALUES (?)";
        try (PreparedStatement stmt = context.getConnection().prepareStatement(sql)) {
            for (String quote : quotes) {
                stmt.setString(1, quote);
                stmt.addBatch();
            }
            stmt.executeBatch();
        }
    }

}
