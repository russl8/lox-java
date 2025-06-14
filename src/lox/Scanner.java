package lox;

import java.sql.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static lox.TokenType.*;

public class Scanner {

    private static final Map<String,TokenType> keywords;
    // let source code be a string
    private final String source;
    private final List<Token> tokens = new ArrayList<>();
    private int start = 0;
    private int current = 0;
    private int line = 1;
    static {
        keywords = new HashMap<>();
        keywords.put("and", AND);
        keywords.put("class", CLASS);
        keywords.put("else", ELSE);
        keywords.put("false", FALSE);
        keywords.put("for", FOR);
        keywords.put("fun", FUN);
        keywords.put("if", IF);
        keywords.put("nil", NIL);
        keywords.put("or", OR);
        keywords.put("print", PRINT);
        keywords.put("return", RETURN);
        keywords.put("super", SUPER);
        keywords.put("this", THIS);
        keywords.put("true", TRUE);
        keywords.put("var", VAR);
        keywords.put("while", WHILE);
    }
    /**
     * Init scanner using source code.
     *
     * @param source source code in string format.
     */
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
            case '(':
                addToken(LEFT_PAREN);
                break;
            case ')':
                addToken(RIGHT_PAREN);
                break;
            case '{':
                addToken(LEFT_BRACE);
                break;
            case '}':
                addToken(RIGHT_BRACE);
                break;
            case ',':
                addToken(COMMA);
                break;
            case '.':
                addToken(DOT);
                break;
            case '-':
                addToken(MINUS);
                break;
            case '+':
                addToken(PLUS);
                break;
            case ';':
                addToken(SEMICOLON);
                break;
            case '*':
                addToken(STAR);
                break;
            case ' ':
            case '\r':
            case '\t':
                break;
            case '\n':
                line++;
                break;
            case '/':
                // if comment, advance until EOL.
                if (match('/')) {
                    while (peek() != '\n' && !isAtEnd()) advance();

                } else {
                    addToken(SLASH);
                }
                break;
            case '"':
                string();
                break;
            case '!':
                addToken(match('=') ? BANG_EQUAL : BANG);
                break;
            case '<':
                addToken(match('=') ? LESS_EQUAL : LESS);
                break;
            case '>':
                addToken(match('=') ? GREATER_EQUAL : GREATER);
                break;
            case '=':
                addToken(match('=') ? EQUAL_EQUAL : EQUAL);
                break;
            default:
                if (isDigit(c)) {
                    number();
                } else if (isAlpha(c)) {
                    identifier();
                } else {
                    // reports the error but keeps scanning to capture ALL errors.
                    Lox.error(line, "Unexpected character.");
                }
                break;
        }
    }

    /**
     * Adds an identifier to token list.
     * An identifier can either be a token type stored in the 'keywords'
     * hashmap (e.g. and, true, var, etc. )
     * or it can be user-defined (if not in the map).
     */
    private void identifier() {
        while (isAlphaNumeric(peek())) advance();

        String text=source.substring(start,current);
        TokenType type = keywords.get(text);

        if (type==null) type= IDENTIFIER;
        addToken(type);
    }


    private void number() {
        while (isDigit(peek())) advance();

        // look for a fractional part.
        if (peek() == '.' && isDigit(peekNext())) {
            //consume the "."
            advance();

            while (isDigit(peek())) advance();
        }

        addToken(NUMBER,
                Double.parseDouble(source.substring(start, current)));
    }

    private void string() {
        while (peek() != '"' && !isAtEnd()) {
            if (peek() == '\n') line++;
            advance();
        }

        if (isAtEnd()) {
            Lox.error(line, "Unterminated string.");
            return;
        }
        advance();

        String value = source.substring(start + 1, current + 1);
        addToken(STRING, value);
    }

    private boolean isAlphaNumeric(char c) {
        return isAlpha(c) || isDigit(c);
    }

    private boolean isDigit(char c) {
        return c >= '0' && c <= '9';
    }

    private boolean isAlpha(char c) {
        return (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z') || c == '_';
    }

    /**
     * Look at current char without advancing.
     *
     * @return
     */
    private char peek() {
        if (isAtEnd()) return ('\0');

        return source.charAt(current);
    }


    private char peekNext() {
        if (current + 1 >= source.length()) return ('\0');

        return source.charAt(current + 1);
    }

    /**
     * Checks if the next character in source matches the expected token.
     *
     * @param expected the expected token.
     * @return
     */
    private boolean match(char expected) {
        if (source.charAt(current) != expected) return false;
        if (isAtEnd()) return false;
        advance();
        return true;
    }

    /**
     * @return whether scanner has finished reading the source file
     */
    private boolean isAtEnd() {
        return current >= source.length();
    }

    /**
     * returns current character and goes to the next character in the source file.
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
