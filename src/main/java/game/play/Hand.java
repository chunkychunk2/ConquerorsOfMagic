package game.play;

import game.cards.*;

import java.util.ArrayList;
import java.util.List;

public class Hand {

    /**
     * Список актуальных заклинаний игрока
     */
    private List<Spell> spells = new ArrayList<>();

    /**
     * Список сброса заклинаний игрока
     */
    private List<Spell> discard = new ArrayList<>();

    /**
     * Конструктор создает стартовую руку заклинаний
     */
    Hand() {
        spells.add(new Reflection());
        spells.add(new Wall());
        spells.add(new LightningBolt());
        spells.add(new Light());
        spells.add(new Freezing());
        spells.add(new FireBall());
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

    public Spell getSpellFromDiscard(int i){
        Spell spell = discard.get(i);
        discard.remove(i);
        return spell;
    }

    public void addSpellToDiscard(Spell spell){
        discard.add(spell);
    }

    public List<Spell> getDiscard(){
        return discard;
    }
}
