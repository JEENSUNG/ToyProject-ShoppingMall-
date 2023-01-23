package toy.toy.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;
import toy.toy.config.auth.PrincipalDetails;
import toy.toy.domain.*;
import toy.toy.service.CartFinderService;
import toy.toy.service.CartService;
import toy.toy.service.ItemService;
import toy.toy.service.UserPageService;

import java.util.List;

@RequiredArgsConstructor
@Controller
public class UserPageController {

    private final UserPageService userPageService;
    private final CartService cartService;
    private final ItemService itemService;
    private final CartFinderService cartFinderService;

    @GetMapping("/user/{id}")
    public String userPage(@PathVariable("id") long id, Model model,
                           @AuthenticationPrincipal PrincipalDetails principalDetails){
        if(principalDetails.getUser().getId() == id && principalDetails.getUser().getRole().equals("ROLE_SELLER")){
            User loginUser = userPageService.findUser(principalDetails.getUser().getId());
            model.addAttribute("user", userPageService.findUser(id));
            return "/seller/sellerUserPage";
        }
        else if(principalDetails.getUser().getId() == id && principalDetails.getUser().getRole().equals("ROLE_USER")){
            User loginUser = userPageService.findUser(principalDetails.getUser().getId());
            Cart cart = cartFinderService.findCart(loginUser.getId());
            List<Cart_Item> userItems = cartFinderService.findUserCartItems(cart);
            int count = userItems.size();
            model.addAttribute("count", count);
            model.addAttribute("user", userPageService.findUser(id));
            return "/user/userPage";
        }else{
            return "redirect:/main";
        }
    }

    @GetMapping("/user/{id}/update")
    public String userPageModify(@PathVariable("id") long id, User user, MultipartFile file) throws Exception{
        userPageService.userPageModify(user, file);
        return "redirect:/user/{id}";
    }

    @GetMapping("/user/{id}/cart")
    public String userCartView(@PathVariable("id") long id, Model model, @AuthenticationPrincipal PrincipalDetails principalDetails){
        if(principalDetails.getUser().getId() == id){
            Cart cart = cartFinderService.findCart(id);
            if(cart == null){
                return "redirect:/main";
            }else{
                List<Cart_Item> userCart_items = cartFinderService.findUserCartItems(cart);
                int count = 0;
                int total = 0;
                for(Cart_Item item : userCart_items){
                    count += 1;
                    total += item.getCount() * item.getItem().getPrice();
                }
                model.addAttribute("cartCount", count);
                model.addAttribute("totalPrice", total);
                model.addAttribute("cartItems", userCart_items);
                model.addAttribute("user", userPageService.findUser(id));
                return "/user/userCart";
            }
        }else{
            return "redirect:/main";
        }
    }

    @PostMapping("/user/{id}/cart/{itemId}")
    public String addCartItem(@PathVariable("id") long id, @PathVariable("itemId") long itemId, int quantity){
        User user = userPageService.findUser(id);
        Item item = itemService.itemView(itemId);
        cartService.addItem(user, item, quantity);
        return "redirect:/item/{itemId}";
    }

    @GetMapping("/user/{id}/cart/{cart_itemId}/delete")
    public String deleteCartItem(@PathVariable("id") long id, @PathVariable("cart_itemId") long cart_itemId, Model model){
        cartService.deleteCart_item(cart_itemId);
        Cart cart = cartFinderService.findCart(id);
        List<Cart_Item> cart_items = cartFinderService.findUserCartItems(cart);
        int total = 0;
        for(Cart_Item item : cart_items){
            total += item.getCount() * item.getItem().getPrice();
        }
        model.addAttribute("totalPrice", total);
        model.addAttribute("cartItems", cart_items);
        model.addAttribute("user", userPageService.findUser(id));
        return "/user/userCart";
    }

    @PostMapping("/user/{id}/cart/checkout")
    public String checkOut(@PathVariable("id") long id, @AuthenticationPrincipal PrincipalDetails principalDetails, Model model){
        if(principalDetails.getUser().getId() == id){
            Cart cart = cartFinderService.findCart(id);
            List<Cart_Item> cart_items = cartFinderService.findUserCartItems(cart);
            int total = 0;
            for(Cart_Item item : cart_items){
                if(item.getItem().getStock() == 0 || item.getCount() > item.getItem().getStock()){
                    return "redirect:/main";
                }
                total += item.getCount() * item.getItem().getPrice();
            }
            User user = userPageService.findUser(id);
            int userMoney = user.getMoney();
            if(userMoney < total){
                return "redirect:/main";
            }
            for(Cart_Item item : cart_items){
                User seller = item.getItem().getUser();
                user.setMoney(user.getMoney() - (item.getCount() * item.getItem().getPrice()));
                seller.setMoney(seller.getMoney() + (item.getCount() * item.getItem().getPrice()));;

                item.getItem().setStock(item.getItem().getStock() - item.getCount());
                item.getItem().setCount(item.getItem().getCount() + item.getCount());

                cartService.saveHistory(id, item);
                cartService.deleteCart_item(item.getId());
            }
            model.addAttribute("totalPrice", total);
            model.addAttribute("cartItems", cart_items);
            model.addAttribute("user", userPageService.findUser(id));
            return "redirect:/user/{id}/cart";
        }else{
            return "redirect:/main";
        }
    }

    @GetMapping("/user/{id}/history")
    public String userHistory(@PathVariable("id") long id, @AuthenticationPrincipal PrincipalDetails principalDetails, Model model){
        if(principalDetails.getUser().getId() == id){
            User user = userPageService.findUser(principalDetails.getUser().getId());
            Cart cart = cartFinderService.findCart(user.getId());
            List<Cart_Item> cart_items = cartFinderService.findUserCartItems(cart);
            int count = cart_items.size();

            List<History> histories = cartService.getHistoriesForUser(user);

            model.addAttribute("histories", histories);
            model.addAttribute("cartCount", count);
            model.addAttribute("user", user);
            return "/user/userHistory";
        }else{
            return "redirect:/main";
        }
    }

    @GetMapping("/user/{id}/charge")
    public String chargeMoney(@PathVariable("id") long id, @AuthenticationPrincipal PrincipalDetails principalDetails, Model model){
        if(id != principalDetails.getUser().getId()){
            return "redirect:/main";
        }
        model.addAttribute("user", userPageService.findUser(id));
        return "/user/chargePage";
    }

    @GetMapping("/user/charge/process")
    public String chargeMoneyProcess(@AuthenticationPrincipal PrincipalDetails principalDetails, int mount){
        userPageService.chargeMoney(principalDetails.getUser(), mount);
        return "redirect:/main";
    }
}
