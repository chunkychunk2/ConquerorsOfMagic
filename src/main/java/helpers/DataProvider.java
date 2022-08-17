package helpers;

import game.play.Database;
import game.play.Player;
import game.targets.FrostMage;
import game.targets.Pyromancer;
import game.targets.Target;
import org.junit.jupiter.params.provider.Arguments;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
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

        List<Player> twoPlayers = new ArrayList<>(Arrays.asList(new Player(new FrostMage()), new Player(new Pyromancer())));
        List<String> twoPlayersMoveDecision = new ArrayList<>(Arrays.asList("Нет", "Да"));
        List<Integer> twoPlayersFirstSpellSelections = new ArrayList<>(Arrays.asList(0,4,1,2));
        List<Integer> twoPlayersFirstTargetSelections = new ArrayList<>(Arrays.asList(1,1,0,0));
        List<String> twoPlayersFirstDirectionSelections = new ArrayList<>(Arrays.asList("На себя", "На цель", "На цель", "На цель"));
        List<String> twoPlayersFirstSpellCastingDecision = new ArrayList<>(Arrays.asList("Да", "Да", "Нет", "Нет"));

        List<Integer> twoPlayersSecondSpellSelections = new ArrayList<>(Arrays.asList(1,4,1,1));
        List<Integer> twoPlayersSecondTargetSelections = new ArrayList<>(Arrays.asList(1,0,1,0));
        List<String> twoPlayersSecondDirectionSelections = new ArrayList<>(Arrays.asList("На себя", "На цель", "На себя", "На цель"));
        List<String> twoPlayersSecondSpellCastingDecision = new ArrayList<>(Arrays.asList("Да", "Да"));
        List<Integer> twoPlayersSpellChoice = new ArrayList<>(Arrays.asList(1,2,1,2));


        return Stream.of(
                Arguments.of(monsters, twoPlayers, twoPlayersMoveDecision,
                        twoPlayersFirstSpellSelections, twoPlayersFirstTargetSelections,
                        twoPlayersFirstDirectionSelections,twoPlayersFirstSpellCastingDecision,
                        twoPlayersSecondSpellSelections,twoPlayersSecondTargetSelections,twoPlayersSecondDirectionSelections,
                        twoPlayersSecondSpellCastingDecision,twoPlayersSpellChoice)
        );
    }
}
