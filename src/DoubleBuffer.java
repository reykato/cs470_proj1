public class DoubleBuffer {
    private char buff1[];
    private char buff2[];
    private char curr_buff[];
    private boolean buff1_active;
    public int forward;
    private int buffer_size;
    private java.io.Reader reader;

    public DoubleBuffer(int buffer_size, java.io.Reader reader) {
        this.buffer_size = buffer_size;
        this.buff1 = new char[buffer_size];
        this.buff2 = new char[buffer_size];
        this.curr_buff = new char[buffer_size];
        this.buff1_active = true;
        this.reader = reader;
    }

    public char NextChar() {
        

        return '\0';
    }

    private void swapBuffers() {
        if (buff1_active) {
            curr_buff = buff2;
            fillBuffer(buff1);
        }
    }

    private void fillBuffer(char[] buffer) {
        
    }
}
