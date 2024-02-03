import java.io.IOException;

public class DoubleBuffer {
    private char buff1[];
    private char buff2[];
    public char curr_buff[];
    private boolean buff1_active;
    public int f;
    private int buffer_size;
    private java.io.Reader reader;

    public DoubleBuffer(int buffer_size, java.io.Reader reader) throws IOException {
        this.buffer_size = buffer_size;
        this.buff1 = new char[buffer_size];
        this.buff2 = new char[buffer_size];
        this.curr_buff = new char[buffer_size];
        this.buff1_active = true;
        this.reader = reader;
        fillBuffer(buff1);
        fillBuffer(buff2);
        curr_buff = buff1;
    }

    public char NextChar() throws IOException {
        char next_char = curr_buff[this.f];
        // System.out.println("NextChar: " + next_char);
        if (next_char == '\0') { // if sentinel/eof is read
            // System.out.println("EOF READ");
            if (this.f == buffer_size - 1) { // if the sentinel/eof is at the end of the buffer, it's a sentinel so swap buffers and return next character
                swapBuffers();
                return NextChar();
            } else { // if the sentinel/eof is within the buffer, it's an eof
                return '\0'; // return end of file
            }
        } else { // if non-eof character is read
            this.f++;
            return next_char; // return actual character
        }
    }

    private void swapBuffers() throws IOException {
        if (buff1_active) {
            this.curr_buff = buff2;
            fillBuffer(buff1);
            buff1_active = false;
        } else {
            this.curr_buff = buff1;
            fillBuffer(buff2);
            buff1_active = true;
        }

        this.f = 0;
    }

    private void fillBuffer(char[] buffer) throws IOException {
        int a;
        int i = 0;
        do {
            a = reader.read();
            buffer[i] = (char) a;
            i++;
        } while (a != -1 && i < buffer_size - 1);
        if (a != -1 ) buffer[buffer_size - 1] = '\0'; // sentinel
        else buffer[i] = '\0'; // eof in middle of the buffer 
        // System.out.println(curr_buff);
    }
}
