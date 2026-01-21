package eu.martin.store.products;

import org.springframework.data.jpa.repository.JpaRepository;

interface BuyHistoryRepository extends JpaRepository<BuyHistory, Long> {
}