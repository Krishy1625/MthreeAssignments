package flooringmastery.view;

public interface UserIO {
    void print(String message);
    String readString(String prompt);
    int readInt(String prompt);
    int readInt(String prompt, int min, int max);
}
