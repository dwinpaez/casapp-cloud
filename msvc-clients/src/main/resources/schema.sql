CREATE TABLE client(
    id INT AUTO_INCREMENT,
    email VARCHAR(255) NOT NULL UNIQUE,
    phone VARCHAR(100) NOT NULL UNIQUE,
    first_name VARCHAR(255) NOT NULL,
    last_name VARCHAR(255) NOT NULL,
    date_of_birth DATE
);

 INSERT INTO client( email, phone, first_name, last_name, date_of_birth ) VALUES ( 'dwinpaez@gmail.com', '4505028511', 'Dwin', 'Paez', '1985-11-02' )