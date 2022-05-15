package ro.proiect.Banca;

import ro.proiect.Enums.AccountTypes;
import ro.proiect.Enums.CashTypes;
import ro.proiect.Helpers.Checkers;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class Cont extends DateCont {
    private static final int ibanLength = 10;
    private final ArrayList<Tranzactie> tranzactii = new ArrayList<>();
    private final String iban;
    private final TreeSet<ExtrasDeCont> extraseDeCont = new TreeSet<>();
    private final HashSet<Card> carduri = new HashSet<>();

    public Cont(AccountTypes tipCont, CashTypes tipValuta) {
        this(0, tipCont, tipValuta);
    }

    public Cont(Cont cont) {
        super(cont.sumaStocata, cont.tipCont, cont.tipValuta);
        this.iban = cont.iban;
        for (ExtrasDeCont extrasDeCont : cont.extraseDeCont) {
            this.extraseDeCont.add(new ExtrasDeCont(extrasDeCont));
        }
        for (Tranzactie tranzactie : cont.tranzactii) {
            this.tranzactii.add(new Tranzactie(tranzactie));
        }
        for (Card card : cont.carduri) {
            this.carduri.add(new Card(card));
        }
    }

    public Cont(int sumaStocata, AccountTypes tipCont, CashTypes tipValuta) {
        super(sumaStocata, tipCont, tipValuta);
        this.iban = "RO" + genIBAN();
    }

    public Cont(int sumaStocata, AccountTypes tipCont, CashTypes tipValuta, String iban) {
        super(sumaStocata, tipCont, tipValuta);
        Checkers.checkProperty(iban, x -> x.length() != 10 && x.startsWith("RO"), "Incorrect IBAN format");
        this.iban = iban;
    }

    public HashSet<Card> getCarduri() {
        return carduri.stream().map(Card::clone).collect(Collectors.toCollection(HashSet::new));
    }

    public TreeSet<ExtrasDeCont> getExtraseDeCont() {
        return extraseDeCont.stream().map(ExtrasDeCont::new).collect(Collectors.toCollection(TreeSet::new));
    }

    public String getIban() {
        return iban;
    }

    private String genIBAN() {
        char[] array = new Random().ints(Cont.ibanLength, '0', '9' + 1)
                .mapToObj(c -> Character.toString((char) c))
                .collect(Collectors.joining())
                .toCharArray();
        return new String(array);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cont cont = (Cont) o;
        return iban.equals(cont.iban);
    }

    @Override
    public int hashCode() {
        return Objects.hash(iban);
    }

    public int compareTo(DateCont o) {
        if (this.getEur() > o.getEur())
            return 1;
        else if (this.getEur() < o.getEur())
            return -1;
        return 0;
    }

    @Override
    public String toString() {
        return "Cont{" +
                "_tranzactii=" + tranzactii +
                ", _IBAN='" + iban + '\'' +
                ", _extraseDeCont=" + extraseDeCont +
                ", _carduri=" + carduri +
                ", _sumaStocata=" + sumaStocata +
                ", _tipCont=" + tipCont +
                ", _tipValuta=" + tipValuta +
                '}';
    }

    public void addCard(Card card) {
        this.carduri.add(card.clone());
    }

    public void removeCard(Card card) {
        this.carduri.remove(card);
    }

    public void addTranzacite(Tranzactie tranzactie) {
        this.tranzactii.add(new Tranzactie(tranzactie));
    }

    public void removeTranzactie(Tranzactie tranzactie) {
        this.tranzactii.remove(tranzactie);
    }

    public void addExtrasDeCont() {
        this.extraseDeCont.add(new ExtrasDeCont(this.sumaStocata, this.tipCont, this.tipValuta, LocalDate.now()));
    }

    public void addExtrasDeCont(ExtrasDeCont extrasDeCont) {
        this.extraseDeCont.add(new ExtrasDeCont(extrasDeCont));
    }

    public void addExtraseDeCont(List<ExtrasDeCont> extraseDeCont) {
        for (ExtrasDeCont extrasDeCont : extraseDeCont) {
            this.addExtrasDeCont(extrasDeCont);
        }
    }

    public void removeExtrasDeCont(ExtrasDeCont extrasDeCont) {
        this.extraseDeCont.remove(extrasDeCont);
    }

    public ArrayList<Tranzactie> getTranzactii() {
        return tranzactii.stream().map(Tranzactie::new).collect(Collectors.toCollection(ArrayList::new));
    }

    public void removeExtraseDeCont(List<ExtrasDeCont> extraseDeCont) {
        for (ExtrasDeCont extrasDeCont : extraseDeCont) {
            this.removeExtrasDeCont(extrasDeCont);
        }
    }

}
