package helpers;

import game.play.Database;
import game.play.Player;
import game.targets.FrostMage;
import game.targets.Pyromancer;
import game.targets.Target;
import org.junit.jupiter.params.provider.Arguments;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

/**
 * Класс, содержащий входные данные для тестов
 */
public class DataProvider {

    /**
     * Метод содержит данные для теста
     * menuElement - название сервиса в шапке домашней страницы яндекса
     * category - название категории в шапке яндекса маркета
     * subCategory - название категории в боковом меню на странице категории
     * brand - Производитель
     *
     * @return String menuElement, String category, String subCategory, List<String> brand , String maxView
     */
    public static Stream<Arguments> checkBrands() throws SQLException, ClassNotFoundException {

        List<Target> monsters = Database.getMonsters();


        return Stream.of(
                Arguments.of(monsters.get(6),new Player(new FrostMage()), new Player(new Pyromancer()),monsters.get(6).getName()),
                Arguments.of(monsters.get(2),new Player(new FrostMage()), new Player(new Pyromancer()), monsters.get(2).getName())
//                Arguments.of(monsters.get(1)),
//                Arguments.of(monsters.get(2)),
//                Arguments.of(monsters.get(3)),
//                Arguments.of(monsters.get(4)),
//                Arguments.of(monsters.get(5)),
//                Arguments.of(monsters.get(6)),
//                Arguments.of(monsters.get(7)),
//                Arguments.of(monsters.get(8)),
//                Arguments.of(monsters.get(9))
              );
    }
}
