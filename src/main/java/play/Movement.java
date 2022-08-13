package play;

import targets.Target;

@FunctionalInterface
public interface Movement {
    void to(Target from, Target to);
}
