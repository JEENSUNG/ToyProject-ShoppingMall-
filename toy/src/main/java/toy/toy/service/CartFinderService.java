package toy.toy.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import toy.toy.domain.Cart;
import toy.toy.domain.Cart_Item;
import toy.toy.repository.CartItemRepository;
import toy.toy.repository.CartRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CartFinderService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;

    public Cart findCart(Long id) {
        return cartRepository.findByUserId(id);
    }

    public List<Cart_Item> findUserCartItems(Cart userCart) {
        List<Cart_Item> cartItems = new ArrayList<>();
        List<Cart_Item> allCartItems = cartItemRepository.findAll();
        for(Cart_Item cart_item: allCartItems) {
            if(cart_item.getCart().getId() == userCart.getId()){
                cartItems.add(cart_item);
            }
        }
        return cartItems;
    }

    public List<Cart_Item> findCart_itemByItemId(long id) {
        return cartItemRepository.findCart_itemByItemId(id);
    }
}
