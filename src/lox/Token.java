package lox;

public class Token {
    final TokenType type;
    final String lexeme;
    final Object literal;
    final int line;

    /**
     *
     * @param type type of token. ex: "for" is type IDENTIFIER
     * @param lexeme a blob of characters. ex: "int count = 5;"
     *               has lexemes: 'int', 'count', '=', '5', ';'
     * @param literal a lexeme representing a CONSTANT value in code.
     *                ex: 42 is an integer literal.
     * @param line the line number of the token.
     */
    Token(TokenType type, String lexeme, Object literal, int line) {
        this.type = type;
        this.lexeme = lexeme;
        this.literal = literal;
        this.line = line;
    }

    public String toString() {
        return type + " " + lexeme + " " + literal;
    }
}
