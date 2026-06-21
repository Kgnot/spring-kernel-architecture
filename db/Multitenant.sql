CREATE EXTENSION IF NOT EXISTS postgis;
CREATE EXTENSION IF NOT EXISTS postgis_topology;


CREATE SCHEMA "geo";

CREATE SCHEMA "tenant";

CREATE SCHEMA "identity";

CREATE SCHEMA "hr";

CREATE SCHEMA "stock";

CREATE SCHEMA "sale";

CREATE SCHEMA "logistics";

CREATE SCHEMA "analytics";

CREATE TABLE "geo"."country" (
  "id" uuid PRIMARY KEY DEFAULT (gen_random_uuid()),
  "iso_code" varchar(2) UNIQUE NOT NULL,
  "name" varchar(100) NOT NULL,
  "phone_prefix" varchar(6)
);

CREATE TABLE "geo"."state_province" (
  "id" uuid PRIMARY KEY DEFAULT (gen_random_uuid()),
  "country_id" uuid NOT NULL,
  "name" varchar(100) NOT NULL,
  "code" varchar(10)
);

CREATE TABLE "geo"."city" (
  "id" uuid PRIMARY KEY DEFAULT (gen_random_uuid()),
  "state_province_id" uuid NOT NULL,
  "name" varchar(150) NOT NULL
);

CREATE TABLE "tenant"."lkp_tenant_status" (
  "id" uuid PRIMARY KEY DEFAULT (gen_random_uuid()),
  "code" varchar(30) UNIQUE NOT NULL,
  "name" varchar(100) NOT NULL
);

CREATE TABLE "tenant"."lkp_site_type" (
  "id" uuid PRIMARY KEY DEFAULT (gen_random_uuid()),
  "code" varchar(30) UNIQUE NOT NULL,
  "name" varchar(100) NOT NULL
);

CREATE TABLE "tenant"."lkp_plugin_status" (
  "id" uuid PRIMARY KEY DEFAULT (gen_random_uuid()),
  "code" varchar(30) UNIQUE NOT NULL,
  "name" varchar(100) NOT NULL
);

CREATE TABLE "tenant"."lkp_relationship_type" (
  "id" uuid PRIMARY KEY DEFAULT (gen_random_uuid()),
  "code" varchar(40) UNIQUE NOT NULL,
  "name" varchar(100) NOT NULL
);

CREATE TABLE "tenant"."lkp_industry" (
  "id" uuid PRIMARY KEY DEFAULT (gen_random_uuid()),
  "code" varchar(50) UNIQUE NOT NULL,
  "name" varchar(150) NOT NULL
);

CREATE TABLE "tenant"."tenants" (
  "id" uuid PRIMARY KEY DEFAULT (gen_random_uuid()),
  "legal_name" varchar(255) NOT NULL,
  "trade_name" varchar(255),
  "tax_id" varchar(50) UNIQUE NOT NULL,
  "industry_id" uuid,
  "status_id" uuid NOT NULL,
  "subdomain" varchar(100) UNIQUE NOT NULL,
  "timezone" varchar(50) NOT NULL DEFAULT 'America/Bogota',
  "default_currency" varchar(3) NOT NULL DEFAULT 'COP',
  "trial_ends_at" timestamptz,
  "created_at" timestamptz NOT NULL DEFAULT (now()),
  "updated_at" timestamptz NOT NULL DEFAULT (now()),
  "deleted_at" timestamptz
);

CREATE TABLE "tenant"."sites" (
  "id" uuid PRIMARY KEY DEFAULT (gen_random_uuid()),
  "tenant_id" uuid NOT NULL,
  "name" varchar(150) NOT NULL,
  "site_type_id" uuid NOT NULL,
  "address_line1" varchar(255),
  "address_line2" varchar(255),
  "country_id" uuid,
  "state_province_id" uuid,
  "city_id" uuid,
  "postal_code" varchar(20),
  "location" geography(Point,4326),
  "phone" varchar(30),
  "is_active" boolean NOT NULL DEFAULT true,
  "created_at" timestamptz NOT NULL DEFAULT (now())
);

CREATE TABLE "tenant"."lkp_plugin_category" (
  "id" uuid PRIMARY KEY DEFAULT (gen_random_uuid()),
  "code" varchar(50) UNIQUE NOT NULL,
  "name" varchar(150) NOT NULL
);

CREATE TABLE "tenant"."lkp_pricing_model" (
  "id" uuid PRIMARY KEY DEFAULT (gen_random_uuid()),
  "code" varchar(30) UNIQUE NOT NULL,
  "name" varchar(100) NOT NULL
);

CREATE TABLE "tenant"."plugin_catalog" (
  "id" uuid PRIMARY KEY DEFAULT (gen_random_uuid()),
  "code" varchar(50) UNIQUE NOT NULL,
  "name" varchar(150) NOT NULL,
  "description" text,
  "category_id" uuid,
  "pricing_model_id" uuid,
  "base_price" numeric(12,2),
  "is_active" boolean NOT NULL DEFAULT true
);

CREATE TABLE "tenant"."tenant_plugin" (
  "id" uuid PRIMARY KEY DEFAULT (gen_random_uuid()),
  "tenant_id" uuid NOT NULL,
  "plugin_id" uuid NOT NULL,
  "status_id" uuid NOT NULL,
  "enabled_at" timestamptz NOT NULL DEFAULT (now()),
  "expires_at" timestamptz,
  "config" jsonb
);

CREATE TABLE "tenant"."tenant_partner_link" (
  "id" uuid PRIMARY KEY DEFAULT (gen_random_uuid()),
  "tenant_id" uuid NOT NULL,
  "partner_tenant_id" uuid NOT NULL,
  "relationship_type_id" uuid NOT NULL,
  "is_active" boolean NOT NULL DEFAULT true,
  "created_at" timestamptz NOT NULL DEFAULT (now())
);

CREATE TABLE "identity"."lkp_user_status" (
  "id" uuid PRIMARY KEY DEFAULT (gen_random_uuid()),
  "code" varchar(30) UNIQUE NOT NULL,
  "name" varchar(100) NOT NULL
);

CREATE TABLE "identity"."lkp_contact_type" (
  "id" uuid PRIMARY KEY DEFAULT (gen_random_uuid()),
  "code" varchar(20) UNIQUE NOT NULL,
  "name" varchar(50) NOT NULL
);

CREATE TABLE "identity"."lkp_document_type" (
  "id" uuid PRIMARY KEY DEFAULT (gen_random_uuid()),
  "code" varchar(20) UNIQUE NOT NULL,
  "name" varchar(100) NOT NULL
);

CREATE TABLE "identity"."users_login" (
  "id" uuid PRIMARY KEY DEFAULT (gen_random_uuid()),
  "tenant_id" uuid NOT NULL,
  "email" varchar(255) NOT NULL,
  "password_hash" varchar(255) NOT NULL,
  "status_id" uuid NOT NULL,
  "mfa_enabled" boolean NOT NULL DEFAULT false,
  "last_login_at" timestamptz,
  "failed_login_attempts" smallint NOT NULL DEFAULT 0,
  "locked_until" timestamptz,
  "created_at" timestamptz NOT NULL DEFAULT (now()),
  "updated_at" timestamptz NOT NULL DEFAULT (now())
);

CREATE TABLE "identity"."profile" (
  "id" uuid PRIMARY KEY DEFAULT (gen_random_uuid()),
  "tenant_id" uuid NOT NULL,
  "user_login_id" uuid,
  "first_name" varchar(100) NOT NULL,
  "last_name" varchar(100) NOT NULL,
  "document_type_id" uuid,
  "document_number" varchar(50),
  "avatar_url" varchar(500),
  "birth_date" date,
  "created_at" timestamptz NOT NULL DEFAULT (now())
);

CREATE TABLE "identity"."user_contact" (
  "id" uuid PRIMARY KEY DEFAULT (gen_random_uuid()),
  "profile_id" uuid NOT NULL,
  "contact_type_id" uuid NOT NULL,
  "value" varchar(255) NOT NULL,
  "is_primary" boolean NOT NULL DEFAULT false,
  "is_login_email" boolean NOT NULL DEFAULT false,
  "is_verified" boolean NOT NULL DEFAULT false,
  "created_at" timestamptz NOT NULL DEFAULT (now())
);

CREATE TABLE "identity"."role" (
  "id" uuid PRIMARY KEY DEFAULT (gen_random_uuid()),
  "tenant_id" uuid,
  "name" varchar(80) NOT NULL,
  "description" varchar(255)
);

CREATE TABLE "identity"."permission" (
  "id" uuid PRIMARY KEY DEFAULT (gen_random_uuid()),
  "code" varchar(100) UNIQUE NOT NULL,
  "description" varchar(255)
);

CREATE TABLE "identity"."role_permission" (
  "role_id" uuid NOT NULL,
  "permission_id" uuid NOT NULL,
  PRIMARY KEY ("role_id", "permission_id")
);

CREATE TABLE "identity"."user_role" (
  "user_login_id" uuid NOT NULL,
  "role_id" uuid NOT NULL,
  "assigned_at" timestamptz NOT NULL DEFAULT (now()),
  PRIMARY KEY ("user_login_id", "role_id")
);

CREATE TABLE "hr"."lkp_employment_status" (
  "id" uuid PRIMARY KEY DEFAULT (gen_random_uuid()),
  "code" varchar(30) UNIQUE NOT NULL,
  "name" varchar(100) NOT NULL
);

CREATE TABLE "hr"."lkp_ledger_entry_type" (
  "id" uuid PRIMARY KEY DEFAULT (gen_random_uuid()),
  "code" varchar(20) UNIQUE NOT NULL,
  "name" varchar(100) NOT NULL
);

CREATE TABLE "hr"."employee" (
  "id" uuid PRIMARY KEY DEFAULT (gen_random_uuid()),
  "tenant_id" uuid NOT NULL,
  "profile_id" uuid NOT NULL,
  "site_id" uuid,
  "position" varchar(100),
  "hire_date" date NOT NULL,
  "termination_date" date,
  "salary" numeric(14,2),
  "employment_status_id" uuid NOT NULL,
  "created_at" timestamptz NOT NULL DEFAULT (now())
);

CREATE TABLE "hr"."employee_ledger" (
  "id" uuid PRIMARY KEY DEFAULT (gen_random_uuid()),
  "tenant_id" uuid NOT NULL,
  "employee_id" uuid NOT NULL,
  "entry_type_id" uuid NOT NULL,
  "amount" numeric(14,2) NOT NULL,
  "balance_after" numeric(14,2) NOT NULL,
  "reason" varchar(255),
  "reference_invoice_id" uuid,
  "created_by" uuid,
  "created_at" timestamptz NOT NULL DEFAULT (now())
);

CREATE TABLE "stock"."lkp_product_category" (
  "id" uuid PRIMARY KEY DEFAULT (gen_random_uuid()),
  "code" varchar(50) UNIQUE NOT NULL,
  "name" varchar(150) NOT NULL,
  "parent_category_id" uuid
);

CREATE TABLE "stock"."lkp_unit_of_measure" (
  "id" uuid PRIMARY KEY DEFAULT (gen_random_uuid()),
  "code" varchar(20) UNIQUE NOT NULL,
  "name" varchar(100) NOT NULL
);

CREATE TABLE "stock"."lkp_movement_type" (
  "id" uuid PRIMARY KEY DEFAULT (gen_random_uuid()),
  "code" varchar(30) UNIQUE NOT NULL,
  "name" varchar(100) NOT NULL,
  "direction" smallint NOT NULL
);

CREATE TABLE "stock"."product" (
  "id" uuid PRIMARY KEY DEFAULT (gen_random_uuid()),
  "tenant_id" uuid NOT NULL,
  "sku" varchar(80) NOT NULL,
  "name" varchar(255) NOT NULL,
  "description" text,
  "category_id" uuid,
  "unit_of_measure_id" uuid NOT NULL,
  "cost_price" numeric(14,2),
  "sale_price" numeric(14,2) NOT NULL,
  "is_active" boolean NOT NULL DEFAULT true,
  "created_at" timestamptz NOT NULL DEFAULT (now())
);

CREATE TABLE "stock"."supplier" (
  "id" uuid PRIMARY KEY DEFAULT (gen_random_uuid()),
  "tenant_id" uuid NOT NULL,
  "name" varchar(255) NOT NULL,
  "tax_id" varchar(50),
  "is_active" boolean NOT NULL DEFAULT true
);

CREATE TABLE "stock"."inventory" (
  "id" uuid PRIMARY KEY DEFAULT (gen_random_uuid()),
  "tenant_id" uuid NOT NULL,
  "product_id" uuid NOT NULL,
  "site_id" uuid NOT NULL,
  "quantity_on_hand" numeric(14,3) NOT NULL DEFAULT 0,
  "quantity_reserved" numeric(14,3) NOT NULL DEFAULT 0,
  "reorder_point" numeric(14,3),
  "updated_at" timestamptz NOT NULL DEFAULT (now())
);

CREATE TABLE "stock"."inventory_movement" (
  "id" uuid PRIMARY KEY DEFAULT (gen_random_uuid()),
  "tenant_id" uuid NOT NULL,
  "inventory_id" uuid NOT NULL,
  "movement_type_id" uuid NOT NULL,
  "quantity" numeric(14,3) NOT NULL,
  "supplier_id" uuid,
  "reference_id" uuid,
  "notes" varchar(255),
  "created_by" uuid,
  "created_at" timestamptz NOT NULL DEFAULT (now())
);

CREATE TABLE "sale"."lkp_invoice_status" (
  "id" uuid PRIMARY KEY DEFAULT (gen_random_uuid()),
  "code" varchar(30) UNIQUE NOT NULL,
  "name" varchar(100) NOT NULL
);

CREATE TABLE "sale"."lkp_customer_type" (
  "id" uuid PRIMARY KEY DEFAULT (gen_random_uuid()),
  "code" varchar(20) UNIQUE NOT NULL,
  "name" varchar(100) NOT NULL
);

CREATE TABLE "sale"."lkp_payment_method" (
  "id" uuid PRIMARY KEY DEFAULT (gen_random_uuid()),
  "code" varchar(30) UNIQUE NOT NULL,
  "name" varchar(100) NOT NULL
);

CREATE TABLE "sale"."customers" (
  "id" uuid PRIMARY KEY DEFAULT (gen_random_uuid()),
  "tenant_id" uuid NOT NULL,
  "profile_id" uuid,
  "customer_type_id" uuid NOT NULL,
  "company_name" varchar(255),
  "tax_id" varchar(50),
  "credit_limit" numeric(14,2) DEFAULT 0,
  "created_at" timestamptz NOT NULL DEFAULT (now())
);

CREATE TABLE "sale"."services" (
  "id" uuid PRIMARY KEY DEFAULT (gen_random_uuid()),
  "tenant_id" uuid NOT NULL,
  "code" varchar(80) NOT NULL,
  "name" varchar(255) NOT NULL,
  "description" text,
  "price" numeric(14,2) NOT NULL,
  "duration_minutes" integer,
  "is_active" boolean NOT NULL DEFAULT true,
  "created_at" timestamptz NOT NULL DEFAULT (now())
);

CREATE TABLE "sale"."invoice" (
  "id" uuid PRIMARY KEY DEFAULT (gen_random_uuid()),
  "tenant_id" uuid NOT NULL,
  "site_id" uuid NOT NULL,
  "customer_id" uuid,
  "employee_id" uuid,
  "invoice_number" varchar(50) NOT NULL,
  "status_id" uuid NOT NULL,
  "subtotal" numeric(14,2) NOT NULL DEFAULT 0,
  "tax_total" numeric(14,2) NOT NULL DEFAULT 0,
  "discount_total" numeric(14,2) NOT NULL DEFAULT 0,
  "grand_total" numeric(14,2) NOT NULL DEFAULT 0,
  "currency" varchar(3) NOT NULL DEFAULT 'COP',
  "issued_at" timestamptz,
  "due_at" timestamptz,
  "created_at" timestamptz NOT NULL DEFAULT (now())
);

CREATE TABLE "sale"."invoice_details" (
  "id" uuid PRIMARY KEY DEFAULT (gen_random_uuid()),
  "invoice_id" uuid NOT NULL,
  "product_id" uuid,
  "service_id" uuid,
  "description" varchar(255) NOT NULL,
  "quantity" numeric(14,3) NOT NULL DEFAULT 1,
  "unit_price" numeric(14,2) NOT NULL,
  "discount" numeric(14,2) NOT NULL DEFAULT 0,
  "tax_rate" numeric(5,2) NOT NULL DEFAULT 0,
  "line_total" numeric(14,2) NOT NULL
);

CREATE TABLE "sale"."payment" (
  "id" uuid PRIMARY KEY DEFAULT (gen_random_uuid()),
  "tenant_id" uuid NOT NULL,
  "invoice_id" uuid NOT NULL,
  "payment_method_id" uuid NOT NULL,
  "amount" numeric(14,2) NOT NULL,
  "paid_at" timestamptz NOT NULL DEFAULT (now()),
  "reference_code" varchar(100)
);

CREATE TABLE "logistics"."lkp_shipment_status" (
  "id" uuid PRIMARY KEY DEFAULT (gen_random_uuid()),
  "code" varchar(30) UNIQUE NOT NULL,
  "name" varchar(100) NOT NULL
);

CREATE TABLE "logistics"."lkp_event_type" (
  "id" uuid PRIMARY KEY DEFAULT (gen_random_uuid()),
  "code" varchar(40) UNIQUE NOT NULL,
  "name" varchar(100) NOT NULL
);

CREATE TABLE "logistics"."lkp_event_source" (
  "id" uuid PRIMARY KEY DEFAULT (gen_random_uuid()),
  "code" varchar(20) UNIQUE NOT NULL,
  "name" varchar(100) NOT NULL
);

CREATE TABLE "logistics"."lkp_vehicle_type" (
  "id" uuid PRIMARY KEY DEFAULT (gen_random_uuid()),
  "code" varchar(20) UNIQUE NOT NULL,
  "name" varchar(100) NOT NULL
);

CREATE TABLE "logistics"."carrier_catalog" (
  "id" uuid PRIMARY KEY DEFAULT (gen_random_uuid()),
  "code" varchar(50) UNIQUE NOT NULL,
  "name" varchar(150) NOT NULL,
  "api_base_url" varchar(255),
  "tracking_url_template" varchar(255),
  "is_active" boolean NOT NULL DEFAULT true
);

CREATE TABLE "logistics"."tenant_carrier_credential" (
  "id" uuid PRIMARY KEY DEFAULT (gen_random_uuid()),
  "tenant_id" uuid NOT NULL,
  "carrier_id" uuid NOT NULL,
  "api_key_encrypted" varchar(500),
  "account_code" varchar(100),
  "is_active" boolean NOT NULL DEFAULT true
);

CREATE TABLE "logistics"."shipment" (
  "id" uuid PRIMARY KEY DEFAULT (gen_random_uuid()),
  "tenant_id" uuid NOT NULL,
  "invoice_id" uuid,
  "origin_site_id" uuid,
  "carrier_id" uuid NOT NULL,
  "shared_with_tenant_id" uuid,
  "tracking_number" varchar(100) NOT NULL,
  "status_id" uuid NOT NULL,
  "origin_point" geography(Point,4326),
  "destination_point" geography(Point,4326),
  "destination_address" varchar(255),
  "destination_city_id" uuid,
  "estimated_delivery_at" timestamptz,
  "delivered_at" timestamptz,
  "created_at" timestamptz NOT NULL DEFAULT (now())
);

CREATE TABLE "logistics"."shipment_event" (
  "id" uuid PRIMARY KEY DEFAULT (gen_random_uuid()),
  "shipment_id" uuid NOT NULL,
  "event_type_id" uuid NOT NULL,
  "description" varchar(255),
  "location" geography(Point,4326),
  "occurred_at" timestamptz NOT NULL,
  "source_id" uuid NOT NULL,
  "raw_payload" jsonb
);

CREATE TABLE "logistics"."delivery_agent" (
  "id" uuid PRIMARY KEY DEFAULT (gen_random_uuid()),
  "tenant_id" uuid NOT NULL,
  "employee_id" uuid,
  "vehicle_type_id" uuid,
  "current_location" geography(Point,4326),
  "is_available" boolean NOT NULL DEFAULT true,
  "last_ping_at" timestamptz
);

CREATE TABLE "logistics"."shipment_assignment" (
  "shipment_id" uuid NOT NULL,
  "delivery_agent_id" uuid NOT NULL,
  "assigned_at" timestamptz NOT NULL DEFAULT (now()),
  PRIMARY KEY ("shipment_id", "delivery_agent_id")
);

CREATE TABLE "analytics"."fact_sales_daily" (
  "id" uuid PRIMARY KEY DEFAULT (gen_random_uuid()),
  "tenant_id" uuid NOT NULL,
  "site_id" uuid,
  "date" date NOT NULL,
  "total_invoices" integer NOT NULL DEFAULT 0,
  "total_items_sold" numeric(14,3) NOT NULL DEFAULT 0,
  "gross_revenue" numeric(16,2) NOT NULL DEFAULT 0,
  "total_discounts" numeric(16,2) NOT NULL DEFAULT 0,
  "total_tax" numeric(16,2) NOT NULL DEFAULT 0,
  "net_revenue" numeric(16,2) NOT NULL DEFAULT 0
);

CREATE TABLE "analytics"."fact_product_performance" (
  "id" uuid PRIMARY KEY DEFAULT (gen_random_uuid()),
  "tenant_id" uuid NOT NULL,
  "product_id" uuid NOT NULL,
  "site_id" uuid,
  "date" date NOT NULL,
  "units_sold" numeric(14,3) NOT NULL DEFAULT 0,
  "revenue" numeric(16,2) NOT NULL DEFAULT 0,
  "margin_estimate" numeric(16,2)
);

CREATE TABLE "analytics"."fact_inventory_snapshot" (
  "id" uuid PRIMARY KEY DEFAULT (gen_random_uuid()),
  "tenant_id" uuid NOT NULL,
  "product_id" uuid NOT NULL,
  "site_id" uuid NOT NULL,
  "snapshot_date" date NOT NULL,
  "quantity_on_hand" numeric(14,3) NOT NULL,
  "inventory_value" numeric(16,2)
);

CREATE TABLE "analytics"."fact_employee_ledger_summary" (
  "id" uuid PRIMARY KEY DEFAULT (gen_random_uuid()),
  "tenant_id" uuid NOT NULL,
  "employee_id" uuid NOT NULL,
  "period_month" date NOT NULL,
  "total_loans" numeric(14,2) NOT NULL DEFAULT 0,
  "total_repayments" numeric(14,2) NOT NULL DEFAULT 0,
  "ending_balance" numeric(14,2) NOT NULL DEFAULT 0
);

CREATE TABLE "analytics"."fact_shipment_performance" (
  "id" uuid PRIMARY KEY DEFAULT (gen_random_uuid()),
  "tenant_id" uuid NOT NULL,
  "carrier_id" uuid NOT NULL,
  "date" date NOT NULL,
  "total_shipments" integer NOT NULL DEFAULT 0,
  "delivered_on_time" integer NOT NULL DEFAULT 0,
  "delivered_late" integer NOT NULL DEFAULT 0,
  "avg_delivery_hours" numeric(8,2)
);

CREATE INDEX ON "geo"."state_province" ("country_id", "name");

CREATE INDEX ON "geo"."city" ("state_province_id", "name");

CREATE INDEX ON "tenant"."sites" ("tenant_id", "site_type_id");

CREATE INDEX ON "tenant"."sites" ("city_id");

CREATE UNIQUE INDEX ON "tenant"."tenant_plugin" ("tenant_id", "plugin_id");

CREATE UNIQUE INDEX ON "tenant"."tenant_partner_link" ("tenant_id", "partner_tenant_id");

CREATE UNIQUE INDEX ON "identity"."users_login" ("tenant_id", "email");

CREATE INDEX ON "identity"."profile" ("tenant_id", "document_number");

CREATE INDEX ON "identity"."user_contact" ("profile_id", "contact_type_id");

CREATE INDEX ON "hr"."employee" ("tenant_id", "employment_status_id");

CREATE INDEX ON "hr"."employee_ledger" ("tenant_id", "employee_id", "created_at");

CREATE UNIQUE INDEX ON "stock"."product" ("tenant_id", "sku");

CREATE UNIQUE INDEX ON "stock"."inventory" ("tenant_id", "product_id", "site_id");

CREATE INDEX ON "stock"."inventory_movement" ("tenant_id", "inventory_id", "created_at");

CREATE INDEX ON "sale"."customers" ("tenant_id", "tax_id");

CREATE UNIQUE INDEX ON "sale"."services" ("tenant_id", "code");

CREATE UNIQUE INDEX ON "sale"."invoice" ("tenant_id", "invoice_number");

CREATE UNIQUE INDEX ON "logistics"."tenant_carrier_credential" ("tenant_id", "carrier_id");

CREATE UNIQUE INDEX ON "logistics"."shipment" ("tenant_id", "tracking_number");

CREATE INDEX ON "logistics"."shipment" ("carrier_id", "status_id");

CREATE INDEX ON "logistics"."shipment_event" ("shipment_id", "occurred_at");

CREATE UNIQUE INDEX ON "analytics"."fact_sales_daily" ("tenant_id", "site_id", "date");

CREATE UNIQUE INDEX ON "analytics"."fact_product_performance" ("tenant_id", "product_id", "site_id", "date");

CREATE INDEX ON "analytics"."fact_inventory_snapshot" ("tenant_id", "site_id", "snapshot_date");

CREATE UNIQUE INDEX ON "analytics"."fact_employee_ledger_summary" ("tenant_id", "employee_id", "period_month");

CREATE INDEX ON "analytics"."fact_shipment_performance" ("tenant_id", "carrier_id", "date");

COMMENT ON TABLE "geo"."country" IS 'Catálogo global de países. Cualquier schema que necesite país referencia esto por id de forma LÓGICA (sin FK real entre schemas).';

COMMENT ON COLUMN "geo"."country"."iso_code" IS 'ISO 3166-1 alpha-2, ej: CO, MX, US';

COMMENT ON COLUMN "geo"."country"."phone_prefix" IS 'ej: +57';

COMMENT ON TABLE "geo"."state_province" IS 'Departamento/estado/provincia, depende del país. FK real porque country vive en el mismo schema geo.';

COMMENT ON COLUMN "geo"."state_province"."code" IS 'ej: código DIVIPOLA en Colombia, o abreviación de estado';

COMMENT ON TABLE "geo"."city" IS 'Ciudad/municipio. Resto de schemas (tenant, sale, identity) guardan city_id como referencia LÓGICA a esta tabla, no como FK real.';

COMMENT ON TABLE "tenant"."lkp_tenant_status" IS 'Catálogo de estados posibles de un tenant. Lookup en lugar de ENUM para poder agregar estados sin migrar tipos.';

COMMENT ON COLUMN "tenant"."lkp_tenant_status"."code" IS 'trial, active, suspended, cancelled';

COMMENT ON TABLE "tenant"."lkp_site_type" IS 'Tipos de sede. Lookup para que cada tenant eventualmente pueda tener tipos propios (ej. "patio_acopio") sin tocar un ENUM global.';

COMMENT ON COLUMN "tenant"."lkp_site_type"."code" IS 'store, warehouse, hq, kitchen, other';

COMMENT ON COLUMN "tenant"."lkp_plugin_status"."code" IS 'active, suspended, cancelled, trial';

COMMENT ON TABLE "tenant"."lkp_relationship_type" IS 'Tipos de relación posibles entre dos tenants aliados (tenant_partner_link).';

COMMENT ON COLUMN "tenant"."lkp_relationship_type"."code" IS 'shared_tracking, marketplace_supplier, franchise';

COMMENT ON TABLE "tenant"."lkp_industry" IS 'Catálogo normalizado de verticales/industrias de negocio. Antes era un varchar libre en tenants; ahora es tabla lookup propia de este schema (no jerárquico; no vive en geo porque no es un dato geográfico sino de clasificación de negocio).';

COMMENT ON COLUMN "tenant"."lkp_industry"."code" IS 'retail, restaurant, services, manufacturing, healthcare, etc.';

COMMENT ON TABLE "tenant"."tenants" IS 'La entidad raíz del sistema. Todo dato tenant-scoped cuelga de aquí (por tenant_id lógico/real según el schema). El subdomain o un header X-Tenant-Id resuelve quién está pidiendo qué.';

COMMENT ON COLUMN "tenant"."tenants"."legal_name" IS 'Razón social';

COMMENT ON COLUMN "tenant"."tenants"."trade_name" IS 'Nombre comercial / marca';

COMMENT ON COLUMN "tenant"."tenants"."tax_id" IS 'NIT / RUT / identificación fiscal';

COMMENT ON COLUMN "tenant"."tenants"."industry_id" IS 'normalizado: antes era varchar libre, ahora FK real (mismo schema) hacia lkp_industry';

COMMENT ON COLUMN "tenant"."tenants"."subdomain" IS 'usado para resolver el tenant en el login: subdomain.app.com';

COMMENT ON COLUMN "tenant"."tenants"."default_currency" IS 'ISO 4217';

COMMENT ON COLUMN "tenant"."tenants"."deleted_at" IS 'soft delete';

COMMENT ON TABLE "tenant"."sites" IS 'Sedes/sucursales/bodegas físicas del tenant. La dirección vive aquí mismo (no en tabla address separada) porque es el dato crítico de esta entidad; country/state/city son ids lógicos hacia el schema geo. stock.inventory se ubica por site_id (lógico).';

COMMENT ON COLUMN "tenant"."sites"."name" IS 'Ej: "Sede Norte", "Bodega Principal"';

COMMENT ON COLUMN "tenant"."sites"."address_line1" IS 'calle/carrera/número, dirección embebida a propósito (no tabla address aparte)';

COMMENT ON COLUMN "tenant"."sites"."address_line2" IS 'complemento: oficina, piso, referencia';

COMMENT ON COLUMN "tenant"."sites"."country_id" IS 'referencia LÓGICA a geo.country.id, sin FK real entre schemas';

COMMENT ON COLUMN "tenant"."sites"."state_province_id" IS 'referencia LÓGICA a geo.state_province.id';

COMMENT ON COLUMN "tenant"."sites"."city_id" IS 'referencia LÓGICA a geo.city.id';

COMMENT ON COLUMN "tenant"."sites"."location" IS 'PostGIS: lat/lng de la sede, para geocercas, ruteo y distancia a clientes';

COMMENT ON TABLE "tenant"."lkp_plugin_category" IS 'Categorías de plugin, normalizadas por consistencia con la regla 7 (antes varchar libre en plugin_catalog).';

COMMENT ON COLUMN "tenant"."lkp_plugin_category"."code" IS 'logistics, analytics, hr, integrations';

COMMENT ON TABLE "tenant"."lkp_pricing_model" IS 'Modelos de cobro de un plugin, normalizados (antes varchar libre).';

COMMENT ON COLUMN "tenant"."lkp_pricing_model"."code" IS 'flat, per_seat, usage_based, free';

COMMENT ON TABLE "tenant"."plugin_catalog" IS 'Catálogo GLOBAL (sin tenant_id) de módulos/plugins que la plataforma ofrece, ej: tracking de pedidos, analítica avanzada, facturación electrónica. No se borra, se desactiva.';

COMMENT ON COLUMN "tenant"."plugin_catalog"."code" IS 'slug interno, ej: "tracking", "advanced_analytics", "multi_currency"';

COMMENT ON TABLE "tenant"."tenant_plugin" IS 'Tabla puente: qué plugins tiene contratados/activos cada tenant. El backend consulta esto para saber si mostrar el módulo de tracking, analítica, etc.';

COMMENT ON COLUMN "tenant"."tenant_plugin"."expires_at" IS 'null = no expira / se renueva automáticamente';

COMMENT ON COLUMN "tenant"."tenant_plugin"."config" IS 'configuración específica del tenant para ese plugin, ej: límites, webhooks, claves';

COMMENT ON TABLE "tenant"."tenant_partner_link" IS 'Permite que dos tenants distintos se vinculen, por ejemplo para compartir el mismo servicio de tracking o red logística.';

COMMENT ON COLUMN "tenant"."tenant_partner_link"."tenant_id" IS 'tenant que inicia la relación';

COMMENT ON COLUMN "tenant"."tenant_partner_link"."partner_tenant_id" IS 'tenant aliado';

COMMENT ON COLUMN "identity"."lkp_user_status"."code" IS 'pending_verification, active, suspended, disabled';

COMMENT ON COLUMN "identity"."lkp_contact_type"."code" IS 'email, phone, whatsapp';

COMMENT ON COLUMN "identity"."lkp_document_type"."code" IS 'CC, CE, NIT, passport, RUT';

COMMENT ON TABLE "identity"."users_login" IS 'Credenciales de acceso. email es una copia sincronizada (no la fuente de verdad) para que el login nunca se rompa al cambiar el contacto principal. La fuente de verdad de emails/teléfonos vive en identity.user_contact.';

COMMENT ON COLUMN "identity"."users_login"."tenant_id" IS 'referencia LÓGICA a tenant.tenants.id';

COMMENT ON COLUMN "identity"."users_login"."email" IS 'COPIA desnormalizada del contacto marcado is_login_email en user_contact. Es el campo real usado para autenticar; se sincroniza por trigger/aplicación cuando cambia el principal.';

COMMENT ON TABLE "identity"."profile" IS 'Datos de la persona física. NO guarda email/teléfono directamente (ver user_contact): una persona puede tener varios de cada uno. employee y customers referencian un profile en lugar de duplicar datos personales.';

COMMENT ON COLUMN "identity"."profile"."tenant_id" IS 'referencia LÓGICA a tenant.tenants.id';

COMMENT ON COLUMN "identity"."profile"."user_login_id" IS 'nullable: un profile (ej. un cliente) puede no tener cuenta de acceso';

COMMENT ON TABLE "identity"."user_contact" IS 'Múltiples emails/teléfonos por persona. Resuelve el caso de negocio: el usuario puede tener 2+ emails y 2+ teléfonos, y solo UNO está marcado is_login_email como el vigente para iniciar sesión.';

COMMENT ON COLUMN "identity"."user_contact"."value" IS 'el email o número en sí';

COMMENT ON COLUMN "identity"."user_contact"."is_primary" IS 'contacto principal de ese tipo para mostrar en UI';

COMMENT ON COLUMN "identity"."user_contact"."is_login_email" IS 'true si este es el email vigente para autenticación; debe sincronizarse hacia users_login.email al cambiar';

COMMENT ON TABLE "identity"."role" IS 'Roles tipo RBAC. Permite roles globales predefinidos (admin, employee, viewer) y roles custom por tenant.';

COMMENT ON COLUMN "identity"."role"."tenant_id" IS 'referencia LÓGICA a tenant.tenants.id; null = rol global del sistema (ej. super_admin)';

COMMENT ON TABLE "identity"."permission" IS 'Catálogo GLOBAL de permisos granulares del sistema.';

COMMENT ON COLUMN "identity"."permission"."code" IS 'ej: "sale.invoice.create", "stock.adjust"';

COMMENT ON TABLE "identity"."role_permission" IS 'Tabla puente N:N entre roles y permisos, FK real porque ambas tablas viven en identity.';

COMMENT ON TABLE "identity"."user_role" IS 'Qué roles tiene asignado cada cuenta. Un usuario puede tener varios roles (ej. cajero + bodeguero).';

COMMENT ON COLUMN "hr"."lkp_employment_status"."code" IS 'active, on_leave, terminated';

COMMENT ON TABLE "hr"."lkp_ledger_entry_type" IS 'Tipos de movimiento del libro de créditos/débitos a empleados.';

COMMENT ON COLUMN "hr"."lkp_ledger_entry_type"."code" IS 'loan, repayment';

COMMENT ON TABLE "hr"."employee" IS 'Datos laborales del empleado. La identidad (nombre, documento, contacto) vive en identity.profile / identity.user_contact (referencia lógica); aquí solo lo específico de RRHH.';

COMMENT ON COLUMN "hr"."employee"."tenant_id" IS 'referencia LÓGICA a tenant.tenants.id';

COMMENT ON COLUMN "hr"."employee"."profile_id" IS 'referencia LÓGICA a identity.profile.id';

COMMENT ON COLUMN "hr"."employee"."site_id" IS 'referencia LÓGICA a tenant.sites.id, sede base donde trabaja';

COMMENT ON COLUMN "hr"."employee"."position" IS 'cargo: cajero, vendedor, bodeguero, etc.';

COMMENT ON TABLE "hr"."employee_ledger" IS 'Créditos y débitos prestados a empleados. Libro contable simple: cada fila es un movimiento (préstamo o abono) y guarda el saldo resultante para auditoría rápida.';

COMMENT ON COLUMN "hr"."employee_ledger"."tenant_id" IS 'referencia LÓGICA a tenant.tenants.id';

COMMENT ON COLUMN "hr"."employee_ledger"."amount" IS 'siempre positivo; el signo lo da entry_type';

COMMENT ON COLUMN "hr"."employee_ledger"."balance_after" IS 'saldo acumulado de deuda tras este movimiento';

COMMENT ON COLUMN "hr"."employee_ledger"."reason" IS 'ej: "préstamo nómina", "descuento por daño de inventario"';

COMMENT ON COLUMN "hr"."employee_ledger"."reference_invoice_id" IS 'referencia LÓGICA opcional a sale.invoice.id, si el descuento viene de una compra del empleado';

COMMENT ON COLUMN "hr"."employee_ledger"."created_by" IS 'referencia LÓGICA a identity.users_login.id';

COMMENT ON TABLE "stock"."lkp_product_category" IS 'Categorías de producto normalizadas (antes era un varchar libre). Jerárquica vía parent_category_id.';

COMMENT ON COLUMN "stock"."lkp_product_category"."parent_category_id" IS 'permite categorías jerárquicas, ej: Bebidas > Gaseosas';

COMMENT ON COLUMN "stock"."lkp_unit_of_measure"."code" IS 'unit, kg, lt, box, m, etc.';

COMMENT ON COLUMN "stock"."lkp_movement_type"."code" IS 'purchase_in, sale_out, transfer_in, transfer_out, adjustment, return_in';

COMMENT ON COLUMN "stock"."lkp_movement_type"."direction" IS '+1 entra, -1 sale — evita interpretar el signo desde el code';

COMMENT ON TABLE "stock"."product" IS 'Catálogo maestro de productos físicos INVENTARIABLES únicamente. Ya no tiene is_service: product y sale.services son entidades totalmente independientes (sin tabla padre, sin herencia), cada una vive y se gestiona por completo dentro de su propio schema. "product" es la ficha del producto; stock.inventory es cuánto hay físicamente de cada uno.';

COMMENT ON COLUMN "stock"."product"."tenant_id" IS 'referencia LÓGICA a tenant.tenants.id';

COMMENT ON COLUMN "stock"."product"."cost_price" IS 'último costo de compra';

COMMENT ON TABLE "stock"."supplier" IS 'Proveedores del tenant. Sus contactos (teléfono/email) se modelan igual que personas vía identity.user_contact si se requiere multiplicidad; aquí solo el dato fiscal mínimo.';

COMMENT ON COLUMN "stock"."supplier"."tenant_id" IS 'referencia LÓGICA a tenant.tenants.id';

COMMENT ON TABLE "stock"."inventory" IS 'Existencias reales: cuánto hay de cada producto, en cada sede (site_id lógico hacia tenant.sites).';

COMMENT ON COLUMN "stock"."inventory"."tenant_id" IS 'referencia LÓGICA a tenant.tenants.id';

COMMENT ON COLUMN "stock"."inventory"."site_id" IS 'referencia LÓGICA a tenant.sites.id';

COMMENT ON COLUMN "stock"."inventory"."quantity_reserved" IS 'reservado por ventas/pedidos en curso';

COMMENT ON COLUMN "stock"."inventory"."reorder_point" IS 'umbral para alertar reabastecimiento';

COMMENT ON TABLE "stock"."inventory_movement" IS 'Kardex / historial de movimientos de inventario, para trazabilidad y auditoría.';

COMMENT ON COLUMN "stock"."inventory_movement"."tenant_id" IS 'referencia LÓGICA a tenant.tenants.id';

COMMENT ON COLUMN "stock"."inventory_movement"."quantity" IS 'siempre positivo; la dirección (+/-) la da movement_type.direction';

COMMENT ON COLUMN "stock"."inventory_movement"."supplier_id" IS 'si el movimiento es una compra';

COMMENT ON COLUMN "stock"."inventory_movement"."reference_id" IS 'referencia LÓGICA polimórfica al documento origen, ej: sale.invoice.id';

COMMENT ON COLUMN "stock"."inventory_movement"."created_by" IS 'referencia LÓGICA a identity.users_login.id';

COMMENT ON COLUMN "sale"."lkp_invoice_status"."code" IS 'draft, issued, paid, partially_paid, cancelled, overdue';

COMMENT ON COLUMN "sale"."lkp_customer_type"."code" IS 'person, company';

COMMENT ON COLUMN "sale"."lkp_payment_method"."code" IS 'cash, card, transfer, credit';

COMMENT ON TABLE "sale"."customers" IS 'Clientes del tenant. email/teléfono del cliente persona se consultan vía identity.user_contact a través de profile_id (referencia lógica); no se duplican aquí.';

COMMENT ON COLUMN "sale"."customers"."tenant_id" IS 'referencia LÓGICA a tenant.tenants.id';

COMMENT ON COLUMN "sale"."customers"."profile_id" IS 'referencia LÓGICA a identity.profile.id; nullable: clientes de mostrador pueden no tener profile';

COMMENT ON COLUMN "sale"."customers"."company_name" IS 'si customer_type = company';

COMMENT ON COLUMN "sale"."customers"."credit_limit" IS 'para ventas a crédito/fiado';

COMMENT ON TABLE "sale"."services" IS 'Servicios facturables (mano de obra, consultoría, citas, etc.), entidad TOTALMENTE INDEPENDIENTE de stock.product: sin tabla padre, sin product_id, sin relación lógica entre schemas. Un producto es siempre inventariable (vive en stock); un servicio nunca lo es (vive solo en sale). invoice_details decide cuál de los dos factura mediante sus columnas product_id / service_id, sin necesitar herencia.';

COMMENT ON COLUMN "sale"."services"."tenant_id" IS 'referencia LÓGICA a tenant.tenants.id';

COMMENT ON COLUMN "sale"."services"."code" IS 'identificador propio del servicio, equivalente al sku de product pero independiente';

COMMENT ON COLUMN "sale"."services"."duration_minutes" IS 'útil para servicios agendables, ej: peluquería, consultoría';

COMMENT ON TABLE "sale"."invoice" IS 'Cabecera de factura/venta. Los montos vienen agregados aquí; el detalle línea por línea está en invoice_details.';

COMMENT ON COLUMN "sale"."invoice"."tenant_id" IS 'referencia LÓGICA a tenant.tenants.id';

COMMENT ON COLUMN "sale"."invoice"."site_id" IS 'referencia LÓGICA a tenant.sites.id';

COMMENT ON COLUMN "sale"."invoice"."employee_id" IS 'referencia LÓGICA a hr.employee.id, quién vendió/atendió';

COMMENT ON COLUMN "sale"."invoice"."due_at" IS 'para ventas a crédito';

COMMENT ON TABLE "sale"."invoice_details" IS 'Detalle de cada línea de la factura. Exactamente uno de product_id / service_id debe estar lleno (constraint CHECK a nivel de aplicación o CHECK (num_nonnulls(product_id, service_id) = 1) en Postgres) — esta pareja de columnas es el discriminador, sin necesitar tabla padre ni herencia. product_id es lógico porque cruza a stock; service_id es FK real porque service vive en este mismo schema sale. Ambos casos guardan snapshot de nombre/precio porque el catálogo de origen puede cambiar después de vender.';

COMMENT ON COLUMN "sale"."invoice_details"."product_id" IS 'referencia LÓGICA opcional a stock.product.id — se llena cuando la línea es un producto inventariable';

COMMENT ON COLUMN "sale"."invoice_details"."service_id" IS 'FK real (mismo schema) — se llena cuando la línea es un servicio';

COMMENT ON COLUMN "sale"."invoice_details"."description" IS 'snapshot del nombre del ítem al momento de vender';

COMMENT ON COLUMN "sale"."invoice_details"."unit_price" IS 'snapshot del precio al momento de vender';

COMMENT ON TABLE "sale"."payment" IS 'Pagos aplicados a una factura. Permite pagos parciales o mixtos sin sobrecargar invoice.';

COMMENT ON COLUMN "sale"."payment"."tenant_id" IS 'referencia LÓGICA a tenant.tenants.id';

COMMENT ON COLUMN "sale"."payment"."reference_code" IS 'número de transacción/aprobación';

COMMENT ON COLUMN "logistics"."lkp_shipment_status"."code" IS 'created, picked_up, in_transit, out_for_delivery, delivered, exception, cancelled';

COMMENT ON COLUMN "logistics"."lkp_event_type"."code" IS 'picked_up, in_transit, out_for_delivery, delivered, exception';

COMMENT ON COLUMN "logistics"."lkp_event_source"."code" IS 'internal, carrier_webhook';

COMMENT ON COLUMN "logistics"."lkp_vehicle_type"."code" IS 'moto, carro, bicicleta, a_pie';

COMMENT ON TABLE "logistics"."carrier_catalog" IS 'Catálogo GLOBAL de transportadoras externas soportadas, más un registro especial "in_house" para flota propia del tenant.';

COMMENT ON COLUMN "logistics"."carrier_catalog"."code" IS 'ej: "servientrega", "coordinadora", "dhl", "in_house"';

COMMENT ON COLUMN "logistics"."carrier_catalog"."tracking_url_template" IS 'ej: https://carrier.com/track/{tracking_number}';

COMMENT ON TABLE "logistics"."tenant_carrier_credential" IS 'Credenciales/contrato de cada tenant con cada transportadora externa.';

COMMENT ON COLUMN "logistics"."tenant_carrier_credential"."tenant_id" IS 'referencia LÓGICA a tenant.tenants.id';

COMMENT ON TABLE "logistics"."shipment" IS 'Envío/pedido en tránsito, propio o de un carrier externo. shared_with_tenant_id permite que dos empresas vinculadas compartan visibilidad de un mismo servicio de tracking.';

COMMENT ON COLUMN "logistics"."shipment"."tenant_id" IS 'referencia LÓGICA a tenant.tenants.id, dueño/responsable del envío';

COMMENT ON COLUMN "logistics"."shipment"."invoice_id" IS 'referencia LÓGICA opcional a sale.invoice.id';

COMMENT ON COLUMN "logistics"."shipment"."origin_site_id" IS 'referencia LÓGICA opcional a tenant.sites.id';

COMMENT ON COLUMN "logistics"."shipment"."shared_with_tenant_id" IS 'referencia LÓGICA opcional a tenant.tenants.id, ver tenant.tenant_partner_link';

COMMENT ON COLUMN "logistics"."shipment"."origin_point" IS 'PostGIS: punto de recogida';

COMMENT ON COLUMN "logistics"."shipment"."destination_point" IS 'PostGIS: punto de entrega';

COMMENT ON COLUMN "logistics"."shipment"."destination_city_id" IS 'referencia LÓGICA opcional a geo.city.id';

COMMENT ON TABLE "logistics"."shipment_event" IS 'Línea de tiempo del envío: cada escaneo, cambio de estado o actualización GPS, tanto de flota propia como de webhooks externos.';

COMMENT ON COLUMN "logistics"."shipment_event"."location" IS 'PostGIS: ubicación del evento, propia (GPS de repartidor) o reportada por el carrier externo';

COMMENT ON COLUMN "logistics"."shipment_event"."raw_payload" IS 'respuesta cruda del webhook del carrier externo, para debugging';

COMMENT ON TABLE "logistics"."delivery_agent" IS 'Repartidores de flota PROPIA del tenant (no aplica a carriers externos).';

COMMENT ON COLUMN "logistics"."delivery_agent"."tenant_id" IS 'referencia LÓGICA a tenant.tenants.id';

COMMENT ON COLUMN "logistics"."delivery_agent"."employee_id" IS 'referencia LÓGICA opcional a hr.employee.id, si el repartidor es empleado propio';

COMMENT ON COLUMN "logistics"."delivery_agent"."current_location" IS 'PostGIS: última posición GPS conocida';

COMMENT ON TABLE "logistics"."shipment_assignment" IS 'Qué repartidor propio tiene asignado cada envío. Solo aplica cuando carrier_catalog = in_house.';

COMMENT ON TABLE "analytics"."fact_sales_daily" IS 'Ventas agregadas por tenant/sede/día. Se llena por ETL desde sale.invoice + invoice_details.';

COMMENT ON COLUMN "analytics"."fact_sales_daily"."tenant_id" IS 'referencia LÓGICA a tenant.tenants.id';

COMMENT ON COLUMN "analytics"."fact_sales_daily"."site_id" IS 'referencia LÓGICA a tenant.sites.id';

COMMENT ON TABLE "analytics"."fact_product_performance" IS 'Performance por producto/sede/día, sin tocar las tablas transaccionales.';

COMMENT ON COLUMN "analytics"."fact_product_performance"."tenant_id" IS 'referencia LÓGICA a tenant.tenants.id';

COMMENT ON COLUMN "analytics"."fact_product_performance"."product_id" IS 'referencia LÓGICA a stock.product.id';

COMMENT ON COLUMN "analytics"."fact_product_performance"."site_id" IS 'referencia LÓGICA a tenant.sites.id';

COMMENT ON COLUMN "analytics"."fact_product_performance"."margin_estimate" IS 'revenue - (units_sold * cost_price al momento)';

COMMENT ON TABLE "analytics"."fact_inventory_snapshot" IS 'Foto diaria del inventario valorizado, para evolución en el tiempo.';

COMMENT ON COLUMN "analytics"."fact_inventory_snapshot"."tenant_id" IS 'referencia LÓGICA a tenant.tenants.id';

COMMENT ON COLUMN "analytics"."fact_inventory_snapshot"."product_id" IS 'referencia LÓGICA a stock.product.id';

COMMENT ON COLUMN "analytics"."fact_inventory_snapshot"."site_id" IS 'referencia LÓGICA a tenant.sites.id';

COMMENT ON COLUMN "analytics"."fact_inventory_snapshot"."inventory_value" IS 'quantity_on_hand * cost_price';

COMMENT ON TABLE "analytics"."fact_employee_ledger_summary" IS 'Resumen mensual de préstamos/abonos por empleado, para reportes de RRHH/nómina.';

COMMENT ON COLUMN "analytics"."fact_employee_ledger_summary"."tenant_id" IS 'referencia LÓGICA a tenant.tenants.id';

COMMENT ON COLUMN "analytics"."fact_employee_ledger_summary"."employee_id" IS 'referencia LÓGICA a hr.employee.id';

COMMENT ON COLUMN "analytics"."fact_employee_ledger_summary"."period_month" IS 'primer día del mes, ej: 2026-06-01';

COMMENT ON TABLE "analytics"."fact_shipment_performance" IS 'KPIs logísticos por carrier/día: cumplimiento de entrega, tiempos promedio.';

COMMENT ON COLUMN "analytics"."fact_shipment_performance"."tenant_id" IS 'referencia LÓGICA a tenant.tenants.id';

COMMENT ON COLUMN "analytics"."fact_shipment_performance"."carrier_id" IS 'referencia LÓGICA a logistics.carrier_catalog.id';

ALTER TABLE "geo"."state_province" ADD FOREIGN KEY ("country_id") REFERENCES "geo"."country" ("id") DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE "geo"."city" ADD FOREIGN KEY ("state_province_id") REFERENCES "geo"."state_province" ("id") DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE "tenant"."tenants" ADD FOREIGN KEY ("industry_id") REFERENCES "tenant"."lkp_industry" ("id") DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE "tenant"."tenants" ADD FOREIGN KEY ("status_id") REFERENCES "tenant"."lkp_tenant_status" ("id") DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE "tenant"."sites" ADD FOREIGN KEY ("tenant_id") REFERENCES "tenant"."tenants" ("id") DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE "tenant"."sites" ADD FOREIGN KEY ("site_type_id") REFERENCES "tenant"."lkp_site_type" ("id") DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE "tenant"."plugin_catalog" ADD FOREIGN KEY ("category_id") REFERENCES "tenant"."lkp_plugin_category" ("id") DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE "tenant"."plugin_catalog" ADD FOREIGN KEY ("pricing_model_id") REFERENCES "tenant"."lkp_pricing_model" ("id") DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE "tenant"."tenant_plugin" ADD FOREIGN KEY ("tenant_id") REFERENCES "tenant"."tenants" ("id") DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE "tenant"."tenant_plugin" ADD FOREIGN KEY ("plugin_id") REFERENCES "tenant"."plugin_catalog" ("id") DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE "tenant"."tenant_plugin" ADD FOREIGN KEY ("status_id") REFERENCES "tenant"."lkp_plugin_status" ("id") DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE "tenant"."tenant_partner_link" ADD FOREIGN KEY ("tenant_id") REFERENCES "tenant"."tenants" ("id") DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE "tenant"."tenant_partner_link" ADD FOREIGN KEY ("partner_tenant_id") REFERENCES "tenant"."tenants" ("id") DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE "tenant"."tenant_partner_link" ADD FOREIGN KEY ("relationship_type_id") REFERENCES "tenant"."lkp_relationship_type" ("id") DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE "identity"."users_login" ADD FOREIGN KEY ("status_id") REFERENCES "identity"."lkp_user_status" ("id") DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE "identity"."profile" ADD FOREIGN KEY ("user_login_id") REFERENCES "identity"."users_login" ("id") DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE "identity"."profile" ADD FOREIGN KEY ("document_type_id") REFERENCES "identity"."lkp_document_type" ("id") DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE "identity"."user_contact" ADD FOREIGN KEY ("profile_id") REFERENCES "identity"."profile" ("id") DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE "identity"."user_contact" ADD FOREIGN KEY ("contact_type_id") REFERENCES "identity"."lkp_contact_type" ("id") DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE "identity"."role_permission" ADD FOREIGN KEY ("role_id") REFERENCES "identity"."role" ("id") DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE "identity"."role_permission" ADD FOREIGN KEY ("permission_id") REFERENCES "identity"."permission" ("id") DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE "identity"."user_role" ADD FOREIGN KEY ("user_login_id") REFERENCES "identity"."users_login" ("id") DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE "identity"."user_role" ADD FOREIGN KEY ("role_id") REFERENCES "identity"."role" ("id") DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE "hr"."employee" ADD FOREIGN KEY ("employment_status_id") REFERENCES "hr"."lkp_employment_status" ("id") DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE "hr"."employee_ledger" ADD FOREIGN KEY ("employee_id") REFERENCES "hr"."employee" ("id") DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE "hr"."employee_ledger" ADD FOREIGN KEY ("entry_type_id") REFERENCES "hr"."lkp_ledger_entry_type" ("id") DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE "stock"."lkp_product_category" ADD FOREIGN KEY ("parent_category_id") REFERENCES "stock"."lkp_product_category" ("id") DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE "stock"."product" ADD FOREIGN KEY ("category_id") REFERENCES "stock"."lkp_product_category" ("id") DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE "stock"."product" ADD FOREIGN KEY ("unit_of_measure_id") REFERENCES "stock"."lkp_unit_of_measure" ("id") DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE "stock"."inventory" ADD FOREIGN KEY ("product_id") REFERENCES "stock"."product" ("id") DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE "stock"."inventory_movement" ADD FOREIGN KEY ("inventory_id") REFERENCES "stock"."inventory" ("id") DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE "stock"."inventory_movement" ADD FOREIGN KEY ("movement_type_id") REFERENCES "stock"."lkp_movement_type" ("id") DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE "stock"."inventory_movement" ADD FOREIGN KEY ("supplier_id") REFERENCES "stock"."supplier" ("id") DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE "sale"."customers" ADD FOREIGN KEY ("customer_type_id") REFERENCES "sale"."lkp_customer_type" ("id") DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE "sale"."invoice" ADD FOREIGN KEY ("customer_id") REFERENCES "sale"."customers" ("id") DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE "sale"."invoice" ADD FOREIGN KEY ("status_id") REFERENCES "sale"."lkp_invoice_status" ("id") DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE "sale"."invoice_details" ADD FOREIGN KEY ("invoice_id") REFERENCES "sale"."invoice" ("id") DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE "sale"."invoice_details" ADD FOREIGN KEY ("service_id") REFERENCES "sale"."services" ("id") DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE "sale"."payment" ADD FOREIGN KEY ("invoice_id") REFERENCES "sale"."invoice" ("id") DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE "sale"."payment" ADD FOREIGN KEY ("payment_method_id") REFERENCES "sale"."lkp_payment_method" ("id") DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE "logistics"."tenant_carrier_credential" ADD FOREIGN KEY ("carrier_id") REFERENCES "logistics"."carrier_catalog" ("id") DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE "logistics"."shipment" ADD FOREIGN KEY ("carrier_id") REFERENCES "logistics"."carrier_catalog" ("id") DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE "logistics"."shipment" ADD FOREIGN KEY ("status_id") REFERENCES "logistics"."lkp_shipment_status" ("id") DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE "logistics"."shipment_event" ADD FOREIGN KEY ("shipment_id") REFERENCES "logistics"."shipment" ("id") DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE "logistics"."shipment_event" ADD FOREIGN KEY ("event_type_id") REFERENCES "logistics"."lkp_event_type" ("id") DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE "logistics"."shipment_event" ADD FOREIGN KEY ("source_id") REFERENCES "logistics"."lkp_event_source" ("id") DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE "logistics"."delivery_agent" ADD FOREIGN KEY ("vehicle_type_id") REFERENCES "logistics"."lkp_vehicle_type" ("id") DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE "logistics"."shipment_assignment" ADD FOREIGN KEY ("shipment_id") REFERENCES "logistics"."shipment" ("id") DEFERRABLE INITIALLY IMMEDIATE;

ALTER TABLE "logistics"."shipment_assignment" ADD FOREIGN KEY ("delivery_agent_id") REFERENCES "logistics"."delivery_agent" ("id") DEFERRABLE INITIALLY IMMEDIATE;
