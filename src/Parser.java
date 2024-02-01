import java.util.HashMap;

public class Parser
{
    public static final int OP          = 10;    // +  -  *  /
    public static final int RELOP       = 11;    // <  >  <=  >=  ...
    public static final int LPAREN      = 12;    // (
    public static final int RPAREN      = 13;    // )
    public static final int SEMI        = 14;    // ;
    public static final int COMMA       = 15;    // ,
    public static final int INT         = 16;    // int
    public static final int NUM         = 17;    // number
    public static final int ID          = 18;    // identifier
    public static final int PRINT       = 19;    // print
    public static final int ASSIGN      = 20;
    public static final int TYPEOF      = 21;
    public static final int VAR         = 22;
    public static final int FUNC        = 23;
    public static final int IF          = 24;
    public static final int THEN        = 25;
    public static final int ELSE        = 26;
    public static final int WHILE       = 27;
    public static final int VOID        = 28;
    public static final int BEGIN       = 29;
    public static final int END         = 30;


    Compiler         compiler;
    Lexer            lexer;     // lexer.yylex() returns token-name
    public ParserVal yylval;    // yylval contains token-attribute

    public Parser(java.io.Reader r, Compiler compiler) throws Exception
    {
        this.compiler = compiler;
        this.lexer    = new Lexer(r, this);
    }

    // 1. parser call lexer.yylex that should return (token-name, token-attribute)
    // 2. lexer
    //    a. assign token-attribute to yyparser.yylval
    //       token attribute can be lexeme, line number, colume, etc.
    //    b. return token-id defined in Parser as a token-name
    // 3. parser print the token on console
    //    if there was an error (-1) in lexer, then print error message
    // 4. repeat until EOF (0) is reached
    public int yyparse() throws Exception
    {
        while ( true )
        {
            int token = lexer.yylex();  // get next token-name
            Object attr = yylval.obj;   // get      token-attribute
            String tokenname = "NULLVAL";

            // if(token == 0)
            // {
            //     // EOF is reached
            //     System.out.println("Success!");
            //     return 0;
            // }
            // if(token == -1)
            // {
            //     // lexical error is found
            //     System.out.println("Error! There is a lexical error at " + lexer.lineno + ":" + lexer.column + ".");
            //     return -1;
            // }

            switch(token) {
                case -1:
                    // lexical error is found
                    System.out.println("Error! There is a lexical error at " + lexer.lineno + ":" + lexer.column + ".");
                    return -1;
                case 0:
                    // EOF is reached
                    System.out.println("Success!");
                    return 0;
                case OP:
                    tokenname = "OP";
                    break;
                case RELOP:
                    tokenname = "RELOP";
                    break;
                case LPAREN:
                    tokenname = "LPAREN";
                    break;
                case RPAREN:
                    tokenname = "RPAREN";
                    break;
                case SEMI:
                    tokenname = "SEMI";
                    break;
                case COMMA:
                    tokenname = "COMMA";
                    break;
                case INT:
                    tokenname = "INT";
                    break;
                case NUM:
                    tokenname = "NUM";
                    break;
                case ID:
                    tokenname = "ID";
                    break;
                case PRINT:
                    tokenname = "PRINT";
                    break;
                case ASSIGN:
                    tokenname = "ASSIGN";
                    break;
                case TYPEOF:
                    tokenname = "TYPEOF";
                    break;
                case VAR:
                    tokenname = "VAR";
                    break;
                case FUNC:
                    tokenname = "FUNC";
                    break;
                case IF:
                    tokenname = "IF";
                    break;
                case THEN:
                    tokenname = "THEN";
                    break;
                case ELSE:
                    tokenname = "ELSE";
                    break;
                case WHILE:
                    tokenname = "WHILE";
                    break;
                case VOID:
                    tokenname = "VOID";
                    break;
                case BEGIN:
                    tokenname = "BEGIN";
                    break;
                case END:
                    tokenname = "END";
                    break;
            }
            System.out.println("<" + tokenname + ", token-attr:\"" + attr + "\", " + lexer.lineno + ":" + lexer.column + ">");
        }
    }
}
