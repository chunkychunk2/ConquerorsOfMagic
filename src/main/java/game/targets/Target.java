package game.targets;

import game.cards.*;
import game.play.Player;

import java.util.*;

import static game.targets.ElementType.COMMON;

public abstract class Target {

    /**
     * Имя цели
     */
    private String name;

    /**
     * Здоровье цели
     */
    private int hp;
    /**
     * Активные карты цели цели
     */
    private Map<Player, List<Deque<Map<Spell, Boolean>>>> activeCards;

    /**
     * Активные маркеры цели
     */
    private boolean activeMarkers;

    /**
     * Сила цели
     */
    private int damagePower;

    /**
     * Наличие рывка у цели
     */
    private boolean isDash;

    /**
     * Тип урона цели
     */
    private DamageType dType;

    /**
     * Вид цели
     */
    private MonsterTypes mType;

    /**
     * Стихия цели
     */
    private ElementType eType;

    /**
     * Индекс позиции цели на поле
     */
    private int linePosition;

    /**
     * Цели устанавливается кол-во здоровья, рука, активные маркеры и тип существа
     */
    Target() {
        setHp(10);
        activeCards = new HashMap<>();
        activeMarkers = false;
        seteType(COMMON);
    }

    /**
     * Геттер индекса позиции на поле
     */
    public int getLinePosition() {
        return linePosition;
    }

    /**
     * Сеттер индекса позиции на поле
     */
    public void setLinePosition(int linePosition) {
        this.linePosition = linePosition;
    }

    /**
     * Сеттер силы урона
     */
    public void setDamagePower(int damagePower) {
        this.damagePower = damagePower;
    }

    /**
     * Сеттер наличия рывка
     */
    public void setDash(boolean dash) {
        isDash = dash;
    }

    /**
     * Сеттер типа урона
     */
    public void setdType(DamageType dType) {
        this.dType = dType;
    }

    /**
     * Сеттер типа монстра
     */
    public void setmType(MonsterTypes mType) {
        this.mType = mType;
    }

    /**
     * Геттер кол-ва урона
     */
    public int getDamagePower() {
        return damagePower;
    }

    /**
     * Геттер наличия рывка
     */
    public boolean isDash() {
        return isDash;
    }

    /**
     * Геттер типа урона
     */
    public DamageType getdType() {
        return dType;
    }

    /**
     * Геттер типа монстра
     */
    public MonsterTypes getmType() {
        return mType;
    }

    /**
     * Геттер стихии существа
     */
    public ElementType geteType() {
        return eType;
    }

    /**
     * Сеттер стихии существа
     */
    public void seteType(ElementType eType) {
        this.eType = eType;
    }

    /**
     * Геттер имени существа
     */
    public String getName() {
        return name;
    }

    /**
     * Геттер активных карт на цели
     */
    public Map<Player, List<Deque<Map<Spell, Boolean>>>> getActiveCards() {
        return activeCards;
    }

    /**
     * Сеттер имени цели
     * @param name - имя
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Геттер хп цели
     */
    public int getHp() {
        return hp;
    }

    /**
     * Сеттер хп цели
     */
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

    /**
     * Метод определяет эффект от полученного заклинания учитывая прочие модификаторы
     * @param spell - входящее заклинание
     * @param position - позизиция заклинателя
     * @param field - игровое поле
     */
    public void getSpellEffect(Spell spell, int position, List<Target> field) {
        int positionPower = 0;
        if (position == 1) positionPower++;
        else if (position == field.size()-1) positionPower--;
         if (spell.getClass().equals(FireBall.class)){
             if (this.geteType().equals(ElementType.FROSTY)) this.getDamage(positionPower + 4);
             else if (this.geteType().equals(ElementType.FIERY)) this.getDamage(positionPower + 1);
             else {
                 this.getDamage(positionPower + 2);
             }
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

    /**
     * Геттер активных маркеров
     * @return - активные маркеры
     */
    public boolean isActiveMarkers(){
        return activeMarkers;
    }
}
