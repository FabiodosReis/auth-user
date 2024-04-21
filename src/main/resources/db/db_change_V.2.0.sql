-- liquibase formatted SQL
-- changeset fabio.silva:1 endDelimiter:;


INSERT INTO role(id, name)
VALUES("018ebae3-1802-7d10-97ba-385fda6bd7fc","ROLE_USER");

INSERT INTO role(id, name)
VALUES("cf5345b5-2aaf-49eb-823d-66e5431c2f9b","ROLE_ADMIN");

INSERT INTO user(
    id,
    name,
    email,
    password,
    createDate,
    enable
)
VALUES(
    "1736696d-96b8-4d2d-9eef-45ed33032d01",
    "Fabio Reis",
    "fabio_dos_reis@outlook.com",
    "$2a$10$9WNOcCoCljqZ7CYSHrRtqej4Kj/mOyFwkjmcz.wYCpk92xSFaW.SK",
    "2024-04-14",
    true
);

INSERT INTO user(
    id,
    name,
    email,
    password,
    createDate,
    enable
)
VALUES(
    "51da68c4-9a7b-4fac-8b09-3c608a355d34",
    "Naiane Oliveira",
    "nai.oliver@outlook.com",
    "$2a$10$9WNOcCoCljqZ7CYSHrRtqej4Kj/mOyFwkjmcz.wYCpk92xSFaW.SK",
    "2024-04-14",
    true
);


INSERT INTO user_roles(roleId, userId) VALUES("cf5345b5-2aaf-49eb-823d-66e5431c2f9b", "1736696d-96b8-4d2d-9eef-45ed33032d01");

INSERT INTO user_roles(roleId, userId) VALUES("018ebae3-1802-7d10-97ba-385fda6bd7fc", "51da68c4-9a7b-4fac-8b09-3c608a355d34");