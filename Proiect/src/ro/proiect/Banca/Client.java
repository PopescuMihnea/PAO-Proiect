package ro.proiect.Banca;

import ro.proiect.Enums.GenderTypes;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class Client {
    private final LocalDate dateOfBirth;
    private final UUID id;
    private String firstName;
    private String lastName;
    private GenderTypes gender;
    private final TreeSet<Cont> conturi = new TreeSet<>();

    public Client() {
        this("", "", LocalDate.now(), GenderTypes.UNSPECIFIED);
    }

    public Client(String firstName, String lastName) {
        this(firstName, lastName, LocalDate.now(), GenderTypes.UNSPECIFIED);
    }

    public Client(Client client) {
        this.firstName = client.firstName;
        this.lastName = client.lastName;
        this.dateOfBirth = client.dateOfBirth;
        this.gender = client.gender;
        this.id = UUID.randomUUID();
        for (Cont cont : client.conturi) {
            this.conturi.add(new Cont(cont));
        }
    }

    public Client(String firstName, String lastName, LocalDate dateOfBirth, GenderTypes gender) {
        this(firstName, lastName, dateOfBirth, gender, UUID.randomUUID());
    }

    public Client(String firstName, String lastName, LocalDate dateOfBirth, GenderTypes gender, UUID id) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public GenderTypes getGender() {
        return gender;
    }

    public void setGender(GenderTypes gender) {
        this.gender = gender;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Client client = (Client) o;
        return id.equals(client.id);
    }

    public TreeSet<Cont> getConturi() {
        return conturi.stream().map(Cont::new).collect(Collectors.toCollection(TreeSet::new));
    }

    @Override
    public String toString() {
        return "Client{" +
                "_firstName='" + firstName + '\'' +
                ", _lastName='" + lastName + '\'' +
                ", _dateOfBirth=" + dateOfBirth +
                ", _gender=" + gender +
                ", _id=" + id +
                ", _conturi=" + conturi +
                '}';
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public UUID getId() {
        return id;
    }

    public void addCont(Cont cont) {
        this.conturi.add(new Cont(cont));
    }

    public void addConturi(List<Cont> conturi) {
        for (Cont cont : conturi) {
            this.addCont(cont);
        }
    }

    public void removeConturi(List<Cont> conturi) {
        for (Cont cont : conturi) {
            this.removeCont(cont);
        }
    }

    public void removeCont(Cont cont) {
        this.conturi.remove(cont);
    }

}
