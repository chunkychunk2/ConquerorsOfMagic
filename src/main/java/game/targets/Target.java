package game.targets;

import game.markers.Marker;
import game.spells.*;
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
    private LinkedList<Marker> activeMarkers;

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
     * Цели устанавливается кол-во здоровья, рука, тип существа и слоты для маркеров
     */
    Target() {
        setHp(10);
        activeCards = new HashMap<>();
        seteType(COMMON);
        activeMarkers = new LinkedList<>();
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
        if (d<=0) d = 1;
        this.hp -= d;
    }

    /**
     * Метод определяет эффект от полученного заклинания учитывая прочие модификаторы
     * @param spell - входящее заклинание
     * @param position - позизиция заклинателя
     * @param field - игровое поле
     */
    public void getSpellEffect(Spell spell, int position, List<Target> field) {
        // определение модификаторов позиции
        int positionPower = 0;
        int damageReduction = 0;
        int totalDamage = 0;

        // если цель монстр, то действую модификаторы позиций
        if (field.get(0).equals(this)){
            if (position == 1) positionPower++;
            else if (position == field.size()-1) positionPower--;
        }

        // если огненный шар
         if (spell.getClass().equals(FireBall.class)){

             // если у цели есть маркеры
            if (!this.getActiveMarkers().isEmpty()){
                // если цель заморожена
                if (this.getActiveMarkers().getLast().equals(Marker.FREEZEMARKER)){
                    damageReduction = -2;
                    this.removeMarker();
                    System.out.println("Огненный шар снял маркер!");
                }
            }
             // если монстр ледяной
             if (this.geteType().equals(ElementType.FROSTY)) {
                 totalDamage = (4-damageReduction) <= 0 ? 1 : 4-damageReduction;
                 this.getDamage(positionPower + totalDamage);
                 System.out.println("Урон ледяному монстру");
             }
             // если монстр огненный
             else if (this.geteType().equals(ElementType.FIERY)) {
                 totalDamage = (1-damageReduction) <= 0 ? 1 : 1-damageReduction;
                 this.getDamage(positionPower + totalDamage);
                 System.out.println("Урон огненному монстру");
             }
             else {
                 totalDamage = (2-damageReduction) <= 0 ? 1 : 2-damageReduction;
                 System.out.println("Урон цели");
                 System.out.println(" pos p " + positionPower);
                 System.out.println(" total d " + totalDamage);
                 this.getDamage(positionPower + totalDamage);
             }
         }
        // если заклиниие заморозка
         else if (spell.getClass().equals(Freeze.class)){
             // если у цели есть маркеры
             if (!this.getActiveMarkers().isEmpty()){
                 // если цель заморожена
                 if (this.getActiveMarkers().getLast().equals(Marker.FREEZEMARKER)){
                     damageReduction = -2;
                     this.removeMarker();
                 }
             }
             // если монстр ледяной
             if (this.geteType().equals(ElementType.FROSTY)) {
                 totalDamage = (1-damageReduction) <= 0 ? 1 : 1-damageReduction;
                 this.getDamage(positionPower + totalDamage);
                 this.addMarker(Marker.FREEZEMARKER);
             }
                 // если монстр огненный
             else if (this.geteType().equals(ElementType.FIERY)) {
                 totalDamage = (3-damageReduction) <= 0 ? 1 : 3-damageReduction;
                 this.getDamage(positionPower + totalDamage);
                 this.addMarker(Marker.FREEZEMARKER);
             }
             else {
                 totalDamage = (1-damageReduction) <= 0 ? 1 : 1-damageReduction;
                 this.getDamage(positionPower + totalDamage);
                 this.addMarker(Marker.FREEZEMARKER);
             }
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


    public void addMarker(Marker marker){
        activeMarkers.add(marker);
    }

    public LinkedList<Marker> getActiveMarkers(){
        return activeMarkers;
    }

    public void removeMarker(){
        activeMarkers.removeLast();
    }
}
