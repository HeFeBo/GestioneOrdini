package com.hector.orders.model;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "Prodotti")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nome", unique = true, nullable = false)
    private String name;

    @Column(name = "categoria", nullable = false)
    @Enumerated(EnumType.STRING)
    private ProductCategory category;

    @Column(name = "origine", nullable = true)
    private String origin;

    @Column(name = "prezzo", nullable = false)
    private Double price;

    @Column(name = "costo", nullable = false)
    private Double cost;

    @Column(name = "dataAcquisto", nullable = true)
    private LocalDateTime purchaseDate;

    @Column(name = "dataVendita", nullable = true)
    private LocalDateTime saleDate;

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (int) (id ^ (id >>> 32));
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Product other = (Product) obj;
        if (id != other.id)
            return false;
        return true;
    }

}
