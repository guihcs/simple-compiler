package token;

import java.util.ArrayList;
import java.util.List;

public class TransitionSet {

    private List<TransitionCase> transitionCases = new ArrayList<>();

    public void addCase(TransitionCase transitionCase){
        transitionCases.add(transitionCase);
    }

    public String getNextState(char index, char next){
        for (TransitionCase transitionCase : transitionCases) {
            if (transitionCase.check(index, next)) return transitionCase.getNextCase();
        }

        return "error";
    }
}
