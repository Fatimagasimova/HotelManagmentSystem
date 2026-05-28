package az.fatia.model;

import az.fatia.enums.Status;
import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "apartments")
public class Apartment {

    @Id
    @Column(name = "id")
    private Integer id;

    @Column(name = "price", nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private Status status;

    @Column(name = "client_name", length = 100)
    private String clientName = null;

    public Apartment() {
    }

    public Apartment(int id, BigDecimal price) {
        this.id = id;
        this.price = price;
        this.status = Status.AVAILABLE;
        this.clientName = null;
    }

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }
    public BigDecimal getPrice() { return price; }
    public void setPrice(BigDecimal price) { this.price = price; }
    public Status getStatus() { return status; }
    public void setStatus(Status status) { this.status = status; }
    public String getClientName() { return clientName; }
    public void setClientName(String clientName) { this.clientName = clientName; }

    @Override
    public String toString() {
        return id + " | " + price + " | " + status + " | " + clientName;
    }
}