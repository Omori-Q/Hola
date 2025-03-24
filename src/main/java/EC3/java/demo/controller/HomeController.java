package EC3.java.demo.controller;

import EC3.java.demo.model.Post;
import EC3.java.demo.service.PostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class HomeController {
    @Autowired
    private PostService postService;

    @GetMapping("/")
    public String home(Model model) {
        List<Post> posts = postService.listarPost();

        model.addAttribute("posts", posts);

        return "index";
    }
}
