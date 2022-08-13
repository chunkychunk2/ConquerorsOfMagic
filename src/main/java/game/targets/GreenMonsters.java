package game.targets;

public class GreenMonsters extends Target {

    public GreenMonsters(int damage, int hp, boolean b, String damageType,
                         String monsterType, String elementType, String name) {
        setDamagePower(damage);
        setHp(hp);
        setDash(b);
        if ("Magical".equals(damageType)) {
            setdType(DamageType.MAGICAL);
        } else setdType(DamageType.PHYSICAL);

        if ("Undead".equals(monsterType)) {
            setmType(MonsterTypes.UNDEAD);
        } else setmType(MonsterTypes.OTHERS);

        if ("Fiery".equals(elementType)) {
            seteType(ElementType.FIERY);
        } else if  ("Frosty".equals(elementType)){
            seteType(ElementType.FROSTY);
        } else seteType(ElementType.COMMON);
        setName(name);
    }


}
