package EC3.java.demo.controller;

import EC3.java.demo.model.Post;
import EC3.java.demo.model.Usuario;
import EC3.java.demo.service.PostService;
import EC3.java.demo.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@Controller
@RequestMapping("/posts")
public class PostController {
    @Autowired
    private PostService postService;

    @Autowired
    private UsuarioService usuarioService;

    @GetMapping("")
    public String listarPosts(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        model.addAttribute("posts", postService.listarPost());
        model.addAttribute("usuarioActual", usuarioService.encontrarPorUsername(userDetails.getUsername()));

        return "posts";
    }

    @GetMapping("/nuevo")
    public String mostrarFormCreacion(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        model.addAttribute("post", new Post());
        model.addAttribute("usuarioActual", usuarioService.encontrarPorUsername(userDetails.getUsername()));

        return "post_form";
    }

    @PostMapping("/guardar")
    public String guardarPost(@ModelAttribute Post post, @AuthenticationPrincipal UserDetails userDetails) {
        Usuario autor = usuarioService.encontrarPorUsername(userDetails.getUsername());
        post.setAutor(autor);
        post.setFechaPublicacion(LocalDateTime.now());

        postService.guardarPost(post);

        return "redirect:/";
    }

    @GetMapping("/editar/{id}")
    public String mostrarFormEdit(@PathVariable Long id, Model model, @AuthenticationPrincipal UserDetails userDetails) {
        Post post = postService.obtenerPorId(id);
        Usuario usuarioActual = usuarioService.encontrarPorUsername(userDetails.getUsername());

        if (post.getAutor().getId().equals(usuarioActual.getId())) {
            model.addAttribute("post", post);
            return "post_form";
        } else {
            return "redirect:/";
        }
    }

    @GetMapping("/eliminar/{id}")
    public String eliminarPost(@PathVariable Long id, @AuthenticationPrincipal UserDetails userDetails) {
        Post post = postService.obtenerPorId(id);
        Usuario usuarioActual = usuarioService.encontrarPorUsername(userDetails.getUsername());

        if (post.getAutor().getId().equals(usuarioActual.getId()) ||
                userDetails.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            postService.eliminarPost(id);
        }
        return "redirect:/";
    }
}
