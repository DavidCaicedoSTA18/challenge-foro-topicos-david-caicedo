package com.forum.ApiForumUltraApplication.controller;

import com.forum.ApiForumUltraApplication.dto.DatosAutenticacion;
import com.forum.ApiForumUltraApplication.dto.DatosJWTToken;
import com.forum.ApiForumUltraApplication.model.Usuario;
import com.forum.ApiForumUltraApplication.service.TokenService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/login")
public class AutenticacionController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenService tokenService;

    @PostMapping
    public ResponseEntity<DatosJWTToken> autenticarUsuario(@RequestBody @Valid DatosAutenticacion datosAutenticacion) {
        var authToken = new UsernamePasswordAuthenticationToken(
                datosAutenticacion.email(),
                datosAutenticacion.password()
        );

        var authentication = authenticationManager.authenticate(authToken);
        var tokenJWT = tokenService.generarToken((Usuario) authentication.getPrincipal());

        return ResponseEntity.ok(new DatosJWTToken(tokenJWT));
    }
}