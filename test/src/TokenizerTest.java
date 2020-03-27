package src;

import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.CsvSource;
import token.SimpleCTokenizer;
import token.Token;
import token.Tokenizer;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TokenizerTest {


    private Tokenizer initTokenizer(String input){
        Tokenizer tokenizer = new SimpleCTokenizer();
        tokenizer.read(input);
        return tokenizer;
    }



    @ParameterizedTest
    @CsvFileSource(resources = "../resources/token_test.txt")
    public void testToken(String input, String label) throws IOException {
        input = input.replaceAll("''", "\"");
        Tokenizer tokenizer = initTokenizer(input);


        assertTrue(tokenizer.hasNext());
        List<Token> tokenList = new ArrayList<>();
        while (tokenizer.hasNext()){
            tokenList.add(tokenizer.next());
        }

        assertEquals(1, tokenList.size());
        assertEquals(input, tokenList.get(0).getValue());
        assertEquals(label, tokenList.get(0).getLabel());
    }



    @Test
    public void testSimpleScript() throws IOException {

        String input = Files.readString(Path.of("test/resources/testscript1.txt"));
        List<String> tokenLabels = Files.readAllLines(Path.of("test/resources/ts1tokens.txt"));
        Tokenizer tokenizer = initTokenizer(input);
        List<Token> tokenList = new ArrayList<>();
        while (tokenizer.hasNext()){
            Token token = tokenizer.next();
            tokenList.add(token);
        }

        assertEquals(tokenLabels.size(), tokenList.size());

        for (int i = 0; i < tokenLabels.size(); i++) {
            assertEquals(tokenLabels.get(i), tokenList.get(i).getLabel());
        }
    }

}
