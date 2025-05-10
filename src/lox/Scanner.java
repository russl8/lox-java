package lox;

import java.sql.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static lox.TokenType.*;

public class Scanner {
    // let source code be a string
    private final String source;
    private final List<Token> tokens = new ArrayList<>();
    private int start = 0;
    private int current = 0;
    private int line = 1;

    Scanner(String source) {
        this.source = source;
    }

    /**
     * Fills tokens list from source code and appends an EOF token at the end.
     *
     * @return a reference to the tokens list
     */
    List<Token> scanTokens() {
        while (!isAtEnd()) {
            start = current;
            scanToken();
        }

        tokens.add(new Token(EOF, "", null, line));
        return tokens;
    }


    private void scanToken() {
        char c = advance();

        switch (c) {
            case '(': addToken(LEFT_PAREN); break;
            case ')': addToken(RIGHT_PAREN); break;
            case '{': addToken(LEFT_BRACE); break;
            case '}': addToken(RIGHT_BRACE); break;
            case ',': addToken(COMMA); break;
            case '.': addToken(DOT); break;
            case '-': addToken(MINUS); break;
            case '+': addToken(PLUS); break;
            case ';': addToken(SEMICOLON); break;
            case '*': addToken(STAR); break;
            default:
                // reports the error but keeps scanning to capture ALL errors.
                Lox.error(line,"Unexpected character.");
                break;
        }
    }

    /**
     *
     * @return whether scanner has finished reading the source file
     */
    private boolean isAtEnd() {
        return current >= source.length();
    }

    /**
     *  returns current character and goes to the next character in the source file.
     *
     * @return
     */
    private char advance() {
        current++;
        return source.charAt(current - 1);
    }

    /**
     * Adds tokens with null literals. ex: "(", "+", ...
     *
     * @param type
     */
    private void addToken(TokenType type) {
        addToken(type, null);
    }

    /**
     * Adds tokens with literals
     *
     * @param type
     * @param literal
     */
    private void addToken(TokenType type, Object literal) {
        String text = source.substring(start, current);
        tokens.add(new Token(type, text, literal, line));
    }
}
