package token;

public class Token {

    private String label;
    private String value;

    public Token(String label, String value) {
        this.label = label;
        this.value = value;
    }


    public String getLabel() {
        return label;
    }

    public String getValue() {
        return value;
    }


    @Override
    public String toString() {
        return "Token{" +
                "label='" + label + '\'' +
                ", value='" + value + '\'' +
                '}';
    }
}
