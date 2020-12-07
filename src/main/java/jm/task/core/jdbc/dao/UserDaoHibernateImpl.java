package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;


public class UserDaoHibernateImpl implements UserDao {

    String sqlQueryCreateTable = "CREATE TABLE `testdb`.`users` (\n" +
            "  `id` BIGINT(1) NOT NULL AUTO_INCREMENT,\n" +
            "  `name` VARCHAR(45) NULL,\n" +
            "  `lastName` VARCHAR(45) NULL,\n" +
            "  `age` TINYINT(1) NULL,\n" +
            "  PRIMARY KEY (`id`))\n" +
            "ENGINE = InnoDB\n" +
            "DEFAULT CHARACTER SET = utf8;\n";

    String sqlQueryDeleteTable = "DROP TABLE `users`";

    public UserDaoHibernateImpl() {

    }


    @Override
    public void createUsersTable() {

        Util util = new Util();

        Connection connection = util.getConnection();

        try {

            connection.setAutoCommit(false);

            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SHOW TABLES LIKE 'users'");

            boolean tableExist = false;

            while (resultSet.next()) {
                tableExist = true;
            }

            if (!tableExist) {
                statement.execute(sqlQueryCreateTable);
                System.out.println("В базу данных добавлена таблица users");
                connection.commit();
            } else {
                System.out.println("Таблица users уже есть в базе данных!");
            }

        } catch (SQLException throwables) {
            try {
                connection.rollback();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            throwables.printStackTrace();
        } finally {
            try {
                connection.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }

    @Override
    public void dropUsersTable() {
        Util util = new Util();

        Connection connection = util.getConnection();

        try {
            connection.setAutoCommit(false);
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SHOW TABLES LIKE 'users'");

            boolean tableExist = false;

            while (resultSet.next()) {
                tableExist = true;
            }

            if (tableExist) {
                statement.execute(sqlQueryDeleteTable);
                System.out.println("Из базы данных удалена таблица users");
                connection.commit();
            } else {
                System.out.println("Невозможно удалить таблицу users! Ее нет в базе данных");
            }

        } catch (SQLException throwables) {
            try {
                connection.rollback();
            } catch (SQLException e) {
                e.printStackTrace();
            }
            throwables.printStackTrace();
        } finally {
            try {
                connection.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }

    @Override
    public void saveUser(String name, String lastName, byte age) {

        Transaction transaction = null;

        Session session = Util.getSessionFactory().openSession();

        User user = new User();
        user.setName(name);
        user.setLastName(lastName);
        user.setAge(age);

        // auto close session object
        try {

            // start the transaction
            transaction = session.beginTransaction();

            // save student object
            session.save(user);

            // commit transction
            transaction.commit();
            session.close();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
        }
    }

    @Override
    public void removeUserById(long id) {
        Transaction transaction = null;

        Session session = Util.getSessionFactory().openSession();

        User user = new User();
        user.setId(id);

        // auto close session object
        try {

            // start the transaction
            transaction = session.beginTransaction();

            // save student object
            session.delete(user);

            // commit transction
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
        }
    }

    @Override
    public List<User> getAllUsers() {

        Session session = Util.getSessionFactory().openSession();

        Query q = session.createQuery("From User");

        List<User> resultList = q.list();

        return resultList;
    }

    @Override
    public void cleanUsersTable() {
        Transaction transaction = null;

        Session session = Util.getSessionFactory().openSession();

        // auto close session object
        try {

            // start the transaction
            transaction = session.beginTransaction();

            // save student object
            //session.delete(user);
            session.createQuery("delete from User");
            //session.

            // commit transction
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
        }
    }
}
