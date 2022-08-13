package targets;

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
        if ("FIERY".equals(elementType)) {
            seteType(ElementType.FIERY);
        } else seteType(ElementType.FROSTY);
        setName(name);
    }


}
