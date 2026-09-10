CREATE TABLE storage_locations (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    type VARCHAR(20) NOT NULL,
    user_id BIGINT NULL,

    CONSTRAINT fk_storage_locations_user
    FOREIGN KEY (user_id)
    REFERENCES users(id)
);