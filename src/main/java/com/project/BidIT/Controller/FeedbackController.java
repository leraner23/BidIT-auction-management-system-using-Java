package com.project.BidIT.Controller;

import com.project.BidIT.DTO.UserDto;
import com.project.BidIT.Repo.FeedbackRepo;
import com.project.BidIT.entity.Feedback;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class FeedbackController {

    @Autowired
    private FeedbackRepo feedbackRepo;
@PostMapping("/Contacts")
public String FindMessage(@ModelAttribute Feedback feedback, RedirectAttributes redirectAttributes){
    feedbackRepo.save(feedback);
    redirectAttributes.addFlashAttribute("success", true);
    return "redirect:/#contact";
}

}
