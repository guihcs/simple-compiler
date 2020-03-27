package token;

import java.util.List;

public class TransitionState {

    private List<TransitionCase> transitionCases;

    private TransitionState(List<TransitionCase> transitionCases) {
        this.transitionCases = transitionCases;
    }

    public String getNextState(char c){
        for (TransitionCase transitionCase : transitionCases) {
            if (transitionCase.checkTransition(c)) return transitionCase.getNextState();
        }
        return "error";
    }

    public static TransitionState of(TransitionCase ...transitionCases){
        return new TransitionState(List.of(transitionCases));
    }


    @Override
    public String toString() {
        return "TransitionState{" +
                "transitionCases=" + transitionCases +
                '}';
    }
}
