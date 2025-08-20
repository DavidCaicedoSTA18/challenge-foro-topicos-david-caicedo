package com.forum.ApiForumUltraApplication.repository;

import com.forum.ApiForumUltraApplication.model.Topico;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TopicoRepository extends JpaRepository<Topico, Long> {

    // Verificar tópicos duplicados
    boolean existsByTituloAndMensaje(String titulo, String mensaje);

    // Buscar por curso
    Page<Topico> findByCursoNombre(String nombreCurso, Pageable pageable);

    // Buscar por curso y año
    @Query("SELECT t FROM Topico t WHERE t.curso.nombre = :curso AND YEAR(t.fechaCreacion) = :year")
    Page<Topico> findByCursoAndYear(@Param("curso") String curso, @Param("year") int year, Pageable pageable);

    // Listar ordenados por fecha
    Page<Topico> findAllByOrderByFechaCreacionAsc(Pageable pageable);
}