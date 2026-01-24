package eu.martin.store.products;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

interface BuyLogRepository extends JpaRepository<BuyLog, Long> {
    @Query("select b from BuyLog b where b.product.id = :id")
    List<BuyLog> getProductBuyLogs(@Param("id") Integer id);
}