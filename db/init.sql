-- =====================================================================
-- 01_init.sql
-- Datos de INICIALIZACIÓN GLOBAL de la plataforma (NO tenant-scoped).
-- Pre-requisito: el DDL (CREATE SCHEMA / CREATE TABLE / CREATE EXTENSION
-- postgis) ya fue aplicado por separado (migraciones de Flyway/Liquibase).
--
-- Contiene únicamente:
--   - geo.country / state_province / city (catálogo geográfico, Colombia)
--   - Todas las tablas lkp_* de todos los schemas (tenant, identity, hr,
--     stock, sale, logistics)
--   - tenant.plugin_catalog (marketplace de plugins)
--   - logistics.carrier_catalog (transportadoras + in_house)
--   - identity.role / permission / role_permission GLOBALES
--     (tenant_id IS NULL -> roles predefinidos del sistema)
--
-- NO contiene tenants, sites, empleados, productos ni ningún dato
-- tenant-scoped: eso va en 02_datos_tenant.sql.
--
-- Convención de IDs: se usan UUIDs LITERALES FIJOS (no gen_random_uuid())
-- para todas las filas semilla, porque varias tablas hijas necesitan
-- referenciar el id del padre dentro de este mismo script y en
-- 02_datos_tenant.sql. Son deterministas y reproducibles entre entornos.
-- Convención: cada bloque usa un prefijo de UUID legible
-- (ej. 00000000-0000-0000-0000-0000000000XX) para facilitar lectura/debug;
-- en un entorno real basta con que sean válidos y únicos.
-- =====================================================================

BEGIN;

-- #####################################################################
-- SCHEMA: geo
-- #####################################################################

INSERT INTO geo.country (id, iso_code, name, phone_prefix) VALUES
                                                               ('10000000-0000-0000-0000-000000000001', 'CO', 'Colombia', '+57'),
                                                               ('10000000-0000-0000-0000-000000000002', 'MX', 'México',   '+52'),
                                                               ('10000000-0000-0000-0000-000000000003', 'US', 'Estados Unidos', '+1');

-- Departamentos de Colombia (subset representativo para arrancar)
INSERT INTO geo.state_province (id, country_id, name, code) VALUES
                                                                ('10000000-0000-0000-0000-000000000101', '10000000-0000-0000-0000-000000000001', 'Bogotá D.C.',       '11'),
                                                                ('10000000-0000-0000-0000-000000000102', '10000000-0000-0000-0000-000000000001', 'Antioquia',         '05'),
                                                                ('10000000-0000-0000-0000-000000000103', '10000000-0000-0000-0000-000000000001', 'Valle del Cauca',   '76'),
                                                                ('10000000-0000-0000-0000-000000000104', '10000000-0000-0000-0000-000000000001', 'Atlántico',         '08'),
                                                                ('10000000-0000-0000-0000-000000000105', '10000000-0000-0000-0000-000000000001', 'Santander',         '68');

-- Ciudades principales (subset representativo)
INSERT INTO geo.city (id, state_province_id, name) VALUES
                                                       ('10000000-0000-0000-0000-000000000201', '10000000-0000-0000-0000-000000000101', 'Bogotá'),
                                                       ('10000000-0000-0000-0000-000000000202', '10000000-0000-0000-0000-000000000102', 'Medellín'),
                                                       ('10000000-0000-0000-0000-000000000203', '10000000-0000-0000-0000-000000000102', 'Envigado'),
                                                       ('10000000-0000-0000-0000-000000000204', '10000000-0000-0000-0000-000000000103', 'Cali'),
                                                       ('10000000-0000-0000-0000-000000000205', '10000000-0000-0000-0000-000000000104', 'Barranquilla'),
                                                       ('10000000-0000-0000-0000-000000000206', '10000000-0000-0000-0000-000000000105', 'Bucaramanga');


-- #####################################################################
-- SCHEMA: tenant (solo catálogos globales, NO tenants reales)
-- #####################################################################

INSERT INTO tenant.lkp_tenant_status (id, code, name) VALUES
                                                          ('20000000-0000-0000-0000-000000000001', 'trial',     'En periodo de prueba'),
                                                          ('20000000-0000-0000-0000-000000000002', 'active',    'Activo'),
                                                          ('20000000-0000-0000-0000-000000000003', 'suspended', 'Suspendido'),
                                                          ('20000000-0000-0000-0000-000000000004', 'cancelled', 'Cancelado');

INSERT INTO tenant.lkp_site_type (id, code, name) VALUES
                                                      ('20000000-0000-0000-0000-000000000011', 'store',     'Tienda / punto de venta'),
                                                      ('20000000-0000-0000-0000-000000000012', 'warehouse', 'Bodega'),
                                                      ('20000000-0000-0000-0000-000000000013', 'hq',        'Oficina principal'),
                                                      ('20000000-0000-0000-0000-000000000014', 'kitchen',   'Cocina / producción'),
                                                      ('20000000-0000-0000-0000-000000000015', 'other',     'Otro');

INSERT INTO tenant.lkp_plugin_status (id, code, name) VALUES
                                                          ('20000000-0000-0000-0000-000000000021', 'active',    'Activo'),
                                                          ('20000000-0000-0000-0000-000000000022', 'suspended', 'Suspendido'),
                                                          ('20000000-0000-0000-0000-000000000023', 'cancelled', 'Cancelado'),
                                                          ('20000000-0000-0000-0000-000000000024', 'trial',     'En prueba');

INSERT INTO tenant.lkp_relationship_type (id, code, name) VALUES
                                                              ('20000000-0000-0000-0000-000000000031', 'shared_tracking',     'Tracking compartido'),
                                                              ('20000000-0000-0000-0000-000000000032', 'marketplace_supplier','Proveedor de marketplace'),
                                                              ('20000000-0000-0000-0000-000000000033', 'franchise',           'Franquicia');

INSERT INTO tenant.lkp_industry (id, code, name) VALUES
                                                     ('20000000-0000-0000-0000-000000000041', 'retail',        'Retail / comercio'),
                                                     ('20000000-0000-0000-0000-000000000042', 'restaurant',    'Restaurante / food service'),
                                                     ('20000000-0000-0000-0000-000000000043', 'services',      'Servicios profesionales'),
                                                     ('20000000-0000-0000-0000-000000000044', 'manufacturing', 'Manufactura'),
                                                     ('20000000-0000-0000-0000-000000000045', 'healthcare',    'Salud'),
                                                     ('20000000-0000-0000-0000-000000000046', 'logistics',     'Logística y transporte');

INSERT INTO tenant.lkp_plugin_category (id, code, name) VALUES
                                                            ('20000000-0000-0000-0000-000000000051', 'logistics',     'Logística'),
                                                            ('20000000-0000-0000-0000-000000000052', 'analytics',     'Analítica'),
                                                            ('20000000-0000-0000-0000-000000000053', 'hr',            'Recursos humanos'),
                                                            ('20000000-0000-0000-0000-000000000054', 'integrations',  'Integraciones');

INSERT INTO tenant.lkp_pricing_model (id, code, name) VALUES
                                                          ('20000000-0000-0000-0000-000000000061', 'flat',        'Tarifa fija'),
                                                          ('20000000-0000-0000-0000-000000000062', 'per_seat',    'Por usuario'),
                                                          ('20000000-0000-0000-0000-000000000063', 'usage_based', 'Por uso'),
                                                          ('20000000-0000-0000-0000-000000000064', 'free',        'Gratuito');

-- Catálogo de plugins (marketplace) disponibles para cualquier tenant.
INSERT INTO tenant.plugin_catalog (id, code, name, description, category_id, pricing_model_id, base_price, is_active) VALUES
                                                                                                                          ('20000000-0000-0000-0000-000000000071', 'tracking',           'Tracking de pedidos',
                                                                                                                           'Seguimiento de envíos propios y de transportadoras externas con mapa en tiempo real.',
                                                                                                                           '20000000-0000-0000-0000-000000000051', '20000000-0000-0000-0000-000000000062', 25000.00, true),
                                                                                                                          ('20000000-0000-0000-0000-000000000072', 'advanced_analytics', 'Analítica avanzada',
                                                                                                                           'Dashboards de ventas, inventario y desempeño de empleados con datos históricos.',
                                                                                                                           '20000000-0000-0000-0000-000000000052', '20000000-0000-0000-0000-000000000061', 60000.00, true),
                                                                                                                          ('20000000-0000-0000-0000-000000000073', 'payroll_lite',       'Nómina simplificada',
                                                                                                                           'Gestión básica de préstamos/abonos a empleados y resumen mensual.',
                                                                                                                           '20000000-0000-0000-0000-000000000053', '20000000-0000-0000-0000-000000000064', 0.00, true),
                                                                                                                          ('20000000-0000-0000-0000-000000000074', 'electronic_invoice', 'Facturación electrónica',
                                                                                                                           'Integración con el proveedor tecnológico autorizado para emisión de factura electrónica.',
                                                                                                                           '20000000-0000-0000-0000-000000000054', '20000000-0000-0000-0000-000000000063', 0.00, true);


-- #####################################################################
-- SCHEMA: identity (lookups + roles/permisos GLOBALES)
-- #####################################################################

INSERT INTO identity.lkp_user_status (id, code, name) VALUES
                                                          ('30000000-0000-0000-0000-000000000001', 'pending_verification', 'Pendiente de verificación'),
                                                          ('30000000-0000-0000-0000-000000000002', 'active',               'Activo'),
                                                          ('30000000-0000-0000-0000-000000000003', 'suspended',            'Suspendido'),
                                                          ('30000000-0000-0000-0000-000000000004', 'disabled',             'Deshabilitado');

INSERT INTO identity.lkp_contact_type (id, code, name) VALUES
                                                           ('30000000-0000-0000-0000-000000000011', 'email',    'Correo electrónico'),
                                                           ('30000000-0000-0000-0000-000000000012', 'phone',    'Teléfono'),
                                                           ('30000000-0000-0000-0000-000000000013', 'whatsapp', 'WhatsApp');

INSERT INTO identity.lkp_document_type (id, code, name) VALUES
                                                            ('30000000-0000-0000-0000-000000000021', 'CC',       'Cédula de ciudadanía'),
                                                            ('30000000-0000-0000-0000-000000000022', 'CE',       'Cédula de extranjería'),
                                                            ('30000000-0000-0000-0000-000000000023', 'NIT',      'NIT'),
                                                            ('30000000-0000-0000-0000-000000000024', 'passport', 'Pasaporte'),
                                                            ('30000000-0000-0000-0000-000000000025', 'RUT',      'RUT');

-- Roles GLOBALES del sistema (tenant_id IS NULL): plantilla para cualquier tenant.
INSERT INTO identity.role (id, tenant_id, name, description) VALUES
                                                                 ('30000000-0000-0000-0000-000000000031', NULL, 'super_admin', 'Administrador de la plataforma (Anthropic-side, no del tenant)'),
                                                                 ('30000000-0000-0000-0000-000000000032', NULL, 'admin',       'Administrador del tenant: acceso total dentro de su empresa'),
                                                                 ('30000000-0000-0000-0000-000000000033', NULL, 'cashier',     'Cajero: ventas y cobros'),
                                                                 ('30000000-0000-0000-0000-000000000034', NULL, 'warehouse',   'Bodeguero: inventario y movimientos de stock'),
                                                                 ('30000000-0000-0000-0000-000000000035', NULL, 'viewer',      'Solo lectura / reportes');

-- Catálogo GLOBAL de permisos granulares.
INSERT INTO identity.permission (id, code, description) VALUES
                                                            ('30000000-0000-0000-0000-000000000041', 'sale.invoice.create',   'Crear facturas/ventas'),
                                                            ('30000000-0000-0000-0000-000000000042', 'sale.invoice.cancel',   'Cancelar facturas'),
                                                            ('30000000-0000-0000-0000-000000000043', 'stock.adjust',          'Ajustar inventario manualmente'),
                                                            ('30000000-0000-0000-0000-000000000044', 'stock.view',            'Ver inventario'),
                                                            ('30000000-0000-0000-0000-000000000045', 'hr.employee.manage',    'Gestionar empleados'),
                                                            ('30000000-0000-0000-0000-000000000046', 'hr.ledger.manage',      'Registrar préstamos/abonos a empleados'),
                                                            ('30000000-0000-0000-0000-000000000047', 'tenant.settings.manage','Administrar configuración del tenant'),
                                                            ('30000000-0000-0000-0000-000000000048', 'analytics.view',        'Ver reportes y analítica');

-- Asignación de permisos por rol global (role_permission, N:N).
INSERT INTO identity.role_permission (role_id, permission_id) VALUES
                                                                  -- admin: todos los permisos
                                                                  ('30000000-0000-0000-0000-000000000032', '30000000-0000-0000-0000-000000000041'),
                                                                  ('30000000-0000-0000-0000-000000000032', '30000000-0000-0000-0000-000000000042'),
                                                                  ('30000000-0000-0000-0000-000000000032', '30000000-0000-0000-0000-000000000043'),
                                                                  ('30000000-0000-0000-0000-000000000032', '30000000-0000-0000-0000-000000000044'),
                                                                  ('30000000-0000-0000-0000-000000000032', '30000000-0000-0000-0000-000000000045'),
                                                                  ('30000000-0000-0000-0000-000000000032', '30000000-0000-0000-0000-000000000046'),
                                                                  ('30000000-0000-0000-0000-000000000032', '30000000-0000-0000-0000-000000000047'),
                                                                  ('30000000-0000-0000-0000-000000000032', '30000000-0000-0000-0000-000000000048'),
                                                                  -- cashier: solo ventas y ver stock
                                                                  ('30000000-0000-0000-0000-000000000033', '30000000-0000-0000-0000-000000000041'),
                                                                  ('30000000-0000-0000-0000-000000000033', '30000000-0000-0000-0000-000000000044'),
                                                                  -- warehouse: stock completo
                                                                  ('30000000-0000-0000-0000-000000000034', '30000000-0000-0000-0000-000000000043'),
                                                                  ('30000000-0000-0000-0000-000000000034', '30000000-0000-0000-0000-000000000044'),
                                                                  -- viewer: solo lectura
                                                                  ('30000000-0000-0000-0000-000000000035', '30000000-0000-0000-0000-000000000044'),
                                                                  ('30000000-0000-0000-0000-000000000035', '30000000-0000-0000-0000-000000000048');


-- #####################################################################
-- SCHEMA: hr (solo lookups)
-- #####################################################################

INSERT INTO hr.lkp_employment_status (id, code, name) VALUES
                                                          ('40000000-0000-0000-0000-000000000001', 'active',     'Activo'),
                                                          ('40000000-0000-0000-0000-000000000002', 'on_leave',   'En licencia'),
                                                          ('40000000-0000-0000-0000-000000000003', 'terminated', 'Terminado');

INSERT INTO hr.lkp_ledger_entry_type (id, code, name) VALUES
                                                          ('40000000-0000-0000-0000-000000000011', 'loan',      'Préstamo (débito)'),
                                                          ('40000000-0000-0000-0000-000000000012', 'repayment', 'Abono (crédito)');


-- #####################################################################
-- SCHEMA: stock (solo lookups)
-- #####################################################################

-- Categorías de producto jerárquicas: padres primero, luego hijas.
INSERT INTO stock.lkp_product_category (id, code, name, parent_category_id) VALUES
                                                                                ('50000000-0000-0000-0000-000000000001', 'general',   'General',          NULL),
                                                                                ('50000000-0000-0000-0000-000000000002', 'beverages', 'Bebidas',          NULL),
                                                                                ('50000000-0000-0000-0000-000000000003', 'food',      'Alimentos',        NULL),
                                                                                ('50000000-0000-0000-0000-000000000004', 'services',  'Insumos/servicios',NULL);

INSERT INTO stock.lkp_product_category (id, code, name, parent_category_id) VALUES
                                                                                ('50000000-0000-0000-0000-000000000011', 'soda',  'Gaseosas', '50000000-0000-0000-0000-000000000002'),
                                                                                ('50000000-0000-0000-0000-000000000012', 'juice', 'Jugos',    '50000000-0000-0000-0000-000000000002');

INSERT INTO stock.lkp_unit_of_measure (id, code, name) VALUES
                                                           ('50000000-0000-0000-0000-000000000021', 'unit', 'Unidad'),
                                                           ('50000000-0000-0000-0000-000000000022', 'kg',   'Kilogramo'),
                                                           ('50000000-0000-0000-0000-000000000023', 'lt',   'Litro'),
                                                           ('50000000-0000-0000-0000-000000000024', 'box',  'Caja'),
                                                           ('50000000-0000-0000-0000-000000000025', 'm',    'Metro');

INSERT INTO stock.lkp_movement_type (id, code, name, direction) VALUES
                                                                    ('50000000-0000-0000-0000-000000000031', 'purchase_in',   'Entrada por compra',    1),
                                                                    ('50000000-0000-0000-0000-000000000032', 'sale_out',      'Salida por venta',     -1),
                                                                    ('50000000-0000-0000-0000-000000000033', 'transfer_in',   'Entrada por traslado',  1),
                                                                    ('50000000-0000-0000-0000-000000000034', 'transfer_out',  'Salida por traslado',  -1),
                                                                    ('50000000-0000-0000-0000-000000000035', 'adjustment',    'Ajuste manual',         1),
                                                                    ('50000000-0000-0000-0000-000000000036', 'return_in',     'Entrada por devolución',1);


-- #####################################################################
-- SCHEMA: sale (solo lookups)
-- #####################################################################

INSERT INTO sale.lkp_invoice_status (id, code, name) VALUES
                                                         ('60000000-0000-0000-0000-000000000001', 'draft',           'Borrador'),
                                                         ('60000000-0000-0000-0000-000000000002', 'issued',          'Emitida'),
                                                         ('60000000-0000-0000-0000-000000000003', 'paid',            'Pagada'),
                                                         ('60000000-0000-0000-0000-000000000004', 'partially_paid',  'Pago parcial'),
                                                         ('60000000-0000-0000-0000-000000000005', 'cancelled',       'Cancelada'),
                                                         ('60000000-0000-0000-0000-000000000006', 'overdue',         'Vencida');

INSERT INTO sale.lkp_customer_type (id, code, name) VALUES
                                                        ('60000000-0000-0000-0000-000000000011', 'person',  'Persona natural'),
                                                        ('60000000-0000-0000-0000-000000000012', 'company', 'Empresa');

INSERT INTO sale.lkp_payment_method (id, code, name) VALUES
                                                         ('60000000-0000-0000-0000-000000000021', 'cash',     'Efectivo'),
                                                         ('60000000-0000-0000-0000-000000000022', 'card',     'Tarjeta'),
                                                         ('60000000-0000-0000-0000-000000000023', 'transfer', 'Transferencia'),
                                                         ('60000000-0000-0000-0000-000000000024', 'credit',   'Crédito/fiado');


-- #####################################################################
-- SCHEMA: logistics (lookups + carrier_catalog)
-- #####################################################################

INSERT INTO logistics.lkp_shipment_status (id, code, name) VALUES
                                                               ('70000000-0000-0000-0000-000000000001', 'created',         'Creado'),
                                                               ('70000000-0000-0000-0000-000000000002', 'picked_up',       'Recogido'),
                                                               ('70000000-0000-0000-0000-000000000003', 'in_transit',      'En tránsito'),
                                                               ('70000000-0000-0000-0000-000000000004', 'out_for_delivery','En reparto'),
                                                               ('70000000-0000-0000-0000-000000000005', 'delivered',       'Entregado'),
                                                               ('70000000-0000-0000-0000-000000000006', 'exception',       'Excepción/incidente'),
                                                               ('70000000-0000-0000-0000-000000000007', 'cancelled',       'Cancelado');

INSERT INTO logistics.lkp_event_type (id, code, name) VALUES
                                                          ('70000000-0000-0000-0000-000000000011', 'picked_up',        'Recogido'),
                                                          ('70000000-0000-0000-0000-000000000012', 'in_transit',       'En tránsito'),
                                                          ('70000000-0000-0000-0000-000000000013', 'out_for_delivery', 'En reparto'),
                                                          ('70000000-0000-0000-0000-000000000014', 'delivered',        'Entregado'),
                                                          ('70000000-0000-0000-0000-000000000015', 'exception',        'Excepción/incidente');

INSERT INTO logistics.lkp_event_source (id, code, name) VALUES
                                                            ('70000000-0000-0000-0000-000000000021', 'internal',        'Flota propia'),
                                                            ('70000000-0000-0000-0000-000000000022', 'carrier_webhook', 'Webhook de transportadora');

INSERT INTO logistics.lkp_vehicle_type (id, code, name) VALUES
                                                            ('70000000-0000-0000-0000-000000000031', 'moto',      'Motocicleta'),
                                                            ('70000000-0000-0000-0000-000000000032', 'carro',     'Automóvil'),
                                                            ('70000000-0000-0000-0000-000000000033', 'bicicleta', 'Bicicleta'),
                                                            ('70000000-0000-0000-0000-000000000034', 'a_pie',     'A pie');

-- Catálogo de transportadoras externas + registro especial in_house.
INSERT INTO logistics.carrier_catalog (id, code, name, api_base_url, tracking_url_template, is_active) VALUES
                                                                                                           ('70000000-0000-0000-0000-000000000041', 'in_house',      'Flota propia',  NULL, NULL, true),
                                                                                                           ('70000000-0000-0000-0000-000000000042', 'servientrega',  'Servientrega',  'https://api.servientrega.com', 'https://www.servientrega.com/track/{tracking_number}', true),
                                                                                                           ('70000000-0000-0000-0000-000000000043', 'coordinadora',  'Coordinadora',  'https://api.coordinadora.com',  'https://www.coordinadora.com/track/{tracking_number}', true),
                                                                                                           ('70000000-0000-0000-0000-000000000044', 'dhl',           'DHL',           'https://api-eu.dhl.com',         'https://www.dhl.com/track/{tracking_number}', true);

COMMIT;