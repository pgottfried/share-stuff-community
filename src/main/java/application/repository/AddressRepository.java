package application.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import application.entity.Address;


public interface AddressRepository extends JpaRepository<Address, Long> {
	
	public Address findByUserUserName(String userName);
	public Address findByUserId(Long id);
	
}
