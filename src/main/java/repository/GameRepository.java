package repository;

import domain.Game;
import utils.JdbcUtils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class GameRepository {
    private final JdbcUtils dbUtils;

    public GameRepository(Properties props) {
        dbUtils = new JdbcUtils(props);
    }

    public Game findOne(Integer id) {
        Connection conn = dbUtils.getConnection();
        Game game = null;
        try(PreparedStatement preparedStatement = conn.prepareStatement("select * from games where id = ?")){

            preparedStatement.setInt(1, id);

            try(ResultSet resultSet = preparedStatement.executeQuery()){
                while (resultSet.next()){
                    String name = resultSet.getString("name");
                    Float price = resultSet.getFloat("price");
                    game = new Game(id, name, price);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error DB "+e);
        }
        return game;
    }


    public List<Game> findAll() {
        Connection conn = dbUtils.getConnection();
        List<Game> games = new ArrayList<>();
        try(PreparedStatement preparedStatement = conn.prepareStatement("select * from games")){
            try(ResultSet resultSet = preparedStatement.executeQuery()){
                while (resultSet.next()){
                    int id = resultSet.getInt("id");
                    String name = resultSet.getString("name");
                    Float price = resultSet.getFloat("price");
                    Game game = new Game(id, name, price);
                    games.add(game);
                }
            }
        } catch (SQLException e) {
            System.err.println("Error DB "+e);
        }
        return games;
    }


    public Game save(Game Game) {
        Connection conn = dbUtils.getConnection();
        try(PreparedStatement preparedStatement = conn.prepareStatement("insert into games(name, price) values (?, ?)")){
            preparedStatement.setString(1, Game.getName());
            preparedStatement.setFloat(2, Game.getPrice());

            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error DB "+e);
            return Game;
        }
        return null;
    }


    public void delete(Integer id) {
        Connection conn = dbUtils.getConnection();
        Game Game =this.findOne(id);
        if(Game!=null) {
            try (PreparedStatement preparedStatement = conn.prepareStatement("delete from games where id = ?")) {
                preparedStatement.setInt(1, id);
                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                System.err.println("Error DB " + e);
            }
            int maxID=0;
            try (PreparedStatement preparedStatement = conn.prepareStatement("SELECT MAX( id ) as maxid FROM games")) {
                ResultSet resultSet = preparedStatement.executeQuery();
                if (resultSet.next()) {
                    maxID = resultSet.getInt("maxid") + 1;
                }
            } catch (SQLException e) {
                System.err.println("Error DB " + e);
            }

            try (PreparedStatement preparedStatement = conn.prepareStatement("UPDATE sqlite_sequence SET seq = ? WHERE name = 'games'; ")) {
                preparedStatement.setInt(1, maxID-1);
                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                System.err.println("Error DB " + e);
            }
        }
    }


    public void update(Game game) {
        Connection conn = dbUtils.getConnection();
        try(PreparedStatement preparedStatement = conn.prepareStatement("update games set name=?, price=?  where id=?")){
            preparedStatement.setString(1, game.getName());
            preparedStatement.setFloat(2, game.getPrice());
            preparedStatement.setInt(3, game.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            System.err.println("Error DB "+e);
        }
    }
}
