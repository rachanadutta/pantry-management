ALTER TABLE pantry_items
ADD COLUMN storage_location_id BIGINT NULL;

ALTER TABLE pantry_items
ADD CONSTRAINT fk_pantry_items_storage_location
FOREIGN KEY (storage_location_id)
REFERENCES storage_locations(id);