CREATE TABLE IF NOT EXISTS apartments (
                                          id SERIAL PRIMARY KEY,
                                          price NUMERIC(19, 2) NOT NULL,
    status VARCHAR(20) NOT NULL,
    client_name VARCHAR(255)
    );