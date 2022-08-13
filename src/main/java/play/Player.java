package play;

import cards.Spell;
import targets.Target;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class Player {

    BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

    private Hand hand;

    private Target target;

    private int selectedTarget;
    private String name;



    Player(Target tagret) {
        this.target = tagret;
        this.hand = new Hand();
        this.name = tagret.getName();

    }

    public int getSelectedTarget() {
        return selectedTarget;
    }

    /**
     * Метод позволяет получить управление над выбранным перснажем
     *
     * @return персонаж
     */
    public Target getTarget() {
        return target;
    }

    /**
     * Метод позволяет получить управление над рукой игрока
     *
     * @return рука
     */
    public Hand getHand() {
        return hand;
    }

    public void checkHP() {
        System.out.println(target.getHp());
    }

    /**
     * создание заклинания
     *
     * @return
     * @throws IOException
     */
    public int spellCreation() throws IOException {
        System.out.println("Выберите заклинание: ");
        hand.checkSpells();
        return Integer.parseInt(reader.readLine());
    }

    /**
     * решение поменяться местами с другим игроков
     *
     * @param field - поле
     * @throws IOException
     */
    public void moveDecision(List<Target> field) throws IOException {

        System.out.println("Переместиться? [Да]/[Нет]");
        System.out.println(field);
        if (reader.readLine().equals("Да")) {
            for (int i = 1; i < field.size(); i++) {
                if (!field.get(i).equals(getTarget())) System.out.println("[" + i + "]:" + field.get(i));
            }
            int to = Integer.parseInt(reader.readLine());
            Collections.swap(field, field.indexOf(field.get(field.indexOf(getTarget()))), field.indexOf(field.get(to)));
            System.out.println("Фаза перемещения: " + field);
        }
        getTarget().setLinePosition(field.indexOf(getTarget()));
    }

    public int targetSelection(List<Target> field) throws IOException {
        System.out.println("Выберите цель:");
        for (int i = 0; i < field.size(); i++) {
            System.out.println("[" + i + "] " + field.get(i));
        }
        selectedTarget = Integer.parseInt(reader.readLine());
        return selectedTarget;
    }

    public boolean directionSelection() throws IOException {
        System.out.println("Выберите направление: [На цель]/[На себя]");
        boolean direction = false;
        String directionChoice = reader.readLine();
        if (directionChoice.equals("На цель")) direction = true;
        return direction;
    }

    public boolean cardLocation(List<Target> field, int i) throws IOException {
        boolean newCardPlace = false;
        if (field.get(selectedTarget).getActiveCards().containsKey(this)){
            if (i == 2 && field.get(selectedTarget).getActiveCards().get(this).get(0).size() == 1) {
                System.out.println("Разместить заклинание поверх своей ранее неразыгранной карты или в другое место? [Да]/[Нет]");
                String cardLocationChoice = reader.readLine();
                if (cardLocationChoice.equals("Нет")) newCardPlace = true;
            }
        }
        return newCardPlace;
    }

    public boolean drawDecision() throws IOException {
        System.out.println("Разыграть заклинание? [Да]/[Нет]");
        String decision = reader.readLine();
        return decision.equals("Да");
    }

    public void drawSpell(List<Target> field) throws IOException {
        int decision = this.getSelectedTarget();
        int spellNumber = 0;
        List<Integer> targets = new ArrayList<>();
        for (int i = 0; i < field.size(); i++) {
            if (field.get(i).getActiveCards().containsKey(this)) targets.add(i);
        }
        if (targets.size() == 2) {
            System.out.println("Какое заклинание разыграть?");
            System.out.println("Заклинание напротив [1] " + field.get(targets.get(0)) + " или напротив [2] " + field.get(targets.get(1)));
            decision = Integer.parseInt(reader.readLine());
            if (decision == 2) decision = field.indexOf(field.get(targets.get(1)));
            else decision = field.indexOf(field.get(targets.get(0)));
        }
        else if (field.get(decision).getActiveCards().containsKey(this)){
              if (field.get(decision).getActiveCards().get(this).size() == 2) {
                System.out.println("Какое заклинание разыграть?");
                System.out.println("[1] " + field.get(decision).getActiveCards().get(this).get(0).getLast() +
                        " или [2] " + field.get(decision).getActiveCards().get(this).get(1).getLast());
                spellNumber = Integer.parseInt(reader.readLine());
                spellNumber--;
            }
        }
        Map<Spell, Boolean> spell = field.get(decision).getActiveCards().get(this).get(spellNumber).getLast();
        for (Map.Entry<Spell, Boolean> entry : spell.entrySet()) {
            System.out.println(entry.getValue());
            System.out.println(entry.getKey());
            if (entry.getValue().equals(false)) this.getTarget().getSpellEffect(entry.getKey(),this.getTarget().getLinePosition(),field);
            else field.get(decision).getSpellEffect(entry.getKey(),this.getTarget().getLinePosition(),field);

        }
        field.get(decision).getActiveCards().get(this).get(spellNumber).pollLast();
    }

    @Override
    public String toString() {
        return name;
    }
}
