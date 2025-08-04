INSERT IGNORE INTO roles (id, name) VALUES (1, "ADMIN");
INSERT IGNORE INTO roles (id, name) VALUES (2, "USER");

INSERT IGNORE INTO users (id, password, username, role_id) VALUES (1, "$2a$10$TAlpD.r/V881O7lQQPuvS.yDnxgcxLt0KUBJO.1IiTPM5VJobJkMW", "admin", 1);

INSERT IGNORE INTO apartments (id, area, code, description, floor, number_of_rooms) VALUES (1, 102, "A-101", "Test Description 1", 1, 4);
INSERT IGNORE INTO apartments (id, area, code, description, floor, number_of_rooms) VALUES (2, 102, "A-102", "Test Description 2", 1, 4);

INSERT IGNORE INTO residents (id, date_of_birth, email, full_name, gender, identity_number, is_active, is_representative, phone_number, apartment_id)
       VALUES (1, "2003-12-09", "duybinhh03@gmail.com", "Bui Duy Binh", "MALE",  "1234567", 1, 1, "0337297710", 1);

INSERT IGNORE INTO monthly_bills (id, billing_month, due_date, electricity_fee, electricity_reading, water_fee, water_reading, other_fee, total_amount, is_paid, apartment_id)
       VALUES (1, "2025-08-01", "2025-08-11", 200000.00, 100, 100000.00, 4, 50000.00, 350000.00, 0, 1);

