package com.forum.ApiForumUltraApplication.controller;

import com.forum.ApiForumUltraApplication.dto.*;
import com.forum.ApiForumUltraApplication.model.StatusTopico;
import com.forum.ApiForumUltraApplication.model.Topico;
import com.forum.ApiForumUltraApplication.repository.CursoRepository;
import com.forum.ApiForumUltraApplication.repository.TopicoRepository;
import com.forum.ApiForumUltraApplication.repository.UsuarioRepository;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping("/topicos")
public class TopicoController {

    @Autowired
    private TopicoRepository topicoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private CursoRepository cursoRepository;

    // POST - Registrar nuevo tópico
    @PostMapping
    @Transactional
    public ResponseEntity<DatosRespuestaTopico> registrarTopico(
            @RequestBody @Valid DatosRegistroTopico datos,
            UriComponentsBuilder uriBuilder) {

        // Verificar si existe un tópico duplicado
        if (topicoRepository.existsByTituloAndMensaje(datos.titulo(), datos.mensaje())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Ya existe un tópico con el mismo título y mensaje");
        }

        // Buscar el autor y el curso
        var autor = usuarioRepository.findById(datos.autorId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Autor no encontrado"));

        var curso = cursoRepository.findById(datos.cursoId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Curso no encontrado"));

        // Crear el nuevo tópico
        var topico = new Topico();
        topico.setTitulo(datos.titulo());
        topico.setMensaje(datos.mensaje());
        topico.setAutor(autor);
        topico.setCurso(curso);
        topico.setStatus(StatusTopico.ABIERTO);

        topico = topicoRepository.save(topico);

        var uri = uriBuilder.path("/topicos/{id}").buildAndExpand(topico.getId()).toUri();

        return ResponseEntity.created(uri).body(new DatosRespuestaTopico(topico));
    }

    // GET - Listar todos los tópicos con paginación
    @GetMapping
    public ResponseEntity<Page<DatosListadoTopico>> listarTopicos(
            @PageableDefault(size = 10, sort = "fechaCreacion", direction = Sort.Direction.ASC) Pageable paginacion,
            @RequestParam(required = false) String curso,
            @RequestParam(required = false) Integer year) {

        Page<Topico> topicos;

        if (curso != null && year != null) {
            topicos = topicoRepository.findByCursoAndYear(curso, year, paginacion);
        } else if (curso != null) {
            topicos = topicoRepository.findByCursoNombre(curso, paginacion);
        } else {
            topicos = topicoRepository.findAllByOrderByFechaCreacionAsc(paginacion);
        }

        return ResponseEntity.ok(topicos.map(DatosListadoTopico::new));
    }

    // GET - Obtener detalle de un tópico específico
    @GetMapping("/{id}")
    public ResponseEntity<DatosDetalleTopico> detalleTopico(@PathVariable Long id) {
        var topico = topicoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Tópico no encontrado con ID: " + id));

        return ResponseEntity.ok(new DatosDetalleTopico(topico));
    }

    // PUT - Actualizar tópico
    @PutMapping("/{id}")
    @Transactional
    public ResponseEntity<DatosRespuestaTopico> actualizarTopico(
            @PathVariable Long id,
            @RequestBody @Valid DatosActualizarTopico datos) {

        var topico = topicoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Tópico no encontrado con ID: " + id));

        // Verificar duplicados si se actualiza título o mensaje
        if (datos.titulo() != null || datos.mensaje() != null) {
            String nuevoTitulo = datos.titulo() != null ? datos.titulo() : topico.getTitulo();
            String nuevoMensaje = datos.mensaje() != null ? datos.mensaje() : topico.getMensaje();

            // Solo verificar si realmente cambió algo
            if (!nuevoTitulo.equals(topico.getTitulo()) || !nuevoMensaje.equals(topico.getMensaje())) {
                if (topicoRepository.existsByTituloAndMensaje(nuevoTitulo, nuevoMensaje)) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                            "Ya existe un tópico con el mismo título y mensaje");
                }
            }
        }

        // Actualizar campos si fueron proporcionados
        if (datos.titulo() != null) {
            topico.setTitulo(datos.titulo());
        }
        if (datos.mensaje() != null) {
            topico.setMensaje(datos.mensaje());
        }
        if (datos.status() != null) {
            try {
                topico.setStatus(StatusTopico.valueOf(datos.status().toUpperCase()));
            } catch (IllegalArgumentException e) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "Status inválido. Valores permitidos: ABIERTO, CERRADO, SOLUCIONADO, SIN_RESPUESTA");
            }
        }

        topico = topicoRepository.save(topico);

        return ResponseEntity.ok(new DatosRespuestaTopico(topico));
    }

    // DELETE - Eliminar tópico
    @DeleteMapping("/{id}")
    @Transactional
    public ResponseEntity<Void> eliminarTopico(@PathVariable Long id) {
        var topico = topicoRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Tópico no encontrado con ID: " + id));

        topicoRepository.deleteById(id);

        return ResponseEntity.noContent().build();
    }
}