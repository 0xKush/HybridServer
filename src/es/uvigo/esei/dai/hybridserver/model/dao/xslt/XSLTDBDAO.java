package es.uvigo.esei.dai.hybridserver.model.dao.xslt;

import es.uvigo.esei.dai.hybridserver.model.entity.xslt.XSLT;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class XSLTDBDAO implements XSLTDAO {

    String url, user, password;


    public XSLTDBDAO(Properties properties) {
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
    public XSLT get(String uuid) {
        XSLT xslt = null;

        try (Connection db = connect(); PreparedStatement statement = db.prepareStatement(
                "SELECT uuid,content,xsd FROM XSLT WHERE uuid = ?")) {
            statement.setString(1, uuid);

            try (ResultSet result = statement.executeQuery()) {
                if (result.next()) {
                    xslt = new XSLT(result.getString("uuid"), result.getString("content"), result.getString("xsd"));
                }
            } catch (SQLException e) {
                throw new RuntimeException("The XSLT could not be retrieved.");

            }
        } catch (SQLException e) {
            throw new RuntimeException("DB connection error" + e.getMessage());
        }
        return xslt;
    }

    @Override
    public void add(String uuid, String content, String xsd) {
        try (Connection db = connect(); PreparedStatement statement = db.prepareStatement(
                "INSERT INTO XSLT (uuid,content,xsd) VALUES (?,?,?)")) {
            statement.setString(1, uuid);
            statement.setString(2, content);
            statement.setString(3, xsd);

            int rows = statement.executeUpdate();

            if (rows != 1)
                throw new RuntimeException("The XSLT could not be added.");
        } catch (SQLException e) {
            throw new RuntimeException("DB connection error" + e.getMessage());
        }
    }

    @Override
    public void delete(String uuid) {
        try (Connection db = connect(); PreparedStatement statement = db.prepareStatement(
                "DELETE FROM XSLT WHERE uuid = ?")) {
            statement.setString(1, uuid);

            int rows = statement.executeUpdate();

            if (rows != 1)
                throw new RuntimeException("The XSLT could not be deleted.");
        } catch (SQLException e) {
            throw new RuntimeException("DB connection error" + e.getMessage());
        }
    }

    @Override
    public List<XSLT> list() {
        List<XSLT> xsltList = new ArrayList<>();

        try (Connection db = connect(); Statement statement = db.createStatement()) {

            try (ResultSet result = statement.executeQuery("SELECT uuid,content,xsd FROM XSLT")) {

                while (result.next()) {
                    XSLT xslt = new XSLT(result.getString("uuid"), result.getString("content"), result.getString("xsd"));
                    xsltList.add(xslt);
                }

            } catch (SQLException e) {
                throw new RuntimeException("The XSLTs could not be retrieved.");
            }

        } catch (SQLException e) {
            throw new RuntimeException("DB connection error" + e.getMessage());
        }
        return xsltList;
    }
}
