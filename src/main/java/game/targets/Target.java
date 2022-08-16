package game.targets;

import game.markers.Marker;
import game.spells.*;
import game.play.Player;

import java.util.*;

import static game.targets.ElementType.COMMON;
import static game.targets.MonsterTypes.HUMAN;

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
     * Максимальное здоровье цели
     */
    private int maxHp;

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
        setMaxHp(10);
        activeCards = new HashMap<>();
        seteType(COMMON);
        setmType(HUMAN);

        activeMarkers = new LinkedList<>();
        // добавляет вначале игры маркеры игрокам
//        this.addMarker(Marker.REFLECTIONMARKER);
//        this.addMarker(Marker.REFLECTIONMARKER);
        this.addMarker(Marker.WALLMARKER);
        this.addMarker(Marker.WALLMARKER);
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
     *
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
    public void heal(int points) {
        hp += points;
    }

    /**
     * Сеттер хп цели
     */
    public void setHp(int hp) {
        this.hp = hp;
    }

    /**
     * Сеттер максимального кол-ва хп цели
     */
    public void setMaxHp(int hp) {
        this.maxHp = hp;
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
        if (d <= 0) d = 1;
        this.hp -= d;
    }

    /**
     * Метод определяет эффект от полученного заклинания учитывая прочие модификаторы
     *
     * @param spell    - входящее заклинание
     * @param position - позизиция заклинателя
     * @param field    - игровое поле
     */
    public void getSpellEffect(Spell spell, int position, List<Target> field, int spellLevel, Player player) {
        // определение модификаторов позиции
        int positionPower = 0;
        int damageReduction = 0;
        int totalDamage = 0;

        // если цель монстр, то действую модификаторы позиций
        if (position == 1) positionPower++;
        else if (position == field.size() - 1) positionPower--;

        if (this.getmType().equals(HUMAN)) positionPower = 0;

        // если огненный шар
        if (spell.getClass().equals(FireBall.class)) {
            int fireDamage = 0;
            if (spellLevel == 1) fireDamage = 2;
            if (spellLevel == 2) fireDamage = 4;

            // если у цели есть маркеры
            if (!this.getActiveMarkers().isEmpty()) {
                // если цель заморожена
                if (this.getActiveMarkers().getLast().equals(Marker.FREEZEMARKER)) {
                    damageReduction = -2;
                    this.removeMarker();
                    System.out.println("Огненный шар снял маркер!");
                } else if (this.getActiveMarkers().getLast().equals(Marker.WALLMARKER)) {
                    this.removeMarker();
                    System.out.println("Преграда поглотила урон!");
                } else if (this.getActiveMarkers().getLast().equals(Marker.REFLECTIONMARKER)) {
                    this.removeMarker();
                    boolean pingPong = true;
                    // пока в взаимодействующих есть маркер отражения или преграды
                    while (pingPong) {
                        // если у кастера есть маркеры
                        if (!player.getTarget().getActiveMarkers().isEmpty()) {
                            // если у кастера есть маркер отражения, то теряется маркер и перенаправляется на цель
                            if (player.getTarget().getActiveMarkers().getLast().equals(Marker.REFLECTIONMARKER)) {
                                player.getTarget().removeMarker();
                                // если у цели есть маркеры
                                if (!this.getActiveMarkers().isEmpty()) {
                                    // если у цели есть маркер отражения, то теряется маркер и перенаправляется на кастера
                                    if (this.getActiveMarkers().getLast().equals(Marker.REFLECTIONMARKER)) {
                                        this.removeMarker();
                                    }
                                    // если у цели есть маркер преграды, то теряется маркер и отражение поглощается
                                    else if (this.getActiveMarkers().getLast().equals(Marker.WALLMARKER)) {
                                        this.removeMarker();
                                        pingPong = false;
                                    }
                                    // если у цели есть нет маркера преграды и отражения, то фаербол срабатывает
                                    else {
                                        if (this.getHp() > 0) {
                                            // если монстр ледяной
                                            if (this.geteType().equals(ElementType.FROSTY)) {
                                                totalDamage = (fireDamage + 2 + damageReduction + positionPower) <= 0 ? 1 : fireDamage + 2 + damageReduction + positionPower;
                                                this.getDamage(totalDamage);
                                                System.out.println("Урон ледяному монстру");
                                            }
                                            // если монстр огненный
                                            else if (this.geteType().equals(ElementType.FIERY)) {
                                                totalDamage = (fireDamage - 1 + damageReduction + positionPower) <= 0 ? 1 : fireDamage - 1 + damageReduction + positionPower;
                                                this.getDamage(totalDamage);
                                                System.out.println("Урон огненному монстру");
                                            } else {
                                                totalDamage = (fireDamage + damageReduction + positionPower) <= 0 ? 1 : fireDamage + damageReduction + positionPower;
                                                System.out.println("Урон цели");
                                                System.out.println(totalDamage);
                                                this.getDamage(totalDamage);
                                            }
                                        }
                                        pingPong = false;
                                    }
                                }
                                // если у цели есть нет маркеров, то фаербол срабатывает
                                else {
                                    if (this.getHp() > 0) {
                                        // если монстр ледяной
                                        if (this.geteType().equals(ElementType.FROSTY)) {
                                            totalDamage = (fireDamage + 2 + damageReduction + positionPower) <= 0 ? 1 : fireDamage + 2 + damageReduction + positionPower;
                                            this.getDamage(totalDamage);
                                            System.out.println("Урон ледяному монстру");
                                        }
                                        // если монстр огненный
                                        else if (this.geteType().equals(ElementType.FIERY)) {
                                            totalDamage = (fireDamage - 1 + damageReduction + positionPower) <= 0 ? 1 : fireDamage - 1 + damageReduction + positionPower;
                                            this.getDamage(totalDamage);
                                            System.out.println("Урон огненному монстру");
                                        } else {
                                            totalDamage = (fireDamage + damageReduction + positionPower) <= 0 ? 1 : fireDamage + damageReduction + positionPower;
                                            System.out.println("Урон цели");
                                            System.out.println(totalDamage);
                                            this.getDamage(totalDamage);
                                        }
                                    }
                                    pingPong = false;
                                }
                            }
                            // если у кастера есть маркер преграды, то теряется маркер и отражение поглощается
                            else if (player.getTarget().getActiveMarkers().getLast().equals(Marker.WALLMARKER)) {
                                player.getTarget().removeMarker();
                                pingPong = false;
                            }
                            // если у кастера нет маркера, то фаербол срабатывает
                            else {
                                if (player.getTarget().getHp() > 0) {
                                    // если монстр ледяной
                                    if (player.getTarget().geteType().equals(ElementType.FROSTY)) {
                                        totalDamage = (fireDamage + 2 + damageReduction + positionPower) <= 0 ? 1 : fireDamage + 2 + damageReduction + positionPower;
                                        player.getTarget().getDamage(totalDamage);
                                        System.out.println("Урон ледяному монстру");
                                    }
                                    // если монстр огненный
                                    else if (player.getTarget().geteType().equals(ElementType.FIERY)) {
                                        totalDamage = (fireDamage - 1 + damageReduction + positionPower) <= 0 ? 1 : fireDamage - 1 + damageReduction + positionPower;
                                        player.getTarget().getDamage(totalDamage);
                                        System.out.println("Урон огненному монстру");
                                    } else {
                                        totalDamage = (fireDamage + damageReduction + positionPower) <= 0 ? 1 : fireDamage + damageReduction + positionPower;
                                        System.out.println("Урон цели");
                                        System.out.println(totalDamage);
                                        player.getTarget().getDamage(totalDamage);
                                    }
                                }
                                pingPong = false;
                            }
                            // если у кастера нет маркеров, то фаербол срабатывает
                        } else {
                            if (player.getTarget().getHp() > 0) {
                                // если монстр ледяной
                                if (player.getTarget().geteType().equals(ElementType.FROSTY)) {
                                    totalDamage = (fireDamage + 2 + damageReduction + positionPower) <= 0 ? 1 : fireDamage + 2 + damageReduction + positionPower;
                                    player.getTarget().getDamage(totalDamage);
                                    System.out.println("Урон ледяному монстру");
                                }
                                // если монстр огненный
                                else if (player.getTarget().geteType().equals(ElementType.FIERY)) {
                                    totalDamage = (fireDamage - 1 + damageReduction + positionPower) <= 0 ? 1 : fireDamage - 1 + damageReduction + positionPower;
                                    player.getTarget().getDamage(totalDamage);
                                    System.out.println("Урон огненному монстру");
                                } else {
                                    totalDamage = (fireDamage + damageReduction + positionPower) <= 0 ? 1 : fireDamage + damageReduction + positionPower;
                                    System.out.println("Урон цели");
                                    System.out.println(totalDamage);
                                    player.getTarget().getDamage(totalDamage);
                                }
                            }
                            pingPong = false;
                        }
                    }
                }
            }
            // если есть преграда, то атака не проходит
            else {
                if (this.getHp() > 0) {
                    // если монстр ледяной
                    if (this.geteType().equals(ElementType.FROSTY)) {
                        totalDamage = (fireDamage + 2 + damageReduction + positionPower) <= 0 ? 1 : fireDamage + 2 + damageReduction + positionPower;
                        this.getDamage(totalDamage);
                        System.out.println("Урон ледяному монстру");
                    }
                    // если монстр огненный
                    else if (this.geteType().equals(ElementType.FIERY)) {
                        totalDamage = (fireDamage - 1 + damageReduction + positionPower) <= 0 ? 1 : fireDamage - 1 + damageReduction + positionPower;
                        this.getDamage(totalDamage);
                        System.out.println("Урон огненному монстру");
                    } else {
                        totalDamage = (fireDamage + damageReduction + positionPower) <= 0 ? 1 : fireDamage + damageReduction + positionPower;
                        System.out.println("Урон цели");
                        System.out.println(totalDamage);
                        this.getDamage(totalDamage);
                    }
                }
            }
        }
        // если заклиниие заморозка
        else if (spell.getClass().equals(Freeze.class)) {
            int frostDamage = 0;
            if (spellLevel == 1) frostDamage = 1;
            if (spellLevel == 2) frostDamage = 3;
            // если у цели есть маркеры
            if (!this.getActiveMarkers().isEmpty()) {
                // если цель заморожена
                if (this.getActiveMarkers().getLast().equals(Marker.FREEZEMARKER)) {
                    damageReduction = -2;
                    this.removeMarker();
                } else if (this.getActiveMarkers().getLast().equals(Marker.WALLMARKER)) {
                    this.removeMarker();
                    System.out.println("Преграда поглотила урон!");
                } else if (this.getActiveMarkers().getLast().equals(Marker.REFLECTIONMARKER)) {
                    this.removeMarker();
                    boolean pingPong = true;
                    // пока в взаимодействующих есть маркер отражения или преграды
                    while (pingPong) {
                        // если у кастера есть маркеры
                        if (!player.getTarget().getActiveMarkers().isEmpty()) {
                            // если у кастера есть маркер отражения, то теряется маркер и перенаправляется на цель
                            if (player.getTarget().getActiveMarkers().getLast().equals(Marker.REFLECTIONMARKER)) {
                                player.getTarget().removeMarker();
                                // если у цели есть маркеры
                                if (!this.getActiveMarkers().isEmpty()) {
                                    // если у цели есть маркер отражения, то теряется маркер и перенаправляется на кастера
                                    if (this.getActiveMarkers().getLast().equals(Marker.REFLECTIONMARKER)) {
                                        this.removeMarker();
                                    }
                                    // если у цели есть маркер преграды, то теряется маркер и отражение поглощается
                                    else if (this.getActiveMarkers().getLast().equals(Marker.WALLMARKER)) {
                                        this.removeMarker();
                                        pingPong = false;
                                    }
                                    // если у цели есть нет маркера преграды и отражения, то заморозка срабатывает
                                    else {
                                        if (this.getHp() > 0) {
                                            // если монстр ледяной
                                            if (this.geteType().equals(ElementType.FROSTY)) {
                                                totalDamage = (frostDamage - 1 + damageReduction) <= 0 ? 1 : frostDamage - 1 + damageReduction;
                                                this.getDamage(positionPower + totalDamage);
                                                this.addMarker(Marker.FREEZEMARKER);
                                            }
                                            // если монстр огненный
                                            else if (this.geteType().equals(ElementType.FIERY)) {
                                                totalDamage = (frostDamage + 2 + damageReduction) <= 0 ? 1 : frostDamage + 2 + damageReduction;
                                                this.getDamage(positionPower + totalDamage);
                                                this.addMarker(Marker.FREEZEMARKER);
                                            } else {
                                                totalDamage = (frostDamage + damageReduction) <= 0 ? 1 : frostDamage + damageReduction;
                                                this.getDamage(positionPower + totalDamage);
                                                this.addMarker(Marker.FREEZEMARKER);
                                            }
                                        }
                                        pingPong = false;
                                    }
                                }
                                // если у цели есть нет маркеров, то заморозка срабатывает
                                else {
                                    if (this.getHp() > 0) {
                                        // если монстр ледяной
                                        if (this.geteType().equals(ElementType.FROSTY)) {
                                            totalDamage = (frostDamage - 1 + damageReduction) <= 0 ? 1 : frostDamage - 1 + damageReduction;
                                            this.getDamage(positionPower + totalDamage);
                                            this.addMarker(Marker.FREEZEMARKER);
                                        }
                                        // если монстр огненный
                                        else if (this.geteType().equals(ElementType.FIERY)) {
                                            totalDamage = (frostDamage + 2 + damageReduction) <= 0 ? 1 : frostDamage + 2 + damageReduction;
                                            this.getDamage(positionPower + totalDamage);
                                            this.addMarker(Marker.FREEZEMARKER);
                                        } else {
                                            totalDamage = (frostDamage + damageReduction) <= 0 ? 1 : frostDamage + damageReduction;
                                            this.getDamage(positionPower + totalDamage);
                                            this.addMarker(Marker.FREEZEMARKER);
                                        }
                                    }
                                    pingPong = false;
                                }
                            }
                            // если у кастера есть маркер преграды, то теряется маркер и отражение поглощается
                            else if (player.getTarget().getActiveMarkers().getLast().equals(Marker.WALLMARKER)) {
                                player.getTarget().removeMarker();
                                pingPong = false;
                            }
                            // если у кастера нет маркера, то заморозка срабатывает
                            else {
                                if (player.getTarget().getHp() > 0) {
                                    // если монстр ледяной
                                    if (player.getTarget().geteType().equals(ElementType.FROSTY)) {
                                        totalDamage = (frostDamage - 1 + damageReduction) <= 0 ? 1 : frostDamage - 1 + damageReduction;
                                        player.getTarget().getDamage(positionPower + totalDamage);
                                        player.getTarget().addMarker(Marker.FREEZEMARKER);
                                    }
                                    // если монстр огненный
                                    else if (player.getTarget().geteType().equals(ElementType.FIERY)) {
                                        totalDamage = (frostDamage + 2 + damageReduction) <= 0 ? 1 : frostDamage + 2 + damageReduction;
                                        player.getTarget().getDamage(positionPower + totalDamage);
                                        player.getTarget().addMarker(Marker.FREEZEMARKER);
                                    } else {
                                        totalDamage = (frostDamage + damageReduction) <= 0 ? 1 : frostDamage + damageReduction;
                                        player.getTarget().getDamage(positionPower + totalDamage);
                                        player.getTarget().addMarker(Marker.FREEZEMARKER);
                                    }
                                }
                                pingPong = false;
                            }
                            // если у кастера нет маркеров, то заморозка срабатывает
                        } else {
                            if (player.getTarget().getHp() > 0) {
                                // если монстр ледяной
                                if (player.getTarget().geteType().equals(ElementType.FROSTY)) {
                                    totalDamage = (frostDamage - 1 + damageReduction) <= 0 ? 1 : frostDamage - 1 + damageReduction;
                                    player.getTarget().getDamage(positionPower + totalDamage);
                                    player.getTarget().addMarker(Marker.FREEZEMARKER);
                                }
                                // если монстр огненный
                                else if (player.getTarget().geteType().equals(ElementType.FIERY)) {
                                    totalDamage = (frostDamage + 2 + damageReduction) <= 0 ? 1 : frostDamage + 2 + damageReduction;
                                    player.getTarget().getDamage(positionPower + totalDamage);
                                    player.getTarget().addMarker(Marker.FREEZEMARKER);
                                } else {
                                    totalDamage = (frostDamage + damageReduction) <= 0 ? 1 : frostDamage + damageReduction;
                                    player.getTarget().getDamage(positionPower + totalDamage);
                                    player.getTarget().addMarker(Marker.FREEZEMARKER);
                                }
                            }
                            pingPong = false;
                        }
                    }
                }
            } else {
                if (this.getHp() > 0) {
                    // если монстр ледяной
                    if (this.geteType().equals(ElementType.FROSTY)) {
                        totalDamage = (frostDamage - 1 + damageReduction) <= 0 ? 1 : frostDamage - 1 + damageReduction;
                        this.getDamage(positionPower + totalDamage);
                        this.addMarker(Marker.FREEZEMARKER);
                    }
                    // если монстр огненный
                    else if (this.geteType().equals(ElementType.FIERY)) {
                        totalDamage = (frostDamage + 2 + damageReduction) <= 0 ? 1 : frostDamage + 2 + damageReduction;
                        this.getDamage(positionPower + totalDamage);
                        this.addMarker(Marker.FREEZEMARKER);
                    } else {
                        totalDamage = (frostDamage + damageReduction) <= 0 ? 1 : frostDamage + damageReduction;
                        this.getDamage(positionPower + totalDamage);
                        this.addMarker(Marker.FREEZEMARKER);
                    }
                }
            }
        }
        // если заклинание свет
        else if (spell.getClass().equals(Light.class)) {
            int healPoints = 0;
            if (spellLevel == 1) healPoints = 2;
            if (spellLevel == 2) healPoints = 4;
            // если у цели есть маркеры
            if (!this.getActiveMarkers().isEmpty()) {
                // если цель заморожена
                if (this.getActiveMarkers().getLast().equals(Marker.FREEZEMARKER)) {
                    damageReduction = -2;
                } else if (this.getActiveMarkers().getLast().equals(Marker.WALLMARKER)) {
                    this.removeMarker();
                    System.out.println("Преграда поглотила урон!");
                } else if (this.getActiveMarkers().getLast().equals(Marker.REFLECTIONMARKER)) {
                    this.removeMarker();
                    boolean pingPong = true;
                    // пока в взаимодействующих есть маркер отражения или преграды
                    while (pingPong) {
                        // если у кастера есть маркеры
                        if (!player.getTarget().getActiveMarkers().isEmpty()) {
                            // если у кастера есть маркер отражения, то теряется маркер и перенаправляется на цель
                            if (player.getTarget().getActiveMarkers().getLast().equals(Marker.REFLECTIONMARKER)) {
                                player.getTarget().removeMarker();
                                // если у цели есть маркеры
                                if (!this.getActiveMarkers().isEmpty()) {
                                    // если у цели есть маркер отражения, то теряется маркер и перенаправляется на кастера
                                    if (this.getActiveMarkers().getLast().equals(Marker.REFLECTIONMARKER)) {
                                        this.removeMarker();
                                    }
                                    // если у цели есть маркер преграды, то теряется маркер и отражение поглощается
                                    else if (this.getActiveMarkers().getLast().equals(Marker.WALLMARKER)) {
                                        this.removeMarker();
                                        pingPong = false;
                                    }
                                    // если у цели есть нет маркера преграды и отражения, то свет срабатывает
                                    else {
                                        if (this.getHp() > 0) {
                                            if (this.getmType().equals(MonsterTypes.UNDEAD)) {
                                                totalDamage = (healPoints + damageReduction + positionPower) <= 0 ? 1 : healPoints + damageReduction + positionPower;
                                                this.getDamage(totalDamage);
                                                System.out.println("Нежить получила урон!");
                                            } else {
                                                this.heal(healPoints);
                                                if (this.hp > this.maxHp) hp = maxHp;
                                                System.out.println("у " + this + " здоровье восстановлено");
                                            }
                                        }
                                        pingPong = false;
                                    }
                                }
                                // если у цели есть нет маркеров, то свет срабатывает
                                else {
                                    if (this.getHp() > 0) {
                                        if (this.getmType().equals(MonsterTypes.UNDEAD)) {
                                            totalDamage = (healPoints + damageReduction + positionPower) <= 0 ? 1 : healPoints + damageReduction + positionPower;
                                            this.getDamage(totalDamage);
                                            System.out.println("Нежить получила урон!");
                                        } else {
                                            this.heal(healPoints);
                                            if (this.hp > this.maxHp) hp = maxHp;
                                            System.out.println("у " + this + " здоровье восстановлено");
                                        }
                                    }
                                    pingPong = false;
                                }
                            }
                            // если у кастера есть маркер преграды, то теряется маркер и отражение поглощается
                            else if (player.getTarget().getActiveMarkers().getLast().equals(Marker.WALLMARKER)) {
                                player.getTarget().removeMarker();
                                pingPong = false;
                            }
                            // если у кастера нет маркера, то свет срабатывает
                            else {
                                if (player.getTarget().getHp() > 0) {
                                    if (player.getTarget().getmType().equals(MonsterTypes.UNDEAD)) {
                                        totalDamage = (healPoints + damageReduction + positionPower) <= 0 ? 1 : healPoints + damageReduction + positionPower;
                                        player.getTarget().getDamage(totalDamage);
                                        System.out.println("Нежить получила урон!");
                                    } else {
                                        player.getTarget().heal(healPoints);
                                        if (player.getTarget().hp > player.getTarget().maxHp) hp = maxHp;
                                        System.out.println("у " + player.getTarget() + " здоровье восстановлено");
                                    }
                                }
                                pingPong = false;
                            }
                            // если у кастера нет маркеров, то свет срабатывает
                        } else {
                            if (player.getTarget().getHp() > 0) {
                                if (player.getTarget().getmType().equals(MonsterTypes.UNDEAD)) {
                                    totalDamage = (healPoints + damageReduction + positionPower) <= 0 ? 1 : healPoints + damageReduction + positionPower;
                                    player.getTarget().getDamage(totalDamage);
                                    System.out.println("Нежить получила урон!");
                                } else {
                                    player.getTarget().heal(healPoints);
                                    if (player.getTarget().hp > player.getTarget().maxHp) hp = maxHp;
                                    System.out.println("у " + player.getTarget() + " здоровье восстановлено");
                                }
                            }
                            pingPong = false;
                        }
                    }
                }
            } else {
                if (this.getHp() > 0) {
                    if (this.getmType().equals(MonsterTypes.UNDEAD)) {
                        totalDamage = (healPoints + damageReduction + positionPower) <= 0 ? 1 : healPoints + damageReduction + positionPower;
                        this.getDamage(totalDamage);
                        System.out.println("Нежить получила урон!");
                    } else {
                        this.heal(healPoints);
                        if (this.hp > this.maxHp) hp = maxHp;
                        System.out.println("у " + this + " здоровье восстановлено");
                    }
                }
            }
            // если заклинание молния
        } else if (spell.getClass().equals(LightningBolt.class)) {
            System.out.println("Молния!!!");
        }
        // если заклинание отражение
        else if (spell.getClass().equals(Reflection.class)) {
            // если игрок кастует на себя
            if (this.equals(player.getTarget())) {
                this.addMarker(Marker.REFLECTIONMARKER);
            }
            // если игрок кастует на другую цели и у нее есть маркеры
            else if (!this.getActiveMarkers().isEmpty()) {
                // если у цели маркер преграды, то отражение не проходит и маркер преграды сбрасывается
                if (this.getActiveMarkers().getLast().equals(Marker.WALLMARKER)) {
                    this.removeMarker();
                }
                // если у цели есть маркер отражения
                else if (this.getActiveMarkers().getLast().equals(Marker.REFLECTIONMARKER)) {
                    this.removeMarker();
                    boolean pingPong = true;
                    // пока в взаимодействующих есть маркер отражения или преграды
                    while (pingPong) {
                        // если у кастера есть маркеры
                        if (!player.getTarget().getActiveMarkers().isEmpty()) {
                            // если у кастера есть маркер отражения, то теряется маркер и перенаправляется на цель
                            if (player.getTarget().getActiveMarkers().getLast().equals(Marker.REFLECTIONMARKER)) {
                                player.getTarget().removeMarker();
                                // если у цели есть маркеры
                                if (!this.getActiveMarkers().isEmpty()) {
                                    // если у цели есть маркер отражения, то теряется маркер и перенаправляется на кастера
                                    if (this.getActiveMarkers().getLast().equals(Marker.REFLECTIONMARKER)) {
                                        this.removeMarker();
                                    }
                                    // если у цели есть маркер преграды, то теряется маркер и отражение поглощается
                                    else if (this.getActiveMarkers().getLast().equals(Marker.WALLMARKER)) {
                                        this.removeMarker();
                                        pingPong = false;
                                    }
                                    // если у цели есть нет маркера преграды и отражения, то цель получает маркер отражения
                                    else {
                                        if (spellLevel == 1) {
                                            this.addMarker(Marker.REFLECTIONMARKER);
                                            System.out.println(this + " получил маркер отражения!");
                                        }
                                        if (spellLevel == 2) {
                                            this.addMarker(Marker.REFLECTIONMARKER);
                                            this.addMarker(Marker.REFLECTIONMARKER);
                                            System.out.println(this + " получил 2 маркера отражения!");
                                        }
                                        pingPong = false;
                                    }
                                }
                                // если у цели есть нет маркеров, то цель получает маркер отражения
                                else {
                                    if (spellLevel == 1) {
                                        this.addMarker(Marker.REFLECTIONMARKER);
                                        System.out.println(this + " получил маркер отражения!");
                                    }
                                    if (spellLevel == 2) {
                                        this.addMarker(Marker.REFLECTIONMARKER);
                                        this.addMarker(Marker.REFLECTIONMARKER);
                                        System.out.println(this + " получил 2 маркера отражения!");
                                    }
                                    pingPong = false;
                                }
                            }
                            // если у кастера есть маркер преграды, то теряется маркер и отражение поглощается
                            else if (player.getTarget().getActiveMarkers().getLast().equals(Marker.WALLMARKER)) {
                                player.getTarget().removeMarker();
                                pingPong = false;
                            }
                            // если у кастера нет маркера отражения или преграды, то кастер получает маркер преграды
                            else {
                                if (spellLevel == 1) {
                                    player.getTarget().addMarker(Marker.REFLECTIONMARKER);
                                    System.out.println(player.getTarget() + " получил маркер отражения!");
                                }
                                if (spellLevel == 2) {
                                    player.getTarget().addMarker(Marker.REFLECTIONMARKER);
                                    player.getTarget().addMarker(Marker.REFLECTIONMARKER);
                                    System.out.println(player.getTarget() + " получил 2 маркера отражения!");
                                }
                                pingPong = false;
                            }
                            // если у кастера нет маркеров, то кастер получает маркер отражения
                        } else {
                            if (spellLevel == 1) {
                                player.getTarget().addMarker(Marker.REFLECTIONMARKER);
                                System.out.println(player.getTarget() + " получил маркер отражения!");
                            }
                            if (spellLevel == 2) {
                                player.getTarget().addMarker(Marker.REFLECTIONMARKER);
                                player.getTarget().addMarker(Marker.REFLECTIONMARKER);
                                System.out.println(player.getTarget() + " получил 2 маркера отражения!");
                            }
                            pingPong = false;
                        }
                    }
                }
                // если у цели нет маркеров отражения и преграды, то цель получает отражение
            } else {
                if (spellLevel == 1) {
                    this.addMarker(Marker.REFLECTIONMARKER);
                    System.out.println(this + " получил маркер отражения!");
                }
                if (spellLevel == 2) {
                    this.addMarker(Marker.REFLECTIONMARKER);
                    this.addMarker(Marker.REFLECTIONMARKER);
                    System.out.println(this + " получил 2 маркера отражения!");
                }
            }
            // если заклинание преграда
        } else if (spell.getClass().equals(Wall.class)) {
            if (!this.getActiveMarkers().isEmpty()) {
                if (this.getActiveMarkers().getLast().equals(Marker.WALLMARKER)) {
                    System.out.println("Преграда поглотила урон");
                    this.removeMarker();
                } else if (this.getActiveMarkers().getLast().equals(Marker.REFLECTIONMARKER)) {
                    this.removeMarker();
                    boolean pingPong = true;
                    // пока в взаимодействующих есть маркер отражения или преграды
                    while (pingPong) {
                        // если у кастера есть маркеры
                        if (!player.getTarget().getActiveMarkers().isEmpty()) {
                            // если у кастера есть маркер отражения, то теряется маркер и перенаправляется на цель
                            if (player.getTarget().getActiveMarkers().getLast().equals(Marker.REFLECTIONMARKER)) {
                                player.getTarget().removeMarker();
                                // если у цели есть маркеры
                                if (!this.getActiveMarkers().isEmpty()) {
                                    // если у цели есть маркер отражения, то теряется маркер и перенаправляется на кастера
                                    if (this.getActiveMarkers().getLast().equals(Marker.REFLECTIONMARKER)) {
                                        this.removeMarker();
                                    }
                                    // если у цели есть маркер преграды, то теряется маркер и отражение поглощается
                                    else if (this.getActiveMarkers().getLast().equals(Marker.WALLMARKER)) {
                                        this.removeMarker();
                                        pingPong = false;
                                    }
                                    // если у цели есть нет маркера преграды и отражения, то цель получает маркер преграды
                                    else {
                                        if (spellLevel == 1) {
                                            this.addMarker(Marker.WALLMARKER);
                                            System.out.println(this + " получил маркер преграды!");
                                        }
                                        if (spellLevel == 2) {
                                            this.addMarker(Marker.WALLMARKER);
                                            this.addMarker(Marker.WALLMARKER);
                                            System.out.println(this + " получил 2 маркера преграды!");
                                        }
                                        pingPong = false;
                                    }
                                }
                                // если у цели есть нет маркеров, то цель получает маркер преграды
                                else {
                                    if (spellLevel == 1) {
                                        this.addMarker(Marker.WALLMARKER);
                                        System.out.println(this + " получил маркер преграды!");
                                    }
                                    if (spellLevel == 2) {
                                        this.addMarker(Marker.WALLMARKER);
                                        this.addMarker(Marker.WALLMARKER);
                                        System.out.println(this + " получил 2 маркера преграды!");
                                    }
                                    pingPong = false;
                                }
                            }
                            // если у кастера есть маркер преграды, то теряется маркер и отражение поглощается
                            else if (player.getTarget().getActiveMarkers().getLast().equals(Marker.WALLMARKER)) {
                                player.getTarget().removeMarker();
                                pingPong = false;
                            }
                            // если у кастера нет маркера, он получает маркер преграды
                            else {
                                if (spellLevel == 1) {
                                    player.getTarget().addMarker(Marker.WALLMARKER);
                                    System.out.println(player.getTarget() + " получил маркер преграды!");
                                }
                                if (spellLevel == 2) {
                                    player.getTarget().addMarker(Marker.WALLMARKER);
                                    player.getTarget().addMarker(Marker.WALLMARKER);
                                    System.out.println(player.getTarget() + " получил 2 маркера преграды!");
                                }
                                pingPong = false;
                            }
                            // если у кастера нет маркеров, то кастер получает маркер преграды
                        } else {
                            if (spellLevel == 1) {
                                player.getTarget().addMarker(Marker.WALLMARKER);
                                System.out.println(player.getTarget() + " получил маркер преграды!");
                            }
                            if (spellLevel == 2) {
                                player.getTarget().addMarker(Marker.WALLMARKER);
                                player.getTarget().addMarker(Marker.WALLMARKER);
                                System.out.println(player.getTarget() + " получил 2 маркера преграды!");
                            }

                            pingPong = false;
                        }
                    }
                }
            } else {
                if (spellLevel == 1) this.addMarker(Marker.WALLMARKER);
                if (spellLevel == 2) {
                    System.out.println("Преграда 2 уровня!");
                    this.addMarker(Marker.WALLMARKER);
                    this.addMarker(Marker.WALLMARKER);
                }
            }
        }
    }

    /**
     * Метод добавляет маркер цели
     *
     * @param marker - маркер заклинания
     */
    public void addMarker(Marker marker) {
        activeMarkers.add(marker);
    }

    /**
     * Метод выводит активные маркеры цели
     *
     * @return активные маркеры цели
     */
    public LinkedList<Marker> getActiveMarkers() {
        return activeMarkers;
    }

    /**
     * Метод убирает маркер
     */
    public void removeMarker() {
        activeMarkers.removeLast();
    }

    /**
     * Фаза истощения
     */
    public void exhaustion() {
        if (activeMarkers.contains(Marker.WALLMARKER)) activeMarkers.remove(Marker.WALLMARKER);
        if (activeMarkers.contains(Marker.FREEZEMARKER)) activeMarkers.remove(Marker.FREEZEMARKER);
        if (activeMarkers.contains(Marker.REFLECTIONMARKER)) activeMarkers.remove(Marker.REFLECTIONMARKER);

    }
}
