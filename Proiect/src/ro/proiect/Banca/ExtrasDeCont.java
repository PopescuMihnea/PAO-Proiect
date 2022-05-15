package ro.proiect.Banca;

import ro.proiect.Enums.AccountTypes;
import ro.proiect.Enums.CashTypes;

import java.time.LocalDate;
import java.util.Objects;

public class ExtrasDeCont extends DateCont {

    private final LocalDate data;

    public ExtrasDeCont(int sumaStocata, AccountTypes tipCont, CashTypes tipValuta, LocalDate data) {
        super(sumaStocata, tipCont, tipValuta);
        this.data = data;
    }

    public ExtrasDeCont(ExtrasDeCont extrasDeCont) {
        super(extrasDeCont.sumaStocata, extrasDeCont.tipCont, extrasDeCont.tipValuta);
        this.data = extrasDeCont.data;
    }

    public LocalDate getData() {
        return data;
    }

    @Override
    public String toString() {
        return "ExtrasDeCont{" +
                "_sumaStocata=" + sumaStocata +
                ", _tipCont=" + tipCont +
                ", _tipValuta=" + tipValuta +
                ", _data=" + data +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ExtrasDeCont that = (ExtrasDeCont) o;
        return data.equals(that.data) && super.equals(o);
    }

    @Override
    public int hashCode() {
        return Objects.hash(data);
    }

    public int compareTo(DateCont o) {
        try {
            if (this.data.compareTo(((ExtrasDeCont) o).data) < 0)
                return 1;
            else if (this.data.compareTo(((ExtrasDeCont) o).data) > 0)
                return -1;
            return 0;
        } catch (ClassCastException e) {
            throw new IllegalArgumentException(e);
        }
    }
}
