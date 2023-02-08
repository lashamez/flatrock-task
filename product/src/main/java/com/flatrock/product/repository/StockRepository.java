package com.flatrock.product.repository;

import com.flatrock.product.domain.StockProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StockRepository extends JpaRepository<StockProduct, Long> {
    @Query("select stock from StockProduct stock left join fetch stock.product where stock.product.id =:id")
    Optional<StockProduct> findOneByProductId(@Param("id") Long id);

    @Query("select stock from StockProduct stock left join fetch stock.product where stock.product.id in (:ids)")
    List<StockProduct> findByProductIds(@Param("ids") List<Long> ids);

    @Modifying
    @Query("update StockProduct  stock set stock.quantity=stock.quantity-:quantity where stock.product.id=:productId")
    void reduceProductQuantity(@Param("productId") long productId, @Param("quantity") int quantity);

    @Modifying
    @Query("update StockProduct  stock set stock.quantity=stock.quantity+:quantity where stock.product.id=:productId")
    void increaseProductQuantity(@Param("productId") long productId, @Param("quantity") int quantity);

}
