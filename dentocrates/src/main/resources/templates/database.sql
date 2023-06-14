INSERT INTO customer (email, first_name, last_name, password, role)
VALUES ('email@email.com','QWERTY','TY','12345678','CUSTOMER'),
       ('a@a.com','a','a','12345678','CUSTOMER');

INSERT INTO dentist (email, first_name, last_name, password, role, operating_licence_no)
VALUES ('e@e.com','Dr.QWERTY','TY','12345678','DENTIST','l1ip2jp3'),
       ('em@em.com','Dr.a','Dr.TY','12345678','DENTIST','qeq42342f');

INSERT INTO clinic (clinic_type, contact_number, name, opening_hours, street, dentist_in_contract, location_id)
VALUES ('COMMUNITY_DENTAL_CLINIC','+36 30 1111 1111','ToothFairy','7-14','LaViva',3,1),
       ('PRIVATE_DENTAL_CLINIC','+36 20 1111 1111','Toothel','9-15','Egru',3,2);

INSERT INTO location (zip_code, city) VALUES
                                          (1011, 'Budapest I.'),
                                          (1012, 'Budapest I.'),
                                          (1013, 'Budapest I.'),
                                          (1014, 'Budapest I.'),
                                          (1015, 'Budapest I.'),
                                          (1016, 'Budapest I.'),
                                          (1021, 'Budapest II.'),
                                          (1022, 'Budapest II.'),
                                          (1023, 'Budapest II.'),
                                          (1024, 'Budapest II.'),
                                          (1025, 'Budapest II.'),
                                          (1026, 'Budapest II.'),
                                          (1027, 'Budapest II.'),
                                          (1028, 'Budapest II.'),
                                          (1029, 'Budapest II.'),
                                          (1031, 'Budapest III.'),
                                          (1032, 'Budapest III.'),
                                          (1033, 'Budapest III.'),
                                          (1034, 'Budapest III.'),
                                          (1035, 'Budapest III.'),
                                          (1036, 'Budapest III.'),
                                          (1037, 'Budapest III.'),
                                          (1038, 'Budapest III.'),
                                          (1039, 'Budapest III.'),
                                          (1041, 'Budapest IV.'),
                                          (1042, 'Budapest IV.'),
                                          (1043, 'Budapest IV.'),
                                          (1044, 'Budapest IV.'),
                                          (1045, 'Budapest IV.'),
                                          (1046, 'Budapest IV.'),
                                          (1047, 'Budapest IV.'),
                                          (1048, 'Budapest IV.'),
                                          (1051, 'Budapest V.'),
                                          (1052, 'Budapest V.');
