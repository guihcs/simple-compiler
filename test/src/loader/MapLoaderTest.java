package src.loader;

import loader.TokenizerConfigLoader;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MapLoaderTest {


    @ParameterizedTest
    @CsvSource({
            "test/resources/loader/loader-t1.txt, 2, 1"
    })
    public void testLoad(String path, int stateCount, int labelCount) throws IOException {

        TokenizerConfigLoader configLoader = TokenizerConfigLoader.load(path);

        assertEquals(stateCount, configLoader.getTransitionSetMap().size());
        assertEquals(labelCount, configLoader.getLabelMap().size());
    }


}
