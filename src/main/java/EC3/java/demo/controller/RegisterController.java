package EC3.java.demo.controller;

import EC3.java.demo.model.Rol;
import EC3.java.demo.model.Usuario;
import EC3.java.demo.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Collections;

@Controller
@RequestMapping("/register")
public class RegisterController {
    @Autowired
    private UsuarioService usuarioService;

    @GetMapping("")
    public String mostrarFormRegistro() {
        return "register";
    }

    @PostMapping("")
    public String registrarUsuario(@RequestParam String username, @RequestParam String password, @RequestParam String email, Model model) {

        if (usuarioService.encontrarPorUsername(username) != null) {
            model.addAttribute("usernameError", "El nombre de usuario ya está en uso.");

            return "register";
        }

        if (usuarioService.encontrarPorEmail(email) != null) {
            model.addAttribute("emailError", "El email ya está en uso.");

            return "register";
        }

        if (!password.matches("^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$")) {
            model.addAttribute("passwordError", "La contraseña debe tener al menos 8 caracteres, una mayúscula, una minúscula, un número y un carácter especial.");
            return "register";
        }

        Usuario usuario = new Usuario();
        usuario.setUsername(username);
        usuario.setPassword(password);
        usuario.setEmail(email);
        usuario.setRoles(Collections.singleton(Rol.USER));

        usuarioService.guardarUsuario(usuario);

        Authentication authentication = new UsernamePasswordAuthenticationToken(usuario, null, usuario.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        return "redirect:/";
    }
}
