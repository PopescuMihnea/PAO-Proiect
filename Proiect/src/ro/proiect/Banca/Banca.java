package ro.proiect.Banca;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class Banca {
    private final ArrayList<Client> clienti = new ArrayList<>();
    private String nume;

    public Banca(String nume) {
        this.nume = nume;
    }

    public String getNume() {
        return nume;
    }

    public void setNume(String nume) {
        this.nume = nume;
    }

    @Override
    public String toString() {
        return "Banca{" +
                "_nume='" + nume + '\'' +
                ", _clienti=" + clienti +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Banca banca = (Banca) o;
        return nume.equals(banca.nume) && clienti.equals(banca.clienti);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nume);
    }

    public List<Client> getClienti() {
        return clienti.stream().map(Client::new).collect(Collectors.toList());
    }

    public void addClient(Client client) {
        this.clienti.add(new Client(client));
    }

    public void removeClient(Client client) {
        this.clienti.remove(client);
    }
}
