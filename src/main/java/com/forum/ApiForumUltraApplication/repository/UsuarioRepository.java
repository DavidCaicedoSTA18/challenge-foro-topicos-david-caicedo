package com.forum.ApiForumUltraApplication.repository;

import com.forum.ApiForumUltraApplication.model.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    UserDetails findByEmail(String email);

    Optional<Usuario> findOptionalByEmail(String email);

    boolean existsByEmail(String email);
}