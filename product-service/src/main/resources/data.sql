INSERT INTO categories (name, is_deleted, created_at, updated_at) VALUES
                                                                      ('Toys', false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                      ('Electronics', false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                      ('Books', false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                      ('Clothing', false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
                                                                      ('Home', false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
INSERT INTO products
(title, description, price, image_url, category_id, is_deleted, created_at, updated_at)
VALUES
-- ======================
-- TOYS (category_id = 1)
-- ======================
('Toy Car', 'Toy Car description', 10, 'img/toycar.jpg', 1, false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Action Figure', 'Action Figure description', 15, 'img/action.jpg', 1, false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Building Blocks', 'Building Blocks description', 20, 'img/blocks.jpg', 1, false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Teddy Bear', 'Teddy Bear description', 18, 'img/teddy.jpg', 1, false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Puzzle', 'Puzzle description', 12, 'img/puzzle.jpg', 1, false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Drone Toy', 'Drone Toy description', 45, 'img/drone.jpg', 1, false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Remote Car', 'Remote Car description', 35, 'img/remotecar.jpg', 1, false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Yo-Yo', 'Yo-Yo description', 5, 'img/yoyo.jpg', 1, false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Board Game', 'Board Game description', 25, 'img/boardgame.jpg', 1, false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Doll House', 'Doll House description', 40, 'img/dollhouse.jpg', 1, false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Train Set', 'Train Set description', 55, 'img/train.jpg', 1, false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Lego Set', 'Lego Set description', 60, 'img/lego.jpg', 1, false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Water Gun', 'Water Gun description', 8, 'img/watergun.jpg', 1, false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Kite', 'Kite description', 7, 'img/kite.jpg', 1, false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Robot Toy', 'Robot Toy description', 50, 'img/robot.jpg', 1, false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Marbles', 'Marbles description', 6, 'img/marbles.jpg', 1, false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('RC Helicopter', 'RC Helicopter description', 70, 'img/heli.jpg', 1, false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Toy Piano', 'Toy Piano description', 30, 'img/piano.jpg', 1, false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Stuffed Dog', 'Stuffed Dog description', 22, 'img/dog.jpg', 1, false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Magic Kit', 'Magic Kit description', 28, 'img/magic.jpg', 1, false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

-- =========================
-- ELECTRONICS (category_id = 2)
-- =========================
('MacBook Air', 'MacBook Air description', 350, 'img/macbook.jpg', 2, false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Laptop Pro', 'Laptop Pro description', 550, 'img/laptop.jpg', 2, false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Smartphone', 'Smartphone description', 300, 'img/phone.jpg', 2, false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Tablet', 'Tablet description', 200, 'img/tablet.jpg', 2, false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Wireless Mouse', 'Wireless Mouse description', 25, 'img/mouse.jpg', 2, false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Keyboard', 'Keyboard description', 30, 'img/keyboard.jpg', 2, false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Monitor', 'Monitor description', 180, 'img/monitor.jpg', 2, false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Smart Watch', 'Smart Watch description', 150, 'img/watch.jpg', 2, false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Headphones', 'Headphones description', 90, 'img/headphones.jpg', 2, false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Bluetooth Speaker', 'Speaker description', 70, 'img/speaker.jpg', 2, false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Webcam', 'Webcam description', 60, 'img/webcam.jpg', 2, false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Printer', 'Printer description', 120, 'img/printer.jpg', 2, false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Router', 'Router description', 85, 'img/router.jpg', 2, false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Power Bank', 'Power Bank description', 40, 'img/powerbank.jpg', 2, false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('USB Hub', 'USB Hub description', 20, 'img/usbhub.jpg', 2, false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('SSD Drive', 'SSD description', 110, 'img/ssd.jpg', 2, false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('External HDD', 'HDD description', 95, 'img/hdd.jpg', 2, false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Microphone', 'Microphone description', 75, 'img/mic.jpg', 2, false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Camera', 'Camera description', 400, 'img/camera.jpg', 2, false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('VR Headset', 'VR description', 250, 'img/vr.jpg', 2, false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

-- ======================
-- BOOKS (category_id = 3)
-- ======================
('Java Basics', 'Java Basics description', 35, 'img/java.jpg', 3, false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Spring Boot', 'Spring Boot description', 45, 'img/spring.jpg', 3, false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Clean Code', 'Clean Code description', 40, 'img/cleancode.jpg', 3, false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Algorithms', 'Algorithms description', 50, 'img/algo.jpg', 3, false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Data Structures', 'Data Structures description', 48, 'img/ds.jpg', 3, false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Design Patterns', 'Design Patterns description', 55, 'img/design.jpg', 3, false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Microservices', 'Microservices description', 52, 'img/micro.jpg', 3, false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Docker', 'Docker description', 38, 'img/docker.jpg', 3, false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Kubernetes', 'Kubernetes description', 60, 'img/k8s.jpg', 3, false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('SQL Guide', 'SQL description', 30, 'img/sql.jpg', 3, false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('NoSQL', 'NoSQL description', 33, 'img/nosql.jpg', 3, false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('REST APIs', 'REST description', 36, 'img/rest.jpg', 3, false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('System Design', 'System Design description', 58, 'img/system.jpg', 3, false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Cloud Basics', 'Cloud description', 42, 'img/cloud.jpg', 3, false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Linux', 'Linux description', 37, 'img/linux.jpg', 3, false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Git', 'Git description', 25, 'img/git.jpg', 3, false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('DevOps', 'DevOps description', 49, 'img/devops.jpg', 3, false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Python', 'Python description', 32, 'img/python.jpg', 3, false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('AI Intro', 'AI description', 65, 'img/ai.jpg', 3, false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('ML Basics', 'ML description', 68, 'img/ml.jpg', 3, false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

-- ======================
-- CLOTHING (category_id = 4)
-- ======================
('T-Shirt', 'T-Shirt description', 20, 'img/tshirt.jpg', 4, false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Jeans', 'Jeans description', 45, 'img/jeans.jpg', 4, false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Jacket', 'Jacket description', 80, 'img/jacket.jpg', 4, false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Sneakers', 'Sneakers description', 70, 'img/sneakers.jpg', 4, false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Cap', 'Cap description', 15, 'img/cap.jpg', 4, false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Sweater', 'Sweater description', 55, 'img/sweater.jpg', 4, false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Shorts', 'Shorts description', 25, 'img/shorts.jpg', 4, false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Socks', 'Socks description', 10, 'img/socks.jpg', 4, false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Belt', 'Belt description', 18, 'img/belt.jpg', 4, false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Coat', 'Coat description', 120, 'img/coat.jpg', 4, false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Scarf', 'Scarf description', 22, 'img/scarf.jpg', 4, false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Gloves', 'Gloves description', 30, 'img/gloves.jpg', 4, false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Hoodie', 'Hoodie description', 60, 'img/hoodie.jpg', 4, false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Dress', 'Dress description', 75, 'img/dress.jpg', 4, false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Suit', 'Suit description', 200, 'img/suit.jpg', 4, false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Pajamas', 'Pajamas description', 40, 'img/pajamas.jpg', 4, false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Skirt', 'Skirt description', 35, 'img/skirt.jpg', 4, false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Blouse', 'Blouse description', 42, 'img/blouse.jpg', 4, false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Boots', 'Boots description', 90, 'img/boots.jpg', 4, false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Sandals', 'Sandals description', 50, 'img/sandals.jpg', 4, false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),

-- ======================
-- HOME (category_id = 5)
-- ======================
('Chair', 'Chair description', 60, 'img/chair.jpg', 5, false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Table', 'Table description', 150, 'img/table.jpg', 5, false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Sofa', 'Sofa description', 500, 'img/sofa.jpg', 5, false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Lamp', 'Lamp description', 40, 'img/lamp.jpg', 5, false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Bed', 'Bed description', 700, 'img/bed.jpg', 5, false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Bookshelf', 'Bookshelf description', 120, 'img/bookshelf.jpg', 5, false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Carpet', 'Carpet description', 200, 'img/carpet.jpg', 5, false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Curtains', 'Curtains description', 80, 'img/curtains.jpg', 5, false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Mirror', 'Mirror description', 90, 'img/mirror.jpg', 5, false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Clock', 'Clock description', 35, 'img/clock.jpg', 5, false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Vase', 'Vase description', 30, 'img/vase.jpg', 5, false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Cushion', 'Cushion description', 25, 'img/cushion.jpg', 5, false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Dining Set', 'Dining Set description', 650, 'img/dining.jpg', 5, false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Wardrobe', 'Wardrobe description', 800, 'img/wardrobe.jpg', 5, false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Coffee Table', 'Coffee Table description', 180, 'img/coffeetable.jpg', 5, false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('TV Stand', 'TV Stand description', 220, 'img/tvstand.jpg', 5, false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Shelf', 'Shelf description', 110, 'img/shelf.jpg', 5, false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Laundry Basket', 'Laundry Basket description', 45, 'img/basket.jpg', 5, false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Door Mat', 'Door Mat description', 20, 'img/mat.jpg', 5, false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP),
('Wall Art', 'Wall Art description', 75, 'img/art.jpg', 5, false, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP);
