package loader;

import token.TransitionCase;
import token.TransitionState;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class TransitionMapLoader {

    private static Map<String, Character> characterMap = Map.of(
            "'\\n'", '\n',
            "'\\t'", '\t'
    );

    private static Map<String, Character[]> macroMap = new HashMap<>();

    static {
        Character[] num = new Character['9' - '0' + 1];
        Character[] alpha = new Character[('z' - 'a' + 1) * 2];
        Character[] alphanum = new Character[alpha.length + num.length];

        for (int i = 0; i <= 'z' - 'a'; i++) {
            alpha[i] = (char)('A' + i);
            alpha[i + ('z' - 'a' + 1)] = (char)('a' + i);
            alphanum[i] = (char)('A' + i);
            alphanum[i + ('z' - 'a' + 1)] = (char)('a' + i);
        }

        for (int i = 0; i <= '9' - '0'; i++) {
            num[i] = (char)('0' + i);
            alphanum[i + ('z' - 'a' + 1) * 2] = (char)('0' + i);
        }

        macroMap.put("alphanum", alphanum);
        macroMap.put("alpha", alpha);
        macroMap.put("digit", num);
    }



    public static Map.Entry<Map<String, TransitionState>, Map<String, String>> loadMap(String path){
        Map<String, TransitionState> transitionStateMap = new HashMap<>();
        Map<String, String> labelMap = new HashMap<>();
        List<TransitionCase> transitionCaseList = new ArrayList<>();
        try {
            List<String> strings = Files.readAllLines(Paths.get(path));
            int index = 0;
            int readCase = 0;
            String stateVal = null;
            while (index < strings.size()){
                String line = strings.get(index);
                if (line.isBlank()){
                    index++;
                    continue;
                }
                if (line.startsWith(":")){
                    if (stateVal != null){
                        transitionStateMap.put(stateVal, TransitionState.of(transitionCaseList.toArray(new TransitionCase[0])));
                        transitionCaseList.clear();
                    }
                    readCase = 0;
                    stateVal = line.replaceAll(":", "");
                    line = strings.get(++index);
                }else if (line.startsWith("!")){
                    if (stateVal != null){
                        transitionStateMap.put(stateVal, TransitionState.of(transitionCaseList.toArray(new TransitionCase[0])));
                        transitionCaseList.clear();
                    }
                    readCase = 1;
                    line = strings.get(++index);
                }
                switch (readCase){
                    case 0:{
                        String[] split = line.split(", ");
                        if (split[0].equals("null") || split[0].equals("any")){
                            transitionCaseList.add(TransitionCase.of(TransitionCase.getPredicate(split[0]), split[1]));

                        }else {
                            Character[] charList = new Character[split.length-2];
                            for (int i = 0; i < split.length-2; i++) {
                                if(macroMap.containsKey(split[i+2])){
                                    Character[] values = macroMap.get(split[i + 2]);
                                    Character[] newList = new Character[charList.length + values.length - 1];
                                    System.arraycopy(charList, 0, newList, 0, i);
                                    System.arraycopy(values, 0, newList, newList.length - values.length, values.length);
                                    charList = newList;
                                }else if(split[i+2].length() > 3){
                                    charList[i] = characterMap.get(split[i+2]);
                                }else {

                                    charList[i] = split[i+2].charAt(1);
                                }
                            }
                            transitionCaseList.add(TransitionCase.of(TransitionCase.getPredicate(split[0]), split[1], charList));
                        }
                        break;
                    }
                    case 1: {
                        String[] split = line.split(", ");
                        labelMap.put(split[0], split[1].trim());
                        break;
                    }

                }

                index++;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return Map.entry(transitionStateMap, labelMap);
    }


}
