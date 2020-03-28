package token;

import loader.TransitionMapLoader;

import java.util.*;

public class SimpleCTokenizer implements Tokenizer{

    @Override
    public void read(String input) {

    }

    @Override
    public boolean hasNext() {
        return false;
    }

    @Override
    public Token next() {
        return null;
    }
}
