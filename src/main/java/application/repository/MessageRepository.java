package application.repository;






import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import application.entity.Message;


public interface MessageRepository extends JpaRepository<Message, Long> {
	
//	@Query("SELECT count(*) from Rating r where r.onUser= ?1")
//	SELECT * FROM test_v03.message
//	WHERE conv_id = 28 AND id > (select(max(id) - 20) from test_v03.message);
	@Query("SELECT m from Message m WHERE m.conversation.id =?1 AND m.id > (Select (max(m.id) - 20) from Message m)")
	List<Message> findLast20Messages(Long convId);
	
	List<Message> findTop20ByConversationIdOrderByCreatedDesc(Long convId);
	
}
