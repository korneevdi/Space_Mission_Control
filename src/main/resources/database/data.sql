-- 1
INSERT INTO missions (name, launch_date, status)
VALUES
    ('Crew-6', '2023-03-02', 'completed'),
    ('Artemis I', '2022-11-16', 'completed'),
    ('JUICE', '2023-04-14', 'en_route');

-- 2
INSERT INTO spaceships (model, manufacturer, capacity, weight_kg)
VALUES
    ('Crew Dragon Endeavour', 'SpaceX', 4, 12519),
    ('Orion MPCV', 'NASA/ESA', 4, 26000),
    ('Ariane 5 ECA', 'Arianespace', 0, 77700);

-- 3
INSERT INTO astronauts (first_name, last_name, rank, birth_date, country)
VALUES
    ('Stephen', 'Bowen', 'Commander', '1964-02-13', 'USA'),
    ('Warren', 'Hoburg', 'Pilot', '1985-09-14', 'USA'),
    ('Sultan', 'Al Neyadi', 'Mission Specialist', '1981-05-23', 'UAE'),
    ('Andrey', 'Fedyaev', 'Mission Specialist', '1981-02-26', 'Russia'),
    ('Koichi', 'Wakata', 'Mission Specialist', '1963-08-01', 'Japan');

-- 4
INSERT INTO equipment (mission_id, name, category, weight_kg)
VALUES
    (1, 'Microgravity Science Glovebox', 'Science', 180),
    (1, 'Radiation Monitoring Sensors', 'Sensors', 12),
    (2, 'Orion Guidance Computer', 'Navigation', 95),
    (3, 'JANUS Camera', 'Science', 23),
    (3, 'Radar for Icy Moons Exploration (RIME)', 'Science', 45);

-- 5
INSERT INTO mission_details (mission_id, budget_million_usd, duration_days, description)
VALUES
    (1, 55, 180, 'Crew-6 ISS expedition'),
    (2, 4200, 25, 'Artemis I uncrewed lunar test flight'),
    (3, 1600, 4380, 'JUICE mission to study Jupiter’s icy moons');

-- 6
INSERT INTO mission_spaceships (mission_id, spaceship_id)
VALUES
    (1, 1),  -- Crew-6 → Crew Dragon
    (2, 2),  -- Artemis I → Orion
    (3, 3);  -- JUICE → Ariane 5

-- 7
INSERT INTO mission_astronauts (mission_id, astronaut_id, role)
VALUES
    (1, 1, 'Commander'),
    (1, 2, 'Pilot'),
    (1, 3, 'Mission Specialist'),
    (1, 4, 'Mission Specialist'),
    (1, 5, 'Mission Specialist');
