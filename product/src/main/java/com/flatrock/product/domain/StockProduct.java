package com.flatrock.product.domain;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Entity
@Table(name = "stock")
@Getter
@Setter
public class StockProduct implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Min(value = 0)
    @Column(name = "quantity")
    private Integer quantity;


    @OneToOne(cascade=CascadeType.ALL)
    @JoinColumn(name="product_id")
    private Product product;


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof StockProduct)) {
            return false;
        }
        return id != null && id.equals(((StockProduct) o).id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

}
