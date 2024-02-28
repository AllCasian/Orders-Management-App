package BusinessLogic;

import java.time.LocalDateTime;

/**
 * Responsabila cu retinerea datelor despre o factura a unei comenzi.
 */
public record Bill(int orderId, double amount, LocalDateTime timestamp) {
    /**
     * Constructor pentru initializarea unei facturi.
     *
     * @param orderId   Id-ul comenzii
     * @param amount    Suma facturata
     * @param timestamp Data și ora facturii
     */
    public Bill {
        if (orderId <= 0) {
            throw new IllegalArgumentException("orderId must be positive");
        }
        if (amount <= 0) {
            throw new IllegalArgumentException("amount must be positive");
        }
        if (timestamp == null) {
            throw new IllegalArgumentException("timestamp cannot be null");
        }
    }

    /**
     * Returneaza id-ul comenzii asociate facturii.
     *
     * @return Id-ul comenzii
     */
    public int getOrderId() {
        return orderId;
    }

    /**
     * Returneaza suma facturata.
     *
     * @return Suma facturata
     */
    public double getAmount() {
        return amount;
    }

    /**
     * Returneaza data și ora facturii.
     *
     * @return Data și ora facturii
     */
    public LocalDateTime getTimestamp() {
        return timestamp;
    }
}
