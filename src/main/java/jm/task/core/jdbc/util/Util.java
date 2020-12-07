package jm.task.core.jdbc.util;

import java.sql.*;
import java.util.Properties;

import jm.task.core.jdbc.model.User;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import org.hibernate.service.ServiceRegistry;

public class Util {

    private static SessionFactory sessionFactory;

    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            try {
                Configuration configuration = new Configuration();

                // Hibernate settings equivalent to hibernate.cfg.xml's properties
                Properties settings = new Properties();
                settings.put(Environment.DRIVER, "com.mysql.cj.jdbc.Driver");
                settings.put(Environment.URL, "jdbc:mysql://localhost:3306/testdb");
                settings.put(Environment.USER, "root");
                settings.put(Environment.PASS, "root1");
                settings.put(Environment.DIALECT, "org.hibernate.dialect.MySQL5Dialect");

                settings.put(Environment.SHOW_SQL, "true");

                settings.put(Environment.CURRENT_SESSION_CONTEXT_CLASS, "thread");

                settings.put(Environment.HBM2DDL_AUTO, "create-drop");

                configuration.setProperties(settings);

                configuration.addAnnotatedClass(User.class);

                ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                        .applySettings(configuration.getProperties()).build();

                sessionFactory = configuration.buildSessionFactory(serviceRegistry);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return sessionFactory;
    }

    static String URL = "jdbc:mysql://localhost:3306/testdb";
    static String USERNAME = "root";
    static String PASSWORD = "root1";

    Connection connection = null;

    public Util() {
        Driver driver = null;
        try {
            driver = new com.mysql.cj.jdbc.Driver();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        try {
            DriverManager.registerDriver(driver);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        try {
            connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

/*        try {
            Driver driver = new com.mysql.cj.jdbc.Driver();
            DriverManager.registerDriver(driver);
        } catch (SQLException e) {
            System.out.println("драйвер не подключился");
        }
        try (Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD); Statement statement = connection.createStatement()) {
            if (!connection.isClosed()) System.out.println("соединение с БД установлено");
        } catch (SQLException e) {
            System.out.println(e);
        }*/
    }

    public Connection getConnection() {
        return connection;
    }
}
