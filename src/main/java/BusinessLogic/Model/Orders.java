package BusinessLogic.Model;

/**
 * Orders: Responsabila cu retinerea datelor despre
 * o comanda. Retine informatii precum: id-ul comenzii
 * care e realizat cu auto-increment in baza de date,
 * id-ul clientului care a efectuat comanda, id-ul
 * produsului, cantitatea totala si pretul final.
 */
public class Orders {
    private int idOrder;
    private int idClient;
    private int idProduct;
    private int totalAmount;
    private int price;

    /**
     * Constructor cu parametri pentru initializarea unei comenzi.
     *
     * @param idOrder     Id-ul comenzii
     * @param idCustomer  Id-ul clientului care a efectuat comanda
     * @param idProduct   Id-ul produsului
     * @param totalAmount Cantitatea totala
     * @param price       Pretul final
     */
    public Orders(int idOrder, int idCustomer, int idProduct, int totalAmount, int price){
        this.idOrder = idOrder;
        this.idClient = idCustomer;
        this.idProduct = idProduct;
        this.totalAmount = totalAmount;
        this.price = price;
    }

    /**
     * Constructor implicit pentru initializarea unei comenzi.
     */
    public Orders(){

    }

    /**
     * Constructor cu parametri pentru initializarea unei comenzi.
     *
     * @param idClient    Id-ul clientului care a efectuat comanda
     * @param idProduct   Id-ul produsului
     * @param totalAmount Cantitatea totala
     * @param price       Pretul final
     */
    public Orders(int idClient, int idProduct, int totalAmount, int price){
        this.idClient = idClient;
        this.idProduct = idProduct;
        this.totalAmount = totalAmount;
        this.price = price;
    }

    /**
     * Seteaza id-ul comenzii.
     *
     * @param idOrder Id-ul comenzii
     */
    public void setIdOrder(int idOrder) {
        this.idOrder = idOrder;
    }

    /**
     * Returneaza id-ul comenzii.
     *
     * @return Id-ul comenzii
     */
    public int getIdOrder() {
        return this.idOrder;
    }

    /**
     * Seteaza id-ul clientului care a efectuat comanda.
     *
     * @param idClient Id-ul clientului
     */
    public void setIdClient(int idClient) {
        this.idClient = idClient;
    }

    /**
     * Returneaza id-ul clientului care a efectuat comanda.
     *
     * @return Id-ul clientului
     */
    public int getIdClient() {
        return this.idClient;
    }

    /**
     * Seteaza id-ul produsului din comanda.
     *
     * @param idProduct Id-ul produsului
     */
    public void setIdProduct(int idProduct) {
        this.idProduct = idProduct;
    }

    /**
     * Returneaza id-ul produsului din comanda.
     *
     * @return Id-ul produsului
     */
    public int getIdProduct() {
        return this.idProduct;
    }

    /**
     * Seteaza cantitatea totala din comanda.
     *
     * @param totalAmount Cantitatea totala
     */
    public void setTotalAmount(int totalAmount) {
        this.totalAmount = totalAmount;
    }

    /**
     * Returneaza cantitatea totala din comanda.
     *
     * @return Cantitatea totala
     */
    public int getTotalAmount() {
        return this.totalAmount;
    }

    /**
     * Seteaza pretul final al comenzii.
     *
     * @param price Pretul final
     */
    public void setPrice(int price) {
        this.price = price;
    }

    /**
     * Returneaza pretul final al comenzii.
     *
     * @return Pretul final
     */
    public int getPrice() {
        return this.price;
    }
}
