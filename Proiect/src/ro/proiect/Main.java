package ro.proiect;

import ro.proiect.Service.BancaService;
import ro.proiect.Banca.*;
import ro.proiect.Enums.AccountTypes;
import ro.proiect.Enums.CardTypes;
import ro.proiect.Enums.CashTypes;
import ro.proiect.Service.ReadService;

import java.io.IOException;
import java.text.ParseException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Main {

    public static void main(String[] args) throws InterruptedException, IOException{
        BancaService SystemOperations = new BancaService();
        Banca BCR = new Banca("BCR");
        Client client1 = new Client("Mihnea-Valentin", "Popescu");
        Cont cont1 = new Cont(300, AccountTypes.STANDARD, CashTypes.EUR);
        Cont cont2 = new Cont(150, AccountTypes.STUDENT, CashTypes.EUR);
        cont1.addExtrasDeCont();
        cont1.addTranzacite(new Tranzactie("6530372461", 300, CashTypes.EUR));
        TimeUnit.SECONDS.sleep(1);
        cont1.addExtrasDeCont();
        client1.addConturi(new ArrayList<>(List.of(cont1, cont2)));

        Client client2 = new Client("Razvan-Florin", "Potcoveanu");
        cont1 = new Cont(300, AccountTypes.STANDARD, CashTypes.EUR, "RO1821378725");
        cont1.addCard(new CreditCard("04312", LocalDate.of(2025, 5, 1)));
        cont1.addCard(new DebitCard("5431", LocalDate.of(2024, 11, 25)));
        cont1.addExtrasDeCont();
        cont2 = new Cont(15000, AccountTypes.STUDENT, CashTypes.RON);
        cont2.addExtrasDeCont();
        client2.addConturi(new ArrayList<>(List.of(cont1, cont2)));
        BCR.addClient(client1);
        BCR.addClient(client2);
        System.out.println("Testam operatiile si obiectele:\n\n");
        System.out.println(BCR);
        System.out.println(SystemOperations.GetAccountsByCashType(BCR, CashTypes.RON));
        System.out.println(SystemOperations.GetAccountsByStoredCash(BCR, 0, 300));
        System.out.println(SystemOperations.GetClientByName(BCR, "Razvan-Florin", "Potcoveanu"));
        System.out.println(SystemOperations.GetContByIBAN(SystemOperations.GetClientByName(BCR, "Razvan-Florin", "Potcoveanu"), "RO1821378725"));
        System.out.println(SystemOperations.GetClientByIBAN(BCR, "RO1821378725"));
        System.out.println(SystemOperations.GetCardByType(cont1, CardTypes.CREDIT_CARD));
        System.out.println(SystemOperations.GetExtrasDeContBeforeDate(cont1, LocalDate.of(2023, 1, 1)));
        System.out.println("\n\n");

        //SystemOperations.Save(BCR);
        System.out.println("Testam load:\n\n");
        System.out.println(SystemOperations.Load("Banca_BCR"));
        System.out.println("\n\n");
        ReadService read = ReadService.getInstance();
        System.out.println("Testam log:\n\n");
        System.out.println(read.read("log.csv"));
    }
}
