package token;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiPredicate;

public class TransitionCase {

    private String nextCase;
    private BiPredicate<Character, Character> checkFunction;

    public TransitionCase(String nextCase, BiPredicate<Character, Character> checkFunction) {
        this.nextCase = nextCase;
        this.checkFunction = checkFunction;
    }

    public boolean check(char index, char next){
        return checkFunction.test(index, next);
    }

    public String getNextCase() {
        return nextCase;
    }
}
