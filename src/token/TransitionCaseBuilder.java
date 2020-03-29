package token;

import java.nio.charset.Charset;
import java.util.*;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;

public class TransitionCaseBuilder {


    private interface TriPredicate<T, K, V> {
        boolean test(T v1, K v2, V v3);
    }

    private final static Map<String, Character> characterMap = Map.of(
            "'\\n'", '\n',
            "'\\0'", '\0'
    );

    private final static Map<String, TriPredicate<Boolean, Set<Character>, Character>> functionMap = Map.of(
            "include", (b, s, c) -> s.contains(c) | b,
            "exclude", (b, s, c) -> !s.contains(c) && !b,
            "any", (b, s, c) -> true,
            "null", (b, s, c) -> c == '\0'
    );

    private final static Map<String, Predicate<Character>> macroMap = Map.of(
            "digit", Character::isDigit,
            "alphanum", Character::isLetterOrDigit,
            "blank", Character::isWhitespace,
            "alpha", Character::isLetter
    );

    public static TransitionCase build(String position, String function, String nextState, String[] charStringList){

        List<Character> charList = new ArrayList<>();
        List<Predicate<Character>> predicateList = new ArrayList<>();
        buildCharacterAndPredicateList(charStringList, charList, predicateList);

        Set<Character> characterSet = new HashSet<>(charList);

        BiPredicate<Character, Character> pred = getTestPredicate(position, function, predicateList, characterSet);

        return new TransitionCase(nextState, pred);
    }

    private static BiPredicate<Character, Character> getTestPredicate(String position, String function, List<Predicate<Character>> predicateList, Set<Character> characterSet) {
        return (i, f) -> {
                char character;
                Predicate<Character> preTestPredicate = c -> true;
                switch(position){
                    case "index": {
                        character = i;
                        break;
                    }
                    case "next": {
                        character = f;
                        break;
                    }
                    default: {
                        character = f;
                        if (macroMap.containsKey(position)){
                            preTestPredicate = macroMap.get(position);
                        }else {
                            char testChar = position.length() > 3 ? characterMap.get(position) : position.charAt(1);
                            preTestPredicate = c -> c == testChar;
                        }
                    }
                }

                boolean testResult = false;
                for (Predicate<Character> characterPredicate : predicateList) {
                    testResult = testResult || characterPredicate.test(character);
                }
                return preTestPredicate.test(i) && functionMap.get(function).test(testResult, characterSet, character);
            };
    }

    private static void buildCharacterAndPredicateList(String[] charStringList, List<Character> charList, List<Predicate<Character>> predicateList) {
        for (String charValue : charStringList) {
            if (!charValue.startsWith("'")) {
                predicateList.add(macroMap.get(charValue));
            } else if (charValue.length() > 3) {
                charList.add(characterMap.get(charValue));
            } else {
                charList.add(charValue.charAt(1));
            }
        }
    }


}
