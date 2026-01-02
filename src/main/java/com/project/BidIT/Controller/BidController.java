package com.project.BidIT.Controller;

import com.project.BidIT.Repo.BidDetailsRepo;
import com.project.BidIT.Repo.ItemRepository;
import com.project.BidIT.Repo.UserRepository;
import com.project.BidIT.Service.BidService;
import com.project.BidIT.Service.CategoryService;
import com.project.BidIT.entity.Bid;
import com.project.BidIT.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;
import java.util.List;

@Controller
@RequestMapping("/bid")
public class BidController {
@Autowired
  private  UserRepository userRepository;
@Autowired
private CategoryService categoryService;

@Autowired
private BidDetailsRepo bidDetailsRepo;

@Autowired
private BidService bidService;
@Autowired
private ItemRepository itemRepository;
    @GetMapping("/dashboard")
    public String table(Model model, Principal principal){
        if (principal == null) return "redirect:/login";

        User users = userRepository.findByEmail(principal.getName()).orElse(null);
        model.addAttribute("users", users);

        List<Bid> allBids = bidService.getAllParticipatedBids(users);
        List<Bid> winningBids = bidService.getWinningBids(users);

        model.addAttribute("userBids", allBids);
        model.addAttribute("winningBids", winningBids);
        return "BidWar/BidTable";
    }

//    @GetMapping("/delete/{id}")
//    public String deleteItem(@PathVariable Long id, RedirectAttributes redirectAttributes) {
//        try {
//            bidService.deleteBidDetails(id);
//
//        }catch (RuntimeException ex) {
//            redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
//        }
//        return "redirect:/bid/dashboard";
//    }

    @GetMapping("/delete/{id}")
    public String deleteItem(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            bidService.deleteBidDetails(id);
        } catch (RuntimeException ex) {
            redirectAttributes.addFlashAttribute("errorMessage", ex.getMessage());
        }
        return "redirect:/bid/dashboard";
    }

}
