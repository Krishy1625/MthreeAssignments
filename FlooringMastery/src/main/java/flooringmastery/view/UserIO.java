package flooringmastery.view;

import java.time.LocalDate;

public interface UserIO {
    void print(String message);
    String readString(String prompt);
    int readInt(String prompt);
    int readInt(String prompt, int min, int max);

    LocalDate readDate(String s);
}
