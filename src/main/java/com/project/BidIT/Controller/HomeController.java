package com.project.BidIT.Controller;

import com.project.BidIT.entity.Feedback;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {
    @GetMapping("/")
    public String homePage(Model model){
        model.addAttribute("feedback", new Feedback());
        return "index";
    }

}
