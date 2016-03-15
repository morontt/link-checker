package info.morontt.link_checker;

public interface Logger {
    void init();
    void write(String[] args);
    void finish();
}
