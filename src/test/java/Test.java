
import game.markers.Marker;
import game.play.Player;
import game.targets.Target;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * Класс содержит тесты по тесткейсу Задание 2.1 Selenide
 */
public class Test {

    /**
     * Поле, где размещаются игроки и монстр
     */
    static List<Target> field = new ArrayList<>();

    /**
     * Список игроков
     */
    static List<Player> players = new ArrayList<>();

    /**
     * Метод вызывает атаку монстра с рывком
     */
    public static void dashAttack() {
        System.out.print("Атака монстра с рывком: ");
        if (field.get(0).getHp() > 0) {
            if (!field.get(0).getActiveMarkers().isEmpty()) {
                if (!field.get(0).getActiveMarkers().getLast().equals(Marker.FREEZEMARKER)) {
                    if (field.get(0).isDash()) {
                        System.out.print("Монстр атакует!\n");
                        int positionDamage = 0;
                        for (int i = 1; i < field.size(); i++) {
                            if (i == 1) positionDamage = 1;
                            if (i == field.size() - 1) positionDamage = -1;
                            if (!field.get(i).getActiveMarkers().isEmpty()) {
                                if (field.get(i).getActiveMarkers().getLast().equals(Marker.FREEZEMARKER)) {
                                    field.get(i).getDamage(field.get(0).getDamagePower() - 2 + positionDamage);
                                    field.get(i).removeMarker();
                                }
                            } else field.get(i).getDamage(field.get(0).getDamagePower() + positionDamage);
                        }
                    } else System.out.println("Монстр без рывка");
                } else System.out.println("Монстр заморожен!");
            } else {
                if (field.get(0).isDash()) {
                    System.out.print("Монстр атакует!\n");
                    int positionDamage = 0;
                    for (int i = 1; i < field.size(); i++) {
                        if (i == 1) positionDamage = 1;
                        if (i == field.size() - 1) positionDamage = -1;
                        if (!field.get(i).getActiveMarkers().isEmpty()) {
                            if (field.get(i).getActiveMarkers().getLast().equals(Marker.FREEZEMARKER)) {
                                field.get(i).getDamage(field.get(0).getDamagePower() - 2 + positionDamage);
                                field.get(i).removeMarker();
                            }
                        } else field.get(i).getDamage(field.get(0).getDamagePower() + positionDamage);
                    }
                } else System.out.println("Монстр без рывка");
            }
        } else System.out.println("Монстр мертв");
    }

    public static void monsterAttack() {
        System.out.print("Атака монстра без рывка: ");
        if (field.get(0).getHp() > 0) {
            if (!field.get(0).getActiveMarkers().isEmpty()) {
                if (!field.get(0).getActiveMarkers().getLast().equals(Marker.FREEZEMARKER)) {
                    if (!field.get(0).isDash()) {
                        System.out.print("Монстр атакует!\n");
                        int positionDamage = 0;
                        for (int i = 1; i < field.size(); i++) {
                            if (i == 1) positionDamage = 1;
                            if (i == field.size() - 1) positionDamage = -1;
                            if (!field.get(i).getActiveMarkers().isEmpty()) {
                                if (field.get(i).getActiveMarkers().getLast().equals(Marker.FREEZEMARKER)) {
                                    field.get(i).getDamage(field.get(0).getDamagePower() - 2 + positionDamage);
                                    field.get(i).removeMarker();
                                }
                            } else field.get(i).getDamage(field.get(0).getDamagePower() + positionDamage);
                        }
                    } else System.out.println("Монстр с рывком");
                } else System.out.println("Монстр заморожен!");
            } else {
                if (!field.get(0).isDash()) {
                    System.out.print("Монстр атакует!\n");
                    int positionDamage = 0;
                    for (int i = 1; i < field.size(); i++) {
                        if (i == 1) positionDamage = 1;
                        if (i == field.size() - 1) positionDamage = -1;
                        if (!field.get(i).getActiveMarkers().isEmpty()) {
                            if (field.get(i).getActiveMarkers().getLast().equals(Marker.FREEZEMARKER)) {
                                field.get(i).getDamage(field.get(0).getDamagePower() - 2 + positionDamage);
                                field.get(i).removeMarker();
                            }
                        } else field.get(i).getDamage(field.get(0).getDamagePower() + positionDamage);
                    }
                } else System.out.println("Монстр с рывком");
            }

        } else System.out.println("Монстр мертв");
    }

    @DisplayName("Проверка, что ")
    @ParameterizedTest(name = "{displayName} {3} успешно нанес урон")
    @MethodSource({"helpers.DataProvider#checkBrands"})
    public void gameTests(Target monster, Player p1, Player p2, String name) throws IOException {
        field.add(monster);
        field.add(p1.getTarget());
        field.add(p2.getTarget());
        players.add(p1);
        players.add(p2);
        dashAttack();
        if (monster.isDash()){
            Assertions.assertEquals(10 - monster.getDamagePower() - 1, p1.getTarget().getHp(),
                    "Урон отличается, должно быть: " + p1.getTarget().getHp() + ", а результат: " + (10 - monster.getDamagePower() + 1));
            if (monster.getDamagePower()>1) {
                Assertions.assertEquals(10 - monster.getDamagePower() + 1, p2.getTarget().getHp(),
                        "Урон отличается, должно быть: " + p2.getTarget().getHp() + ", а результат: " + (10 - monster.getDamagePower() - 1));
            }
            else {
                Assertions.assertEquals(10 - monster.getDamagePower(), p2.getTarget().getHp(),
                        "Урон отличается, должно быть: " + p2.getTarget().getHp() + ", а результат: " + (10 - monster.getDamagePower() - 1));
            }
        }
        // Размещение карты
        for (Player player : players) {
            // p1.placeSpell(p1.spellCreation(), field.get(p1.targetSelection(field)),p1.directionSelection(),p1.cardLocation(field,1));
            if (!player.getTarget().getActiveMarkers().isEmpty()) {
                if (!player.getTarget().getActiveMarkers().getLast().equals(Marker.FREEZEMARKER))
                    player.placeSpell(player.spellCreation(), field.get(player.targetSelection(field)), player.directionSelection(), player.cardLocation(field, 1));
                else {
                    player.discardAll(field);
                    System.out.println(player + " заморожен и пропускает ход!");
                }
            } else
                player.placeSpell(player.spellCreation(), field.get(player.targetSelection(field)), player.directionSelection(), player.cardLocation(field, 1));
        }

        players.clear();
        field.clear();
    }
}
