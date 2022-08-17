
import game.markers.Marker;
import game.play.Player;
import game.targets.DamageType;
import game.targets.Target;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
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
    static List<Player> players = new ArrayList<>();;

    static List<Target> monstersList = new ArrayList<>();;

    /**
     * Метод вызывает атаку монстра с рывком
     */
    public static void dashAttack() {
        System.out.println("Атака монстра с рывком: ");
        if (field.get(0).getHp() > 0 && field.get(0).isDash()) {
            System.out.print("Монстр атакует!\n");
            for (int i = 1; i < field.size(); i++) {
                int totalDamage = 0;
                if (!field.get(i).getActiveMarkers().isEmpty()) {
                    int positionDamage = 0;
                    if (i == 1) positionDamage = 1;
                    if (i == field.size() - 1) positionDamage = -1;
                    if (field.get(i).getActiveMarkers().getLast().equals(Marker.FREEZEMARKER)) {
                        field.get(i).getDamage(field.get(0).getDamagePower() - 2 + positionDamage);
                        field.get(i).removeMarker();
                    } else if (field.get(i).getActiveMarkers().getLast().equals(Marker.WALLMARKER)) {
                        System.out.println("Преграда поглотила урон");
                        field.get(i).removeMarker();
                    } else if (field.get(0).getdType().equals(DamageType.MAGICAL) && field.get(i).getActiveMarkers().getLast().equals(Marker.REFLECTIONMARKER)) {
                        field.get(i).removeMarker();
                        boolean pingPong = true;
                        // пока в взаимодействующих есть маркер отражения или преграды
                        while (pingPong) {
                            // если у кастера есть маркеры
                            if (!field.get(0).getActiveMarkers().isEmpty()) {
                                // если у кастера есть маркер отражения, то теряется маркер и перенаправляется на цель
                                if (field.get(0).getActiveMarkers().getLast().equals(Marker.REFLECTIONMARKER)) {
                                    field.get(0).removeMarker();
                                    // если у цели есть маркеры
                                    if (!field.get(i).getActiveMarkers().isEmpty()) {
                                        // если у цели есть маркер отражения, то теряется маркер и перенаправляется на кастера
                                        if (field.get(i).getActiveMarkers().getLast().equals(Marker.REFLECTIONMARKER)) {
                                            field.get(i).removeMarker();
                                        }
                                        // если у цели есть маркер преграды, то теряется маркер и отражение поглощается
                                        else if (field.get(i).getActiveMarkers().getLast().equals(Marker.WALLMARKER)) {
                                            field.get(i).removeMarker();
                                            pingPong = false;
                                        } else if (field.get(i).getActiveMarkers().getLast().equals(Marker.FREEZEMARKER)) {
                                            field.get(i).getDamage(field.get(0).getDamagePower() - 2 + positionDamage);
                                            field.get(i).removeMarker();
                                        }
                                        // если у цели есть нет маркера преграды и отражения, то фаербол срабатывает
                                        else {
                                            if (field.get(i).getHp() > 0) {
                                                totalDamage = (field.get(0).getDamagePower() + positionDamage) <= 0 ? 1 : field.get(0).getDamagePower() + positionDamage;
                                                System.out.println("Урон цели");
                                                field.get(i).getDamage(totalDamage);
                                            }
                                            pingPong = false;
                                        }
                                    }
                                    // если у цели есть нет маркеров, то фаербол срабатывает
                                    else {
                                        if (field.get(i).getHp() > 0) {
                                            totalDamage = (field.get(0).getDamagePower() + positionDamage) <= 0 ? 1 : field.get(0).getDamagePower() + positionDamage;
                                            System.out.println("Урон цели");
                                            field.get(i).getDamage(totalDamage);
                                        }
                                        pingPong = false;
                                    }
                                }
                                // если у кастера есть маркер преграды, то теряется маркер и отражение поглощается
                                else if (field.get(0).getActiveMarkers().getLast().equals(Marker.WALLMARKER)) {
                                    field.get(0).removeMarker();
                                    pingPong = false;
                                } else if (field.get(0).getActiveMarkers().getLast().equals(Marker.FREEZEMARKER)) {
                                    field.get(0).getDamage(field.get(0).getDamagePower() - 2 + positionDamage);
                                    field.get(0).removeMarker();
                                }
                                // если у кастера нет маркера, то фаербол срабатывает
                                else {
                                    if (field.get(0).getHp() > 0) {
                                        totalDamage = (field.get(0).getDamagePower() + positionDamage) <= 0 ? 1 : field.get(0).getDamagePower() + positionDamage;
                                        System.out.println("Урон цели");
                                        field.get(0).getDamage(totalDamage);
                                    }
                                    pingPong = false;
                                }
                                // если у кастера нет маркеров, то фаербол срабатывает
                            } else {
                                if (field.get(0).getHp() > 0) {
                                    totalDamage = (field.get(0).getDamagePower() + positionDamage) <= 0 ? 1 : field.get(0).getDamagePower() + positionDamage;
                                    System.out.println("Урон цели");
                                    field.get(0).getDamage(totalDamage);
                                }
                                pingPong = false;
                            }
                        }
                    } else {
                        field.get(i).getDamage(field.get(0).getDamagePower() + positionDamage);
                    }
                } else {
                    int positionDamage = 0;
                    if (i == 1) positionDamage = 1;
                    if (i == field.size() - 1) positionDamage = -1;
                    field.get(i).getDamage(field.get(0).getDamagePower() + positionDamage);
                }
            }
        } else if (field.get(0).isDead()) System.out.println("Монстр мертв!");
        else if (!field.get(0).getActiveMarkers().isEmpty()) {
            if (field.get(0).getActiveMarkers().getLast().equals(Marker.FREEZEMARKER))
                System.out.println("Монстр заморожен!");
        } else System.out.println("Монстр без рывка");
    }

    public static void monsterAttack() {
        System.out.print("Атака монстра без рывка: ");
        if (field.get(0).getHp() > 0 && !field.get(0).isDash()) {

            for (int i = 1; i < field.size(); i++) {
                int totalDamage = 0;
                if (!field.get(i).getActiveMarkers().isEmpty()) {
                    int positionDamage = 0;
                    if (i == 1) positionDamage = 1;
                    if (i == field.size() - 1) positionDamage = -1;
                    if (!field.get(i).getActiveMarkers().isEmpty()) {
                        if (field.get(i).getActiveMarkers().getLast().equals(Marker.FREEZEMARKER)) {
                            field.get(i).getDamage(field.get(0).getDamagePower() - 2 + positionDamage);
                            field.get(i).removeMarker();
                        } else if (field.get(i).getActiveMarkers().getLast().equals(Marker.WALLMARKER)) {
                            System.out.println("Преграда поглотила урон");
                            field.get(i).removeMarker();
                        } else if (field.get(0).getdType().equals(DamageType.MAGICAL) && field.get(i).getActiveMarkers().getLast().equals(Marker.REFLECTIONMARKER)) {
                            field.get(i).removeMarker();
                            boolean pingPong = true;
                            // пока в взаимодействующих есть маркер отражения или преграды
                            while (pingPong) {
                                // если у кастера есть маркеры
                                if (!field.get(0).getActiveMarkers().isEmpty()) {
                                    // если у кастера есть маркер отражения, то теряется маркер и перенаправляется на цель
                                    if (field.get(0).getActiveMarkers().getLast().equals(Marker.REFLECTIONMARKER)) {
                                        field.get(0).removeMarker();
                                        // если у цели есть маркеры
                                        if (!field.get(i).getActiveMarkers().isEmpty()) {
                                            // если у цели есть маркер отражения, то теряется маркер и перенаправляется на кастера
                                            if (field.get(i).getActiveMarkers().getLast().equals(Marker.REFLECTIONMARKER)) {
                                                field.get(i).removeMarker();
                                            }
                                            // если у цели есть маркер преграды, то теряется маркер и отражение поглощается
                                            else if (field.get(i).getActiveMarkers().getLast().equals(Marker.WALLMARKER)) {
                                                field.get(i).removeMarker();
                                                pingPong = false;
                                            } else if (field.get(i).getActiveMarkers().getLast().equals(Marker.FREEZEMARKER)) {
                                                field.get(i).getDamage(field.get(0).getDamagePower() - 2 + positionDamage);
                                                field.get(i).removeMarker();
                                            }
                                            // если у цели есть нет маркера преграды и отражения, то фаербол срабатывает
                                            else {
                                                if (field.get(i).getHp() > 0) {
                                                    totalDamage = (field.get(0).getDamagePower() + positionDamage) <= 0 ? 1 : field.get(0).getDamagePower() + positionDamage;
                                                    System.out.println("Урон цели");
                                                    field.get(i).getDamage(totalDamage);
                                                }
                                                pingPong = false;
                                            }
                                        }
                                        // если у цели есть нет маркеров, то фаербол срабатывает
                                        else {
                                            if (field.get(i).getHp() > 0) {
                                                totalDamage = (field.get(0).getDamagePower() + positionDamage) <= 0 ? 1 : field.get(0).getDamagePower() + positionDamage;
                                                System.out.println("Урон цели");
                                                field.get(i).getDamage(totalDamage);
                                            }
                                            pingPong = false;
                                        }
                                    }
                                    // если у кастера есть маркер преграды, то теряется маркер и отражение поглощается
                                    else if (field.get(0).getActiveMarkers().getLast().equals(Marker.WALLMARKER)) {
                                        field.get(0).removeMarker();
                                        pingPong = false;
                                    } else if (field.get(0).getActiveMarkers().getLast().equals(Marker.FREEZEMARKER)) {
                                        field.get(0).getDamage(field.get(0).getDamagePower() - 2 + positionDamage);
                                        field.get(0).removeMarker();
                                    }
                                    // если у кастера нет маркера, то фаербол срабатывает
                                    else {
                                        if (field.get(0).getHp() > 0) {
                                            totalDamage = (field.get(0).getDamagePower() + positionDamage) <= 0 ? 1 : field.get(0).getDamagePower() + positionDamage;
                                            System.out.println("Урон цели");
                                            field.get(0).getDamage(totalDamage);
                                        }
                                        pingPong = false;
                                    }
                                    // если у кастера нет маркеров, то фаербол срабатывает
                                } else {
                                    if (field.get(0).getHp() > 0) {
                                        totalDamage = (field.get(0).getDamagePower() + positionDamage) <= 0 ? 1 : field.get(0).getDamagePower() + positionDamage;
                                        System.out.println("Урон цели");
                                        field.get(0).getDamage(totalDamage);
                                    }
                                    pingPong = false;
                                }
                            }
                        }

                    } else field.get(i).getDamage(field.get(0).getDamagePower() + positionDamage);
                } else {
                    int positionDamage = 0;
                    if (i == 1) positionDamage = 1;
                    if (i == field.size() - 1) positionDamage = -1;
                    field.get(i).getDamage(field.get(0).getDamagePower() + positionDamage);
                }
            }
        } else if (field.get(0).isDead()) System.out.println("Монстр мертв!");
        else if (!field.get(0).getActiveMarkers().isEmpty()) {
            if (field.get(0).getActiveMarkers().getLast().equals(Marker.FREEZEMARKER))
                System.out.println("Монстр заморожен!");
        } else System.out.println("Монстр с рывком");
    }


    @DisplayName("Проверка, что ")
    @ParameterizedTest(name = " игроки получили опыт в нужном размере")
    @MethodSource({"helpers.DataProvider#checkBrands"})
    public void gameTests(List<Target> monsters, List<Player> playerList, List<String> moveDecision,
                          List<Integer> fSpellSelections,List<Integer> fTargetSelections,
                          List<String> fDirectionSelections, List<String> fSpellCastingDecision,
                          List<Integer> sSpellSelections,List<Integer> sTargetSelections,
                          List<String> sDirectionSelections,List<String> sSpellCastingDecision,
                          List<Integer> spellChoice) throws IOException {
        int turnCount = 0;
        monstersList = monsters;
        field.add(0, null);
        monstersList.remove(0);
        players.addAll(playerList);
        for (int i = 0; i < players.size(); i++) {
            field.add(players.get(i).getTarget());
        }
        if (turnCount == 0) {
            players.get(0).moveDecision(field, moveDecision.get(0));
            turnCount++;
        } else if (turnCount == 1 && players.size() == 2) {
            players.get(1).moveDecision(field, moveDecision.get(1));
            turnCount--;
        }
        field.add(0,monstersList.get(0));
        /*
          while (!ggg.isEmpty()){
            System.out.println(ggg.get(0));;
            ggg.remove(0);
        }
         */

        dashAttack();
        if (field.get(0).isDash()){
            Assertions.assertEquals(10 - field.get(0).getDamagePower() - 1, field.get(1).getHp(),
                    "Урон отличается, должно быть: " + field.get(1).getHp() + ", а результат: " + (10 - field.get(0).getDamagePower() + 1));
            if (field.get(0).getDamagePower()>1) {
                Assertions.assertEquals(10 - field.get(0).getDamagePower() + 1, field.get(2).getHp(),
                        "Урон отличается, должно быть: " + field.get(2).getHp() + ", а результат: " + (10 - field.get(0).getDamagePower() - 1));
            }
            else {
                Assertions.assertEquals(10 - field.get(0).getDamagePower(), field.get(2).getHp(),
                        "Урон отличается, должно быть: " + field.get(2).getHp() + ", а результат: " + (10 - field.get(0).getDamagePower() - 1));
            }
        }
        // Размещение карты
//        for (Player player : players) {
//            // p1.placeSpell(p1.spellCreation(), field.get(p1.targetSelection(field)),p1.directionSelection(),p1.cardLocation(field,1));
//            if (!player.getTarget().getActiveMarkers().isEmpty()) {
//                if (!player.getTarget().getActiveMarkers().getLast().equals(Marker.FREEZEMARKER))
//                    player.placeSpell(player.spellCreation(), field.get(player.targetSelection(field)), player.directionSelection(), player.cardLocation(field, 1));
//                else {
//                    player.discardAll(field);
//                    System.out.println(player + " заморожен и пропускает ход!");
//                }
//            } else
//                player.placeSpell(player.spellCreation(), field.get(player.targetSelection(field)), player.directionSelection(), player.cardLocation(field, 1));
//        }

        players.clear();
        field.clear();
    }
}
