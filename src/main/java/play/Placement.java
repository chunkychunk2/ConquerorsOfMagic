package play;

import targets.Target;

@FunctionalInterface
public interface Placement {
    void card(Player player, int spell, Target target, boolean direction, boolean newPlace);
}
