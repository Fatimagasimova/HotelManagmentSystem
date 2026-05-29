package az.fatia.model;

import az.fatia.enums.Status;
import jakarta.persistence.*;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "apartments")
@Data
@RequiredArgsConstructor
public class Apartment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Integer id;

    @Column(name = "price", nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private Status status;

    @Column(name = "client_name", length = 100)
    private String clientName = null;

    public Apartment(int id, BigDecimal price) {
    }

    @Override
    public String toString() {
        return id + " | " + price + " | " + status + " | " + clientName;
    }
}