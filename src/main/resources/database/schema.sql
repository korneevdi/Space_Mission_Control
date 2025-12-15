-- 1
CREATE TABLE IF NOT EXISTS missions
(
	mission_id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
	name VARCHAR(100) UNIQUE NOT NULL,
	launch_date DATE,
	status VARCHAR(20) NOT NULL
);

-- 2
CREATE TABLE IF NOT EXISTS spaceships
(
	ship_id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
	model VARCHAR(50) NOT NULL,
	manufacturer VARCHAR(100),
	capacity INT,
	weight_kg DECIMAL
);

-- 3
CREATE TABLE IF NOT EXISTS astronauts
(
	astronaut_id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
	first_name VARCHAR(50) NOT NULL,
	last_name VARCHAR(50) NOT NULL,
	rank VARCHAR(50),
	birth_date DATE NOT NULL,
	country VARCHAR(50) NOT NULL
);

-- 4
CREATE TABLE IF NOT EXISTS equipment
(
	equipment_id INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
	mission_id INT,
	name VARCHAR(100) NOT NULL,
	category VARCHAR(50),
	weight_kg DECIMAL,

	CONSTRAINT mission_equipment_fk FOREIGN KEY (mission_id)
		REFERENCES missions(mission_id)
);

-- 5
CREATE TABLE IF NOT EXISTS mission_details
(
    mission_id INT PRIMARY KEY,
    budget_million_usd DECIMAL,
    duration_days INT,
    description TEXT NOT NULL,

    CONSTRAINT mission_mission_details_fk FOREIGN KEY (mission_id)
        REFERENCES missions(mission_id)
        ON DELETE CASCADE
);


-- Link tables for many-to-many relations

-- 6
CREATE TABLE IF NOT EXISTS mission_spaceships
(
	mission_id INT NOT NULL,
    spaceship_id INT NOT NULL,

    PRIMARY KEY (mission_id, spaceship_id),

    CONSTRAINT mission_spaceships_mission_fk FOREIGN KEY (mission_id)
        REFERENCES missions(mission_id)
        ON DELETE CASCADE,

    CONSTRAINT mission_spaceships_ship_fk FOREIGN KEY (spaceship_id)
        REFERENCES spaceships(ship_id)
        ON DELETE CASCADE
);

-- 7
CREATE TABLE IF NOT EXISTS mission_astronauts
(
	mission_id INT NOT NULL,
    astronaut_id INT NOT NULL,
    role VARCHAR(50),

    PRIMARY KEY (mission_id, astronaut_id),

    CONSTRAINT mission_astronauts_mission_fk FOREIGN KEY (mission_id)
        REFERENCES missions(mission_id)
        ON DELETE CASCADE,

    CONSTRAINT mission_astronauts_astronaut_fk FOREIGN KEY (astronaut_id)
        REFERENCES astronauts(astronaut_id)
        ON DELETE CASCADE
);
