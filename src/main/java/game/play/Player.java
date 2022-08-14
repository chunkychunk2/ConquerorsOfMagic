package game.play;

import game.spells.Spell;
import game.targets.Target;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class Player {

    BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

    /**
     * Рука игрока
     */
    private Hand hand;

    /**
     * Персонаж игрока
     */
    private Target target;

    /**
     * Индекс выбранной цели игрока на поле
     */
    private int selectedTarget;

    /**
     * Имя персонажа игрока
     */
    private String name;

    /**
     * Конструктор инициализирует персонажа из параметра, руку и имя выбранного персонажа
     *
     * @param tagret
     */
    Player(Target tagret) {
        this.target = tagret;
        this.hand = new Hand();
        this.name = tagret.getName();
    }

    /**
     * Метод выводит индекс выбранной цели
     *
     * @return индексе цели поля
     * @see Game#field - игровое поле
     */
    public int getSelectedTarget() {
        return selectedTarget;
    }

    /**
     * Метод позволяет получить управление над выбранным персонажем
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

    /**
     * Метод позволяет узнать текущее здоровье персонажа
     */
    public void checkHP() {
        System.out.println(target.getHp());
    }

    /**
     * Метод возвращает индекс выбранного заклинания с руки
     *
     * @return индекс заклинания
     * @throws IOException
     * @see #hand - рука
     */
    public int spellCreation() throws IOException {
        System.out.println(this + " выбирает заклинание");
        System.out.println("Выберите заклинание: ");
        this.getHand().checkSpells();
        return Integer.parseInt(reader.readLine());
    }

    /**
     * Метод принимает в качестве параметра игровое поле и позволяет поменяться местами с другим игроком
     *
     * @param field - поле
     * @throws IOException
     * @see Game#field - игровое поле
     */
    public void moveDecision(List<Target> field) throws IOException {
        System.out.println(this + " решает переместиться или нет");
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

    /**
     * Метод позволяет выбрать цель, напротив которой размещается заклинание
     *
     * @param field - поле
     * @return индекс выбранной цели на игровом поле
     * @throws IOException
     * @see Game#field - игровое поле
     * @see #selectedTarget - индекс выбранной цели
     */
    public int targetSelection(List<Target> field) throws IOException {
        System.out.println("Выберите цель:");
        for (int i = 0; i < field.size(); i++) {
            System.out.println("[" + i + "] " + field.get(i));
        }
        selectedTarget = Integer.parseInt(reader.readLine());
        return selectedTarget;
    }

    /**
     * Метод позволчет выбрать направление размещенной карты, куда указывает стрелка
     * Если true, то на цель
     * Если false, то на себя
     *
     * @return направление заклинания
     * @throws IOException
     */
    public boolean directionSelection() throws IOException {
        System.out.println("Выберите направление: [На цель]/[На себя]");
        boolean direction = false;
        String directionChoice = reader.readLine();
        if (directionChoice.equals("На цель")) direction = true;
        return direction;
    }

    /**
     * Метод определяет есть ли ранее разыгранные заклинания на выбранной цели
     * и позволяет выбрать разместить заклинание в новое место или поверх своей ранее размещеннной карты
     * В качестве параметра передаетсяч игровое поле и номер фазы розыгрыша заклинаний
     * Если это первое размещение в раунде, то значение = 1
     * Если второе размещение, то значение = 2
     *
     * @param field - игровое поле
     * @param i     - номер розыграша заклинаний
     * @return решение о размещение картты в новом месте
     * @throws IOException
     */
    public boolean cardLocation(List<Target> field, int i) throws IOException {
        boolean newCardPlace = false;
        if (field.get(selectedTarget).getActiveCards().containsKey(this)) {
           // if (i == 2 && !field.get(selectedTarget).getActiveCards().get(this).get(0).isEmpty()) {
                 if (i == 2 && field.get(selectedTarget).getActiveCards().get(this).get(0).size() == 1) {
                System.out.println("Разместить заклинание поверх своей ранее неразыгранной карты или в другое место? [Да]/[Нет]");
                String cardLocationChoice = reader.readLine();
                if (cardLocationChoice.equals("Нет")) newCardPlace = true;
            }
        }
        return newCardPlace;
    }

    /**
     * Метод определяет решение игрока о розыгрыше заклинания
     * Если игрок решил не разыгрывать заклинание, то метод возвращает false
     *
     * @return решение о розыгрыше карт
     * @throws IOException
     */
    public boolean drawDecision() throws IOException {
        System.out.println(this + " делает выбор о розыгрыше");
        System.out.println("Разыграть заклинание? [Да]/[Нет]");
        String decision = reader.readLine();
        return decision.equals("Да");
    }

    /**
     * Метод определяет размещенные заклинания
     * Если размещенных заклинаний несколько, то игроку предоставляется выбор, какое заклинание разыгрыть
     * После того, как игрок сделал выбор, карта уходит в сброс
     *
     * @param field - игровое поле
     * @throws IOException
     */
    public void drawSpell(List<Target> field) throws IOException {
        int spellLevel = 0;
        int decision = this.getSelectedTarget();
        int spellNumber = 0;
        if (field.get(decision).getActiveCards().containsKey(this)) {
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
//            System.out.println(entry.getValue());
//            System.out.println(entry.getKey());
            hand.addSpellToDiscard(entry.getKey());
            if (field.get(selectedTarget).getActiveCards().get(this).get(0).size() == 1) spellLevel = 1;
            if (field.get(selectedTarget).getActiveCards().get(this).get(0).size() == 2) spellLevel = 2;
                if (entry.getValue().equals(false))
                this.getTarget().getSpellEffect(entry.getKey(), this.getTarget().getLinePosition(), field, spellLevel);
            else field.get(decision).getSpellEffect(entry.getKey(), this.getTarget().getLinePosition(), field, spellLevel);

        }
        field.get(decision).getActiveCards().get(this).get(spellNumber).pollLast();
    }

    /**
     * Метод размещает заклинание на выбранную цель с учетом направления заклинания и места размещения
     * Если заклинание размещается на ранее размещенное, то образуется стэк
     * @param spell - заклинание
     * @param target - цель заклинания
     * @param direction - направление заклинания
     * @param newPlace - новое место заклинания или размещение на свою ранее размещенную карту
     * @throws IOException
     */
    public void placeSpell(int spell, Target target, boolean direction, boolean newPlace) throws IOException {
        Map<Spell, Boolean> playerSpell = new HashMap<>();
        playerSpell.put(this.getHand().getSpell(spell), direction);
        // если на цели нет своих заклинаний, или если есть и игрок хочет положить не в стек а в еще одну карту
        if (!target.getActiveCards().containsKey(this) || target.getActiveCards().containsKey(this) && newPlace) {
            //Создается группа заклинаний
            Deque<Map<Spell, Boolean>> playerSpells = new ArrayDeque<>();
            //Дабавляется заклинание и его направление;
            playerSpells.add(playerSpell);
            List<Deque<Map<Spell, Boolean>>> cards = new LinkedList<>();
            cards.add(playerSpells);
            if (!newPlace) target.getActiveCards().put(this, cards);
            else {
                target.getActiveCards().get(this).add(playerSpells);
            }
        } else target.getActiveCards().get(this).get(0).add(playerSpell);

    }

    /**
     * Метод отправляет в сброс все размещенные карты
     * @param field - игровое поле
     */
    public void discardAll(List<Target> field) {
        for (int i = 0; i < field.size(); i++) {
            if (field.get(i).getActiveCards().containsKey(this)) {
                if (field.get(i).getActiveCards().get(this).get(0).size() == 1) {
                //  if (!field.get(selectedTarget).getActiveCards().get(this).get(0).isEmpty()){
                    Map<Spell, Boolean> spell = field.get(i).getActiveCards().get(this).get(0).getLast();
                    for (Map.Entry<Spell, Boolean> entry : spell.entrySet()) {
                        hand.addSpellToDiscard(entry.getKey());
                    }
                    field.get(i).getActiveCards().get(this).get(0).pollLast();
                }
                if (field.get(i).getActiveCards().get(this).get(0).size() == 2) {
                    //  if (!field.get(selectedTarget).getActiveCards().get(this).get(0).isEmpty()){
                    Map<Spell, Boolean> spell = field.get(i).getActiveCards().get(this).get(0).getLast();
                    for (Map.Entry<Spell, Boolean> entry : spell.entrySet()) {
                        hand.addSpellToDiscard(entry.getKey());
                    }
                    field.get(i).getActiveCards().get(this).get(0).pollLast();
                    spell = field.get(i).getActiveCards().get(this).get(0).getLast();
                    for (Map.Entry<Spell, Boolean> entry : spell.entrySet()) {
                        hand.addSpellToDiscard(entry.getKey());
                    }
                    field.get(i).getActiveCards().get(this).get(0).pollLast();
                }
            }
        }
    }


    @Override
    public String toString() {
        return name;
    }
}
