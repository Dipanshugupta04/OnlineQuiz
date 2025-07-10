package com.OnlineQuiz.OnlineQuiz.Controller;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller 
@CrossOrigin(origins = { "https://majestic-kangaroo-33ba55.netlify.app","http://127.0.0.1:5502", "http://localhost:5502","https://heroic-sunburst-56c10d.netlify.app","http://quizwiz-frontend.s3-website.ap-south-1.amazonaws.com " })
 // ✅ Important: use @Controller for MVC
public class YourCntroller {
    @GetMapping("/home")
    public String home() {
        return "redirect:/index.html"; // now it will redirect correctly
    }
}
