package play;

import cards.*;

import java.util.ArrayList;
import java.util.List;

public class Hand {

    private List<Spell> spells = new ArrayList<>();

    Hand() {
        spells.add(new Reflection());
        spells.add(new Wall());
        spells.add(new LightningBolt());
        spells.add(new Light());
        spells.add(new Freezing());
        spells.add(new FireBall());
    }

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

    public int getSize() {
        return spells.size();
    }
}
