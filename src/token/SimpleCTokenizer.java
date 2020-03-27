package token;

import loader.TransitionMapLoader;

import java.util.*;

public class SimpleCTokenizer implements Tokenizer{

    private Map<String, TransitionState> transitionStateMap;
    private String input;
    private String currentState = "start";
    private String lastState = "start";
    private int currentInputIndex;
    private int tokenStartIndex;
    private Map<String, String> tokenMap;
    private Queue<Token> tokenBuffer = new LinkedList<>();
    private Set<String> keywords= Set.of(
            "double", "char", "int", "bool", "if", "else", "true", "false", "return", "static", "void", "while", "for", "string"
    );




    public SimpleCTokenizer() {
       var mapMapEntry = TransitionMapLoader.loadMap("resources/simpleCDescriptor.txt");

        transitionStateMap = mapMapEntry.getKey();
        tokenMap = mapMapEntry.getValue();
    }


    private void loadToken(){
        if (input.length() <= 0) return;
        char[] chars = input.toCharArray();

        while (currentInputIndex < input.length()){
            if (currentState.equals("start")) tokenStartIndex = currentInputIndex;
            lastState = currentState;

            if (currentState.equals("error")){
                System.out.println(currentState);
                System.out.println(lastState);
                System.out.println(currentInputIndex);
                System.out.println(input.length());
                System.out.println(input.charAt(currentInputIndex));
            }
            currentState = transitionStateMap.get(currentState).getNextState(chars[currentInputIndex]);

//            if (currentState == -1) throw new LexError();
            if (tokenMap.containsKey(currentState)){
                String tokenValue;
                if (currentInputIndex-tokenStartIndex == 0) tokenValue = new String(new char[]{input.charAt(tokenStartIndex)});
                else tokenValue = new String(Arrays.copyOfRange(chars, tokenStartIndex, currentInputIndex));
                if (keywords.contains(tokenValue)) tokenBuffer.add(new Token("keyword", tokenValue));
                else tokenBuffer.add(new Token(tokenMap.get(currentState), tokenValue));
                currentState = "start";
                if (currentInputIndex-tokenStartIndex == 0) currentInputIndex++;
                return;
            }
            currentInputIndex++;
        }

        currentState = transitionStateMap.get(currentState).getNextState('\0');
//            if (currentState == -1) throw new LexError();
        if (tokenMap.containsKey(currentState)){
            String tokenValue = new String(Arrays.copyOfRange(chars, tokenStartIndex, currentInputIndex));
            if (keywords.contains(tokenValue)) tokenBuffer.add(new Token("keyword", tokenValue));
            else tokenBuffer.add(new Token(tokenMap.get(currentState), tokenValue));
        }
        input = "";
    }

    @Override
    public void read(String input) {
        this.input = input;
    }

    @Override
    public boolean hasNext() {
        loadToken();
        return tokenBuffer.size() > 0 || currentInputIndex < input.length();
    }

    @Override
    public Token next() {
        return tokenBuffer.poll();
    }
}
