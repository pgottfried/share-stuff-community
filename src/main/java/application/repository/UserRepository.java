package application.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import application.entity.User;



public interface UserRepository extends JpaRepository<User, Long> {
	
	public User findByUserName(String userName);
	public User findByUuid(String uuid);
}
