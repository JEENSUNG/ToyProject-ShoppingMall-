package toy.toy.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import toy.toy.config.auth.PrincipalDetails;
import toy.toy.domain.History;
import toy.toy.service.CartService;
import toy.toy.service.UserPageService;

@RequiredArgsConstructor
@Controller
public class SaleHistoryController {

    private final UserPageService userPageService;
    private final CartService cartService;

    @GetMapping("/seller/{sellerId}/history/{historyId}")
    public String salePageView(@PathVariable("sellerId") long sellerId, @PathVariable("historyId") long historyId, Model model,
                               @AuthenticationPrincipal PrincipalDetails principalDetails){
        if(principalDetails.getUser().getId() != sellerId){
            return "redirect:/main";
        }
        History history = cartService.getHistory(historyId);
        model.addAttribute("user", history.getUser());
        model.addAttribute("seller", history.getSeller());
        model.addAttribute("history", history);
        return "/seller/salePage";
    }
}
