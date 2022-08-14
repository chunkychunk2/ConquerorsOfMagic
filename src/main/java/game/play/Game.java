package game.play;

import game.markers.Marker;
import game.targets.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.*;
import java.util.*;

public class Game {

    /**
     * Отслеживание маркера хода
     */
    static int turnCount = 0;

    /**
     * Поле, где размещаются игроки и монстр
     */
    static List<Target> field = new ArrayList<>();

    /**
     * Ввод данных
     */
    static BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

    /**
     * Список для хранения монстров
     */
    static List<Target> monstersList;

    /**
     * Загружаются монстры из базы данных
     */
    static {
        try {
            monstersList = Database.getMonsters();
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Список игроков
     */
    static List<Player> players = new ArrayList<>();

    /**
     * Метод создает игроков
     *
     * @throws IOException
     */
    public static void generatePlayers() throws IOException {
        List<Player> allPlayers = new ArrayList<>();
        Player p1 = new Player(new Pyromancer());
        Player p2 = new Player(new FrostMage());
        Player p3;
        Player p4;
        Player p5;
        Player p6;
        allPlayers.add(p1);
        allPlayers.add(p2);
//         allPlayers.add(p3);
//         allPlayers.add(p4);
//         allPlayers.add(p5);
        Collections.shuffle(allPlayers);
        System.out.println("Кол-во игроков?");
        int max = Integer.parseInt(reader.readLine());
        for (int i = 0; i < max; i++) {
            if (i == 0) {
                field.add(allPlayers.get(0).getTarget());
                players.add(allPlayers.get(0));
            } else if (i == 1) {
                field.add(allPlayers.get(1).getTarget());
                players.add(allPlayers.get(1));
            }
        }
        Collections.shuffle(field);
    }

    /**
     * Метод добавляет на поле пустой элемент, куда будет добавляться монстр
     */
    public static void createMonsterSlot() {
        field.add(0, null);
    }

    /**
     * Метод добавляет на поле случайного монстра и удаляет его из списка монстров
     *
     * @see #monstersList - список монстров
     */
    public static void appearanceOfMonster() {
        field.set(0, monstersList.get(0));
        System.out.println("Появление монстра: " + field);
        monstersList.remove(0);
    }

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

    public static void main(String[] args) throws IOException, SQLException, ClassNotFoundException {
        generatePlayers();
        createMonsterSlot();
//      Фаза 1: перемещание
        if (turnCount == 0) {
            players.get(0).moveDecision(field);
            turnCount++;
        } else if (turnCount == 1 && players.size() == 2) {
            players.get(1).moveDecision(field);
            turnCount--;
        }
//        else if(turnCount == 1 && players.size() > 2){
//            p2.moveDecision(field);
//            turnCount++;
//        }
//        else if (turnCount == 2 && players.size() == 3) {
//            p3.moveDecision(field);
//            turnCount = 0;
//        }
//        else if(turnCount == 2 && players.size() > 2) {
//            p3.moveDecision(field);
//            turnCount++;
//        }

        //Фаза 2: появление монстра
        appearanceOfMonster();

        //Фаза 3: атака монстра с рывком
        dashAttack();
        System.out.println(players);

        for (Target target : field) {
            System.out.println(target + " " + target.getHp());
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

        // Решение о розыгрыше карт
        for (Player player : players) {
            if (!player.getTarget().getActiveMarkers().isEmpty()) {
                if (!player.getTarget().getActiveMarkers().getLast().equals(Marker.FREEZEMARKER)) {
                    if (player.drawDecision()) player.drawSpell(field);
                } else {
                    player.discardAll(field);
                    System.out.println(player + " заморожен и пропускает ход!");
                }

            } else {
                if (player.drawDecision()) player.drawSpell(field);
            }
        }

        for (Target target : field) {
            System.out.println(target + " " + target.getHp());
        }

        // Атака монстра с рывком если жив или не заморожен
        //Фаза 5: вторая атака монстра с рывком
        dashAttack();


        for (Target target : field) {
            System.out.println(target + " " + target.getHp());
        }

        // Размещение карты
        for (Player player : players) {
            if (!player.getTarget().getActiveMarkers().isEmpty()) {
                if (!player.getTarget().getActiveMarkers().getLast().equals(Marker.FREEZEMARKER))
                    player.placeSpell(player.spellCreation(), field.get(player.targetSelection(field)), player.directionSelection(), player.cardLocation(field, 2));
                else {
                    player.discardAll(field);
                    System.out.println(player + " заморожен и пропускает ход!");
                }
            } else
                player.placeSpell(player.spellCreation(), field.get(player.targetSelection(field)), player.directionSelection(), player.cardLocation(field, 2));
        }

        // Решение о розыгрыше карт
        for (Player player : players) {
            if (!player.getTarget().getActiveMarkers().isEmpty()) {
                if (!player.getTarget().getActiveMarkers().getLast().equals(Marker.FREEZEMARKER)) {
                    if (player.drawDecision()) player.drawSpell(field);
                } else {
                    player.discardAll(field);
                    System.out.println(player + " заморожен и пропускает ход!");
                }
            } else {
                for (Target target : field) {
                    if (target.getActiveCards().containsKey(player)) {
                        if (!target.getActiveCards().get(player).get(0).isEmpty()) {
                            if (player.drawDecision()) player.drawSpell(field);
                            break;
                        }
                    }
                }
            }
        }

        for (Target target : field) {
            System.out.println(target + " " + target.getHp());
        }

        // сброс размещенных и разыгранных карт
        for (Player player : players) {
            player.discardAll(field);
        }

        // атака монстра без рывка
        monsterAttack();

        for (Target target : field) {
            System.out.println(target + " " + target.getHp());
        }
    }
}
