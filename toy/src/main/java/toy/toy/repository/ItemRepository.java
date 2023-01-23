package toy.toy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import toy.toy.domain.Item;

import java.util.List;

public interface ItemRepository extends JpaRepository<Item, Long> {
    List<Item> findItemByUserIdOrderByCountDesc(long id);
}
