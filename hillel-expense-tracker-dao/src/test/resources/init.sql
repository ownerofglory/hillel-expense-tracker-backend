--
--
--
-- schema
--
--
--
create table t_user
(
    id int auto_increment primary key,
    first_name varchar(100) default '' null,
    last_name  varchar(100) default '' null,
    email      varchar(256) not null,
    birth_date date default '2000-01-01' not null,
    constraint t_user_email_unique unique (email)
);

create table t_category
(
    id int auto_increment primary key,
    name varchar(100) not null,
    user_id int,
    constraint t_category_user_fk
        foreign key (user_id) references t_user (id)
);

create index t_category_user_id_idx on t_category (user_id);

create table t_expense
(
    id int auto_increment primary key,
    amount double not null,
    description varchar(200),
    category_id int,
    user_id int,
    constraint t_expense_category_fk
        foreign key (category_id) references t_category (id)
            on delete set null,
    constraint t_expense_user_fk
        foreign key (user_id) references t_user (id)
            on delete cascade,
    check (amount > 0)
);

create index t_expense_category_id_idx on t_expense (category_id);
create index t_expense_user_id_idx on t_expense (user_id);

create table t_profile
(
    id int auto_increment primary key,
    avatarUrl varchar(1024),
    user_id int,
    constraint t_profile_user_unique unique (user_id),
    constraint t_profile_user_fk
        foreign key (user_id) references t_user (id)
);

create table t_report
(
    id int auto_increment primary key,
    user_id int,
    constraint t_report_user_fk
        foreign key (user_id) references t_user (id)
);

create index t_report_user_id_idx on t_report (user_id);

create table report_category
(
    id int auto_increment primary key,
    total_amount double,
    report_id int,
    category_id int,
    constraint report_category_report_fk
        foreign key (report_id) references t_report (id),
    constraint report_category_category_fk
        foreign key (category_id) references t_category (id)
);

create index report_category_category_id_idx on report_category (category_id);
create index report_category_report_id_idx on report_category (report_id);

create table t_tag
(
    id int auto_increment primary key,
    name varchar(20) not null,
    user_id int,
    constraint t_tag_user_fk
        foreign key (user_id) references t_user (id)
);

create index t_tag_user_id_idx on t_tag (user_id);

create table t_tag_expense
(
    tag_id int not null,
    expense_id int not null,
    primary key (tag_id, expense_id),
    constraint t_tag_expense_tag_fk
        foreign key (tag_id) references t_tag (id),
    constraint t_tag_expense_expense_fk
        foreign key (expense_id) references t_expense (id)
            on delete cascade
);

create index t_tag_expense_expense_id_idx on t_tag_expense (expense_id);

create index t_user_email_idx on t_user (email);

-- Create view without definer and without database references:
create view category_report_total as
select c.id as category_id, sum(e.amount) as total_amount, 1 as report_id
from t_user u
         join t_category c on c.user_id = u.id
         left join t_expense e on e.category_id = c.id
where u.id = 1
group by c.id;

--
--
--
-- data
--
--
--
INSERT INTO t_user (first_name, last_name, email, birth_date)
VALUES
    ('John', 'Doe', 'john.doe@example.com', '1995-05-15'),
    ('Jane', 'Smith', 'jane.smith@example.com', '1988-11-23'),
    ('Michael', 'Johnson', 'michael.johnson@example.com', '1990-03-30'),
    ('Emily', 'Davis', 'emily.davis@example.com', '1992-07-12'),
    ('David', 'Brown', 'david.brown@example.com', '1985-10-05');

INSERT INTO t_category (name, user_id)
VALUES
    ('Food', 1),
    ('Transportation', 1),
    ('Entertainment', 1),
    ('Health', 1),

    ('Food', 2),
    ('Transportation', 2),
    ('Entertainment', 2),
    ('Health', 2),

    ('Food', 3),
    ('Transportation', 3),
    ('Entertainment', 3),
    ('Health', 3),

    ('Food', 4),
    ('Transportation', 4),
    ('Entertainment', 4),
    ('Health', 4),

    ('Food', 5),
    ('Transportation', 5),
    ('Entertainment', 5),
    ('Health', 5);

INSERT INTO t_expense (amount, description, category_id, user_id) VALUES
                                                                      (25.50, 'Lunch at restaurant', 1, 1), -- Food
                                                                      (15.00, 'Bus fare', 2, 1), -- Transportation
                                                                      (40.00, 'Movie ticket', 3, 1); -- Entertainment

-- Expenses for User 2
INSERT INTO t_expense (amount, description, category_id, user_id) VALUES
                                                                      (50.75, 'Groceries', 5, 2), -- Food
                                                                      (20.00, 'Gas for car', 6, 2), -- Transportation
                                                                      (75.00, 'Concert ticket', 7, 2); -- Entertainment

-- Expenses for User 3
INSERT INTO t_expense (amount, description, category_id, user_id) VALUES
                                                                      (30.00, 'Dinner at cafe', 9, 3), -- Food
                                                                      (10.50, 'Subway ride', 10, 3), -- Transportation
                                                                      (100.00, 'Theater play', 11, 3); -- Entertainment

INSERT INTO t_expense (amount, description, category_id, user_id) VALUES
    (15.50, 'Lunch at cafe', 1, 1);


INSERT INTO t_tag (name, user_id) VALUES
                                      ('Work', 1),
                                      ('Personal', 1),
                                      ('Groceries', 2),
                                      ('Travel', 2),
                                      ('Entertainment', 3),
                                      ('Commute', 3);

INSERT INTO t_tag_expense (tag_id, expense_id) VALUES
                                                   (1, 1), -- Tag Lunch at restaurant with 'Work'
                                                   (2, 1), -- Tag Lunch at restaurant with 'Personal'
                                                   (1, 2), -- Tag Bus fare with 'Work'
                                                   (2, 3), -- Tag Movie ticket with 'Personal'
                                                   (3, 4), -- Tag Groceries with 'Groceries'
                                                   (4, 5), -- Tag Gas for car with 'Travel'
                                                   (4, 6), -- Tag Concert ticket with 'Travel'
                                                   (5, 7), -- Tag Dinner at cafe with 'Entertainment'
                                                   (6, 8), -- Tag Subway ride with 'Commute'
                                                   (5, 9); -- Tag Theater play with 'Entertainment'