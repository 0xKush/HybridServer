package es.uvigo.esei.dai.hybridserver.model.dao.xsd;

import es.uvigo.esei.dai.hybridserver.Configuration;
import es.uvigo.esei.dai.hybridserver.model.entity.xsd.XSD;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class XSDDBDAO implements XSDDAO {
    String url, user, password;


    public XSDDBDAO(Configuration config) {
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
    public XSD get(String uuid) {
        XSD xsd = null;

        try (Connection db = connect(); PreparedStatement statement = db.prepareStatement(
                "SELECT * FROM XSD WHERE uuid = ?")) {
            statement.setString(1, uuid);

            try (ResultSet result = statement.executeQuery()) {
                if (result.next()) {
                    xsd = new XSD(result.getString("uuid"), result.getString("content"));
                }
            } catch (SQLException e) {
                throw new RuntimeException("The XSD could not be retrieved.");

            }
        } catch (SQLException e) {
            throw new RuntimeException("DB connection error" + e.getMessage());
        }
        return xsd;
    }

    @Override
    public void add(String uuid, String content) {
        try (Connection db = connect(); PreparedStatement statement = db.prepareStatement(
                "INSERT INTO XSD (uuid,content) VALUES (?,?)")) {
            statement.setString(1, uuid);
            statement.setString(2, content);

            int rows = statement.executeUpdate();

            if (rows != 1)
                throw new RuntimeException("The XSD could not be added.");
        } catch (SQLException e) {
            throw new RuntimeException("DB connection error" + e.getMessage());
        }
    }

    @Override
    public void delete(String uuid) {
        try (Connection db = connect(); PreparedStatement statement = db.prepareStatement(
                "DELETE FROM XSD WHERE uuid = ?")) {
            statement.setString(1, uuid);

            int rows = statement.executeUpdate();

            if (rows != 1)
                throw new RuntimeException("The XSD could not be deleted.");
        } catch (SQLException e) {
            throw new RuntimeException("DB connection error" + e.getMessage());
        }
    }

    @Override
    public List<XSD> list() {
        List<XSD> xsdList = new ArrayList<>();

        try (Connection db = connect(); Statement statement = db.createStatement()) {

            try (ResultSet result = statement.executeQuery("SELECT uuid,content FROM XSD")) {

                while (result.next()) {
                    XSD xsd = new XSD(result.getString("uuid"), result.getString("content"));
                    xsdList.add(xsd);
                }

            } catch (SQLException e) {
                throw new RuntimeException("The XSDs could not be retrieved.");
            }

        } catch (SQLException e) {
            throw new RuntimeException("DB connection error" + e.getMessage());
        }
        return xsdList;
    }
}
