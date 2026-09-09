ALTER TABLE pantry_items
ADD COLUMN category_id BIGINT NULL;

ALTER TABLE pantry_items
ADD CONSTRAINT fk_pantry_items_category
FOREIGN KEY (category_id)
REFERENCES categories(id);