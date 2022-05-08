package ro.proiect.Service;

import ro.proiect.Banca.*;
import ro.proiect.Enums.AccountTypes;
import ro.proiect.Enums.CardTypes;
import ro.proiect.Enums.CashTypes;
import ro.proiect.Enums.GenderTypes;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

public class BancaService {

    private final String _logFileName = "log.csv";

    public BancaService() throws IOException {
        WriteService writer = WriteService.getInstance();
        writer.Clear(_logFileName);
    }

    public Iterable<Client> GetClientsByFirstName(Banca banca, String firstName) throws IOException {
        List<Client> clienti = banca.get_clienti();
        WriteLog("GetClientsByFirstName");
        return clienti.stream().filter(x -> x.get_firstName().equals(firstName)).collect(Collectors.toList());
    }

    public Iterable<Client> GetClientsByLastName(Banca banca, String lastName) throws IOException {
        List<Client> clienti = banca.get_clienti();
        WriteLog("GetClientsByLastName");
        return clienti.stream().filter(x -> x.get_lastName().equals(lastName)).collect(Collectors.toList());
    }

    public Iterable<Client> GetClientsByName(Banca banca, String firstName, String lastName) throws IOException {
        List<Client> clienti = banca.get_clienti();
        WriteLog("GetClientsByName");
        return clienti.stream().filter(x -> x.get_lastName().equals(lastName) && x.get_firstName().equals(firstName)).collect(Collectors.toList());
    }

    public Client GetClientByName(Banca banca, String firstName, String lastName) throws IOException {
        List<Client> clienti = banca.get_clienti();
        Optional<Client> client = clienti.stream().filter(x -> x.get_lastName().equals(lastName) && x.get_firstName().equals(firstName)).findFirst();
        WriteLog("GetClientByName");
        if (client.isPresent())
            return client.get();
        return null;
    }

    public Iterable<Cont> GetAccountsByCashType(Banca banca, CashTypes tipValuta) throws IOException {
        List<Client> clienti = banca.get_clienti();
        List<Cont> conturiReturnate = new ArrayList<Cont>();
        for (Client client : clienti) {
            TreeSet<Cont> conturi = client.get_conturi();
            conturiReturnate.addAll(conturi.stream().filter(x -> x.getTipValuta() == tipValuta).collect(Collectors.toList()));
        }
        WriteLog("GetAccountsByCashType");
        return conturiReturnate;
    }

    public Iterable<Cont> GetAccountsByStoredCash(Banca banca, int lowerLimit, int upperLimit) throws IOException {
        List<Client> clienti = banca.get_clienti();
        List<Cont> conturiReturnate = new ArrayList<Cont>();
        WriteLog("GetAccountsByStoredCash");
        for (Client client : clienti) {
            TreeSet<Cont> conturi = client.get_conturi();
            conturiReturnate.addAll(conturi.stream().filter(x -> x.GetEur() >= lowerLimit && x.GetEur() <= upperLimit).collect(Collectors.toList()));
        }
        return conturiReturnate;
    }

    public Cont GetContByIBAN(Client client, String IBAN) throws IOException {
        TreeSet<Cont> conturi = client.get_conturi();
        Optional<Cont> cont = conturi.stream().filter(x -> x.get_IBAN().equals(IBAN)).findFirst();
        WriteLog("GetContByIBAN");
        return cont.isPresent() ? cont.get() : null;
    }

    public Client GetClientByIBAN(Banca banca, String IBAN) throws IOException {
        Optional<Client> client = banca.get_clienti().stream().filter(x -> {
            try {
                return GetContByIBAN(x, IBAN) != null;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return false;
        }).findFirst();
        WriteLog("GetClientByIBAN");
        return client.isPresent() ? client.get() : null;
    }

    public Iterable<Card> GetCardByType(Cont cont, CardTypes type) throws IOException {
        WriteLog("GetCardByType");
        return cont.get_carduri().stream().filter(x -> x.get_tip().equals(type)).collect(Collectors.toList());
    }

    public Iterable<ExtrasDeCont> GetExtrasDeContBeforeDate(Cont cont, Date date) throws IOException {
        WriteLog("GetExtrasDeContBeforeDate");
        return cont.get_extraseDeCont().stream().filter(x -> x.get_data().before(date)).collect(Collectors.toList());
    }

    public Iterable<ExtrasDeCont> GetExtrasDeContAfterDate(Cont cont, Date date) throws IOException {
        WriteLog("GetExtrasDeContAfterDate");
        return cont.get_extraseDeCont().stream().filter(x -> x.get_data().after(date)).collect(Collectors.toList());
    }

    public void Save(Banca banca) throws IOException {
        WriteService write = WriteService.getInstance();
        File bancaRoot = new File("Banca_" + banca.get_nume());
        if (!bancaRoot.exists())
            bancaRoot.mkdir();
        File bancaCsv = new File(bancaRoot.getAbsolutePath() + "\\banca.csv");
        if (!bancaCsv.exists())
            bancaCsv.createNewFile();
        String bancaCsvPath = bancaCsv.getAbsolutePath();
        write.Clear(bancaCsvPath);
        write.Write(bancaCsvPath, banca.get_nume());

        File clientRoot = new File(bancaRoot.getAbsolutePath() + "\\Client");
        if (!clientRoot.exists())
            clientRoot.mkdir();
        File clientCsv = new File(clientRoot.getAbsolutePath() + "\\client.csv");
        if (!clientCsv.exists())
            clientCsv.createNewFile();

        String clientCsvPath = clientCsv.getAbsolutePath();
        write.Clear(clientCsvPath);
        for (Client client : banca.get_clienti()) {
            String deserializedClient = client.get_firstName() + "," + client.get_lastName() + "," + client.get_id() + ","
                    + client.get_gender() + "," + client.get_dateOfBirth() + "\n";
            write.Write(clientCsvPath, deserializedClient);

            File contRoot = new File(clientRoot.getAbsolutePath() + "\\" + client.get_id() + "_conturi");
            if (!contRoot.exists())
                contRoot.mkdir();
            File contCsv = new File(contRoot.getAbsolutePath() + "\\cont.csv");
            if (!contCsv.exists())
                contCsv.createNewFile();

            String contCsvPath = contCsv.getAbsolutePath();
            write.Clear(contCsvPath);

            for (Cont cont : client.get_conturi()) {
                String deserializedCont = cont.getSumaStocata() + "," + cont.getTipCont() + "," + cont.getTipValuta()
                        + "," + cont.get_IBAN() + "\n";
                write.Write(contCsvPath, deserializedCont);

                File tranzactieRoot = new File(contRoot.getAbsolutePath() + "\\" + cont.get_IBAN());
                if (!tranzactieRoot.exists())
                    tranzactieRoot.mkdir();
                File tranzactieCsv = new File(tranzactieRoot.getAbsolutePath() + "\\tranzactie.csv");
                if (!tranzactieCsv.exists())
                    tranzactieCsv.createNewFile();

                String tranzactieCsvPath = tranzactieCsv.getAbsolutePath();
                write.Clear(tranzactieCsvPath);

                for (Tranzactie tranzactie : cont.get_tranzactii()) {
                    String deserializedTranzactie = tranzactie.get_IBANDestinatar() + "," + tranzactie.get_data()
                            + "," + tranzactie.get_suma() + "," + tranzactie.get_tipValuta() + "," + tranzactie.get_id() + "\n";
                    write.Write(tranzactieCsvPath, deserializedTranzactie);
                }

                File extrasDeContRoot = tranzactieRoot;
                File extrasDeContCsv = new File(extrasDeContRoot.getAbsolutePath() + "\\extrasDeCont.csv");
                if (!extrasDeContCsv.exists())
                    extrasDeContCsv.createNewFile();

                String extrasDeContCsvPath = extrasDeContCsv.getAbsolutePath();
                write.Clear(extrasDeContCsvPath);

                for (ExtrasDeCont extrasDeCont : cont.get_extraseDeCont()) {
                    String deserializedExtrasDeCont = extrasDeCont.getSumaStocata() + "," + extrasDeCont.getTipCont()
                            + "," + extrasDeCont.getTipValuta() + "," + extrasDeCont.get_data() + "\n";
                    write.Write(extrasDeContCsvPath, deserializedExtrasDeCont);
                }

            }
        }
        WriteLog("Save");
    }

    public Banca Load(String baseFolder) throws IOException, ParseException {
        ReadService reader = ReadService.getInstance();
        File banca = new File(baseFolder);
        if (!banca.exists())
            throw new FileNotFoundException("Banca folder not found");

        File bancaCSV = new File(banca.getAbsolutePath() + "\\banca.csv");
        if (!bancaCSV.exists())
            throw new FileNotFoundException("Banca data not found");
        ArrayList<ArrayList<String>> bancaData = reader.Read(bancaCSV.getAbsolutePath());
        Banca bancaRead = new Banca(bancaData.get(0).get(0));

        File client = new File(banca.getAbsolutePath() + "\\Client");
        if (!client.exists())
            throw new FileNotFoundException("Client folder not found");

        File clientCSV = new File(client.getAbsolutePath() + "\\client.csv");
        if (!clientCSV.exists())
            throw new FileNotFoundException("Client data not found");

        ArrayList<ArrayList<String>> clientData = reader.Read(clientCSV.getAbsolutePath());
        for (ArrayList<String> serializedClient : clientData) {
            String firstName = serializedClient.get(0);
            String lastName = serializedClient.get(1);
            UUID id = UUID.fromString(serializedClient.get(2));
            GenderTypes gender = GenderTypes.valueOf(serializedClient.get(3));
            SimpleDateFormat formatter = new SimpleDateFormat("EEE MMM dd HH:mm:ss z yyyy");
            Date dateOfBirth = formatter.parse(serializedClient.get(4));
            Client clientRead = new Client(firstName, lastName, dateOfBirth, gender, id);

            File cont = new File(client.getAbsolutePath() + "\\" + id + "_conturi");
            if (!cont.exists())
                throw new FileNotFoundException("Cont folder not found");

            File contCsv = new File(cont.getAbsolutePath() + "\\cont.csv");
            if (!contCsv.exists())
                throw new FileNotFoundException("Cont data not found");

            ArrayList<ArrayList<String>> contData = reader.Read(contCsv.getAbsolutePath());

            for (ArrayList<String> serializedCont : contData) {
                int sumaStocata = Integer.parseInt(serializedCont.get(0));
                AccountTypes tipCont = AccountTypes.valueOf(serializedCont.get(1));
                CashTypes tipValuta = CashTypes.valueOf(serializedCont.get(2));
                String IBAN = serializedCont.get(3);
                Cont contRead = new Cont(sumaStocata, tipCont, tipValuta, IBAN);

                File extrasDeCont = new File(cont.getAbsolutePath() + "\\" + IBAN);
                if (!extrasDeCont.exists())
                    throw new FileNotFoundException("Cont data FOLDER not found");

                File extrasDeContCsv = new File(extrasDeCont.getAbsolutePath() + "\\extrasDeCont.csv");
                if (!extrasDeCont.exists())
                    throw new FileNotFoundException("Extras de cont data not found");

                ArrayList<ArrayList<String>> extrasDeContData = reader.Read(extrasDeContCsv.getAbsolutePath());

                for (ArrayList<String> serializedExtrasDeCont : extrasDeContData) {
                    int sumaStocataExtras = Integer.parseInt(serializedExtrasDeCont.get(0));
                    AccountTypes tipContExtras = AccountTypes.valueOf(serializedExtrasDeCont.get(1));
                    CashTypes tipValutaExtras = CashTypes.valueOf(serializedExtrasDeCont.get(2));
                    Date dataExtras = formatter.parse(serializedExtrasDeCont.get(3));
                    ExtrasDeCont extrasDeContRead = new ExtrasDeCont(sumaStocataExtras, tipContExtras, tipValutaExtras, dataExtras);

                    contRead.AddExtrasDeCont(extrasDeContRead);
                }

                File tranzactie = extrasDeCont;

                File tranzactieCsv = new File(tranzactie.getAbsolutePath() + "\\tranzactie.csv");
                if (!tranzactieCsv.exists())
                    throw new FileNotFoundException("Tranzactie data not found");

                ArrayList<ArrayList<String>> tranzactieData = reader.Read(tranzactieCsv.getAbsolutePath());

                for (ArrayList<String> serializedTranzactie : tranzactieData) {
                    String IBANDestinatar = serializedTranzactie.get(0);
                    Date dataTranzactie = formatter.parse(serializedTranzactie.get(1));
                    int suma = Integer.parseInt(serializedTranzactie.get(2));
                    CashTypes tipValutaTranzactie = CashTypes.valueOf(serializedTranzactie.get(3));
                    Tranzactie tranzactieRead = new Tranzactie(IBANDestinatar, dataTranzactie, suma, tipValutaTranzactie);

                    contRead.AddTranzacite(tranzactieRead);
                }

                clientRead.AddCont(contRead);
            }

            bancaRead.AddClient(clientRead);
        }
        WriteLog("Load");
        return bancaRead;
    }

    private void WriteLog(String operation) throws IOException {
        WriteService write = WriteService.getInstance();
        write.Write(_logFileName, operation + "," + new Date() + "\n");
    }
}
