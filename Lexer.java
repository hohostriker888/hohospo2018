package spo2018;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
/**
 * Created by Antoschka on 13.04.2018.
 */
public class Lexer {
    private String accumulator = "";
    private int position = 0;
    private boolean waitForSuccess = true;
    private LexemPattern currentLexeme = null;

    List<Token> recognize(String input) {
        List<Token> tokens = new ArrayList<>();

        if (input.length() != 0) {
            while (position < input.length()) {
                accumulator += input.charAt(position++);
                boolean found = find();
                if (!found) {
                    if (!waitForSuccess) {
                        waitForSuccess = true;
                        Token token = new Token(currentLexeme, format(accumulator));
                        tokens.add(token);
                        accumulator = "";
                        back();
                    } else {
                        waitForSuccess = true;
                        System.err.println('\n' + "Ошибка распознавания ввода '" + accumulator + "' на позиции:" + position + "!");
                        System.exit(2);
                    }
                } else {
                    waitForSuccess = false;
                }
            }
            tokens.add(new Token(currentLexeme, accumulator));
        }else {
            System.err.println('\n' + "Ошибка: нулевая длина строки");
            System.exit(1);
        }
        return tokens;
    }

    private void back() {
        position--;
    }

    private boolean find() {
        for (LexemPattern lexeme : LexemPattern.values()) {
            Matcher matcher = lexeme.getPattern().matcher(accumulator);
            if (matcher.matches()) {
                currentLexeme = lexeme;
                return true;
            }
        }
        return false;
    }

    private String format(String accumulator) {
        return accumulator.substring(0, accumulator.length() - 1);
    }

    }


