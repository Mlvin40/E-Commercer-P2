--DROP DATABASE IF EXISTS ecommerce_gt;
--CREATE DATABASE ecommerce_gt;
--\c ecommerce_gt;

-- USUARIOS
CREATE TABLE usuarios (
                          id              BIGSERIAL PRIMARY KEY,
                          nombre          VARCHAR(100) NOT NULL,
                          correo          VARCHAR(150) UNIQUE NOT NULL,
                          password        VARCHAR(255) NOT NULL,
                          rol             VARCHAR(20) NOT NULL DEFAULT 'COMUN'
                              CHECK (rol IN ('COMUN','MODERADOR','LOGISTICA','ADMIN')),
                          activo          BOOLEAN DEFAULT TRUE,
                          fecha_creacion  TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- PRODUCTOS
CREATE TABLE productos (
                           id                   BIGSERIAL PRIMARY KEY,
                           nombre               VARCHAR(120) NOT NULL,
                           descripcion          TEXT,
                           imagen_url           TEXT,
                           precio               NUMERIC(10,2) NOT NULL CHECK (precio > 0),
                           stock                INT NOT NULL CHECK (stock >= 0),
                           estado               VARCHAR(10) NOT NULL
                               CHECK (estado IN ('NUEVO','USADO')),
                           categoria            VARCHAR(20) NOT NULL
                               CHECK (categoria IN ('TECNOLOGIA','HOGAR','ACADEMICO','PERSONAL','DECORACION','OTRO')),
                           estado_publicacion   VARCHAR(12) NOT NULL DEFAULT 'PENDIENTE'
                               CHECK (estado_publicacion IN ('PENDIENTE','APROBADO','RECHAZADO')),
                           usuario_id           BIGINT NOT NULL REFERENCES usuarios(id) ON DELETE CASCADE,
                           fecha_publicacion    TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- HISTÓRICO DE REVISIÓN DE PRODUCTOS
CREATE TABLE producto_revision (
                                   id               BIGSERIAL PRIMARY KEY,
                                   producto_id      BIGINT NOT NULL REFERENCES productos(id) ON DELETE CASCADE,
                                   solicitado_por   BIGINT NOT NULL REFERENCES usuarios(id) ON DELETE CASCADE,
                                   estado           VARCHAR(12) NOT NULL DEFAULT 'PENDIENTE'
                                       CHECK (estado IN ('PENDIENTE','APROBADO','RECHAZADO')),
                                   moderador_id     BIGINT REFERENCES usuarios(id),
                                   comentario       TEXT,
                                   creado_en        TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                   resuelto_en      TIMESTAMP
);

-- CARRITOS
CREATE TABLE carritos (
                          id              BIGSERIAL PRIMARY KEY,
                          usuario_id      BIGINT NOT NULL REFERENCES usuarios(id) ON DELETE CASCADE,
                          fecha_creacion  TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                          CONSTRAINT uq_carrito_usuario UNIQUE (usuario_id)
);

-- DETALLE DE CARRITO
CREATE TABLE carrito_detalle (
                                 id           BIGSERIAL PRIMARY KEY,
                                 carrito_id   BIGINT NOT NULL REFERENCES carritos(id) ON DELETE CASCADE,
                                 producto_id  BIGINT NOT NULL REFERENCES productos(id) ON DELETE CASCADE,
                                 cantidad     INT NOT NULL CHECK (cantidad >= 1),
                                 CONSTRAINT uq_item_unico UNIQUE (carrito_id, producto_id)
);

-- PEDIDOS
CREATE TABLE pedidos (
                         id                       BIGSERIAL PRIMARY KEY,
                         usuario_id               BIGINT NOT NULL REFERENCES usuarios(id) ON DELETE CASCADE,
                         total                    NUMERIC(10,2) NOT NULL,
                         estado                   VARCHAR(10) NOT NULL DEFAULT 'EN_CURSO'
                             CHECK (estado IN ('EN_CURSO','ENTREGADO')),
                         fecha_pedido             DATE DEFAULT CURRENT_DATE,
                         fecha_estimada_entrega   DATE NOT NULL DEFAULT ((CURRENT_DATE + INTERVAL '5 days')::date),
    fecha_entregada          DATE
);

-- DETALLE DE PEDIDOS
CREATE TABLE pedido_detalle (
                                id                 BIGSERIAL PRIMARY KEY,
                                pedido_id          BIGINT NOT NULL REFERENCES pedidos(id) ON DELETE CASCADE,
                                producto_id        BIGINT NOT NULL REFERENCES productos(id) ON DELETE CASCADE,
                                vendedor_id        BIGINT NOT NULL REFERENCES usuarios(id),
                                cantidad           INT NOT NULL CHECK (cantidad >= 1),
                                precio_unitario    NUMERIC(10,2) NOT NULL,
                                subtotal           NUMERIC(10,2) NOT NULL,
                                sitio_fee          NUMERIC(10,2) NOT NULL,
                                ganancia_vendedor  NUMERIC(10,2) NOT NULL
);

-- TARJETAS
CREATE TABLE tarjetas (
                          id               BIGSERIAL PRIMARY KEY,
                          usuario_id       BIGINT NOT NULL REFERENCES usuarios(id) ON DELETE CASCADE,
                          token            VARCHAR(120) NOT NULL,
                          last4            CHAR(4) NOT NULL,
                          marca            VARCHAR(20),
                          exp_mes          SMALLINT,
                          exp_anio         SMALLINT,
                          predeterminada   BOOLEAN DEFAULT FALSE,
                          titular          VARCHAR(100) NOT NULL,
                          fecha_registro   TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- PAGOS
CREATE TABLE pagos (
                       id              BIGSERIAL PRIMARY KEY,
                       pedido_id       BIGINT NOT NULL REFERENCES pedidos(id) ON DELETE CASCADE,
                       monto           NUMERIC(10,2) NOT NULL,
                       estado          VARCHAR(10) NOT NULL
                           CHECK (estado IN ('APROBADO','RECHAZADO')),
                       proveedor_tx_id VARCHAR(100),
                       creado_en       TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- CALIFICACIONES
CREATE TABLE calificaciones (
                                id           BIGSERIAL PRIMARY KEY,
                                usuario_id   BIGINT NOT NULL REFERENCES usuarios(id) ON DELETE CASCADE,
                                producto_id  BIGINT NOT NULL REFERENCES productos(id) ON DELETE CASCADE,
                                estrellas    INT CHECK (estrellas BETWEEN 1 AND 5),
                                comentario   TEXT,
                                fecha        TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                                UNIQUE (usuario_id, producto_id)
);

-- SANCIONES
CREATE TABLE sanciones (
                           id            BIGSERIAL PRIMARY KEY,
                           moderador_id  BIGINT NOT NULL REFERENCES usuarios(id),
                           usuario_id    BIGINT NOT NULL REFERENCES usuarios(id),
                           motivo        TEXT NOT NULL,
                           fecha         DATE DEFAULT CURRENT_DATE,
                           estado        VARCHAR(10) DEFAULT 'ACTIVA'
                               CHECK (estado IN ('ACTIVA','LEVANTADA'))
);

-- NOTIFICACIONES
CREATE TABLE notificaciones (
                                id           BIGSERIAL PRIMARY KEY,
                                usuario_id   BIGINT NOT NULL REFERENCES usuarios(id),
                                tipo         VARCHAR(50) NOT NULL,
                                asunto       TEXT,
                                mensaje      TEXT NOT NULL,
                                meta         JSONB,
                                fecha_envio  TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);



CREATE INDEX IF NOT EXISTS idx_sanciones_usuario_activa
    ON sanciones (usuario_id)
    WHERE estado = 'ACTIVA';






















-- INSERTS
BEGIN;

-- ADMIN ---------------------------------------------------------------
INSERT INTO usuarios (nombre, correo, password, rol, activo)
VALUES
    ('Administrador', 'admin@ecom.test',
     '$2a$10$Ta8nxCEjBBAEle2vEsMzz.VYnPcJ6GQRbnj4t0MxFqY7dKvRbDJIG',
     'ADMIN', TRUE)
    ON CONFLICT (correo) DO NOTHING;

-- MODERADORES (5) -----------------------------------------------------
INSERT INTO usuarios (nombre, correo, password, rol, activo) VALUES
                                                                 ('Moderador 01', 'mod01@ecom.test', '$2a$10$Ta8nxCEjBBAEle2vEsMzz.VYnPcJ6GQRbnj4t0MxFqY7dKvRbDJIG', 'MODERADOR', TRUE),
                                                                 ('Moderador 02', 'mod02@ecom.test', '$2a$10$Ta8nxCEjBBAEle2vEsMzz.VYnPcJ6GQRbnj4t0MxFqY7dKvRbDJIG', 'MODERADOR', TRUE),
                                                                 ('Moderador 03', 'mod03@ecom.test', '$2a$10$Ta8nxCEjBBAEle2vEsMzz.VYnPcJ6GQRbnj4t0MxFqY7dKvRbDJIG', 'MODERADOR', TRUE),
                                                                 ('Moderador 04', 'mod04@ecom.test', '$2a$10$Ta8nxCEjBBAEle2vEsMzz.VYnPcJ6GQRbnj4t0MxFqY7dKvRbDJIG', 'MODERADOR', TRUE),
                                                                 ('Moderador 05', 'mod05@ecom.test', '$2a$10$Ta8nxCEjBBAEle2vEsMzz.VYnPcJ6GQRbnj4t0MxFqY7dKvRbDJIG', 'MODERADOR', TRUE)
    ON CONFLICT (correo) DO NOTHING;

-- LOGÍSTICA (3) -------------------------------------------------------
INSERT INTO usuarios (nombre, correo, password, rol, activo) VALUES
                                                                 ('Logística 01', 'log01@ecom.test', '$2a$10$Ta8nxCEjBBAEle2vEsMzz.VYnPcJ6GQRbnj4t0MxFqY7dKvRbDJIG', 'LOGISTICA', TRUE),
                                                                 ('Logística 02', 'log02@ecom.test', '$2a$10$Ta8nxCEjBBAEle2vEsMzz.VYnPcJ6GQRbnj4t0MxFqY7dKvRbDJIG', 'LOGISTICA', TRUE),
                                                                 ('Logística 03', 'log03@ecom.test', '$2a$10$Ta8nxCEjBBAEle2vEsMzz.VYnPcJ6GQRbnj4t0MxFqY7dKvRbDJIG', 'LOGISTICA', TRUE)
    ON CONFLICT (correo) DO NOTHING;

-- COMUNES (10) --------------------------------------------------------
INSERT INTO usuarios (nombre, correo, password, rol, activo) VALUES
                                                                 ('Usuario 01', 'c01@ecom.test', '$2a$10$Ta8nxCEjBBAEle2vEsMzz.VYnPcJ6GQRbnj4t0MxFqY7dKvRbDJIG', 'COMUN', TRUE),
                                                                 ('Usuario 02', 'c02@ecom.test', '$2a$10$Ta8nxCEjBBAEle2vEsMzz.VYnPcJ6GQRbnj4t0MxFqY7dKvRbDJIG', 'COMUN', TRUE),
                                                                 ('Usuario 03', 'c03@ecom.test', '$2a$10$Ta8nxCEjBBAEle2vEsMzz.VYnPcJ6GQRbnj4t0MxFqY7dKvRbDJIG', 'COMUN', TRUE),
                                                                 ('Usuario 04', 'c04@ecom.test', '$2a$10$Ta8nxCEjBBAEle2vEsMzz.VYnPcJ6GQRbnj4t0MxFqY7dKvRbDJIG', 'COMUN', TRUE),
                                                                 ('Usuario 05', 'c05@ecom.test', '$2a$10$Ta8nxCEjBBAEle2vEsMzz.VYnPcJ6GQRbnj4t0MxFqY7dKvRbDJIG', 'COMUN', TRUE),
                                                                 ('Usuario 06', 'c06@ecom.test', '$2a$10$Ta8nxCEjBBAEle2vEsMzz.VYnPcJ6GQRbnj4t0MxFqY7dKvRbDJIG', 'COMUN', TRUE),
                                                                 ('Usuario 07', 'c07@ecom.test', '$2a$10$Ta8nxCEjBBAEle2vEsMzz.VYnPcJ6GQRbnj4t0MxFqY7dKvRbDJIG', 'COMUN', TRUE),
                                                                 ('Usuario 08', 'c08@ecom.test', '$2a$10$Ta8nxCEjBBAEle2vEsMzz.VYnPcJ6GQRbnj4t0MxFqY7dKvRbDJIG', 'COMUN', TRUE),
                                                                 ('Usuario 09', 'c09@ecom.test', '$2a$10$Ta8nxCEjBBAEle2vEsMzz.VYnPcJ6GQRbnj4t0MxFqY7dKvRbDJIG', 'COMUN', TRUE),
                                                                 ('Usuario 10','c10@ecom.test', '$2a$10$Ta8nxCEjBBAEle2vEsMzz.VYnPcJ6GQRbnj4t0MxFqY7dKvRbDJIG', 'COMUN', TRUE)
    ON CONFLICT (correo) DO NOTHING;

-- =====================================================================
-- 2) Productos: 10 por cada usuario COMUN (total 100).
--    imagen_url en blanco (NULL), estado_publicacion = 'APROBADO'
--    Categoría/estado variando por índice.
--    Idempotente: evita duplicar si ya existen por (usuario,nombre).
-- =====================================================================
INSERT INTO productos
(nombre, descripcion, imagen_url, precio, stock, estado, categoria,
 estado_publicacion, usuario_id, fecha_publicacion)
SELECT
    format('Producto %s-%s', u.alias, gs) AS nombre,
    format('Producto de %s #%s', u.alias, gs) AS descripcion,
    NULL::text AS imagen_url,
  -- Precio ejemplo: > 0 con 2 decimales
    ROUND( (10 + gs) * 7.25, 2 )::numeric(10,2) AS precio,
    (5 + gs) AS stock,
    CASE WHEN (gs % 2) = 0 THEN 'NUEVO' ELSE 'USADO' END AS estado,
    CASE (gs % 6)
        WHEN 0 THEN 'TECNOLOGIA'
        WHEN 1 THEN 'HOGAR'
        WHEN 2 THEN 'ACADEMICO'
        WHEN 3 THEN 'PERSONAL'
        WHEN 4 THEN 'DECORACION'
        ELSE 'OTRO'
        END AS categoria,
    'APROBADO' AS estado_publicacion,
    u.id AS usuario_id,
    CURRENT_TIMESTAMP AS fecha_publicacion
FROM (
         SELECT id,
                correo,
                regexp_replace(correo, '@.*$', '') AS alias
         FROM usuarios
         WHERE rol = 'COMUN'
     ) u
         CROSS JOIN generate_series(1,10) AS gs
WHERE NOT EXISTS (
    SELECT 1 FROM productos p
    WHERE p.usuario_id = u.id
      AND p.nombre = format('Producto %s-%s', u.alias, gs)
);

COMMIT;


