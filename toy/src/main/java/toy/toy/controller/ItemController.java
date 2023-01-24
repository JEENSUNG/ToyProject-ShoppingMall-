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
import toy.toy.domain.Cart;
import toy.toy.domain.Cart_Item;
import toy.toy.domain.Item;
import toy.toy.domain.User;
import toy.toy.service.CartFinderService;
import toy.toy.service.CartService;
import toy.toy.service.ItemService;
import toy.toy.service.UserPageService;

import java.io.IOException;
import java.util.List;

@RequiredArgsConstructor
@Controller
public class ItemController {

    private final ItemService itemService;
    private final CartService cartService;
    private final UserPageService userPageService;
    private final CartFinderService cartFinderService;

    @GetMapping("/")
    public String mainPage(Model model){
        List<Item> items = itemService.allItem();
        model.addAttribute("items", items);
        return "none/main";
    }

    @GetMapping("/main")
    public String main(Model model, @AuthenticationPrincipal PrincipalDetails principalDetails){
        List<Item> items = itemService.allItem();
        User login = userPageService.findUser(principalDetails.getUser().getId());

        if(principalDetails.getUser().getRole().equals("ROLE_USER")){
            Cart userCart = cartFinderService.findCart(login.getId());
            List<Cart_Item> userItems = cartFinderService.findUserCartItems(userCart);
            int count = userItems.size();
            model.addAttribute("cartCount", count);
        }
        model.addAttribute("items", items);
        model.addAttribute("user", login);
        return "mainLoginPage";
    }

    @GetMapping("/item/{id}")
    public String itemView(@PathVariable("id") Long id, Model model, @AuthenticationPrincipal PrincipalDetails principalDetails){
        if(principalDetails.getUser().getRole().equals("ROLE_ADMIN") ||
        principalDetails.getUser().getRole().equals("ROLE_SELLER")){
            model.addAttribute("user", principalDetails.getUser());
            model.addAttribute("item", itemService.itemView(id));
            return "/seller/item";
        }else{
            User login = userPageService.findUser(principalDetails.getUser().getId());
            Cart userCart = cartFinderService.findCart(login.getId());
            List<Cart_Item> cartItems = cartFinderService.findUserCartItems(userCart);
            int count = cartItems.size();
            model.addAttribute("cartCount", count);
            model.addAttribute("user", principalDetails.getUser());
            model.addAttribute("item", itemService.itemView(id));
            return "/user/item";
        }
    }

    @GetMapping("/item/upload")
    public String itemUpload(@AuthenticationPrincipal PrincipalDetails principalDetails){
        if(principalDetails.getUser().getRole().equals("ROLE_ADMIN") ||
        principalDetails.getUser().getRole().equals("ROLE_SELLER")){
            return "/seller/itemUpload";
        }else{
            return "redirect:/main";
        }
    }

    @PostMapping("/item/upload/process")
    public String itemUploadProcess(Item item, MultipartFile file, @AuthenticationPrincipal PrincipalDetails principalDetails) throws Exception{
        if(principalDetails.getUser().getRole().equals("ROLE_ADMIN") ||
        principalDetails.getUser().getRole().equals("ROLE_SELLER")){
            item.setUser(principalDetails.getUser());
            itemService.saveItem(item, file);
            return "redirect:/main";
        }else{
            return "redirect:/main";
        }
    }

    @GetMapping("/item/{id}/modify")
    public String itemModify(@PathVariable("id") Long id, Model model, @AuthenticationPrincipal PrincipalDetails principalDetails){
        if(principalDetails.getUser().getRole().equals("ROLE_ADMIN") ||
        principalDetails.getUser().getRole().equals("ROLE_SELLER")){
            User user = itemService.itemView(id).getUser();
            if(user.getId() == principalDetails.getUser().getId()){
                model.addAttribute("item", itemService.itemView(id));
                return "/seller/itemModify";
            }else{
                return "redirect:/main";
            }
        }else{
            return "redirect:/main";
        }
    }

    @PostMapping("/item/{id}/modify/process")
    public String itemModifyProcess(Item item, MultipartFile file, @PathVariable("ld") long id, @AuthenticationPrincipal PrincipalDetails principalDetails) throws IOException {
        if(principalDetails.getUser().getRole().equals("ROLE_ADMIN") ||
        principalDetails.getUser().getRole().equals("ROLE_SELLER")){
            User user = itemService.itemView(id).getUser();
            if(user.getId() == principalDetails.getUser().getId()){
                itemService.itemModify(item, id, file);
                return "redirect:/main";
            }else{
                return "redirect:/main";
            }
        }else{
            return "redirect:/main";
        }
    }

    @GetMapping("/item/{id}/delete")
    public String itemDelete(@PathVariable("id") long id, @AuthenticationPrincipal PrincipalDetails principalDetails){
        if(principalDetails.getUser().getRole().equals("ROLE_ADMIN") ||
        principalDetails.getUser().getRole().equals("ROLE_SELLER")){
            User user = itemService.itemView(id).getUser();
            if(user.getId() == principalDetails.getUser().getId()){
                itemService.itemDelete(id);
                return "redirect:/main";
            }else{
                return "redirect:/main";
            }
        }else{
            return "redirect:/main";
        }
    }
}
