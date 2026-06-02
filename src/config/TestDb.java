package config;

import java.sql.Connection;

public class TestDb {
    public static void main(String[] args) {
        System.out.println("-> Testing DB connection via HikariCP...");
        Connection c = null;
        try {
            c = JDBCUtil.getConnection();
            if (c != null && !c.isClosed()) {
                System.out.println("OK: acquired connection from pool");
            } else {
                System.out.println("FAIL: connection is null or closed");
            }
        } catch (Exception e) {
            System.out.println("ERROR: " + e.getMessage());
            e.printStackTrace();
        } finally {
            JDBCUtil.closeConnection(c);
            JDBCUtil.shutdownPools();
        }
    }
}
