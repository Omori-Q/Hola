package EC3.java.demo.service;

import EC3.java.demo.model.Post;
import EC3.java.demo.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostService {
    @Autowired
    private PostRepository postRepository;

    public List<Post> listarPost() {
        return postRepository.findAll();
    }

    public void guardarPost(Post post) {
        postRepository.save(post);
    }

    public Post obtenerPorId(Long id) {
        return postRepository.findById(id).orElse(null);
    }

    public void eliminarPost(Long id) {
        postRepository.deleteById(id);
    }
}
