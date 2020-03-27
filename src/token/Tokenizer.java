package token;

public interface Tokenizer {

    void read(String input);
    boolean hasNext();
    Token next();
}
