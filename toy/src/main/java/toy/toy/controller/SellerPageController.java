package toy.toy.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import toy.toy.config.auth.PrincipalDetails;
import toy.toy.domain.History;
import toy.toy.domain.Item;
import toy.toy.domain.User;
import toy.toy.service.CartService;
import toy.toy.service.ItemService;
import toy.toy.service.UserPageService;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Controller
public class SellerPageController {

    private final UserPageService userPageService;
    private final ItemService itemService;
    private final CartService cartService;

    @GetMapping("/seller/{id}")
    public String sellerPage(@PathVariable("id") long id, Model model,
                             @AuthenticationPrincipal PrincipalDetails principalDetails){
        if(principalDetails.getUser().getId() != id){
            return "redirect:/main";
        }
        User seller = userPageService.findUser(id);
        List<Item> items = itemService.allItem();
        List<Item> userItem = new ArrayList<>();
        int total = 0;
        for(Item item : items){
            if(item.getUser().getId() == id){
                userItem.add(item);
                total += item.getPrice() * item.getCount();
            }
        }
        model.addAttribute("totalPrice", total);
        model.addAttribute("seller", seller);
        model.addAttribute("userItem", userItem);
        return "seller/sellerPage";
    }

    @GetMapping("/seller/{id}/history")
    public String sellerHistory(@PathVariable("id") long id, Model model,
                                @AuthenticationPrincipal PrincipalDetails principalDetails){
        if(principalDetails.getUser().getId() == id){
            User user = userPageService.findUser(id);
            List<History> histories = cartService.getHistoryForSeller(user);
            List<Item> items = itemService.itemBySeller(id);

            model.addAttribute("items", items);
            model.addAttribute("histories", histories);
            model.addAttribute("user", user);
            return "/seller/sellerHistory";
        }else{
            return "redirect:/main";
        }
    }
}
