-- =====================================================================
-- 02_datos_tenant.sql
-- UN (1) tenant DEMO completo, de punta a punta, para poder probar login,
-- roles/permisos, sedes con PostGIS, empleados y el libro de préstamos.
-- Pre-requisito: correr 01_init.sql primero (usa sus catálogos globales:
-- lkp_*, geo.city, identity.role/permission, etc.)
--
-- Este archivo es la PLANTILLA que se replicará para las 2 empresas
-- reales en la siguiente entrega; por ahora es un único tenant ficticio
-- ("Tienda Demo SAS") pensado solo para verificar que el modelo funciona.
--
-- Convención de IDs: UUIDs literales fijos con prefijo por bloque, igual
-- que en 01_init.sql, para poder encadenar referencias sin CTEs.
-- =====================================================================

BEGIN;

-- #####################################################################
-- 1) TENANT
-- #####################################################################

INSERT INTO tenant.tenants (
    id, legal_name, trade_name, tax_id, industry_id, status_id,
    subdomain, timezone, default_currency, trial_ends_at
) VALUES (
             'a0000000-0000-0000-0000-000000000001',
             'Tienda Demo SAS',
             'Tienda Demo',
             '900123456-1',
             '20000000-0000-0000-0000-000000000041', -- lkp_industry: retail
             '20000000-0000-0000-0000-000000000002', -- lkp_tenant_status: active
             'tienda-demo',
             'America/Bogota',
             'COP',
             NULL
         );

-- Plugins habilitados para este tenant: tracking (de pago) y payroll_lite (gratis).
INSERT INTO tenant.tenant_plugin (id, tenant_id, plugin_id, status_id, config) VALUES
                                                                                   ('a0000000-0000-0000-0000-000000000011',
                                                                                    'a0000000-0000-0000-0000-000000000001',
                                                                                    '20000000-0000-0000-0000-000000000071', -- plugin_catalog: tracking
                                                                                    '20000000-0000-0000-0000-000000000021', -- lkp_plugin_status: active
                                                                                    '{"max_shipments_per_month": 200}'::jsonb),
                                                                                   ('a0000000-0000-0000-0000-000000000012',
                                                                                    'a0000000-0000-0000-0000-000000000001',
                                                                                    '20000000-0000-0000-0000-000000000073', -- plugin_catalog: payroll_lite
                                                                                    '20000000-0000-0000-0000-000000000021', -- lkp_plugin_status: active
                                                                                    '{}'::jsonb);


-- #####################################################################
-- 2) SITES (sedes): una tienda en Bogotá + una bodega en Bogotá.
--    location usa PostGIS: ST_SetSRID(ST_MakePoint(lng, lat), 4326)
-- #####################################################################

INSERT INTO tenant.sites (
    id, tenant_id, name, site_type_id,
    address_line1, address_line2, country_id, state_province_id, city_id,
    postal_code, location, phone, is_active
) VALUES (
             'a0000000-0000-0000-0000-000000000021',
             'a0000000-0000-0000-0000-000000000001',
             'Tienda Centro',
             '20000000-0000-0000-0000-000000000011', -- lkp_site_type: store
             'Calle 13 # 7-21', 'Local 2',
             '10000000-0000-0000-0000-000000000001', -- geo.country: CO
             '10000000-0000-0000-0000-000000000101', -- geo.state_province: Bogotá D.C.
             '10000000-0000-0000-0000-000000000201', -- geo.city: Bogotá
             '110311',
             ST_SetSRID(ST_MakePoint(-74.0721, 4.7110), 4326)::geography,
             '+57 601 555 0101',
             true
         );

INSERT INTO tenant.sites (
    id, tenant_id, name, site_type_id,
    address_line1, address_line2, country_id, state_province_id, city_id,
    postal_code, location, phone, is_active
) VALUES (
             'a0000000-0000-0000-0000-000000000022',
             'a0000000-0000-0000-0000-000000000001',
             'Bodega Norte',
             '20000000-0000-0000-0000-000000000012', -- lkp_site_type: warehouse
             'Autopista Norte # 145-30', 'Bodega 5',
             '10000000-0000-0000-0000-000000000001',
             '10000000-0000-0000-0000-000000000101',
             '10000000-0000-0000-0000-000000000201',
             '110111',
             ST_SetSRID(ST_MakePoint(-74.0445, 4.7480), 4326)::geography,
             '+57 601 555 0102',
             true
         );


-- #####################################################################
-- 3) PERSONAS: profile + user_contact (multi-contacto) + users_login
--    Tres cuentas: admin, cajero, bodeguero.
-- #####################################################################

-- --- 3.1 Admin: María Pérez -------------------------------------------------
INSERT INTO identity.profile (id, tenant_id, first_name, last_name, document_type_id, document_number) VALUES
    ('a0000000-0000-0000-0000-000000000031', 'a0000000-0000-0000-0000-000000000001',
     'María', 'Pérez', '30000000-0000-0000-0000-000000000021', '1010123456'); -- doc_type: CC

INSERT INTO identity.user_contact (id, profile_id, contact_type_id, value, is_primary, is_login_email, is_verified) VALUES
                                                                                                                        ('a0000000-0000-0000-0000-000000000041', 'a0000000-0000-0000-0000-000000000031',
                                                                                                                         '30000000-0000-0000-0000-000000000011', 'maria.perez@tiendademo.com', true, true, true), -- email login
                                                                                                                        ('a0000000-0000-0000-0000-000000000042', 'a0000000-0000-0000-0000-000000000031',
                                                                                                                         '30000000-0000-0000-0000-000000000011', 'maria.personal@gmail.com', false, false, false), -- 2do email
                                                                                                                        ('a0000000-0000-0000-0000-000000000043', 'a0000000-0000-0000-0000-000000000031',
                                                                                                                         '30000000-0000-0000-0000-000000000012', '+57 3001234567', true, false, true); -- teléfono

-- password_hash es un placeholder bcrypt de ejemplo; reemplazar por hash real en el backend.
INSERT INTO identity.users_login (id, tenant_id, email, password_hash, status_id, mfa_enabled) VALUES
    ('a0000000-0000-0000-0000-000000000051', 'a0000000-0000-0000-0000-000000000001',
     'maria.perez@tiendademo.com',
     '$2a$10$N9qo8uLOickgx2ZMRZoMy.placeholderHASHnoReal000000000001',
     '30000000-0000-0000-0000-000000000002', -- lkp_user_status: active
     false);

-- Vincular profile <-> users_login (relación 1-a-1)
UPDATE identity.profile SET user_login_id = 'a0000000-0000-0000-0000-000000000051'
WHERE id = 'a0000000-0000-0000-0000-000000000031';

INSERT INTO identity.user_role (user_login_id, role_id) VALUES
    ('a0000000-0000-0000-0000-000000000051', '30000000-0000-0000-0000-000000000032'); -- role: admin

-- --- 3.2 Cajero: Juan Gómez --------------------------------------------------
INSERT INTO identity.profile (id, tenant_id, first_name, last_name, document_type_id, document_number) VALUES
    ('a0000000-0000-0000-0000-000000000032', 'a0000000-0000-0000-0000-000000000001',
     'Juan', 'Gómez', '30000000-0000-0000-0000-000000000021', '1020234567');

INSERT INTO identity.user_contact (id, profile_id, contact_type_id, value, is_primary, is_login_email, is_verified) VALUES
                                                                                                                        ('a0000000-0000-0000-0000-000000000044', 'a0000000-0000-0000-0000-000000000032',
                                                                                                                         '30000000-0000-0000-0000-000000000011', 'juan.gomez@tiendademo.com', true, true, true),
                                                                                                                        ('a0000000-0000-0000-0000-000000000045', 'a0000000-0000-0000-0000-000000000032',
                                                                                                                         '30000000-0000-0000-0000-000000000012', '+57 3007654321', true, false, true);

INSERT INTO identity.users_login (id, tenant_id, email, password_hash, status_id, mfa_enabled) VALUES
    ('a0000000-0000-0000-0000-000000000052', 'a0000000-0000-0000-0000-000000000001',
     'juan.gomez@tiendademo.com',
     '$2a$10$N9qo8uLOickgx2ZMRZoMy.placeholderHASHnoReal000000000002',
     '30000000-0000-0000-0000-000000000002',
     false);

UPDATE identity.profile SET user_login_id = 'a0000000-0000-0000-0000-000000000052'
WHERE id = 'a0000000-0000-0000-0000-000000000032';

INSERT INTO identity.user_role (user_login_id, role_id) VALUES
    ('a0000000-0000-0000-0000-000000000052', '30000000-0000-0000-0000-000000000033'); -- role: cashier

-- --- 3.3 Bodeguero: Carlos Ruiz ----------------------------------------------
INSERT INTO identity.profile (id, tenant_id, first_name, last_name, document_type_id, document_number) VALUES
    ('a0000000-0000-0000-0000-000000000033', 'a0000000-0000-0000-0000-000000000001',
     'Carlos', 'Ruiz', '30000000-0000-0000-0000-000000000021', '1030345678');

INSERT INTO identity.user_contact (id, profile_id, contact_type_id, value, is_primary, is_login_email, is_verified) VALUES
                                                                                                                        ('a0000000-0000-0000-0000-000000000046', 'a0000000-0000-0000-0000-000000000033',
                                                                                                                         '30000000-0000-0000-0000-000000000011', 'carlos.ruiz@tiendademo.com', true, true, true),
                                                                                                                        ('a0000000-0000-0000-0000-000000000047', 'a0000000-0000-0000-0000-000000000033',
                                                                                                                         '30000000-0000-0000-0000-000000000012', '+57 3009876543', true, false, true);

INSERT INTO identity.users_login (id, tenant_id, email, password_hash, status_id, mfa_enabled) VALUES
    ('a0000000-0000-0000-0000-000000000053', 'a0000000-0000-0000-0000-000000000001',
     'carlos.ruiz@tiendademo.com',
     '$2a$10$N9qo8uLOickgx2ZMRZoMy.placeholderHASHnoReal000000000003',
     '30000000-0000-0000-0000-000000000002',
     false);

UPDATE identity.profile SET user_login_id = 'a0000000-0000-0000-0000-000000000053'
WHERE id = 'a0000000-0000-0000-0000-000000000033';

INSERT INTO identity.user_role (user_login_id, role_id) VALUES
    ('a0000000-0000-0000-0000-000000000053', '30000000-0000-0000-0000-000000000034'); -- role: warehouse


-- #####################################################################
-- 4) EMPLOYEES: vincula cada profile con su sede y datos laborales.
-- #####################################################################

INSERT INTO hr.employee (id, tenant_id, profile_id, site_id, position, hire_date, salary, employment_status_id) VALUES
                                                                                                                    ('a0000000-0000-0000-0000-000000000061', 'a0000000-0000-0000-0000-000000000001',
                                                                                                                     'a0000000-0000-0000-0000-000000000031', -- María
                                                                                                                     'a0000000-0000-0000-0000-000000000021', -- Tienda Centro
                                                                                                                     'Administradora', '2025-01-15', 3500000.00,
                                                                                                                     '40000000-0000-0000-0000-000000000001'), -- employment_status: active

                                                                                                                    ('a0000000-0000-0000-0000-000000000062', 'a0000000-0000-0000-0000-000000000001',
                                                                                                                     'a0000000-0000-0000-0000-000000000032', -- Juan
                                                                                                                     'a0000000-0000-0000-0000-000000000021', -- Tienda Centro
                                                                                                                     'Cajero', '2025-03-01', 1600000.00,
                                                                                                                     '40000000-0000-0000-0000-000000000001'),

                                                                                                                    ('a0000000-0000-0000-0000-000000000063', 'a0000000-0000-0000-0000-000000000001',
                                                                                                                     'a0000000-0000-0000-0000-000000000033', -- Carlos
                                                                                                                     'a0000000-0000-0000-0000-000000000022', -- Bodega Norte
                                                                                                                     'Bodeguero', '2025-04-10', 1500000.00,
                                                                                                                     '40000000-0000-0000-0000-000000000001');

-- Un préstamo de ejemplo a Juan (cajero) y su primer abono, para ver el
-- libro de movimientos funcionando con balance_after acumulado.
INSERT INTO hr.employee_ledger (id, tenant_id, employee_id, entry_type_id, amount, balance_after, reason, created_by) VALUES
                                                                                                                          ('a0000000-0000-0000-0000-000000000071', 'a0000000-0000-0000-0000-000000000001',
                                                                                                                           'a0000000-0000-0000-0000-000000000062', -- Juan
                                                                                                                           '40000000-0000-0000-0000-000000000011', -- entry_type: loan
                                                                                                                           200000.00, 200000.00,
                                                                                                                           'Préstamo nómina', 'a0000000-0000-0000-0000-000000000051'), -- created_by: María (admin)

                                                                                                                          ('a0000000-0000-0000-0000-000000000072', 'a0000000-0000-0000-0000-000000000001',
                                                                                                                           'a0000000-0000-0000-0000-000000000062',
                                                                                                                           '40000000-0000-0000-0000-000000000012', -- entry_type: repayment
                                                                                                                           50000.00, 150000.00,
                                                                                                                           'Abono quincenal', 'a0000000-0000-0000-0000-000000000051');


-- #####################################################################
-- 5) CUSTOMER de ejemplo: cliente de mostrador SIN profile (anónimo)
--    y un cliente persona CON profile (para mostrar ambos casos).
-- #####################################################################

-- Cliente de mostrador: profile_id NULL a propósito.
INSERT INTO sale.customers (id, tenant_id, profile_id, customer_type_id, company_name, tax_id, credit_limit) VALUES
    ('a0000000-0000-0000-0000-000000000081', 'a0000000-0000-0000-0000-000000000001',
     NULL,
     '60000000-0000-0000-0000-000000000011', -- customer_type: person
     NULL, NULL, 0);

-- Cliente persona con datos completos vía profile + user_contact (sin login propio).
INSERT INTO identity.profile (id, tenant_id, first_name, last_name, document_type_id, document_number) VALUES
    ('a0000000-0000-0000-0000-000000000034', 'a0000000-0000-0000-0000-000000000001',
     'Laura', 'Martínez', '30000000-0000-0000-0000-000000000021', '1040456789');

INSERT INTO identity.user_contact (id, profile_id, contact_type_id, value, is_primary, is_login_email, is_verified) VALUES
                                                                                                                        ('a0000000-0000-0000-0000-000000000048', 'a0000000-0000-0000-0000-000000000034',
                                                                                                                         '30000000-0000-0000-0000-000000000011', 'laura.martinez@example.com', true, false, false),
                                                                                                                        ('a0000000-0000-0000-0000-000000000049', 'a0000000-0000-0000-0000-000000000034',
                                                                                                                         '30000000-0000-0000-0000-000000000012', '+57 3011112222', true, false, true);

INSERT INTO sale.customers (id, tenant_id, profile_id, customer_type_id, company_name, tax_id, credit_limit) VALUES
    ('a0000000-0000-0000-0000-000000000082', 'a0000000-0000-0000-0000-000000000001',
     'a0000000-0000-0000-0000-000000000034', -- Laura
     '60000000-0000-0000-0000-000000000011', -- customer_type: person
     NULL, NULL, 300000.00);

COMMIT;

-- =====================================================================
-- Verificación rápida sugerida tras correr este script:
--
  SELECT email FROM identity.users_login WHERE tenant_id = 'a0000000-0000-0000-0000-000000000001';
  SELECT r.name FROM identity.user_role ur
    JOIN identity.role r ON r.id = ur.role_id
    WHERE ur.user_login_id = 'a0000000-0000-0000-0000-000000000051';
  SELECT name, ST_AsText(location::geometry) FROM tenant.sites
    WHERE tenant_id = 'a0000000-0000-0000-0000-000000000001';
-- =====================================================================