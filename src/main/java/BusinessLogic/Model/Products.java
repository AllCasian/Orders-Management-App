package BusinessLogic.Model;

/**
 * Product: Responsabila cu retinerea datelor despre
 * un produs anume. Retine informatii precum nume,
 * pret, cantitate si id-ul produsului realizat cu
 * auto-increment in baza de date.
 */
public class Products {
    private String name;
    private int price;
    private int quantity;
    private int idProduct;

    /**
     * Constructor cu parametri pentru initializarea unui produs.
     *
     * @param idProduct Id-ul produsului
     * @param price     Pretul produsului
     * @param name      Numele produsului
     * @param quantity  Cantitatea disponibila
     */
    public Products(int idProduct, int price, String name, int quantity){
        this.idProduct = idProduct;
        this.price = price;
        this.name = name;
        this.quantity = quantity;
    }

    /**
     * Constructor implicit pentru initializarea unui produs.
     */
    public Products(){

    }

    /**
     * Constructor cu parametri pentru initializarea unui produs.
     *
     * @param price    Pretul produsului
     * @param name     Numele produsului
     * @param quantity Cantitatea disponibila
     */
    public Products(int price, String name, int quantity){
        this.price = price;
        this.name = name;
        this.quantity = quantity;
    }

    /**
     * Seteaza id-ul produsului.
     *
     * @param idProduct Id-ul produsului
     */
    public void setIdProduct(int idProduct) {
        this.idProduct = idProduct;
    }

    /**
     * Returneaza id-ul produsului.
     *
     * @return Id-ul produsului
     */
    public int getIdProduct() {
        return this.idProduct;
    }

    /**
     * Seteaza pretul produsului.
     *
     * @param price Pretul produsului
     */
    public void setPrice(int price) {
        this.price = price;
    }

    /**
     * Returneaza pretul produsului.
     *
     * @return Pretul produsului
     */
    public int getPrice() {
        return this.price;
    }

    /**
     * Seteaza numele produsului.
     *
     * @param name Numele produsului
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returneaza numele produsului.
     *
     * @return Numele produsului
     */
    public String getName() {
        return this.name;
    }

    /**
     * Seteaza cantitatea disponibila a produsului.
     *
     * @param quantity Cantitatea disponibila
     */
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    /**
     * Returneaza cantitatea disponibila a produsului.
     *
     * @return Cantitatea disponibila
     */
    public int getQuantity() {
        return this.quantity;
    }

    /**
     * Returneaza reprezentarea sub forma de text a produsului (numele).
     *
     * @return Numele produsului
     */
    public String toString(){
        return name;
    }
}
