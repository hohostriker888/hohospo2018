package spo2018;

import java.util.regex.Pattern;


/**
 * Created by Antoschka on 13.04.2018.
 */
public enum LexemPattern {
    //TYPE(Pattern.compile("^int$")),
    FOR(Pattern.compile("^for$")),
    VAR(Pattern.compile("^[a-zA-Z]+$")),
    ASSIGN_OP(Pattern.compile("^=$")),
    DIGIT(Pattern.compile("^0|[1-9][0-9]*")),
    OP(Pattern.compile("^\\+|-|\\*|/|%|\\^$")),
    WS(Pattern.compile("^\\s+")),
    L_F_B(Pattern.compile("^\\{$")),
    R_F_B(Pattern.compile("^}$")),
    L_R_B(Pattern.compile("^\\($")),
    R_R_B(Pattern.compile("^\\)$")),
    LOG_OP(Pattern.compile("^<|>|<=|>=|!=|==$")),
    SEM(Pattern.compile("^;$"));

    private Pattern pattern;

    LexemPattern(Pattern pattern) {
        this.pattern = pattern;
    }

    public Pattern getPattern() {
        return pattern;
    }

}