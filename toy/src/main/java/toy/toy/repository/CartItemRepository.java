package toy.toy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import toy.toy.domain.Cart_Item;

import java.util.List;

public interface CartItemRepository extends JpaRepository<Cart_Item, Long> {
    List<Cart_Item> findCart_itemByItemId(long id);
}
