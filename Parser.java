package spo2018;
/*
lang -> expr*
expr -> value
value -> DIGIT|VAR|brkt_expr
value_expr -> value (OP value)*
brkt_expr -> LEFT_BRACKET value_expr RIGHT_BRACKET
cond-branch -> KEYWORD condition body
condition -> brkt_expr
assign -> VAR ASSIGN value;
body -> LEFTFIGUREBRACKET expr* RIGHTFIGUREBRACKET

DIGIT -> ^0|[1-9][0-9]*"
OP -> AOP | LOP
AOP -> -|+|*|/
LOP -> == | != | > | < | >= | <=
ASSIGN -> =
VAR -> ^[a-zA-Z]+$
KEYWORD -> for
BRACKETS -> [(|)]
FIGUREBRACETS -> [{|}]

*/
import java.util.*;

public class Parser {
    Map<String, Integer> varTable = new HashMap<>();
    List<String> tokensInPRN = new ArrayList<>();
    private Stack<String> stack = new Stack<>();
    private List<Token> tokens = new ArrayList<>();
    private int position = 0;
    private int p1;
    private int p2;

    boolean lang(List<Token> tokens) {
        boolean lang = false;
        for (Token token : tokens) {
            if (token.getLexeme() != LexemPattern.WS) {
                this.tokens.add(token);
            }
        }
        while (this.tokens.size() != position) {
            if (!expr()) {
                System.err.println(" Ошибка синтаксиса ");
                System.exit(4);
            } else
                lang = true;
        }
        System.out.println(tokensInPRN);
        return lang;
    }

    private boolean expr() {
        boolean expr = false;

        if (/*init() ||*/ assign() || for_loop()){
            expr = true;
        }
        return expr;
    }

    /*private boolean init() {
        boolean init = false;
        int old_position = position;

        if (getCurrentTokenLexemeInc() == LexemPattern.TYPE) {
            if (assign_op()) {
                if (getCurrentTokenLexemeInc() == LexemPattern.SEM) {
                    init = true;
                }
            }
        }
        position = init ? position : old_position;
        return init;
    }*/

    private boolean assign() {
        boolean assign = false;
        int old_position = position;

        if (assign_op()) {
            if (getCurrentTokenLexemeInc() == LexemPattern.SEM) {
                assign = true;
            }
        }
        position = assign ? position : old_position;
        return assign;
    }

    private boolean assign_op() {
        boolean assign_op = false;
        int old_position = position;
        boolean add = false;
        String var;

        if (getCurrentTokenLexemeInc() == LexemPattern.VAR) {
            add = tokensInPRN.add(getLastTokenValue());
            var = getLastTokenValue();
            if (getCurrentTokenLexemeInc() == LexemPattern.ASSIGN_OP) {
                stack.push(getLastTokenValue());
                if (value()) {
                    assign_op = true;
                    add = true;
                    varTable.put(var, 0);
                }
            }
        }
        if (add && !assign_op) {
            varTable.remove(tokensInPRN.size()-1);
        }
        if (assign_op) {
            while (!stack.empty()) {
                tokensInPRN.add(stack.pop());
            }
        }
        position = assign_op ? position : old_position;
        return assign_op;
    }

    private boolean value() {
        boolean value = false;

        if (val()) {
            while (OPval()) {
            }
            value = true;
        }
        return value;
    }

    private boolean OPval() {
        boolean OPval = false;
        int old_position = position;

        if (getCurrentTokenLexemeInc() == LexemPattern.OP) {
            String arthOp = getLastTokenValue();
            while (getPriority(arthOp) <= getPriority(stack.peek())) {
                tokensInPRN.add(stack.pop());
            }
            stack.push(arthOp);
            if (val()) {
                OPval = true;
            }
        }
        position = OPval ? position : old_position;
        return OPval;
    }

    private boolean val() {
        boolean val = false;

        if (getCurrentTokenLexemeInc() == LexemPattern.VAR) {
            tokensInPRN.add(getLastTokenValue());
            if (!varTable.containsKey(getLastTokenValue())) {
                System.err.println("Ошибка: Конструкция " + getLastTokenValue() + " не определена");
                System.exit(6);
            }
            val = true;
        } else {
            position--;
        }
        if (getCurrentTokenLexemeInc() == LexemPattern.DIGIT) {
            tokensInPRN.add(getLastTokenValue());
            val = true;
        } else {
            position--;
        }
        if (break_value()) {
            val = true;
        }
        return val;
    }

    private boolean break_value() {
        boolean break_value = false;
        int old_position = position;

        if (getCurrentTokenLexemeInc() == LexemPattern.L_R_B) {
            stack.push(getLastTokenValue());
            if (value()) {
                if (getCurrentTokenLexemeInc() == LexemPattern.R_R_B) {
                    while (!stack.peek().equals("(")) {
                        tokensInPRN.add(stack.pop());
                    }
                    stack.pop();

                    break_value = true;
                }
            }
        }
        position = break_value ? position : old_position;
        return break_value;
    }

    private boolean for_loop() {
        boolean for_loop = false;
        int old_position = position;

        if (getCurrentTokenLexemeInc() == LexemPattern.FOR) {
            if (for_expr()) {
                if (for_body()) {
                    for_loop = true;
                    tokensInPRN.set(p1 ,String.valueOf(tokensInPRN.size()+2));
                    tokensInPRN.add(String.valueOf(p2));
                    tokensInPRN.add("!");
                }
            }
        }
        position = for_loop ? position : old_position;
        return for_loop;
    }

    private boolean for_body() {
        boolean for_body = false;
        int old_position = position;

        if (getCurrentTokenLexemeInc() == LexemPattern.L_F_B) {
            while (for_form_square()) {
            }
            if (getCurrentTokenLexemeInc() == LexemPattern.R_F_B) {
                for_body = true;
            }

        }
        position = for_body ? position : old_position;
        return for_body;
    }

    private boolean for_form_square() {
        boolean for_form_square = false;

        if (/*init() || */assign()) {
            for_form_square = true;
        }
        return for_form_square;
    }

    private boolean for_expr() {
        boolean for_expr = false;
        int old_position = position;

        if (getCurrentTokenLexemeInc() == LexemPattern.L_R_B) {
            if (start_expr()) {
                if (log_expr()) {
                    if (assign_op()) {
                        if (getCurrentTokenLexemeInc() == LexemPattern.R_R_B) {
                            for_expr = true;
                        }
                    }
                }
            }
        }
        position = for_expr ? position : old_position;
        return for_expr;
    }

    private boolean log_expr()
    {

        boolean log_expr = false;
        int old_position = position;

        p2 = tokensInPRN.size();
        if (assign_op() || value()) {
            if (getCurrentTokenLexemeInc() == LexemPattern.LOG_OP) {
                String log_op = getLastTokenValue();
                if (assign_op() || value()) {
                    if (getCurrentTokenLexemeInc() == LexemPattern.SEM) {
                        log_expr = true;
                        tokensInPRN.add(log_op);
                        p1 = tokensInPRN.size();
                        tokensInPRN.add("p1");
                        tokensInPRN.add("!F");
                    }
                }
            }
        }
        position = log_expr ? position : old_position;
        return log_expr;
    }

    private boolean start_expr() {
        boolean start_expr = false;

        if (/*init() || */assign()) {
            start_expr = true;
        }
        return start_expr;
    }

    private LexemPattern getCurrentTokenLexemeInc() {
        try {
            return tokens.get(position++).getLexeme();
        } catch (IndexOutOfBoundsException ex) {
            //System.err.println("Ошибка :ожидается лексема типа: " + LexemPattern.TYPE );
            System.exit(3);
        }
        return null;
    }

    private String getLastTokenValue() {
        return tokens.get(position-1).getValue();
    }

    private int getPriority(String str) {
        switch (str) {
            case "+":
                return 1;
            case "*":
                return 2;
            case "^":
                return 2;
            case "-":
                return 1;
            case "/":
                return 2;
            case "=":
                return 0;
            case "(":
                return 0;
            default:
                System.err.println("Ошибка символа: " + str);
                System.exit(5);
                return 0;
        }
    }
}
