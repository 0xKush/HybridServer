package es.uvigo.esei.dai.hybridserver.model.dao.xml;

import es.uvigo.esei.dai.hybridserver.Configuration;
import es.uvigo.esei.dai.hybridserver.model.entity.xml.XML;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class XMLDBDAO implements XMLDAO {


    String url, user, password;


    public XMLDBDAO(Configuration config) {
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
    public XML get(String uuid) {
        XML xml = null;

        try (Connection db = connect(); PreparedStatement statement = db.prepareStatement(
                "SELECT * FROM XML WHERE uuid = ?")) {
            statement.setString(1, uuid);

            try (ResultSet result = statement.executeQuery()) {
                if (result.next()) {
                    xml = new XML(result.getString("uuid"), result.getString("content"));
                }
                db.close();
            } catch (SQLException e) {
                throw new RuntimeException("The XML could not be retrieved.");

            }
        } catch (SQLException e) {
            throw new RuntimeException("DB connection error" + e.getMessage());
        }
        return xml;
    }

    @Override
    public void add(String uuid, String content) {
        try (Connection db = connect(); PreparedStatement statement = db.prepareStatement(
                "INSERT INTO XML (uuid,content) VALUES (?,?)")) {
            statement.setString(1, uuid);
            statement.setString(2, content);

            int rows = statement.executeUpdate();

            if (rows != 1)
                throw new RuntimeException("The XML could not be added.");
            db.close();
        } catch (SQLException e) {
            throw new RuntimeException("DB connection error" + e.getMessage());
        }
    }

    @Override
    public void delete(String uuid) {
        try (Connection db = connect(); PreparedStatement statement = db.prepareStatement(
                "DELETE FROM XML WHERE uuid = ?")) {
            statement.setString(1, uuid);

            int rows = statement.executeUpdate();

            if (rows != 1)
                throw new RuntimeException("The XML could not be deleted.");
            db.close();
        } catch (SQLException e) {
            throw new RuntimeException("DB connection error" + e.getMessage());
        }
    }

    @Override
    public List<XML> list() {
        List<XML> xmlList = new ArrayList<>();

        try (Connection db = connect(); Statement statement = db.createStatement()) {

            try (ResultSet result = statement.executeQuery("SELECT uuid,content FROM XML")) {

                while (result.next()) {
                    XML xml = new XML(result.getString("uuid"), result.getString("content"));
                    xmlList.add(xml);
                }
                db.close();

            } catch (SQLException e) {
                throw new RuntimeException("The XMLs could not be retrieved.");
            }

        } catch (SQLException e) {
            throw new RuntimeException("DB connection error" + e.getMessage());
        }
        return xmlList;
    }


}
