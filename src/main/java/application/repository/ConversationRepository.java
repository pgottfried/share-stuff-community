package application.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import application.entity.Conversation;



public interface ConversationRepository extends JpaRepository<Conversation, Long> {
	
	public List<Conversation> findByUsersIdOrderByUpdatedDesc(Long userId);
	
}
