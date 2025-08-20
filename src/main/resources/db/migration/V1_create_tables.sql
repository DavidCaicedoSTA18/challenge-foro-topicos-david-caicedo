CREATE TABLE usuarios (
    id BIGINT NOT NULL AUTO_INCREMENT,
    nombre VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(300) NOT NULL,
    activo BOOLEAN DEFAULT TRUE,
    PRIMARY KEY(id)
);

CREATE TABLE cursos (
    id BIGINT NOT NULL AUTO_INCREMENT,
    nombre VARCHAR(100) NOT NULL,
    categoria VARCHAR(50) NOT NULL,
    PRIMARY KEY(id)
);

CREATE TABLE topicos (
    id BIGINT NOT NULL AUTO_INCREMENT,
    titulo VARCHAR(200) NOT NULL,
    mensaje TEXT NOT NULL,
    fecha_creacion DATETIME NOT NULL,
    status VARCHAR(50) NOT NULL,
    autor_id BIGINT NOT NULL,
    curso_id BIGINT NOT NULL,
    PRIMARY KEY(id),
    FOREIGN KEY(autor_id) REFERENCES usuarios(id),
    FOREIGN KEY(curso_id) REFERENCES cursos(id),
    UNIQUE KEY unique_titulo_mensaje (titulo, mensaje(255))
);

CREATE TABLE respuestas (
    id BIGINT NOT NULL AUTO_INCREMENT,
    mensaje TEXT NOT NULL,
    topico_id BIGINT NOT NULL,
    fecha_creacion DATETIME NOT NULL,
    autor_id BIGINT NOT NULL,
    solucion BOOLEAN DEFAULT FALSE,
    PRIMARY KEY(id),
    FOREIGN KEY(topico_id) REFERENCES topicos(id),
    FOREIGN KEY(autor_id) REFERENCES usuarios(id)
);

-- V2__insert_initial_data.sql
-- Ubicación: src/main/resources/db/migration/V2__insert_initial_data.sql

-- Insertar usuarios de prueba (contraseña: 123456)
INSERT INTO usuarios (nombre, email, password) VALUES
('Admin', 'admin@forum.com', '$2a$10$Y50UaMRY.3UkryGKyJ5j8OsJpXiRaS8CN6lKrw1Lrt3SseAdhGm7y'),
('Juan Pérez', 'juan@forum.com', '$2a$10$Y50UaMRY.3UkryGKyJ5j8OsJpXiRaS8CN6lKrw1Lrt3SseAdhGm7y'),
('María García', 'maria@forum.com', '$2a$10$Y50UaMRY.3UkryGKyJ5j8OsJpXiRaS8CN6lKrw1Lrt3SseAdhGm7y');

-- Insertar cursos
INSERT INTO cursos (nombre, categoria) VALUES
('Spring Boot', 'Backend'),
('React', 'Frontend'),
('MySQL', 'Base de Datos'),
('Java', 'Programación'),
('Docker', 'DevOps');

-- Insertar tópicos de ejemplo
INSERT INTO topicos (titulo, mensaje, fecha_creacion, status, autor_id, curso_id) VALUES
('Error al conectar con MySQL', 'Tengo problemas para conectar Spring Boot con MySQL', NOW(), 'ABIERTO', 2, 1),
('Cómo usar Hooks en React', 'Necesito ayuda para entender los hooks de React', NOW(), 'ABIERTO', 3, 2);