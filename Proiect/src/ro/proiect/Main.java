package ro.proiect;

import ro.proiect.Service.BancaService;
import ro.proiect.Banca.*;
import ro.proiect.Enums.AccountTypes;
import ro.proiect.Enums.CardTypes;
import ro.proiect.Enums.CashTypes;
import ro.proiect.Helpers.Function;
import ro.proiect.Service.ReadService;
import ro.proiect.Service.WriteService;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class Main {

    public static void main(String[] args) throws InterruptedException, IOException, ParseException {
        BancaService SystemOperations = new BancaService();
        Banca BCR = new Banca("BCR");
        Client client1 = new Client("Mihnea-Valentin", "Popescu");
        Cont cont1 = new Cont(300, AccountTypes.Standard, CashTypes.EUR);
        Cont cont2 = new Cont(150, AccountTypes.Student, CashTypes.EUR);
        cont1.AddExtrasDeCont();
        cont1.AddTranzacite(new Tranzactie("6530372461", 300, CashTypes.EUR));
        TimeUnit.SECONDS.sleep(1);
        cont1.AddExtrasDeCont();
        client1.AddConturi(new ArrayList<Cont>(List.of(cont1, cont2)));

        Client client2 = new Client("Razvan-Florin", "Potcoveanu");
        cont1 = new Cont(300, AccountTypes.Standard, CashTypes.EUR, "RO1821378725");
        cont1.AddCard(new CreditCard("04312", new Date(Function.DateYear(2025), 5, 1)));
        cont1.AddCard(new DebitCard("5431", new Date(Function.DateYear(2024), 11, 25)));
        cont1.AddExtrasDeCont();
        cont2 = new Cont(15000, AccountTypes.Student, CashTypes.RON);
        cont2.AddExtrasDeCont();
        client2.AddConturi(new ArrayList<Cont>(List.of(cont1, cont2)));
        BCR.AddClient(client1);
        BCR.AddClient(client2);
        System.out.println("Testam operatiile si obiectele:\n\n");
        System.out.println(BCR);
        System.out.println(SystemOperations.GetAccountsByCashType(BCR, CashTypes.RON));
        System.out.println(SystemOperations.GetAccountsByStoredCash(BCR, 0, 300));
        System.out.println(SystemOperations.GetClientByName(BCR, "Razvan-Florin", "Potcoveanu"));
        System.out.println(SystemOperations.GetContByIBAN(SystemOperations.GetClientByName(BCR, "Razvan-Florin", "Potcoveanu"), "RO1821378725"));
        System.out.println(SystemOperations.GetClientByIBAN(BCR, "RO1821378725"));
        System.out.println(SystemOperations.GetCardByType(cont1, CardTypes.CreditCard));
        System.out.println(SystemOperations.GetExtrasDeContBeforeDate(cont1, new Date(Function.DateYear(2023), 1, 1)));
        System.out.println("\n\n");

        //SystemOperations.Save(BCR);
        System.out.println("Testam load:\n\n");
        System.out.println(SystemOperations.Load("Banca_BCR"));
        System.out.println("\n\n");
        ReadService read = ReadService.getInstance();
        System.out.println("Testam log:\n\n");
        System.out.println(read.Read("log.csv"));
    }
}
