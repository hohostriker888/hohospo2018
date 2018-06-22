package spo2018;

/**
 * Created by Antoschka on 13.04.2018.
 */
public class Token {
    private LexemPattern lexeme;
    private String value;

    Token(LexemPattern lexeme, String value) {
        this.lexeme = lexeme;
        this.value = value;
    }

    LexemPattern getLexeme() {
        return lexeme;
    }
    String getValue()  {
        return value;
    }
    @Override
    public String toString(){
        return "Token(" +
                "value=" + value + '\'' + '}';
    }

}
