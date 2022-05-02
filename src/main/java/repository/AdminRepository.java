package repository;

import domain.Admin;
import utils.JdbcUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class AdminRepository {
    private final JdbcUtils dbUtils;

    public AdminRepository(Properties props) {
        dbUtils = new JdbcUtils(props);
    }

    public Admin findOne(Integer id) {
        Connection conn = dbUtils.getConnection();
        Admin admin = null;
        try(PreparedStatement preparedStatement = conn.prepareStatement("select * from admins where id = ?")){

            preparedStatement.setInt(1, id);

            try(ResultSet resultSet = preparedStatement.executeQuery()){
                while (resultSet.next()){
                    String username = resultSet.getString("username");
                    String password = resultSet.getString("password");
                    String name = resultSet.getString("name");
                    admin = new Admin(id, username, password, name);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error DB "+e);
        }
        return admin;
    }


    public List<Admin> findAll() {
        Connection conn = dbUtils.getConnection();
        List<Admin> admins = new ArrayList<>();
        try(PreparedStatement preparedStatement = conn.prepareStatement("select * from admins")){
            try(ResultSet resultSet = preparedStatement.executeQuery()){
                while (resultSet.next()){
                    int id = resultSet.getInt("id");
                    String username = resultSet.getString("username");
                    String password = resultSet.getString("password");
                    String name = resultSet.getString("name");
                    Admin admin = new Admin(id,username,password,name);
                    admins.add(admin);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error DB "+e);
        }
        return admins;
    }


    public Admin save(Admin admin) {
        Connection conn = dbUtils.getConnection();
        try(PreparedStatement preparedStatement = conn.prepareStatement("insert into admins(username, password, name) values (?, ?, ?)")){
            preparedStatement.setString(1, admin.getUsername());
            preparedStatement.setString(2, admin.getPassword());
            preparedStatement.setString(3, admin.getName());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error DB "+e);
            return admin;
        }
        return null;
    }


    public void delete(Integer id) {
        //logger.traceEntry();
        Connection conn = dbUtils.getConnection();
        Admin admin =this.findOne(id);
        if(admin!=null) {
            try (PreparedStatement preparedStatement = conn.prepareStatement("delete from admins where id = ?")) {
                preparedStatement.setInt(1, id);
                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                System.err.println("Error DB " + e);
            }
            int maxID=0;
            try (PreparedStatement preparedStatement = conn.prepareStatement("SELECT MAX( id ) as maxid FROM admins")) {
                ResultSet resultSet = preparedStatement.executeQuery();
                if (resultSet.next()) {
                    maxID = resultSet.getInt("maxid") + 1;
                }
            } catch (SQLException e) {
                System.err.println("Error DB " + e);
            }

            try (PreparedStatement preparedStatement = conn.prepareStatement("UPDATE sqlite_sequence SET seq = ? WHERE name = 'admins'; ")) {
                preparedStatement.setInt(1, maxID-1);
                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                System.err.println("Error DB " + e);
            }
        }
    }


    public void update(Admin admin) {
        Connection conn = dbUtils.getConnection();
        try(PreparedStatement preparedStatement = conn.prepareStatement("update admins set username=? , password=?, name=? where id=?")){
            preparedStatement.setString(1, admin.getUsername());
            preparedStatement.setString(2, admin.getPassword());
            preparedStatement.setString(3, admin.getName());
            preparedStatement.setInt(4, admin.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error DB "+e);
        }
    }
}
