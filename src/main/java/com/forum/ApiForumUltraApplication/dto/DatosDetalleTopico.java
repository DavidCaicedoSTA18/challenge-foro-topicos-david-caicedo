package com.forum.ApiForumUltraApplication.dto;

import com.forum.ApiForumUltraApplication.model.StatusTopico;
import com.forum.ApiForumUltraApplication.model.Topico;
import java.time.LocalDateTime;

public record DatosDetalleTopico(
        Long id,
        String titulo,
        String mensaje,
        LocalDateTime fechaCreacion,
        StatusTopico status,
        String autor,
        String curso,
        String emailAutor
) {
    public DatosDetalleTopico(Topico topico) {
        this(
                topico.getId(),
                topico.getTitulo(),
                topico.getMensaje(),
                topico.getFechaCreacion(),
                topico.getStatus(),
                topico.getAutor().getNombre(),
                topico.getCurso().getNombre(),
                topico.getAutor().getEmail()
        );
    }
}