package es.uvigo.esei.dai.hybridserver.model.dao;

import es.uvigo.esei.dai.hybridserver.model.entity.Document;


import java.sql.*;
import java.util.List;
import java.util.Properties;

public class HTMLDBDAO implements HTMLDAO {

    String url, user, password;


    public HTMLDBDAO(Properties properties) {
        url = properties.getProperty("db.url");
        user = properties.getProperty("db.user");
        password = properties.getProperty("db.password");

    }

    @Override
    public Document get(String uuid) {
        try (Connection connection = DriverManager.getConnection(url, user, password)) {

            try (PreparedStatement statement = connection.prepareStatement(
                    "SELECT * FROM HTML WHERE uuid=?")) {
                statement.setString(1, uuid);
                ResultSet result = statement.executeQuery();

                if (result.next()) {
                    Document doc = new Document(result.getString("id"), result.getString("content"));
                    return doc;
                } else {
                    throw new RuntimeException("Error recuperando la página");
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Document> list() {

        final List<Document> doc_list = this.list();

        try (Connection connection = DriverManager.getConnection(url, user, password)) {

            try (Statement statement = connection.createStatement()) {

                try (ResultSet result = statement.executeQuery("SELECT * FROM HTML")) {

                    while (result.next()) {
                        Document doc = new Document(result.getString("id"), result.getString("content"));
                        doc_list.add(doc);
                    }

                    return doc_list;
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void add(String uuid, String content) {
        try (Connection connection = DriverManager.getConnection(url, user, password)) {

            try (PreparedStatement statement = connection.prepareStatement(
                    "INSERT INTO HTML (uuid,content) VALUES (?,?)")) {
                statement.setString(1, uuid);
                statement.setString(1, uuid);

                int rows = statement.executeUpdate();

                if (rows != 1) {
                    throw new RuntimeException("Error insertando empleado");
                }
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void delete(String uuid) {
        try (Connection connection = DriverManager.getConnection(url, user, password)) {

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }


}
