package targets;

import cards.*;
import play.Player;

import java.lang.management.PlatformLoggingMXBean;
import java.util.*;

public abstract class Target {

    private String name;
    private int hp;
    private Map<Player, List<Deque<Map<Spell, Boolean>>>> activeCards;
    private boolean activeMarkers;

    private int damagePower;
    private boolean isDash;
    private DamageType dType;
    private MonsterTypes mType;
    private ElementType eType;


    private int linePosition;

    Target() {
        setHp(10);
        activeCards = new HashMap<>();
        activeMarkers = false;
    }

    public int getLinePosition() {
        return linePosition;
    }

    public void setLinePosition(int linePosition) {
        this.linePosition = linePosition;
    }

    public void setDamagePower(int damagePower) {
        this.damagePower = damagePower;
    }

    public void setDash(boolean dash) {
        isDash = dash;
    }

    public void setdType(DamageType dType) {
        this.dType = dType;
    }

    public void setmType(MonsterTypes mType) {
        this.mType = mType;
    }


    public int getDamagePower() {
        return damagePower;
    }


    public boolean isDash() {
        return isDash;
    }

    public DamageType getdType() {
        return dType;
    }

    public MonsterTypes getmType() {
        return mType;
    }

    public ElementType geteType() {
        return eType;
    }

    public void seteType(ElementType eType) {
        this.eType = eType;
    }

    public String getName() {
        return name;
    }

    public Map<Player, List<Deque<Map<Spell, Boolean>>>> getActiveCards() {
        return activeCards;
    }


    /**
     * сброс карт
     */
    public void resetCards() {
        activeCards.clear();
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getHp() {
        return hp;
    }

    public void setHp(int hp) {
        this.hp = hp;
    }

    @Override
    public String toString() {
        return name;
    }

    /**
     * Метод получения урона
     *
     * @param d - кол-во урона
     */
    public void getDamage(int d) {
        this.hp -= d;
    }

    public void getSpellEffect(Spell spell, int position, List<Target> field) {
        int positionPower = 0;
        if (position == 1) positionPower++;
        else if (position == field.size()-1) positionPower--;
         if (spell.getClass().equals(FireBall.class)){
             if (this.geteType().equals(ElementType.FROSTY)) this.getDamage(positionPower + 4);
             else if (this.geteType().equals(ElementType.FIERY)) this.getDamage(positionPower + 1);
             else this.getDamage(positionPower + 2);
         }
         else if (spell.getClass().equals(Freezing.class)){
             System.out.println("Заморозка!!!");
             if (!this.isActiveMarkers()) System.out.println("нет маркеров");
         }
         else if (spell.getClass().equals(Light.class)){
             System.out.println("Свет!!!");
         }
         else if (spell.getClass().equals(LightningBolt.class)){
             System.out.println("Молния!!!");
         }
         else if (spell.getClass().equals(Reflection.class)){
             System.out.println("Отражение!!!");
         }
         else if (spell.getClass().equals(Wall.class)){
             System.out.println("Преграда!!!");
         }

    }

    public boolean isActiveMarkers(){
        return activeMarkers;
    }
}
