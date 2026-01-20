package eu.martin.store.buyhistory;

import org.springframework.data.jpa.repository.JpaRepository;

public interface BuyInfoRepository extends JpaRepository<BuyInfo, Long> {
}