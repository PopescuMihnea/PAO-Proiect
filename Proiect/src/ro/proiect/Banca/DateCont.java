package ro.proiect.Banca;

import ro.proiect.Enums.AccountTypes;
import ro.proiect.Enums.CashTypes;

public abstract class DateCont implements Comparable<DateCont> {
    protected int sumaStocata;
    protected AccountTypes tipCont;
    protected CashTypes tipValuta;

    public DateCont(int sumaStocata, AccountTypes tipCont, CashTypes tipValuta) {
        this.sumaStocata = sumaStocata;
        this.tipCont = tipCont;
        this.tipValuta = tipValuta;
    }

    public int getSumaStocata() {
        return sumaStocata;
    }

    public void setSumaStocata(int sumaStocata) {
        this.sumaStocata = sumaStocata;
    }

    public AccountTypes getTipCont() {
        return tipCont;
    }

    public void setTipCont(AccountTypes tipCont) {
        this.tipCont = tipCont;
    }

    public CashTypes getTipValuta() {
        return tipValuta;
    }

    public void setTipValuta(CashTypes tipValuta) {
        this.tipValuta = tipValuta;
    }

    public float getEur() {
        return this.sumaStocata * this.tipValuta.value();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DateCont dateCont = (DateCont) o;
        return sumaStocata == dateCont.sumaStocata && tipCont == dateCont.tipCont && tipValuta == dateCont.tipValuta;
    }

}
