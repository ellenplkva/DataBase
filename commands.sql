CREATE PROCEDURE createTable1() 
LANGUAGE SQL
AS $$
CREATE TABLE IF NOT EXISTS providers (
name VARCHAR(80) primary key not null,
site VARCHAR(80) not null,
tel_num int not null);
$$;

CALL createTable1();


CREATE PROCEDURE createTable2() 
LANGUAGE SQL
AS $$
CREATE TABLE IF NOT EXISTS shop (
id integer primary key not null generated always as identity,
model VARCHAR(80) not null,
price int not null,
lastUpdate timestamptz default current_timestamp not null,
producer VARCHAR(80) not null,
foreign key (producer) references producers(name) ON DELETE CASCADE ON UPDATE CASCADE); 
create index if not exists model on shop(model);

create or replace function update_time()
returns trigger as $u$
begin
new.lastUpdate = current_timestamp;
return new;
end;
$u$ language plpgsql;
drop trigger if exists trigger_update on shop;
create trigger trigger_update before update on shop for row execute procedure update_time();
$$;

CALL createTable2();

CREATE OR REPLACE FUNCTION find(a VARCHAR(80))
RETURNS TABLE(
id integer,model VARCHAR(80),
			  price int,
			lastUpdate timestamptz,
			  producer VARCHAR(80))
AS
$func$
    BEGIN
        RETURN QUERY
        SELECT * FROM shop c WHERE c.model = a;
    END
$func$
LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION add1(
   
			  model_ VARCHAR(80),
			  price_ integer,
			  producer_ VARCHAR(80))
RETURNS void AS
$$
    BEGIN
        INSERT INTO shop(model, price, producer) VALUES(model_, price_, producer_) ON CONFLICT DO NOTHING;
    END
$$
LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION delete1()
RETURNS void AS
$$
    BEGIN
        DELETE FROM shop;
    END
$$
LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION delete2()
RETURNS void AS
$$
    BEGIN
        DELETE FROM producers;
    END
$$
LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION add2(
   
			  name_ VARCHAR(80),
			  site_ VARCHAR(80),
			  tel_num_ integer)
RETURNS void AS
$$
    BEGIN
        INSERT INTO producers(name, site, tel_num) VALUES(name_, site_, tel_num_) ON CONFLICT DO NOTHING;
    END
$$
LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION clear_database() RETURNS void AS
$$
	BEGIN
		truncate shop;
		truncate producers CASCADE;
	END
$$
LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION deletebyindex(model_ VARCHAR(80)) RETURNS void AS
$$
	BEGIN
		DELETE FROM shop WHERE model=model_;
	END
$$
LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION deletebypk1(id_ integer) RETURNS void AS
$$
	BEGIN
		DELETE FROM shop WHERE id=id_;
	END
$$
LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION deletebypk2(name_ VARCHAR(80)) RETURNS void AS
$$
	BEGIN
		DELETE FROM producers WHERE name=name_;
	END
$$
LANGUAGE plpgsql;


CREATE OR REPLACE FUNCTION updatebypk1(id_ integer, model_ VARCHAR(80), price_ integer, producer_ VARCHAR(80)) RETURNS void AS
$$
	BEGIN
		UPDATE shop
		SET model = model_, price=price_, producer=producer_
		WHERE id=id_;
	END
$$
LANGUAGE plpgsql;

CREATE OR REPLACE FUNCTION updatebypk2(name_ VARCHAR(80), site_ VARCHAR(80), tel_num_ integer) RETURNS void AS
$$
	BEGIN
		UPDATE producers
		SET site = site_, tel_num = tel_num_
		WHERE name=name_;
	END
$$
LANGUAGE plpgsql;
