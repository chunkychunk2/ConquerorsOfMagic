package play;


import cards.Spell;
import targets.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.*;
import java.util.*;

public class Game {

    /**
     * Поле, где размещаются игроки и монстр
     */
    static List<Target> field = new ArrayList<>();

    static BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

    static Player p1 = new Player(new Pyromancer());
    static Player p2 = new Player(new FrostMage());
    static Player p3;
    static Player p4;
    static Player p5;
    static Player p6;

    /**
     * Создается список для хранения монстров
     */
    static List<Target> monstersList;

    static {
        try {
            monstersList = Game.getMonsters();
        } catch (SQLException | ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    static List<Player> players = new ArrayList<>();

    public static void generatePlayers() throws IOException {
        System.out.println("Кол-во игроков?");
        int max = Integer.parseInt(reader.readLine());
        for (int i = 0; i < max; i++) {
            if (i == 0) {
                field.add(p1.getTarget());
                players.add(p1);
            } else if (i == 1) {
                field.add(p2.getTarget());
                players.add(p1);
            } else if (i == 2) {
                field.add(p3.getTarget());
                players.add(p1);
            } else if (i == 3) {
                field.add(p4.getTarget());
                players.add(p1);
            } else if (i == 4) {
                field.add(p5.getTarget());
                players.add(p1);
            } else if (i == 5) {
                field.add(p6.getTarget());
                players.add(p1);
            }
        }
        Collections.shuffle(field);
    }

    public static void generateField() {
        field.add(0, null);
    }

    public static void appearanceOfMonster() {
        field.set(0, monstersList.get(0));
        System.out.println("Появление монстра: " + field);
        monstersList.remove(0);
    }

    public static void dashAttack() {
        System.out.print("Атака монстра с рывком: ");
        if (monstersList.get(0).isDash()) {
            System.out.print("Монстр атакует!\n");
            for (int i = 1; i < field.size(); i++) {
                field.get(i).getDamage(monstersList.get(0).getDamagePower());

            }
        } else System.out.println("Монстр без рывка");
    }

    static List<Target> getMonsters() throws SQLException, ClassNotFoundException {
        // monsters Database
        List<Target> monsters = new ArrayList<>();
        String root = "root";
        String password = "password";
        String connectionURL = "jdbc:mysql://localhost:3306/monsters";
        Class.forName("com.mysql.cj.jdbc.Driver");
        try (Connection connection = DriverManager.getConnection(connectionURL, root, password);
             Statement statement = connection.createStatement()) {
            statement.execute("INSERT INTO game SELECT * FROM green_monsters ORDER BY rand()");
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

    public static void main(String[] args) throws IOException, SQLException, ClassNotFoundException {
        generatePlayers();
        generateField();
    //    System.out.println(field.get(0).getHp());


        //Размещение карты напротив цели
        Placement place = ((p, s, t, d, n) ->
        {
            // Создается заклинание и выбирается направление
            Map<Spell, Boolean> spell = new HashMap<>();
            spell.put(p.getHand().getSpell(s), d);
            // если на цели нет своих заклинаний, или если есть и игрок хочет положить не в стек а в еще одну карту
            if (!t.getActiveCards().containsKey(p) || t.getActiveCards().containsKey(p) && n) {
                //Создается группа заклинаний
                Deque<Map<Spell, Boolean>> playerSpells = new ArrayDeque<>();
                //Дабавляется заклинание и его направление;
                playerSpells.add(spell);
                List<Deque<Map<Spell, Boolean>>> cards = new LinkedList<>();
                cards.add(playerSpells);
                if (!n) t.getActiveCards().put(p, cards);
                else {
                    t.getActiveCards().get(p).add(playerSpells);
                }
            } else t.getActiveCards().get(p).get(0).add(spell);
        });

//      Фаза 1: перемещание
        p1.moveDecision(field);

        //Фаза 2: появление монстра
        appearanceOfMonster();
        System.out.println(field.get(0).getHp());
        System.out.println(monstersList);
        System.out.println(field);

        //Фаза 3: атака монстра с рывком
        dashAttack();

//        for (int i = 1; i < players.size(); i++) {
//            place.card(players.get(i), players.get(i).spellCreation(), field.get(players.get(i).targetSelection(field)), players.get(i).directionSelection(), players.get(i).cardLocation(field));
//        }
        place.card(p1, p1.spellCreation(), field.get(p1.targetSelection(field)), p1.directionSelection(), p1.cardLocation(field,1));

        // Решение о розыгрыше карт
        if (p1.drawDecision()) p1.drawSpell(field);

        // Атака монстра с рывком если жив или не заморожен
        //Фаза 5: вторая атака монстра с рывком
        dashAttack();

        // Розырыш заклинаний №2
        place.card(p1, p1.spellCreation(), field.get(p1.targetSelection(field)), p1.directionSelection(), p1.cardLocation(field,2));

        // Решение о розыгрыше карт
        if (p1.drawDecision()) p1.drawSpell(field);

        System.out.println(field.get(0).getHp());

    }

}
