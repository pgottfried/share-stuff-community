package application.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import application.entity.Offer;



public interface OfferRepository extends JpaRepository<Offer, Long> {
	
	public Offer findByTitle(String title);
	public Offer findById(Long id);
	public Offer findFirstByUserUserNameOrderByIdDesc(String userName);
	public Offer findFirstByUserIdOrderByIdDesc(Long id);
//	public Offer findFirstByIdOrderByIdDesc();
	public List<Offer> findByCategoryName(String name);
	public Page<Offer> findByCategoryName(Pageable pageable);
	public List<Offer> findByUserUserName(String userName);
	public Page<Offer> findByUserUserName(Pageable pageable);
	
	
}
