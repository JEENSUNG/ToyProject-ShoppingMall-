package toy.toy.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import toy.toy.domain.*;
import toy.toy.repository.CartItemRepository;
import toy.toy.repository.CartRepository;
import toy.toy.repository.HistoryRepository;

import java.util.List;

@RequiredArgsConstructor
@Service
public class CartService {
    private final CartItemRepository cartItemRepository;
    private final CartRepository cartRepository;
    private final CartFinderService cartFinderService;
    private final HistoryRepository historyRepository;
    private final UserPageService userPageService;

    @Transactional
    public void addItem(User user, Item item, int quantity){
        if(item.getStock() == 0 || item.getStock() < quantity){
            return;
        }

        if(cartRepository.findByUserId(user.getId()) == null){
            Cart cart = new Cart();
            cart.setUser(user);
            cartRepository.save(cart);

            Cart_Item cart_item = new Cart_Item();
            cart_item.setCart(cart);
            cart_item.setItem(item);
            cart_item.setCount(quantity);
            cartItemRepository.save(cart_item);
        }else{
            Cart userCart = cartRepository.findByUserId(user.getId());
            List<Cart_Item> allItems = cartFinderService.findUserCartItems(userCart);
            for(Cart_Item userItem : allItems){
                if(userItem.getItem().getId() == item.getId()){
                    return;
                }
            }
            Cart_Item cart_item = new Cart_Item();
            cart_item.setCart(userCart);
            cart_item.setItem(item);
            cart_item.setCount(quantity);
            cartItemRepository.save(cart_item);
        }
    }
    @Transactional
    public void deleteCart_item(long id){
        cartItemRepository.deleteById(id);
    }

    @Transactional
    public void saveHistory(long id, Cart_Item item){
        User user = userPageService.findUser(id);
        History history = new History();

        history.setUser(user);
        history.setItemName(item.getItem().getName());
        history.setItemPrice(item.getItem().getPrice());
        history.setItemCount(item.getCount());
        history.setSeller(item.getItem().getUser());
        historyRepository.save(history);
    }

    public List<History> getHistoriesForUser(User user){
        if(historyRepository.findAllHistoryByUser(user) == null){
            return null;
        }
        return historyRepository.findAllHistoryByUser(user);
    }

    public List<History> getHistoryForSeller(User user){
        if(historyRepository.findAllHistoryBySeller(user) == null){
            return null;
        }
        return historyRepository.findAllHistoryBySeller(user);
    }

    public History getHistory(long id){
        return historyRepository.findHistoryById(id);
    }
}
