package es.uvigo.esei.dai.hybridserver.model.dao;

import es.uvigo.esei.dai.hybridserver.model.entity.Document;

import java.sql.DriverManager;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class HTMLDBDAO implements HTMLDAO {

    String url, user, password;


    public HTMLDBDAO(Properties properties) {
        url = properties.getProperty("db.url");
        user = properties.getProperty("db.user");
        password = properties.getProperty("db.password");

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

        try (PreparedStatement statement = connect().prepareStatement(
                "SELECT * FROM HTML WHERE uuid = ?")) {
            statement.setString(1, uuid);

            try (ResultSet result = statement.executeQuery()) {
                if (result.next()) {
                    doc = new Document(result.getString("uuid"), result.getString("content"));
                }
            } catch (SQLException e) {
                //throw new RuntimeException("Error recuperando la página");
                e.printStackTrace();
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error recuperando la página" + e.getMessage());
        }
        return doc;
    }

    @Override
    public void add(String uuid, String content) {

        try (PreparedStatement statement = connect().prepareStatement(
                "INSERT INTO HTML (uuid,content) VALUES (?,?)")) {
            statement.setString(1, uuid);
            statement.setString(2, content);

            int rows = statement.executeUpdate();

            if (rows != 1)
                throw new RuntimeException("Error insertando página");
        } catch (SQLException e) {
            throw new RuntimeException("Error insertando página" + e.getMessage());
        }
    }


    @Override
    public void delete(String uuid) {

        try (PreparedStatement statement = connect().prepareStatement(
                "DELETE FROM HTML WHERE uuid = ?")) {
            statement.setString(1, uuid);

            int rows = statement.executeUpdate();

            if (rows != 1)
                throw new RuntimeException("Error eliminando página");
        } catch (SQLException e) {
            throw new RuntimeException("Error eliminando página" + e.getMessage());
        }

    }

    @Override
    public List<Document> list() {

        List<Document> documentList = new ArrayList<>();

        try (Statement statement = connect().createStatement()) {

            try (ResultSet result = statement.executeQuery("SELECT uuid,content FROM HTML")) {

                while (result.next()) {
                    Document doc = new Document(result.getString("uuid"), result.getString("content"));
                    documentList.add(doc);
                }

            } catch (SQLException e) {
                throw new RuntimeException("Error recuperando lista de páginas");

            }

        } catch (SQLException e) {
            throw new RuntimeException("Error recuperando lista de páginas" + e.getMessage());
        }
        return documentList;
    }
}
