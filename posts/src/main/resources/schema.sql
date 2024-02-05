CREATE TABLE IF NOT EXISTS Post (
    id INT NOT NULL,
    userId INT NOT NULL,
    title VARCHAR(250) NOT NULL,
    body TEXT NOT NULL,
    version INT,
    PRIMARY KEY (id)
)