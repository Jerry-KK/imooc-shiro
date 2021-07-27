create table test_user(
                          id bigint auto_increment,
                          name varchar(100),
                          password varchar(100),
                          PRIMARY key (id)
)

insert into test_user(name,password) VALUES('lethe','123456')


select password from test_user where name = 'lethe'

CREATE TABLE test_user_role
(
    user_name VARCHAR(64),
    role_name VARCHAR(32),
    PRIMARY KEY(user_name,role_name)
);

INSERT INTO test_user_role VALUES ('lethe','admin');