package es.uvigo.esei.dai.hybridserver.model.dao.html;

import es.uvigo.esei.dai.hybridserver.Configuration;
import es.uvigo.esei.dai.hybridserver.model.entity.html.Document;

import java.sql.DriverManager;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class HTMLDBDAO implements HTMLDAO {

    String url, user, password;


    public HTMLDBDAO(Configuration config) {
        url = config.getDbURL();
        user = config.getDbUser();
        password = config.getDbPassword();

    }

    public Connection connect() {

        try {
            return DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public Document get(String uuid) {
        Document doc = null;

        try (Connection db = connect(); PreparedStatement statement = db.prepareStatement(
                "SELECT * FROM HTML WHERE uuid = ?")) {
            statement.setString(1, uuid);

            try (ResultSet result = statement.executeQuery()) {
                if (result.next()) {
                    doc = new Document(result.getString("uuid"), result.getString("content"));
                }
                db.close();
            } catch (SQLException e) {
                throw new RuntimeException("The page could not be retrieved.");

            }
        } catch (SQLException e) {
            throw new RuntimeException("DB connection error" + e.getMessage());
        }

        return doc;
    }

    @Override
    public void add(String uuid, String content) {

        try (Connection db = connect(); PreparedStatement statement = db.prepareStatement(
                "INSERT INTO HTML (uuid,content) VALUES (?,?)")) {
            statement.setString(1, uuid);
            statement.setString(2, content);

            int rows = statement.executeUpdate();

            if (rows != 1)
                throw new RuntimeException("The page could not be added.");
            db.close();
        } catch (SQLException e) {
            throw new RuntimeException("DB connection error" + e.getMessage());
        }
    }


    @Override
    public void delete(String uuid) {

        try (Connection db = connect(); PreparedStatement statement = db.prepareStatement(
                "DELETE FROM HTML WHERE uuid = ?")) {
            statement.setString(1, uuid);

            int rows = statement.executeUpdate();

            if (rows != 1)
                throw new RuntimeException("The page could not be deleted.");
            db.close();
        } catch (SQLException e) {
            throw new RuntimeException("DB connection error" + e.getMessage());
        }

    }

    @Override
    public List<Document> list() {

        List<Document> documentList = new ArrayList<>();

        try (Connection db = connect(); Statement statement = db.createStatement()) {

            try (ResultSet result = statement.executeQuery("SELECT uuid,content FROM HTML")) {

                while (result.next()) {
                    Document doc = new Document(result.getString("uuid"), result.getString("content"));
                    documentList.add(doc);
                }

                db.close();
            } catch (SQLException e) {
                throw new RuntimeException("The pages could not be retrieved.");
            }

        } catch (SQLException e) {
            throw new RuntimeException("DB connection error" + e.getMessage());
        }

        return documentList;
    }
}
