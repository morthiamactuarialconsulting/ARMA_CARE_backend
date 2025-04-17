-- Création de la table Professional

CREATE TABLE professionals (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    first_name VARCHAR(255) NOT NULL,
    last_name VARCHAR(255) NOT NULL,
    speciality VARCHAR(255) NOT NULL,
    registration_number VARCHAR(255) NOT NULL UNIQUE,
    email VARCHAR(255) UNIQUE,
    phone VARCHAR(255) NOT NULL,
    address VARCHAR(255) NOT NULL,
    city VARCHAR(255) NOT NULL,
    country VARCHAR(255) DEFAULT 'Sénégal',
    account_status VARCHAR(255) NOT NULL,
    status_change_date TIMESTAMP,
    status_change_reason VARCHAR(255),
    identity_document_path VARCHAR(255),
    diploma_path VARCHAR(255),
    license_path VARCHAR(255),
    professional_insurance_path VARCHAR(255),
    bank_account_number_path VARCHAR(255) UNIQUE
);


INSERT INTO professionals (
    first_name, last_name, speciality, 
    registration_number, email, phone, 
    address, city, country, 
    account_status, status_change_date, status_change_reason, 
    identity_document_path, diploma_path, license_path, 
    professional_insurance_path, bank_account_number_path
) VALUES (
    'Saliou', 'Diop', 'Medecin Chirurgien', 
    '123456789', 'saliou.diop@example.com', '+221772345678', 
    '123 Keur Massar', 'Dakar', 'Sénégal', 
    'ACTIVE', TIMESTAMP '2023-01-01 16:00:00', 'Documents validés', 
    'path/to/identity/document1', 'path/to/diploma1', 'path/to/license1',
    'path/to/professional/insurance1', 'path/to/bank/account/number1'
);

INSERT INTO professionals (
    first_name, last_name, speciality, 
    registration_number, email, phone, 
    address, city, country, 
    account_status, status_change_date, status_change_reason, 
    identity_document_path, diploma_path, license_path, 
    professional_insurance_path, bank_account_number_path
) VALUES (
    'Fatima', 'Diop', 'Medecin gynécologiste', 
    '7773456789', 'fatima.diop@example.com', '762345679', 
    '12 Thiaroye', 'Dakar', 'Sénégal', 
    'PENDING_ACTIVATION', TIMESTAMP '2023-02-01 16:01:00', 'Documents vérifiés et en attente d''activation', 
    'path/to/identity/document2', 'path/to/diploma2', 'path/to/license2',
    'path/to/professional/insurance2', 'path/to/bank/account/number2'
);
INSERT INTO professionals (
    first_name, last_name, speciality, 
    registration_number, email, phone, 
    address, city, country, 
    account_status, status_change_date, status_change_reason, 
    identity_document_path, diploma_path, license_path, 
    professional_insurance_path, bank_account_number_path
) VALUES (
    'Saidou', 'Ly', 'Medecin pédiatride', 
    '99123456789', 'saidou.ly@example.com', '702345699', 
    '14 Gareba', 'Thiès', 'Sénégal', 
    'PENDING_VERIFICATION', TIMESTAMP '2023-03-01 16:02:00', 'Documents en attente de vérification', 
    'path/to/identity/document3', 'path/to/diploma3', 'path/to/license3',
    'path/to/professional/insurance3', 'path/to/bank/account/number3'
);
