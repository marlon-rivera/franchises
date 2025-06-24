CREATE TABLE IF NOT EXISTS franchise (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  name VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS product (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  name VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS branch (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  name VARCHAR(255),
  franchise_id BIGINT,
  FOREIGN KEY (franchise_id) REFERENCES franchise(id)
);

CREATE TABLE IF NOT EXISTS product_branch (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  branch_id BIGINT,
  product_id BIGINT,
  stock INT,
  UNIQUE (branch_id, product_id),
  FOREIGN KEY (branch_id) REFERENCES branch(id),
  FOREIGN KEY (product_id) REFERENCES product(id)
);

INSERT IGNORE INTO franchise (id, name) VALUES
(1, 'Tech Franchise'),
(2, 'Food Franchise'),
(3, 'Fitness Franchise'),
(4, 'Bookstore Franchise');

INSERT IGNORE INTO branch (id, name, franchise_id) VALUES
(1, 'Downtown Tech', 1),
(2, 'Uptown Tech', 1),
(3, 'Airport Food Court', 2),
(4, 'Suburban Food Stop', 2),
(5, 'Central Gym', 3),
(6, 'Westside Fitness', 3),
(7, 'Main Library Shop', 4),
(8, 'Campus Bookstore', 4);

INSERT IGNORE INTO product (id, name) VALUES
(1, 'Smartphone'),
(2, 'Laptop'),
(3, 'Tablet'),
(4, 'Sandwich'),
(5, 'Juice'),
(6, 'Burger'),
(7, 'Protein Powder'),
(8, 'Treadmill'),
(9, 'Yoga Mat'),
(10, 'Mystery Novel'),
(11, 'Science Textbook'),
(12, 'Notebook');

INSERT IGNORE INTO product_branch (id, branch_id, product_id, stock) VALUES
(1, 1, 1, 40),
(2, 1, 2, 25),
(3, 1, 3, 30),
(4, 2, 1, 20),
(5, 2, 2, 50),
(6, 2, 3, 10),
(7, 3, 4, 45),
(8, 3, 5, 60),
(9, 3, 6, 20),
(10, 4, 4, 25),
(11, 4, 5, 15),
(12, 4, 6, 70),
(13, 5, 7, 55),
(14, 5, 8, 5),
(15, 5, 9, 30),
(16, 6, 7, 20),
(17, 6, 8, 10),
(18, 6, 9, 60),
(19, 7, 10, 35),
(20, 7, 11, 20),
(21, 7, 12, 40),
(22, 8, 10, 60),
(23, 8, 11, 15),
(24, 8, 12, 25);
