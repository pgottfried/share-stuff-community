package application.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import application.entity.Image;


public interface ImageRepository extends JpaRepository<Image, Long> {
		
	public Image findById(Long id);
	public List<Image> findByOfferId(Long offerId);
}
