import java.util.ArrayList;
import java.util.HashMap;

public class Lexer
{
    private static final char EOF        =  0;

    private Parser         yyparser; // parent parser object
    private java.io.Reader reader;   // input stream
    public int             lineno;   // line number
    public int             column;   // column
    ArrayList<Character>   input;    // arraylist of all characters in file
    private int            f;
    private int            lB;
    public HashMap<String, Integer> symbol_table;

    public Lexer(java.io.Reader reader, Parser yyparser) throws Exception
    {
        this.reader   = reader;
        this.yyparser = yyparser;
        lineno = 1;
        column = 0;
        init();
    }

    private void init() throws Exception {
        // initialize arraylist
        this.f = -1;
        this.lB = 1;
        this.input = new ArrayList<>();
        int data = reader.read();
        if (data != -1) this.input.add((char) data);
        while(data != -1){
            data = reader.read();
            char dataChar = (char) data;
            this.input.add(dataChar);
        }

        this.symbol_table = new HashMap<>();
        // initialize symbol table
        this.symbol_table.put("int", Parser.INT);
        this.symbol_table.put("print", Parser.PRINT);
        this.symbol_table.put("var", Parser.VAR);
        this.symbol_table.put("func", Parser.FUNC);
        this.symbol_table.put("if", Parser.IF);
        this.symbol_table.put("then", Parser.THEN);
        this.symbol_table.put("else", Parser.ELSE);
        this.symbol_table.put("while", Parser.WHILE);
        this.symbol_table.put("void", Parser.VOID);
        this.symbol_table.put("begin", Parser.BEGIN);
        this.symbol_table.put("end", Parser.END);
    }

    public char NextChar() throws Exception
    {
        // http://tutorials.jenkov.com/java-io/readers-writers.html
        // int data = reader.read();
        // if(data == -1)
        // {
        //     return EOF;
        // }
        // this.column++;
        // return (char)data;
        
        this.column++;
        this.f++;
        if (this.f < this.input.size()){
            return (char) this.input.get(this.f);
        } else return EOF;
    }
    public int Fail()
    {
        return -1;
    }

    private int install_id(String id) {
        if (!this.symbol_table.containsKey(id)) {
            // System.out.println("Adding " + id + " to the symbol table.");
            this.symbol_table.put(id, Parser.ID);
        }
        return this.symbol_table.get(id);
    }

    private String get_token(String in) {
        if (this.symbol_table.containsKey(in) && this.symbol_table.get(in) == Parser.ID) {
            return in;
        }
        return "id";
    }

    // * If yylex reach to the end of file, return  0
    // * If there is an lexical error found, return -1
    // * If a proper lexeme is determined, return token <token-id, token-attribute> as follows:
    //   1. set token-attribute into yyparser.yylval
    //   2. return token-id defined in Parser
    //   token attribute can be lexeme, line number, colume, etc.
    public int yylex() throws Exception
    {
        int state = 0;
        while(true)
        {
            char c;
            switch(state)
            {
                case 0:
                    c = NextChar();
                    if (c == '+')               { state = 1; continue; }
                    if (c == '-')               { state = 2; continue; }
                    if (c == '*')               { state = 3; continue; }
                    if (c == '/')               { state = 4; continue; }
                    if (c == '<')               { state = 5; continue; }
                    if (c == '>')               { state = 9; continue; }
                    if (c == '=')               { state = 12; continue; }
                    if (c == '(')               { state = 13; continue; }
                    if (c == ')')               { state = 14; continue; }
                    if (c == ';')               { state = 15; continue; }
                    if (c == ',')               { state = 16; continue; }
                    if (c == ':')               { state = 17; continue; }
                    if (Character.isDigit(c))   { state = 20; continue; }
                    if (Character.isLetter(c))  { state = 24; continue; }
                    if (c == ' ' || c == '\t'
                        || c == '\f')           { state = 26; continue; }
                    if (c == '\n')              { state = 27; continue; }
                    if (c == '\r')              { state = 28; continue; }
                    if (c == EOF)               { state = 9999; continue; }
                    // System.out.print("character: "); System.out.print( "\\u" + Integer.toHexString(c | 0x10000).substring(1) ); System.out.println(" got through. this.f: " + this.f + " and this.input.size(): " + this.input.size());
                    return Fail();
                case 1:
                    yyparser.yylval = new ParserVal((Object)"+");
                    return Parser.OP;
                case 2:
                    yyparser.yylval = new ParserVal((Object)"-");
                    return Parser.OP;
                case 3:
                    yyparser.yylval = new ParserVal((Object)"*");
                    return Parser.OP;
                case 4:
                    yyparser.yylval = new ParserVal((Object)"/");
                    return Parser.OP;
                case 5:
                    c = NextChar();
                    if (c == '=') { state = 6; }
                    else if (c == '>') { state = 7; }
                    else { state = 8; }
                    continue;
                case 6:
                    yyparser.yylval = new ParserVal((Object)"<=");
                    return Parser.RELOP;
                case 7:
                    yyparser.yylval = new ParserVal((Object)"<>");
                    return Parser.RELOP;
                case 8:
                    yyparser.yylval = new ParserVal((Object)"<");
                    this.f--; // retract
                    this.column--;
                    return Parser.RELOP;
                case 9:
                    c = NextChar();
                    if (c == '=') { state = 10; }
                    else { state = 11; }
                    continue;
                case 10:
                    yyparser.yylval = new ParserVal((Object)">=");
                    return Parser.RELOP;
                case 11:
                    yyparser.yylval = new ParserVal((Object)">");
                    this.f--; // retract
                    this.column--;
                    return Parser.RELOP;
                case 12:
                    yyparser.yylval = new ParserVal((Object)"=");
                    return Parser.RELOP;
                case 13:
                    yyparser.yylval = new ParserVal((Object)"(");
                    return Parser.LPAREN;
                case 14:
                    yyparser.yylval = new ParserVal((Object)")");
                    return Parser.RPAREN;    
                case 15:
                    yyparser.yylval = new ParserVal((Object)";");
                    return Parser.SEMI;
                case 16:
                    yyparser.yylval = new ParserVal((Object)",");
                    return Parser.COMMA;
                case 17: // assign, typeof
                    c = NextChar();
                    if (c == '=') { state = 18; }
                    else if (c == ':') { state = 19; }
                    continue;
                case 18:
                    yyparser.yylval = new ParserVal((Object)":=");
                    return Parser.ASSIGN;
                case 19:
                    yyparser.yylval = new ParserVal((Object)"::");
                    return Parser.TYPEOF;
                case 20: // number state
                    this.lB = this.f;
                    c = NextChar();
                    if (Character.isDigit(c)) { state = 21; }
                    else { state = 23; }
                    continue;
                case 21: // number state
                    c = NextChar();
                    if (Character.isDigit(c)) { break; }
                    else if (c == '.') { state = 22; }
                    else { state = 23; }
                    continue;
                case 22: // number state
                    c = NextChar();
                    if (Character.isDigit(c)) { break; }
                    else { this.f--; state = 23; }
                    continue;
                case 23: // number state
                    String num = "";
                    for (int i = lB; i < f; i++) {
                        num += this.input.get(i);
                    }
                    yyparser.yylval = new ParserVal((Object)num);
                    
                    this.column = this.lB;
                    this.lB = this.f - this.lB;
                    return Parser.NUM;
                case 24: // id/keyword initial state
                    this.lB = this.f;
                    c = NextChar();
                    this.column--;
                    if (Character.isLetter(c) || c == '_') { state = 241; }
                    else { state = 25;}
                    continue;
                case 241: // id/keyword loop state
                    c = NextChar();
                    this.column--;
                    if (Character.isLetter(c) || c == '_') { state = 241; }
                    else { state = 25; }   
                    continue;    
                case 25: // id/keyword return state
                    String in = "";
                    for (int i = lB; i < f; i++) {
                        in += this.input.get(i);
                    }
                    yyparser.yylval = new ParserVal((Object)in);
                    this.install_id(in);
                    // String token = this.get_token(in);
                    this.f--; // retract
                    this.column--;
                    return this.symbol_table.get(in);
                case 26: // whitespace
                    state = 0;
                    // --;
                    break;
                case 27: // newline
                    state = 0;
                    this.lineno++;
                    this.column = 0;
                    break;
                case 28: // carriage return
                    state = 0;
                    break;
                case 9999: // end of file
                    return EOF;
            }
        }
    }
}
