package ro.proiect.Service;

import ro.proiect.Banca.*;
import ro.proiect.Enums.AccountTypes;
import ro.proiect.Enums.CardTypes;
import ro.proiect.Enums.CashTypes;
import ro.proiect.Enums.GenderTypes;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class BancaService {

    private final String _logFileName = "log.csv";

    public BancaService() throws IOException {
        WriteService writer = WriteService.getInstance();
        writer.clear(_logFileName);
    }

    public List<Client> GetClientsByFirstName(Banca banca, String firstName) throws IOException {
        List<Client> clienti = banca.getClienti();
        WriteLog("GetClientsByFirstName");
        return clienti.stream().filter(x -> x.getFirstName().equals(firstName)).collect(Collectors.toList());
    }

    public List<Client> GetClientsByLastName(Banca banca, String lastName) throws IOException {
        List<Client> clienti = banca.getClienti();
        WriteLog("GetClientsByLastName");
        return clienti.stream().filter(x -> x.getLastName().equals(lastName)).collect(Collectors.toList());
    }

    public List<Client> GetClientsByName(Banca banca, String firstName, String lastName) throws IOException {
        List<Client> clienti = banca.getClienti();
        WriteLog("GetClientsByName");
        return clienti.stream().filter(x -> x.getLastName().equals(lastName) && x.getFirstName().equals(firstName)).collect(Collectors.toList());
    }

    public Client GetClientByName(Banca banca, String firstName, String lastName) throws IOException {
        List<Client> clienti = banca.getClienti();
        Optional<Client> client = clienti.stream().filter(x -> x.getLastName().equals(lastName) && x.getFirstName().equals(firstName)).findFirst();
        WriteLog("GetClientByName");
        return client.orElse(null);
    }

    public List<Cont> GetAccountsByCashType(Banca banca, CashTypes tipValuta) throws IOException {
        List<Client> clienti = banca.getClienti();
        List<Cont> conturiReturnate = new ArrayList<>();
        for (Client client : clienti) {
            TreeSet<Cont> conturi = client.getConturi();
            conturiReturnate.addAll(conturi.stream().filter(x -> x.getTipValuta() == tipValuta).toList());
        }
        WriteLog("GetAccountsByCashType");
        return conturiReturnate;
    }

    public List<Cont> GetAccountsByStoredCash(Banca banca, int lowerLimit, int upperLimit) throws IOException {
        List<Client> clienti = banca.getClienti();
        List<Cont> conturiReturnate = new ArrayList<>();
        WriteLog("GetAccountsByStoredCash");
        for (Client client : clienti) {
            TreeSet<Cont> conturi = client.getConturi();
            conturiReturnate.addAll(conturi.stream().filter(x -> x.getEur() >= lowerLimit && x.getEur() <= upperLimit).toList());
        }
        return conturiReturnate;
    }

    public Cont GetContByIBAN(Client client, String IBAN) throws IOException {
        TreeSet<Cont> conturi = client.getConturi();
        Optional<Cont> cont = conturi.stream().filter(x -> x.getIban().equals(IBAN)).findFirst();
        WriteLog("GetContByIBAN");
        return cont.orElse(null);
    }

    public Client GetClientByIBAN(Banca banca, String IBAN) throws IOException {
        Optional<Client> client = banca.getClienti().stream().filter(x -> {
            try {
                return GetContByIBAN(x, IBAN) != null;
            } catch (IOException e) {
                e.printStackTrace();
            }
            return false;
        }).findFirst();
        WriteLog("GetClientByIBAN");
        return client.orElse(null);
    }

    public List<Card> GetCardByType(Cont cont, CardTypes type) throws IOException {
        WriteLog("GetCardByType");
        return cont.getCarduri().stream().filter(x -> x.getTip().equals(type)).collect(Collectors.toList());
    }

    public List<ExtrasDeCont> GetExtrasDeContBeforeDate(Cont cont, LocalDate date) throws IOException {
        WriteLog("GetExtrasDeContBeforeDate");
        return cont.getExtraseDeCont().stream().filter(x -> x.getData().isBefore(date)).collect(Collectors.toList());
    }

    public List<ExtrasDeCont> GetExtrasDeContAfterDate(Cont cont, LocalDate date) throws IOException {
        WriteLog("GetExtrasDeContAfterDate");
        return cont.getExtraseDeCont().stream().filter(x -> x.getData().isAfter(date)).collect(Collectors.toList());
    }

    public void Save(Banca banca) throws IOException {
        WriteService write = WriteService.getInstance();
        File bancaRoot = new File("Banca_" + banca.getNume());
        if (!bancaRoot.exists())
            bancaRoot.mkdir();
        File bancaCsv = new File(bancaRoot.getAbsolutePath() + "\\banca.csv");
        if (!bancaCsv.exists())
            bancaCsv.createNewFile();
        String bancaCsvPath = bancaCsv.getAbsolutePath();
        write.clear(bancaCsvPath);
        write.write(bancaCsvPath, banca.getNume());

        File clientRoot = new File(bancaRoot.getAbsolutePath() + "\\Client");
        if (!clientRoot.exists())
            clientRoot.mkdir();
        File clientCsv = new File(clientRoot.getAbsolutePath() + "\\client.csv");
        if (!clientCsv.exists())
            clientCsv.createNewFile();

        String clientCsvPath = clientCsv.getAbsolutePath();
        write.clear(clientCsvPath);
        for (Client client : banca.getClienti()) {
            String deserializedClient = client.getFirstName() + "," + client.getLastName() + "," + client.getId() + ","
                    + client.getGender() + "," + client.getDateOfBirth() + "\n";
            write.write(clientCsvPath, deserializedClient);

            File contRoot = new File(clientRoot.getAbsolutePath() + "\\" + client.getId() + "_conturi");
            if (!contRoot.exists())
                contRoot.mkdir();
            File contCsv = new File(contRoot.getAbsolutePath() + "\\cont.csv");
            if (!contCsv.exists())
                contCsv.createNewFile();

            String contCsvPath = contCsv.getAbsolutePath();
            write.clear(contCsvPath);

            for (Cont cont : client.getConturi()) {
                String deserializedCont = cont.getSumaStocata() + "," + cont.getTipCont() + "," + cont.getTipValuta()
                        + "," + cont.getIban() + "\n";
                write.write(contCsvPath, deserializedCont);

                File tranzactieRoot = new File(contRoot.getAbsolutePath() + "\\" + cont.getIban());
                if (!tranzactieRoot.exists())
                    tranzactieRoot.mkdir();
                File tranzactieCsv = new File(tranzactieRoot.getAbsolutePath() + "\\tranzactie.csv");
                if (!tranzactieCsv.exists())
                    tranzactieCsv.createNewFile();

                String tranzactieCsvPath = tranzactieCsv.getAbsolutePath();
                write.clear(tranzactieCsvPath);

                for (Tranzactie tranzactie : cont.getTranzactii()) {
                    String deserializedTranzactie = tranzactie.getIbanDestinatar() + "," + tranzactie.getData()
                            + "," + tranzactie.getSuma() + "," + tranzactie.getTipValuta() + "," + tranzactie.getId() + "\n";
                    write.write(tranzactieCsvPath, deserializedTranzactie);
                }

                File extrasDeContRoot = tranzactieRoot;
                File extrasDeContCsv = new File(extrasDeContRoot.getAbsolutePath() + "\\extrasDeCont.csv");
                if (!extrasDeContCsv.exists())
                    extrasDeContCsv.createNewFile();

                String extrasDeContCsvPath = extrasDeContCsv.getAbsolutePath();
                write.clear(extrasDeContCsvPath);

                for (ExtrasDeCont extrasDeCont : cont.getExtraseDeCont()) {
                    String deserializedExtrasDeCont = extrasDeCont.getSumaStocata() + "," + extrasDeCont.getTipCont()
                            + "," + extrasDeCont.getTipValuta() + "," + extrasDeCont.getData() + "\n";
                    write.write(extrasDeContCsvPath, deserializedExtrasDeCont);
                }

            }
        }
        WriteLog("Save");
    }

    public Banca Load(String baseFolder) throws IOException {
        ReadService reader = ReadService.getInstance();
        File banca = new File(baseFolder);
        if (!banca.exists())
            throw new FileNotFoundException("Banca folder not found");

        File bancaCSV = new File(banca.getAbsolutePath() + "\\banca.csv");
        if (!bancaCSV.exists())
            throw new FileNotFoundException("Banca data not found");
        ArrayList<ArrayList<String>> bancaData = reader.read(bancaCSV.getAbsolutePath());
        Banca bancaRead = new Banca(bancaData.get(0).get(0));

        File client = new File(banca.getAbsolutePath() + "\\Client");
        if (!client.exists())
            throw new FileNotFoundException("Client folder not found");

        File clientCSV = new File(client.getAbsolutePath() + "\\client.csv");
        if (!clientCSV.exists())
            throw new FileNotFoundException("Client data not found");

        ArrayList<ArrayList<String>> clientData = reader.read(clientCSV.getAbsolutePath());
        for (ArrayList<String> serializedClient : clientData) {
            String firstName = serializedClient.get(0);
            String lastName = serializedClient.get(1);
            UUID id = UUID.fromString(serializedClient.get(2));
            GenderTypes gender = GenderTypes.valueOf(serializedClient.get(3));
            LocalDate dateOfBirth = LocalDate.parse(serializedClient.get(4));
            Client clientRead = new Client(firstName, lastName, dateOfBirth, gender, id);

            File cont = new File(client.getAbsolutePath() + "\\" + id + "_conturi");
            if (!cont.exists())
                throw new FileNotFoundException("Cont folder not found");

            File contCsv = new File(cont.getAbsolutePath() + "\\cont.csv");
            if (!contCsv.exists())
                throw new FileNotFoundException("Cont data not found");

            ArrayList<ArrayList<String>> contData = reader.read(contCsv.getAbsolutePath());

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

                ArrayList<ArrayList<String>> extrasDeContData = reader.read(extrasDeContCsv.getAbsolutePath());

                for (ArrayList<String> serializedExtrasDeCont : extrasDeContData) {
                    int sumaStocataExtras = Integer.parseInt(serializedExtrasDeCont.get(0));
                    AccountTypes tipContExtras = AccountTypes.valueOf(serializedExtrasDeCont.get(1));
                    CashTypes tipValutaExtras = CashTypes.valueOf(serializedExtrasDeCont.get(2));
                    LocalDate dataExtras = LocalDate.parse(serializedExtrasDeCont.get(3));
                    ExtrasDeCont extrasDeContRead = new ExtrasDeCont(sumaStocataExtras, tipContExtras, tipValutaExtras, dataExtras);

                    contRead.addExtrasDeCont(extrasDeContRead);
                }

                File tranzactie = extrasDeCont;

                File tranzactieCsv = new File(tranzactie.getAbsolutePath() + "\\tranzactie.csv");
                if (!tranzactieCsv.exists())
                    throw new FileNotFoundException("Tranzactie data not found");

                ArrayList<ArrayList<String>> tranzactieData = reader.read(tranzactieCsv.getAbsolutePath());

                for (ArrayList<String> serializedTranzactie : tranzactieData) {
                    String IBANDestinatar = serializedTranzactie.get(0);
                    LocalDate dataTranzactie = LocalDate.parse(serializedTranzactie.get(1));
                    int suma = Integer.parseInt(serializedTranzactie.get(2));
                    CashTypes tipValutaTranzactie = CashTypes.valueOf(serializedTranzactie.get(3));
                    Tranzactie tranzactieRead = new Tranzactie(IBANDestinatar, dataTranzactie, suma, tipValutaTranzactie);

                    contRead.addTranzacite(tranzactieRead);
                }

                clientRead.addCont(contRead);
            }

            bancaRead.addClient(clientRead);
        }
        WriteLog("Load");
        return bancaRead;
    }

    private void WriteLog(String operation) throws IOException {
        WriteService write = WriteService.getInstance();
        write.write(_logFileName, operation + "," + LocalDate.now() + "\n");
    }
}
