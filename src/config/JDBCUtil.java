package config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.Map;
import javax.swing.JOptionPane;

public class JDBCUtil {

    private static final Map<String, String> JDBC_URL_BY_MCN = new HashMap<>();
  private static final String DEFAULT_URL = buildSqlServerUrl("DESKTOP-0G7OJFQ\\MANHDUNG1", "quanlycuahangdongho");
    private static volatile String currentMcn;

    // credentials (keep as-is for now)
    private static final String DB_USER = "sa";
    private static final String DB_PASS = "cc123123";

      static {
        JDBC_URL_BY_MCN.put("CN1", buildSqlServerUrl("DESKTOP-0G7OJFQ\\MANHDUNG2", "quanlycuahangdongho"));
        JDBC_URL_BY_MCN.put("CN2", buildSqlServerUrl("DESKTOP-0G7OJFQ\\MANHDUNG3", "quanlycuahangdongho"));
        JDBC_URL_BY_MCN.put("CN3", buildSqlServerUrl("DESKTOP-0G7OJFQ\\MANHDUNG4", "quanlycuahangdongho"));
    }
    public static void registerJdbcUrl(String mcn, String jdbcUrl) {
        if (mcn == null || mcn.isBlank() || jdbcUrl == null || jdbcUrl.isBlank()) {
            return;
        }
        String key = mcn.trim().toUpperCase();
        String url = jdbcUrl.trim();
        synchronized (JDBC_URL_BY_MCN) {
            JDBC_URL_BY_MCN.put(key, url);
        }
    }

    public static void setCurrentMcn(String mcn) {
        currentMcn = mcn == null ? null : mcn.trim().toUpperCase();
    }

    public static String getCurrentMcn() {
        return currentMcn;
    }

    public static String buildSqlServerUrl(String instanceName, String databaseName) {
        return "jdbc:sqlserver://" + instanceName
                + ";databaseName=" + databaseName
                + ";encrypt=false;trustServerCertificate=true";
    }

    public static Connection getConnection(String mcn) {
        String key = mcn == null ? null : mcn.trim().toUpperCase();
        String url = key != null && JDBC_URL_BY_MCN.containsKey(key)
                ? JDBC_URL_BY_MCN.get(key)
                : DEFAULT_URL;
        return getConnectionFromPool(url, key);
    }

    public static Connection getConnection() {
        if (currentMcn != null && JDBC_URL_BY_MCN.containsKey(currentMcn)) {
            return getConnectionFromPool(JDBC_URL_BY_MCN.get(currentMcn), currentMcn);
        }
        return getConnectionFromPool(DEFAULT_URL, currentMcn);
    }

    private static Connection getConnectionFromPool(String url, String branch) {
        try {
            String key = branch != null && !branch.isBlank() ? branch : "DEFAULT";
            Connection conn = DriverManager.getConnection(url, DB_USER, DB_PASS);
            String serverInfo = extractServerInfo(url);
            String dbInfo = extractDatabaseName(url);
            String branchInfo = key;
            System.out.println("[DB] OPEN | branch=" + branchInfo + " | server=" + serverInfo + " | db=" + dbInfo + " | thread=" + Thread.currentThread().getName());
            logConnectedServer(conn);
            return conn;
        } catch (Exception e) {
            System.out.println("[DB] ERROR | branch=" + (branch != null ? branch : "DEFAULT") + " | " + e.getMessage());
            JOptionPane.showMessageDialog(null, "Không thể kết nối đến cơ sở dữ liệu !", "Lỗi", JOptionPane.ERROR_MESSAGE);
            return null;
        }
    }

    private static void logConnectedServer(Connection connection) {
        if (connection == null) {
            return;
        }
        String sql = "SELECT @@SERVERNAME AS SERVER_NAME, DB_NAME() AS DB_NAME";
        try (PreparedStatement pst = connection.prepareStatement(sql); ResultSet rs = pst.executeQuery()) {
            if (rs.next()) {
                String serverName = rs.getString("SERVER_NAME");
                String databaseName = rs.getString("DB_NAME");
                System.out.println("[DB] ACTIVE | server=" + serverName + " | db=" + databaseName);
            }
        } catch (Exception e) {
            System.out.println("[DB] WARN | cannot read server identity | " + e.getMessage());
        }
    }

    private static String extractServerInfo(String url) {
        String prefix = "jdbc:sqlserver://";
        if (url == null || !url.startsWith(prefix)) {
            return url;
        }
        String remainder = url.substring(prefix.length());
        int semicolonIndex = remainder.indexOf(';');
        return semicolonIndex >= 0 ? remainder.substring(0, semicolonIndex) : remainder;
    }

    private static String extractDatabaseName(String url) {
        if (url == null) {
            return "";
        }
        String marker = "databaseName=";
        int startIndex = url.indexOf(marker);
        if (startIndex < 0) {
            return "";
        }
        startIndex += marker.length();
        int endIndex = url.indexOf(';', startIndex);
        return endIndex >= 0 ? url.substring(startIndex, endIndex) : url.substring(startIndex);
    }

    public static void closeConnection(Connection c) {
        try {
            if (c != null) {
                c.close();
                System.out.println("[DB] CLOSE | thread=" + Thread.currentThread().getName());
            }
        } catch (Exception e) {
            System.out.println("[DB] WARN | close failed | " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void shutdownPools() {
        System.out.println("[DB] shutdownPools() called; using direct connections, nothing to close.");
    }
}