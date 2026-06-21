-- =====================================================================
-- 03_fix_passwords_demo.sql
-- Reemplaza los password_hash PLACEHOLDER de 02_datos_tenant.sql por
-- hashes BCrypt REALES, generados y verificados con la librería bcrypt
-- estándar (mismo algoritmo que usa Spring Security BCryptPasswordEncoder,
-- prefijo $2a$/$2b$ son intercambiables para Spring).
--
-- Passwords en texto plano (SOLO para entorno demo/local, nunca usar en
-- producción ni commitear passwords reales a un repo):
--
--   maria.perez@tiendademo.com   -> Maria123*   (rol: admin)
--   juan.gomez@tiendademo.com    -> Juan123*    (rol: cashier)
--   carlos.ruiz@tiendademo.com   -> Carlos123*  (rol: warehouse)
--
-- Correr DESPUÉS de 01_init.sql y 02_datos_tenant.sql.
-- =====================================================================

BEGIN;

UPDATE identity.users_login
SET password_hash = '$2a$10$1pkcTn4dq5MKxR83/6/pA.Rf5IOMF0Nsp.58FN03nt1CLTy9LmZGW'
WHERE email = 'maria.perez@tiendademo.com';

UPDATE identity.users_login
SET password_hash = '$2a$10$.KTyIgr4Tx.gDfr9N0DQBe4Oo5QwsbqNH1tt8lmFei2TpI.jrYAxK'
WHERE email = 'juan.gomez@tiendademo.com';

UPDATE identity.users_login
SET password_hash = '$2a$10$Bu7555xOMC66a6ppJWPBYOXnJ2rrjztVbNEfD83.Agh158mlD/26i'
WHERE email = 'carlos.ruiz@tiendademo.com';

COMMIT;

-- =====================================================================
-- Prueba sugerida con curl tras correr esto:
--
-- curl -X POST http://localhost:8080/api/auth/login \
--   -H "Content-Type: application/json" \
--   -d '{"subdomain":"tienda-demo","email":"maria.perez@tiendademo.com","password":"Maria123*"}'
-- =====================================================================