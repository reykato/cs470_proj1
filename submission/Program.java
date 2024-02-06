/*
 *  Name:       Tyler Lindsay
 *  Email:      tylerl@psu.edu, tcl5238@psu.edu
 *  Date:       5 February 2024
 *  Class:      CMPSC 470 - Compilers
 *  Instructor: Dr. Hyuntae Na
 *  Assignment: Project 1 - Tokenizer
 */

public class Program {
    public static void main(String[] args) throws Exception
    {
        java.io.Reader r;

        if(args.length <= 0)
            return;
        r = new java.io.FileReader(args[0]);

        Compiler compiler = new Compiler(r);
        compiler.Compile();
    }
}
