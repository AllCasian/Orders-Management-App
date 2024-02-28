package BusinessLogic.Model;

/**
 * Clasa Client: Responsabila cu retinerea datelor despre
 * un client anume. Retine informatii precum: name, email
 * si id-ul corespunzator realizat cu auto-increment in
 * baza de date.
 */
public class Client {
    private String name;
    private String email;
    private int idClient;

    /**
     * Constructor cu parametri pentru initializarea unui obiect Client.
     *
     * @param idClient Id-ul clientului
     * @param name     Numele clientului
     * @param email    Adresa de email a clientului
     */
    public Client(int idClient, String name, String email){
        this.idClient = idClient;
        this.name = name;
        this.email = email;
    }

    /**
     * Constructor implicit pentru initializarea unui obiect Client.
     */
    public Client(){

    }

    /**
     * Constructor cu parametri pentru initializarea unui obiect Client.
     *
     * @param name  Numele clientului
     * @param email Adresa de email a clientului
     */
    public Client(String name, String email){
        this.name = name;
        this.email = email;
    }

    /**
     * Seteaza id-ul clientului.
     *
     * @param idClient Id-ul clientului
     */
    public void setIdClient(int idClient) {
        this.idClient = idClient;
    }

    /**
     * Returneaza id-ul clientului.
     *
     * @return Id-ul clientului
     */
    public int getIdClient(){
        return this.idClient;
    }

    /**
     * Seteaza numele clientului.
     *
     * @param name Numele clientului
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returneaza numele clientului.
     *
     * @return Numele clientului
     */
    public String getName(){
        return this.name;
    }

    /**
     * Seteaza adresa de email a clientului.
     *
     * @param email Adresa de email a clientului
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * Returneaza adresa de email a clientului.
     *
     * @return Adresa de email a clientului
     */
    public String getEmail() {
        return this.email;
    }

    /**
     * Returneaza reprezentarea sub forma de string a obiectului Client (numele clientului).
     *
     * @return Numele clientului
     */
    public  String toString(){
        return name;
    }

}
