import java.io.IOException;

public class DoubleBuffer {
    private char buff0[];
    private char buff1[];
    // public char curr_buff[];
    public boolean buff0_active;
    public int f = 0;
    public int lB = 0;
    private int buffer_size;
    private java.io.Reader reader;
    private char prev_char;

    public DoubleBuffer(int buffer_size, java.io.Reader reader) throws IOException {
        this.buffer_size = buffer_size;
        this.buff0 = new char[buffer_size];
        this.buff1 = new char[buffer_size];
        this.buff0_active = true;
        this.reader = reader;
        fillBuffer(true);
        fillBuffer(false);
        this.prev_char = '\0';
    }

    public char NextChar() throws IOException {
        char next_char;
        if (this.buff0_active)  next_char = this.buff0[this.f];
        else                    next_char = this.buff1[this.f];


        if (next_char == '\0') { // if sentinel/eof is read
            if (this.f == buffer_size - 1) { // if the sentinel/eof is at the end of the buffer, it's a sentinel so swap buffers and return next character

                // don't fill buffer if the id or number is not over
                if (Character.isLetter(this.prev_char) || this.prev_char == '_' || Character.isDigit(this.prev_char) || this.prev_char == '.') {
                    swapBuffers();
                    next_char = NextChar();
                    this.prev_char = next_char;
                    return next_char;
                } else {
                    fillBuffer(buff0_active);
                    swapBuffers();
                    next_char = NextChar();
                    this.prev_char = next_char;
                    return next_char;
                } 

            } else { // if the sentinel/eof is within the buffer, it's an eof
                return '\0'; // return end of file
            }
        } else { // if non-eof character is read
            this.f++;
            this.prev_char = next_char;
            return next_char; // return actual character
        }
    }

    public void swapBuffers() throws IOException {
        this.buff0_active = !this.buff0_active;
        this.f = 0;
    }

    public void fillBuffer(boolean fill_buff0) throws IOException { // fill_buff0 = true means buff0 will be filled, buff1 otherwise
        // System.out.print("filling buffer " + (fill_buff0 ? "buff0" : "buff1") +  " with: ");
        if (fill_buff0) {
            fillBufferHelper(this.buff0);
        } else {
            fillBufferHelper(this.buff1);
        }        
    }

    private void fillBufferHelper(char[] buffer) throws IOException {
        int a;
        int i = 0;
        do {
            a = this.reader.read();
            buffer[i] = (char) a;
            i++;
        } while (a != -1 && i < this.buffer_size - 1);
        if (a != -1 ) buffer[this.buffer_size - 1] = '\0'; // sentinel
        else buffer[i] = '\0'; // eof in middle of the buffer 
        // System.out.println(buffer);
    }

    public String get_id_or_num(boolean[] id_status) {
        String ret_val = "";

        // System.out.println("get_id_or_num, id_status=[" + id_status[0] + ", " + id_status[1] + "], this.lB=" + this.lB + ", this.f="+ this.f);

        if (id_status[0]) { // if the id did not start and end in the same buffer
            if (!id_status[1]) { // if the id started in buffer 1
                for (int i = this.lB; i < this.buffer_size - 1; i++) {
                    ret_val += this.buff1[i];
                }
                for (int i = 0; i < this.f - 1; i++) {
                    ret_val += this.buff0[i];
                }
            } else { // if the id started in buffer 0
                for (int i = this.lB; i < this.buffer_size - 1; i++) {
                    ret_val += this.buff0[i];
                }
                for (int i = 0; i < this.f - 1; i++) {
                    ret_val += this.buff1[i];
                }
            }
        } else {
            if (!id_status[1]) { // if the id is contained in buffer 1
                for (int i = this.lB; i < this.f - 1; i++) {
                    ret_val += this.buff1[i];
                }
            } else { // if the id is contained in buffer 0
                for (int i = this.lB; i < this.f - 1; i++) {
                    ret_val += this.buff0[i];
                }
            }
        }        
        return ret_val;
    }
}
