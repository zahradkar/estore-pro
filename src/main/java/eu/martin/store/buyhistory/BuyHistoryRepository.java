package eu.martin.store.buyhistory;

import org.springframework.data.jpa.repository.JpaRepository;

public interface BuyHistoryRepository extends JpaRepository<BuyHistory, Long> {
}