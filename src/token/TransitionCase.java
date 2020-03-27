package token;

import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

public class TransitionCase {

    public static final BiPredicate<Set<Character>, Character> INCLUSION = Set::contains;
    public static final BiPredicate<Set<Character>, Character> EXCLUSION = (s, c) -> !s.contains(c);
    public static final BiPredicate<Set<Character>, Character> NULL = (s, c) -> c == '\0';
    public static final BiPredicate<Set<Character>, Character> ANY = (s, c) -> true;

    private static final Map<String, BiPredicate<Set<Character>, Character>> predicateMap = Map.of(
            "inclusion", INCLUSION,
            "exclusion", EXCLUSION,
            "null", NULL,
            "any", ANY
    );



    private BiPredicate<Set<Character>, Character> transitionCase;
    private String nextState;
    private Set<Character> characterSet;

    private TransitionCase(BiPredicate<Set<Character>, Character> transitionCase, String nextState, Set<Character> characterSet) {
        this.transitionCase = transitionCase;
        this.nextState = nextState;
        this.characterSet = characterSet;
    }

    public boolean checkTransition(char c){
        return transitionCase.test(characterSet, c);
    }


    public String getNextState(){
        return nextState;
    }

    public static TransitionCase of(BiPredicate<Set<Character>, Character> transitionStrategy, String nextState, Character ...testSet){
        return new TransitionCase(transitionStrategy, nextState, new HashSet<>(List.of(testSet)));
    }


    public static BiPredicate<Set<Character>, Character> getPredicate(String name){
        return predicateMap.get(name);
    }

    @Override
    public String toString() {
        return "TransitionCase{" +
                "transitionCase=" + transitionCase +
                ", nextState=" + nextState +
                ", characterSet=" + characterSet +
                '}';
    }
}
