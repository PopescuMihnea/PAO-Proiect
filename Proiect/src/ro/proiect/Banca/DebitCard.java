package ro.proiect.Banca;

import ro.proiect.Enums.CardTypes;
import ro.proiect.Helpers.Checkers;

import java.time.LocalDate;

public class DebitCard extends Card {
    private int debit;

    public DebitCard(String pin, LocalDate expirationDate, int debit) {
        super(pin, expirationDate);
        this.debit = debit;
        this.tip = CardTypes.DEBIT_CARD;
    }

    public DebitCard(String pin, LocalDate expirationDate) {
        this(pin, expirationDate, 0);
    }

    public DebitCard(DebitCard card) {
        super(card);
        this.debit = card.debit;
        this.tip = CardTypes.DEBIT_CARD;
    }

    @Override
    public String toString() {
        return "DebitCard{" +
                "_PIN='" + pin + '\'' +
                ", _id=" + id +
                ", _expirationDate=" + expirationDate +
                ", _debit=" + debit +
                '}';
    }

    @Override
    public void pay(int value) {
        Checkers.checkProperty(debit, value, (x, y) -> x >= y, "Insufficient funds");
        this.debit -= value;
    }

    @Override
    public void charge(int value) {
        this.debit += value;
    }

    @Override
    public int getAllowance() {
        return this.debit;
    }

    @Override
    public Card clone() {
        return new DebitCard(this);
    }
}
