package com.forum.ApiForumUltraApplication.dto;

import com.forum.ApiForumUltraApplication.model.StatusTopico;
import com.forum.ApiForumUltraApplication.model.Topico;
import java.time.LocalDateTime;

public record DatosRespuestaTopico(
        Long id,
        String titulo,
        String mensaje,
        LocalDateTime fechaCreacion,
        StatusTopico status,
        Long autorId,
        Long cursoId
) {
    public DatosRespuestaTopico(Topico topico) {
        this(
                topico.getId(),
                topico.getTitulo(),
                topico.getMensaje(),
                topico.getFechaCreacion(),
                topico.getStatus(),
                topico.getAutor().getId(),
                topico.getCurso().getId()
        );
    }
}