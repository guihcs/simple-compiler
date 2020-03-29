package token;

import loader.TokenizerConfigLoader;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

public class SimpleCTokenizer implements Tokenizer {

    private Map<String, TransitionSet> transitionSetMap;
    private Map<String, String> labelMap;
    private Token nextToken;
    private String input;
    private String currentState = "start";
    private int currentIndex;
    private int tokenStartIndex;
    private Set<String> keywords = Set.of(
            "double", "char", "int", "string", "true",
            "false", "if", "else", "for", "while", "void", "static", "return",
            "do", "switch", "break", "case"
    );

    public SimpleCTokenizer() {
        try {
            TokenizerConfigLoader configLoader = TokenizerConfigLoader.load("resources/simpleCDescriptor.txt");
            transitionSetMap = configLoader.getTransitionSetMap();
            labelMap = configLoader.getLabelMap();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadToken() throws LexError {

        while (currentIndex < input.length()){

            TransitionSet transitionSet = transitionSetMap.get(currentState);
            char currentChar = input.charAt(currentIndex);
            if(!continueTransition(transitionSet, currentChar)) break;
        }

        if (!currentState.equals("start")) {
            TransitionSet transitionSet = transitionSetMap.get(currentState);
            currentIndex--;
            continueTransition(transitionSet, '\0');
        }
    }

    private boolean continueTransition(TransitionSet transitionSet, char currentChar) throws LexError {
        char nextChar = currentIndex + 1 < input.length() ? input.charAt(currentIndex+1) : '\0';
        String lastState = currentState;
        if (transitionSet == null) throw new LexError("Invalid character at " + currentIndex);
        currentState = transitionSet.getNextState(currentChar, nextChar);
        if (currentState.equals("error")) throw new LexError("Invalid character at " + currentIndex);
        if (lastState.equals("start") && !currentState.equals("start")) tokenStartIndex = currentIndex;
        if (labelMap.containsKey(currentState)){
            String tokenValue = input.substring(tokenStartIndex, currentIndex+1);
            if (labelMap.get(currentState).equals("id") && keywords.contains(tokenValue)) {
                nextToken = new Token("keyword", tokenValue);
            }else {
                nextToken = new Token(labelMap.get(currentState), tokenValue);
            }
            tokenStartIndex = ++currentIndex;
            currentState = "start";
            return false;
        }

        currentIndex++;
        return true;
    }

    @Override
    public void read(String input) {
        this.input = input;
    }

    @Override
    public boolean hasNext() throws LexError {
        loadToken();
        return nextToken != null;
    }

    @Override
    public Token next() {
        Token token = nextToken;
        nextToken = null;
        return token;
    }
}
