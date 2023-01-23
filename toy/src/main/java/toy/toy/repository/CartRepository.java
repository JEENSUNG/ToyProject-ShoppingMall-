package toy.toy.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import toy.toy.domain.Cart;

public interface CartRepository extends JpaRepository<Cart, Long> {
    Cart findByUserId(Long id);
}
