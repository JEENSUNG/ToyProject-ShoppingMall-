package toy.toy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import toy.toy.domain.History;
import toy.toy.domain.User;

import java.util.List;

public interface HistoryRepository extends JpaRepository<History, Long> {

    List<History> findAllHistoryByUser(User user);


    List<History> findAllHistoryBySeller(User user);

    History findHistoryById(long id);
}
