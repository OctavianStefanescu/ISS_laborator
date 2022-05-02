package repository;

import domain.User;
import utils.JdbcUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class UserRepository {

    private final JdbcUtils dbUtils;

    public UserRepository(Properties props) {
        dbUtils = new JdbcUtils(props);
    }

    public User findOne(Integer id) {
        Connection conn = dbUtils.getConnection();
        User user = null;
        try(PreparedStatement preparedStatement = conn.prepareStatement("select * from users where id = ?")){

            preparedStatement.setInt(1, id);

            try(ResultSet resultSet = preparedStatement.executeQuery()){
                while (resultSet.next()){
                    String username = resultSet.getString("username");
                    String password = resultSet.getString("password");
                    String name = resultSet.getString("name");
                    Float currency = resultSet.getFloat("currency");
                    user = new User(id, username, password, name, currency);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error DB "+e);
        }
        return user;
    }

    
    public List<User> findAll() {
        Connection conn = dbUtils.getConnection();
        List<User> users = new ArrayList<>();
        try(PreparedStatement preparedStatement = conn.prepareStatement("select * from users")){
            try(ResultSet resultSet = preparedStatement.executeQuery()){
                while (resultSet.next()){
                    int id = resultSet.getInt("id");
                    String username = resultSet.getString("username");
                    String password = resultSet.getString("password");
                    String name = resultSet.getString("name");
                    Float currency = resultSet.getFloat("currency");
                    User user = new User(id,username,password,name, currency);
                    users.add(user);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error DB "+e);
        }
        return users;
    }

    
    public User save(User user) {
        Connection conn = dbUtils.getConnection();
        try(PreparedStatement preparedStatement = conn.prepareStatement("insert into users(username, password, name, currency) values (?, ?, ?, ?)")){
            preparedStatement.setString(1, user.getUsername());
            preparedStatement.setString(2, user.getPassword());
            preparedStatement.setString(3, user.getName());
            preparedStatement.setFloat(4, user.getCurrency());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error DB "+e);
            return user;
        }
        return null;
    }

    
    public void delete(Integer id) {
        //logger.traceEntry();
        Connection conn = dbUtils.getConnection();
        User user =this.findOne(id);
        if(user!=null) {
            try (PreparedStatement preparedStatement = conn.prepareStatement("delete from users where id = ?")) {
                preparedStatement.setInt(1, id);
                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                System.err.println("Error DB " + e);
            }
            int maxID=0;
            try (PreparedStatement preparedStatement = conn.prepareStatement("SELECT MAX( id ) as maxid FROM users")) {
                ResultSet resultSet = preparedStatement.executeQuery();
                if (resultSet.next()) {
                    maxID = resultSet.getInt("maxid") + 1;
                }
            } catch (SQLException e) {
                System.err.println("Error DB " + e);
            }

            try (PreparedStatement preparedStatement = conn.prepareStatement("UPDATE sqlite_sequence SET seq = ? WHERE name = 'users'; ")) {
                preparedStatement.setInt(1, maxID-1);
                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                System.err.println("Error DB " + e);
            }
        }
    }

    
    public void update(User user) {
        //logger.traceEntry();
        Connection conn = dbUtils.getConnection();
        try(PreparedStatement preparedStatement = conn.prepareStatement("update users set username=? , password=?, name=?, currency=?  where id=?")){
            preparedStatement.setString(1, user.getUsername());
            preparedStatement.setString(2, user.getPassword());
            preparedStatement.setString(3, user.getName());
            preparedStatement.setFloat(4, user.getCurrency());
            preparedStatement.setInt(5, user.getId());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error DB "+e);
        }
    }
}
