
## Scanner
The scanner/lexer is the first stage of the interpreter that takes the raw source code as input and tranforms it into a list of tokens that will be consumed by the parser.

#### Features
<ul>
  <li>Single pass scanning: The scanner loops through the scanner one character at a time</li>
  <li>Keyword recognition: is able to differentiate between native and custom identifiers</li>
  <li>Support for literals: recognizes strings and floating-point numbers and extracts their values</li>
  <li>Comment support: supports single line comments</li>
  <li>Supports line numbering</li>
</ul>

#### How it works
<ol>
  <li>Initialize the scanner with the source code. The scanner keeps track of a cursor (current), a start position, and a line counter.</li>
  <li>For each character in the source code, there are 7 possible actions:</li>
  <ol>
    <li>Directly add the character to the token list if it has no literal (ex: '(', '}', etc).
    <li>Characters such as '<' or '!' can be <= and != respectively. For example, when encountering the character '>', see if the next token is an equal sign.
    <li>Characters ' ', '\r', and '\t' are ignored.
    <li>'\n' is also ignored, but adds 1 to the line number.
    <li>'/' can either be a division or a comment. If the next character is '/', consume the entire line as it is a comment.
    <li>'"' represents the start of string. So capture the entire string as a token.
    <li>The character is the start of an native/user-defined identifier. (ex: `while`, `if` ,`var1`).
  </ol>
</ol>

#### Example tokenization
For the input
```
var x = 3.14;
print x;
```

The scanner produces the following tokens:
```
[VAR, IDENTIFIER(x), EQUAL, NUMBER(3.14), SEMICOLON, PRINT, IDENTIFIER(x), SEMICOLON, EOF]
```
