package ro.proiect.Banca;

import ro.proiect.Enums.CashTypes;

import java.time.LocalDate;
import java.util.Objects;
import java.util.UUID;

public class Tranzactie {
    private final String ibanDestinatar;
    private final LocalDate data;
    private final int suma;
    private final CashTypes tipValuta;
    private final UUID id;

    public Tranzactie(String ibanDestinatar, LocalDate data, int suma, CashTypes tipValuta) {
        this.ibanDestinatar = ibanDestinatar;
        this.data = data;
        this.suma = suma;
        this.tipValuta = tipValuta;
        this.id = UUID.randomUUID();
    }

    public Tranzactie(String ibanDestinatar, int suma, CashTypes tipValuta) {
        this(ibanDestinatar, LocalDate.now(), suma, tipValuta);
    }

    public Tranzactie(Tranzactie tranzactie) {
        this.ibanDestinatar = tranzactie.ibanDestinatar;
        this.data = tranzactie.data;
        this.suma = tranzactie.suma;
        this.tipValuta = tranzactie.tipValuta;
        this.id = tranzactie.id;
    }

    public UUID getId() {
        return id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Tranzactie that = (Tranzactie) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Tranzactie{" +
                "_IBAN='" + ibanDestinatar + '\'' +
                ", _data=" + data +
                ", _suma=" + suma +
                ", _tipValuta=" + tipValuta +
                ", _id=" + id +
                '}';
    }

    public String getIbanDestinatar() {
        return ibanDestinatar;
    }

    public LocalDate getData() {
        return data;
    }

    public int getSuma() {
        return suma;
    }

    public CashTypes getTipValuta() {
        return tipValuta;
    }
}
