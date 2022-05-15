package ro.proiect.Banca;

import ro.proiect.Enums.CardTypes;
import ro.proiect.Helpers.Checkers;
import ro.proiect.Interfaces.Icard;

import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

public class Card implements Icard {

    protected final UUID id;
    protected final LocalDate expirationDate;
    protected String pin;
    protected CardTypes tip = CardTypes.UNKNOWN;

    public CardTypes getTip() {
        return tip;
    }

    public Card(String pin, LocalDate expirationDate) {
        Checkers.checkProperty(pin, x -> x.length() > 3, "PIN length too short");
        this.pin = pin;
        this.id = UUID.randomUUID();
        this.expirationDate = expirationDate;
    }

    public Card(Card card) {
        Checkers.checkProperty(card.pin, x -> x.length() > 3, "PIN length too short");
        this.pin = card.pin;
        this.id = card.id;
        this.tip = card.tip;
        this.expirationDate = card.expirationDate;
    }

    public void pay(int value) {

    }

    public int getAllowance() {
        return -1;
    }

    public void charge(int value) {

    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public LocalDate getExpirationDate() {
        return expirationDate;
    }

    @Override
    public String toString() {
        return "Card{" +
                "_PIN='" + pin + '\'' +
                ", _id=" + id +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Card card = (Card) o;
        return id.equals(card.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public Card clone() {
        return new Card(this);
    }

    public UUID getId() {
        return id;
    }
}
