package spo2018;

import java.util.List;

/**
 * Created by Antoschka on 13.04.2018.
 */
public class LexerTest {
    public static void main(String args[]){
        Lexer lexer = new Lexer();
        Parser parser = new Parser();
        StackMachine stackMachine = new StackMachine();
        String input = "a = 8; for (i = 0; i <= a; i = i+1) { k = i*a; }";
        List<Token> tokens = lexer.recognize(input);


        System.out.println();
        System.out.println("input string: \n" + input);

        System.out.println();
        for (Token token : tokens) {
            System.out.println(token);
        }

        System.out.println();
        System.out.println("RPN:");
        System.out.println(parser.lang(tokens));
        System.out.println('\n');
        System.out.println(stackMachine.stackMachine(parser));
    }
}