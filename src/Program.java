public class Program {
    public static void main(String[] args) throws Exception
    {
        java.io.Reader r;

        //r = new java.io.StringReader
        //("int main()\n"
        //+"{\n"
        //+"    return 0;\n"
        //+"}\n"
        //);
        //
        //  args = new String[] { "proj1-minic-tokenizer\\src\\test1.minc" };

        // if(args.length <= 0)
        //     return;
        // r = new java.io.FileReader("test1.minc");
        r = new java.io.FileReader("C:/Users/Tyler/Documents/VSCode/cs470_proj1/proj1-minic-tokenizer-startup/src/test7.minc");

        // System.out.println("Starting Compilation...");

        Compiler compiler = new Compiler(r);
        compiler.Compile();
    }
}
