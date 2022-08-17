package game.play;

import game.spells.*;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Hand {

    /**
     * Список актуальных заклинаний игрока
     */
    private List<Spell> spells = new ArrayList<>();

    /**
     * Список сброса заклинаний игрока
     */
    private LinkedList<Spell> discard = new LinkedList<>();

    /**
     * Конструктор создает стартовую руку заклинаний
     */
    public Hand() {
        spells.add(new Reflection());
        spells.add(new Wall());
        spells.add(new LightningBolt());
        spells.add(new Light());
        spells.add(new Freeze());
        spells.add(new FireBall());
    }

    public void restoreDiscard(){
        while (!discard.isEmpty()){
            spells.add(discard.getFirst());
            discard.removeFirst();
        }
    }

    /**
     * Метод возвращает заклинание по индексу
     * @param i - индекс значения заклинания
     * @return
     */
    public Spell getSpell(int i) {
        Spell spell = spells.get(i);
        spells.remove(i);
        return spell;
    }

    /**
     * Метод выводит текущие карты на руке
     */
    public void checkSpells() {
        for (int i = 0; i < spells.size(); i++) System.out.println("[" + i + "] " + spells.get(i));
    }

    /**
     * Метод восстанавливает карту из сброса по индексу
     */
    public Spell getSpellFromDiscard(int i){
        Spell spell = discard.get(i);
        discard.remove(i);
        return spell;
    }

    /**
     * Метод отправляет карту в сброс
     */
    public void addSpellToDiscard(Spell spell){
        discard.add(spell);
    }

    /**
     * Геттер сброса
     */
    public List<Spell> getDiscard(){
        return discard;
    }
}
