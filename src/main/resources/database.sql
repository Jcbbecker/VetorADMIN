CREATE TYPE public.maintenance AS ENUM (
    'PREDICTIVE', 'PREVENTIVE', 'SCHEDULEDCORRECTIVE', 'CORRECTIVE'
);

CREATE TYPE public.assetstate AS ENUM (
    'WORKING', 'IDLE'
);

CREATE TYPE public.downtimetype AS ENUM (
	'MANUAL', 'AUTOMATIC'
);

CREATE TYPE public.priority AS ENUM (
	'URGENT', 'HIGH', 'MIDDLE', 'LOW'
);

CREATE TYPE public.servicetype AS ENUM (
    'OUTSOURCED',
    'OWN'
);

CREATE TYPE public.state AS ENUM (
	'COMPLETED', 'PLANNING', 'PROGRESS', 'OPENING'
);

CREATE TYPE public.eventtype AS ENUM (
	'ASSET_DOWN', 'ASSET_UP', 'TEMP_AN', 'VIB_AN', 'VIB_AN_UP', 'VIB_AN_DOWN', 'VIB_MAX', 'VIB_MIN', 'TEMP_MAX', 'TEMP_MIN'
);

CREATE TABLE organization (
	id UUID UNIQUE PRIMARY KEY,
	name VARCHAR(100) NULL
);

CREATE TABLE client (
	id UUID UNIQUE PRIMARY KEY,
	name VARCHAR(100) NULL,
	email VARCHAR(100) NULL,
	password VARCHAR(60) NOT NULL,
    image_link VARCHAR(100) NULL,
	created_at TIMESTAMP WITHOUT TIME ZONE DEFAULT (now() AT TIME ZONE 'UTC')
);

ALTER TABLE client ADD COLUMN id_organization UUID;
ALTER TABLE client ADD CONSTRAINT fk_organization FOREIGN KEY (id_organization)
REFERENCES organization (id);

CREATE TABLE asset (
	id 		UUID                UNIQUE PRIMARY KEY,
	asset_key VARCHAR(50) NULL,
	name VARCHAR(100) NULL,
	manufacturer VARCHAR(100) NULL,
	production_date DATE NULL,
	asset_location VARCHAR(100) NULL,
	asset_model VARCHAR(100) NULL,
	serial_number VARCHAR(100) NULL,
	maintenance_type maintenance NULL,
	downtime	double precision    NULL,
	downtime_type downtimetype NULL,
	asset_state assetstate NULL,
	data_added TIMESTAMP WITHOUT TIME ZONE DEFAULT (now() AT TIME ZONE 'UTC'),
	created_at TIMESTAMP WITHOUT TIME ZONE DEFAULT (now() AT TIME ZONE 'UTC')
);

ALTER TABLE asset ADD COLUMN id_organization UUID;
ALTER TABLE asset ADD CONSTRAINT fk_organization FOREIGN KEY (id_organization)
REFERENCES organization (id);

CREATE TABLE data (
	id 		UUID                UNIQUE PRIMARY KEY,

    rms_x 		    DOUBLE PRECISION    NULL,
    rms_y	double precision    NULL,
	rms_z 		    DOUBLE PRECISION    NULL,

    temperature	double precision    NULL,
	asset_key VARCHAR(50) NULL,
	added TIMESTAMP WITHOUT TIME ZONE DEFAULT (now() AT TIME ZONE 'UTC'),
	is_analysed BOOLEAN DEFAULT FALSE
);

CREATE TABLE vibration_package (
	id 		UUID                UNIQUE PRIMARY KEY,

    data_array 		    double precision[]    NULL,

    start_sample INTEGER NULL,
    end_sample INTEGER NULL,

	asset_key VARCHAR(50) NULL,
	added TIMESTAMP WITHOUT TIME ZONE DEFAULT (now() AT TIME ZONE 'UTC')
);

ALTER TABLE vibration_package ADD COLUMN id_asset UUID;
ALTER TABLE vibration_package ADD CONSTRAINT fk_asset FOREIGN KEY (id_asset)
REFERENCES asset (id);

CREATE TABLE fft (
	id 		UUID                UNIQUE PRIMARY KEY,

	resolution INTEGER NULL,
    samples INTEGER NULL,

    fft_array 		    double precision[]    NULL,

	asset_key VARCHAR(50) NULL,
	added TIMESTAMP WITHOUT TIME ZONE DEFAULT (now() AT TIME ZONE 'UTC')
);

ALTER TABLE fft ADD COLUMN id_asset UUID;
ALTER TABLE fft ADD CONSTRAINT fk_asset FOREIGN KEY (id_asset)
REFERENCES asset (id);

CREATE TABLE vibration_chart_info (
	id 		UUID                UNIQUE PRIMARY KEY,

    data_array 		    double precision[]    NULL,

	asset_key VARCHAR(50) NULL,
	added TIMESTAMP WITHOUT TIME ZONE DEFAULT (now() AT TIME ZONE 'UTC')
);

ALTER TABLE vibration_chart_info ADD COLUMN id_asset UUID;
ALTER TABLE vibration_chart_info ADD CONSTRAINT fk_asset FOREIGN KEY (id_asset)
REFERENCES asset (id);

CREATE TABLE machineintervals(
	id 		UUID                UNIQUE PRIMARY KEY,
    asset_state assetstate NULL,
	added TIMESTAMP WITHOUT TIME ZONE
);

ALTER TABLE machineintervals ADD COLUMN id_asset UUID;
ALTER TABLE machineintervals ADD CONSTRAINT fk_asset FOREIGN KEY (id_asset)
REFERENCES asset (id);

CREATE TABLE workorder(
	id 		UUID                UNIQUE PRIMARY KEY,
	name VARCHAR(100) NULL,
	owner VARCHAR(100) NULL,
	asset_key VARCHAR(100) NULL,
	created_date DATE NULL,
	conclusion_date DATE NULL,
	priority priority NULL,
	maintenance maintenance NULL,
	state state NULL,
	created_at TIMESTAMP WITHOUT TIME ZONE DEFAULT (now() AT TIME ZONE 'UTC')
);

ALTER TABLE workorder ADD COLUMN id_organization UUID;
ALTER TABLE workorder ADD CONSTRAINT fk_organization FOREIGN KEY (id_organization)
REFERENCES organization (id);

CREATE TABLE events(
	id 		UUID                UNIQUE PRIMARY KEY,
	event_type eventtype NULL,
	description VARCHAR(100) NULL,
	added TIMESTAMP WITHOUT TIME ZONE
);

ALTER TABLE events ADD COLUMN id_asset UUID;
ALTER TABLE events ADD CONSTRAINT fk_asset FOREIGN KEY (id_asset)
REFERENCES asset (id);

CREATE TABLE perfomaceindex(
	id 		UUID                UNIQUE PRIMARY KEY,
	completed	double precision    NULL,
	planning	double precision    NULL,
	progress	double precision    NULL,
	opening	double precision    NULL,
	idle	double precision    NULL,
	working	double precision    NULL,
	total_downtime	double precision    NULL,

    MP double precision    NULL,
    MPD double precision    NULL,
    runtime_average double precision    NULL,
    MTBF double precision    NULL,
    MTTR double precision    NULL,
    MTTA double precision    NULL,
    availability double precision    NULL
);

ALTER TABLE perfomaceindex ADD COLUMN id_client UUID;
ALTER TABLE perfomaceindex ADD CONSTRAINT fk_client FOREIGN KEY (id_client)
REFERENCES client (id);

CREATE TABLE roles(
	id 		UUID                UNIQUE PRIMARY KEY,
	role VARCHAR(100) NULL,
	color VARCHAR(10) NULL
);

ALTER TABLE client ADD COLUMN id_role UUID;
ALTER TABLE client ADD CONSTRAINT fk_role FOREIGN KEY (id_role)
REFERENCES roles (id);

ALTER TABLE roles ADD COLUMN id_organization UUID;
ALTER TABLE roles ADD CONSTRAINT fk_organization FOREIGN KEY (id_organization)
REFERENCES organization (id);

CREATE TABLE privileges(
	id 		UUID                UNIQUE PRIMARY KEY,
	name VARCHAR(100) NULL
);

CREATE TABLE roles_privileges(
    id_role 		UUID                UNIQUE,
    id_privilege 		UUID                UNIQUE
)