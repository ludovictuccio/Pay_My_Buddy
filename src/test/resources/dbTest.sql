USE pmb_database_test;

INSERT INTO user 
VALUES -- (id, lastname, firstname, email, password, phone)
(1, "Trump", "Donald", "donald.trump@gmail.com", "love-usa", "000-000-000"),
(2, "Macron", "Emmanuel", "manu.macron@gmail.com", "love-france", "111-111-111"),
(3, "Lady", "Gaga", "lady.gaga@gmail.com", "love-music", "222-222-222"),
(4, "Poutine", "Vladimir", "vlad.poutine@gmail.com", "love-russia", "333-333-333"),
(5, "Jong-un", "Kim", "kim.jong@gmail.com", "love-corea", "444-444-444");

INSERT INTO app_account 
VALUES -- (id, user_id, balance)
(1, 1, 500),
(2, 2, 1000),
(3, 3, 1500),
(4, 4, 2000),
(5, 5, 10);

INSERT INTO personal_payment (app_account_id, amount, cb_number, cb_expiration_date_month, cb_expiration_date_year, cb_security_key)
VALUES -- (id, app_account_id, amount, cb_number, cb_expiration_date_month, cb_expiration_date_year, cb_security_key)
(1, 15, "0000567891012134", "09","22", "789"),
(2, 30, "1111567891012134", "01","20", "246"),
(3, 100.50, "2222567891012134", "03","21", "762"),
(4, 99999.99, "3333567891012134", "05","22", "999"),
(5, 10000, "4444567891012134", "03","23", "000");

INSERT INTO personal_transfer
VALUES -- (id, app_account_id, amount, iban, bic)
(1, 1, 1, "FR01010101010101010101010101FR", "BIC010101"),
(2, 2, 10, "FR020202020202020202FR", "BIC020202"),
(3, 3, 44, "FR0303030303030303003030303FR", "BIC030303"),
(4, 4, 4444.44, "FR040404040404040404FR", "BIC040404"),
(5, 5, 700, "FR050505050505050550505051FR", "BIC050505");


INSERT INTO relation (user_to_connect_id, user_id)
VALUES -- (id, user_to_connect_id, user_id)
(1, 5),
(2, 4),
(3, 2),
(4, 2),
(5, 1);

