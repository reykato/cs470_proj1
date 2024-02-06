import java.io.*;


public class Test {
    private static final int NUMBER_OF_TESTS = 8;
    public static void main(String[] args) throws Exception {
        for (int i = 1; i <= NUMBER_OF_TESTS; i++) {
            try {
                PrintStream outHolder = System.out;
                System.setOut(new PrintStream(new FileOutputStream("src/testout" + i + ".txt")));
                String[] progArgs = new String[1];
                progArgs[0] = "test" + i + ".minc";
                Program.main(progArgs);
                System.setOut(outHolder);
                Reader solution = new FileReader("src/testsolu" + i + ".txt");
                Reader test = new FileReader("src/testout" + i + ".txt");
                char[] solutionarr = new char[100000];
                char[] testarr = new char[100000];
                solution.read(solutionarr);
                test.read(testarr);

                int column = 1;
                int line = 1;
                boolean same = true;
                for (int j = 0; j < 100000; j++) {
                    if (solutionarr[j] == 0 && testarr[j] == 0) {
                        break;
                    }

                    if (solutionarr[j] != testarr[j]) {
                        same = false;
                        char s = solutionarr[j];
                        char t = testarr[j];

                        if (s == 0 || s == '\n' || t == 0 || t == '\n') {
                            System.out.println("Error in " + i + ": line:" + line + " column: " + column + " solarr: " + " ascii: " + (int) s + " testarr: " + " ascii: " + (int) t);
                        } else {
                            System.out.println("Error in " + i + ": line:" + line + " column: " + column + " solarr: " + s + " testarr: " + t);
                        }
                    }

                    if (solutionarr[j] == '\n') {
                        line++;
                        column = 1;
                    }

                    column++;
                }

                if (same) {
                    System.out.print("Case " + i + " success");
                }

            } catch (FileNotFoundException e) {
                throw e;
            }
        
            System.out.println();
        }
    }
}