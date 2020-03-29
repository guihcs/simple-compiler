package token;

public interface Tokenizer {

    void read(String input);
    boolean hasNext() throws LexError;
    Token next();
}
