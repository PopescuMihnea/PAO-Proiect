package ro.proiect.Banca;

import ro.proiect.Enums.CardTypes;
import ro.proiect.Helpers.Checkers;

import java.time.LocalDate;

public class CreditCard extends Card {
    private final static int creditLimit = 500;
    private int credit;

    public CreditCard(String pin, LocalDate expirationDate, int credit) {
        super(pin, expirationDate);
        this.credit = credit;
        this.tip = CardTypes.CREDIT_CARD;
    }

    public CreditCard(String pin, LocalDate expirationDate) {
        this(pin, expirationDate, 0);
    }

    public CreditCard(CreditCard card) {
        super(card);
        this.credit = card.credit;
        this.tip = CardTypes.CREDIT_CARD;
    }

    @Override
    public String toString() {
        return "CreditCard{" +
                "_PIN='" + pin + '\'' +
                ", _id=" + id +
                ", _expirationDate=" + expirationDate +
                ", _credit=" + credit +
                '}';
    }

    @Override
    public void pay(int value) {
        Checkers.checkProperty(CreditCard.creditLimit - this.credit, value, (x, y) -> x >= y, "Surpassing credit limit");
        this.credit += value;
    }

    @Override
    public void charge(int value) {
        this.credit -= value;
    }

    @Override
    public int getAllowance() {
        return CreditCard.creditLimit - this.credit;
    }

    @Override
    public Card clone() {
        return new CreditCard(this);
    }
}
