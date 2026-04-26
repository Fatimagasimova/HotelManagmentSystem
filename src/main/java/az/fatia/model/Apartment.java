package az.fatia.model;


import az.fatia.enums.Status;

import java.math.BigDecimal;

public class Apartment {

    private Integer id;
    private BigDecimal price;
    private Status status;
    private String clientName = null;

    public Apartment() {

    }

    public Apartment(int id, BigDecimal price) {
        this.id = id;
        this.price = price;
        this.status = Status.AVAILABLE;
        this.clientName = null;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    @Override
    public String toString() {
        return id + " | " + price + " | " + status + " | " + clientName;
    }
}