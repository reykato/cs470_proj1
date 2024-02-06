/*
 *  Name:       Tyler Lindsay
 *  Email:      tylerl@psu.edu, tcl5238@psu.edu
 *  Date:       5 February 2024
 *  Class:      CMPSC 470 - Compilers
 *  Instructor: Dr. Hyuntae Na
 *  Assignment: Project 1 - Tokenizer
 */

import java.util.ArrayList;
import java.util.HashMap;

public class Lexer
{
    private static final char EOF        =  0;

    private Parser         yyparser; // parent parser object
    // private java.io.Reader reader;   // input stream
    public int             lineno;   // line number
    public int             column;   // column
    ArrayList<Character>   input;    // arraylist of all characters in file
    public HashMap<String, Integer> symbol_table;
    DoubleBuffer buffer;
    boolean fail_next = false; // set true if you need the program to fail after returning
    public int columns_to_add; // for use after an ID is read


    public Lexer(java.io.Reader reader, Parser yyparser) throws Exception
    {
        // this.reader   = reader;
        this.yyparser = yyparser;
        this.lineno = 1;
        this.column = 0;
        this.columns_to_add = 0;
        this.buffer = new DoubleBuffer(10, reader);
        initialize_symbol_table();
    }

    private void initialize_symbol_table() throws Exception {
        this.symbol_table = new HashMap<>();
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

    // public char NextChar() throws Exception
    // {
    //     // http://tutorials.jenkov.com/java-io/readers-writers.html
    //     // int data = reader.read();
    //     // if(data == -1)
    //     // {
    //     //     return EOF;
    //     // }
    //     // this.column++;
    //     // return (char)data;

    //     if (buff1_active) {

    //         return buff1[f1];

    //     } else {

    //     }
        
    //     this.column++;
    //     buffer.f++;
    //     if (buffer.f < this.input.size()){
    //         return (char) this.input.get(buffer.f);
    //     } else return EOF;
    // }
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

    private char NextChar() throws Exception {
        this.column++;
        return this.buffer.NextChar();
    }


    // * If yylex reach to the end of file, return  0
    // * If there is an lexical error found, return -1
    // * If a proper lexeme is determined, return token <token-id, token-attribute> as follows:
    //   1. set token-attribute into yyparser.yylval
    //   2. return token-id defined in Parser
    //   token attribute can be lexeme, line number, column, etc.
    public int yylex() throws Exception
    {
        int state = 0;
        boolean[] id_status = {false, false}; // [id started in different buffer than it ended, if they are different, the id started in buffer 0]
        if (this.columns_to_add != 0) { this.column += this.columns_to_add; this.columns_to_add = 0; }
        if (this.fail_next) { this.fail_next = false; return Fail(); }
        while(true)
        {
            char c;
            yyparser.yylval = new ParserVal();
            switch(state)
            {
                case 0:
                    c = NextChar();
                    if (c == EOF)               { state = 9999; continue; }
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
                    
                    // System.out.print("character: "); System.out.print( "\\u" + Integer.toHexString(c | 0x10000).substring(1) ); System.out.println(" got through. buffer.f: " + buffer.f + " and this.input.size(): " + this.input.size());
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
                    this.column--;
                    this.columns_to_add++;
                    return Parser.RELOP;
                case 7:
                    yyparser.yylval = new ParserVal((Object)"<>");
                    this.column--;
                    this.columns_to_add++;
                    return Parser.RELOP;
                case 8:
                    yyparser.yylval = new ParserVal((Object)"<");
                    this.column--;
                    if (this.buffer.f == 0) {
                        this.buffer.swapBuffers();
                        this.buffer.f = 8;
                    } else {
                        this.buffer.f--;
                    }
                    return Parser.RELOP;
                case 9:
                    c = NextChar();
                    if (c == '=') { state = 10; }
                    else { state = 11; }
                    continue;
                case 10:
                    yyparser.yylval = new ParserVal((Object)">=");
                    this.column--;
                    this.columns_to_add++;
                    return Parser.RELOP;
                case 11:
                    yyparser.yylval = new ParserVal((Object)">");
                    this.column--;
                    if (this.buffer.f == 0) {
                        this.buffer.swapBuffers();
                        this.buffer.f = 8;
                    } else {
                        this.buffer.f--;
                    }
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
                    this.column--;
                    this.columns_to_add++;
                    return Parser.ASSIGN;
                case 19:
                    yyparser.yylval = new ParserVal((Object)"::");
                    this.columns_to_add++;
                    this.column--;
                    return Parser.TYPEOF;
                case 20: // initial number state
                    id_status[1] = this.buffer.buff0_active;
                    this.buffer.lB = this.buffer.f - 1;

                    c = NextChar();
                    if (Character.isDigit(c)) { state = 21; }
                    else if (c == '.') { state = 22; }
                    else { state = 23; }
                    continue;
                case 21: // number state (before '.')
                    c = NextChar();
                    this.column--;
                    this.columns_to_add++;

                    if (Character.isDigit(c)) { break; }
                    else if (c == '.') { state = 22; }
                    else if (Character.isLetter(c) || c == '_') { state = 23; this.columns_to_add++; this.fail_next = true; }
                    else { state = 221; }
                    continue;
                case 22: // number state (after '.')
                    c = NextChar();
                    this.column--;
                    this.columns_to_add++;
                    if (c == '.') { this.column--; return Fail(); }
                    // else if (Character.isLetter(c) || c == '_') { state = 23; }
                    else { state = 221; }
                    continue;
                case 221: // number state (after '.' and number)
                    c = NextChar();
                    this.column--;
                    this.columns_to_add++;
                    if (c == '.' || Character.isLetter(c) || c == '_') { state = 23; this.columns_to_add++; this.fail_next = true; }
                    else if (Character.isDigit(c)) { break; }
                    else { state = 23; }
                    continue;
                case 23: // final number state
                    String num;

                    id_status[0] = (id_status[1] != this.buffer.buff0_active); // if the id started and ended in different buffers

                    num = this.buffer.get_id_or_num(id_status);
                    yyparser.yylval = new ParserVal((Object)num);

                    this.column--;
                    if (id_status[0]) {
                        this.buffer.fillBuffer(id_status[1]);
                        if (this.buffer.f == 0) {
                            this.buffer.swapBuffers();
                            this.buffer.f = 8;
                        } else {
                            this.buffer.f--;
                        }
                    } else {
                        this.buffer.f--;
                    }


                    return Parser.NUM;
                case 24: // id/keyword initial state
                    id_status[1] = this.buffer.buff0_active;
                    this.buffer.lB = this.buffer.f - 1;

                    c = NextChar();
                    if (Character.isLetter(c) || c == '_' || Character.isDigit(c)) { state = 241; }
                    else { state = 25; }
                    continue;
                case 241: // id/keyword loop state
                    c = NextChar();
                    this.column--;
                    this.columns_to_add++;
                    if (Character.isLetter(c) || c == '_' || Character.isDigit(c)) { state = 241; }
                    else { state = 25; }
                    continue;    
                case 25: // id/keyword return state
                    String in;

                    id_status[0] = (id_status[1] != this.buffer.buff0_active); // if the id started and ended in different buffers

                    in = buffer.get_id_or_num(id_status);
                    yyparser.yylval = new ParserVal((Object)in);
                    this.install_id(in);

                    this.column--;
                    if (id_status[0]) {
                        this.buffer.fillBuffer(id_status[1]);
                        if (this.buffer.f == 0) {
                            this.buffer.swapBuffers();
                            this.buffer.f = 8;
                        } else {
                            this.buffer.f--;
                        }
                    } else {
                        this.buffer.f--;
                    }
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
