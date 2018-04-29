package application.repository;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import application.entity.Rating;
import application.entity.User;


public interface RatingRepository extends JpaRepository<Rating, Long> {
	
	@Query("SELECT avg(stars) from Rating r where r.onUser= ?1")
	public BigDecimal getAvgStars(User user);
	@Query("SELECT count(*) from Rating r where r.onUser= ?1")
	public long getRatingCount(User user);
	public Page<Rating> findByOnUserIdOrderByIdDesc(Long id, Pageable pageable);
	public List<Rating> findTop3ByOnUserIdOrderByIdDesc(Long id);
	
}
