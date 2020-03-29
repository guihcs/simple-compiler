package loader;

import token.TransitionCase;
import token.TransitionCaseBuilder;
import token.TransitionSet;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

public class TokenizerConfigLoader {

    private Map<String, String> labelMap;
    private Map<String, TransitionSet> transitionSetMap;

    private TokenizerConfigLoader(Map<String, String> labelMap, Map<String, TransitionSet> transitionSetMap) {
        this.labelMap = labelMap;
        this.transitionSetMap = transitionSetMap;
    }

    public static TokenizerConfigLoader load(String path) throws IOException {
        List<String> lines = Files.readAllLines(Path.of(path));
        Map<String, TransitionSet> transitionSetMap = new HashMap<>();
        Map<String, String> labelMap = new HashMap<>();
        TransitionSet currentSet = null;
        String currentLabel = null;

        boolean isParsingState = true;

        for (String line : lines) {
            if (line.isBlank() || line.startsWith("#")) continue;
            if (line.startsWith(":")){
                if (currentLabel != null){
                    transitionSetMap.put(currentLabel, currentSet);
                }
                currentLabel = line.replaceFirst(":", "");
                currentSet = new TransitionSet();
            }else if(line.startsWith("|")){
                if (currentLabel != null){
                    transitionSetMap.put(currentLabel, currentSet);
                }
                isParsingState = false;
            }else if (isParsingState) {
                currentSet.addCase(getCase(line));
            }else {
                String[] split = line.split(",");
                if (split.length < 2) labelMap.put(split[0].trim(), split[0].trim());
                else labelMap.put(split[0].trim(), split[1].trim());
            }
        }
        return new TokenizerConfigLoader(labelMap, transitionSetMap);
    }



    private static TransitionCase getCase(String line){
        String[] split = line.split(", ");
        String[] chars = new String[split.length -3];
        System.arraycopy(split, 3, chars, 0, chars.length);

        return TransitionCaseBuilder.build(split[0], split[1], split[2], chars);
    }


    public Map<String, String> getLabelMap() {
        return labelMap;
    }

    public Map<String, TransitionSet> getTransitionSetMap() {
        return transitionSetMap;
    }
}
