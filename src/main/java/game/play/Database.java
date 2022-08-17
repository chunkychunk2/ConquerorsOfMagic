package game.play;

import game.targets.GreenMonsters;
import game.targets.Target;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Database {

    /**
     * Метод загружает монстров из базы данных и возвращает список монстров максимального уровня игроков
     *
     * @return список монстров
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public static List<Target> getMonsters() throws SQLException, ClassNotFoundException{
        // monsters Database
        List<Target> monsters = new ArrayList<>();
        String root = "root";
        String password = "password";
        String connectionURL = "jdbc:mysql://localhost:3306/monsters";
        Class.forName("com.mysql.cj.jdbc.Driver");
        try (Connection connection = DriverManager.getConnection(connectionURL, root, password);
             Statement statement = connection.createStatement()) {
            statement.execute("INSERT INTO game SELECT * FROM green_monsters ORDER BY rand()");
            statement.execute("INSERT INTO game SELECT * FROM green_monsters");
            ResultSet resultSet = statement.executeQuery("SELECT * FROM game");
            while (resultSet.next()) {
                Target monster = new GreenMonsters(resultSet.getInt(2), resultSet.getInt(3),
                        resultSet.getBoolean(4), resultSet.getString(5),
                        resultSet.getString(6), resultSet.getString(7),
                        resultSet.getString(8));
                monsters.add(monster);
            }
            statement.executeUpdate("TRUNCATE TABLE game");
            return monsters;
        }
    }
}
