/*
 *  Name:       Tyler Lindsay
 *  Email:      tylerl@psu.edu, tcl5238@psu.edu
 *  Date:       5 February 2024
 *  Class:      CMPSC 470 - Compilers
 *  Instructor: Dr. Hyuntae Na
 *  Assignment: Project 1 - Tokenizer
 */

public class Compiler
{
    Parser parser;

    public Compiler(java.io.Reader r) throws Exception
    {
        parser = new Parser(r, this);
    }
    public void Compile() throws Exception
    {
        parser.yyparse();
    }
}
