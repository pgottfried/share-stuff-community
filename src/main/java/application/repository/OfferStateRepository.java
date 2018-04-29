package application.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import application.entity.OfferState;

public interface OfferStateRepository extends JpaRepository<OfferState, Long>{
	

}
