CREATE TABLE booking(
    id INT NOT NULL AUTO_INCREMENT,
    state VARCHAR(50),
    client INT,
    total DOUBLE PRECISION,
    start_date TIMESTAMP,
    end_date TIMESTAMP,
    creation_date TIMESTAMP,
    PRIMARY KEY (id)
);

INSERT INTO booking( state, client, total, start_date, end_date, creation_date ) VALUES ( 'CREATED', 1, 100.50, '2024-03-31 11:00:00', '2024-03-31 12:00:00', CURRENT_TIMESTAMP )
