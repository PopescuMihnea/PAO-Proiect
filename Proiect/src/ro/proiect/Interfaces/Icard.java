package ro.proiect.Interfaces;

public interface Icard {
    void pay(int value);

    int getAllowance();

    void charge(int value);

    Icard clone();
}
