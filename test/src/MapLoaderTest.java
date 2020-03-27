package src;

import loader.TransitionMapLoader;
import org.junit.jupiter.api.Test;
import token.TransitionState;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

public class MapLoaderTest {


    @Test
    public void test1(){

        var integerTransitionStateMap = TransitionMapLoader.loadMap("resources/simpleCDescriptor.txt");

        var tmap = integerTransitionStateMap.getKey();
        var tlab = integerTransitionStateMap.getValue();


/*        for (Integer integer : tmap.keySet()) {
            System.out.println(integer);
            System.out.println(tmap.get(integer));
            System.out.println("--------------------------");
        }


        System.out.println(tlab);*/
    }


}
