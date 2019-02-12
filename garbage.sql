---date :
---alter table room 
ALTER table rooms 
DROP column capacity,
DROP column facilities,
DROP column price;

---create room type table

create table Room_type_table(
	room_type TEXT,
	capacity integer,
	price integer
);


create or replace FUNCTION get_room_collide_with_how_many_reservation(r_ID integer, check_in date, check_out date)
	returns integer AS $$
DECLARE 
       ret integer;
begin 
	select count(*) into ret
	from ROOM_RESERVE RM join reservations res 
	ON(RM.RESERVATION_ID = res.reservation_id )
	where RM.ROOM_ID = r_ID and ( ( res.checkin_date <= check_in and check_in <= res.checkout_date ) 
		or ( res.checkout_date <= check_out and check_out <= res.checkout_date ) );

	return ret;

end;
$$ LANGUAGE PLpgSQL;


-- input = city_name, price range, persons 
-- output = a table of listed hotels in the city sorted by their rating in descending order


create or replace FUNCTION total_free_rooms_capacity(h_id integer,check_in date, check_out date,price_min integer, price_max integer)
returns integer AS $$

DECLARE 
	tot integer;
	temp_cap integer;
	rec rooms%rowtype;
BEGIN
	tot := 0;
	for rec IN (select * from rooms R
	where R.hotel_ID = h_id and get_room_collide_with_how_many_reservation(R.room_ID, check_in, check_in) = 0 )
	LOOP
	select capacity into temp_cap
	from Room_type_table rtt
	where rtt.room_type = rec.room_type;
	
	if temp_cap >= price_min and temp_cap <= price_max then
		tot := tot + temp_cap;
	end if;

	end LOOP;

	return tot;
END;
$$ LANGUAGE plpgsql;


CREATE OR REPLACE FUNCTION hotel_search(city_name text,price_max integer, price_min integer,check_in date, check_out date, persons integer)
RETURNS TABLE(
	Hotel_ID integer,
	HOTEL_NAME TEXT,
	RATING NUMERIC
) AS $$
DECLARE
	C_ID INTEGER;
BEGIN
	C_ID = get_city_id(city_name);
	RETURN QUERY 
	SELECT h.hotel_id , h.hotel_name , h.rating
	FROM property p 
	JOIN hotel h
	ON(p.city_id = c_id AND h.property_id = p.property_id ) 
	where total_free_rooms_capacity(h.hotel_ID, check_in, check_out) >= persons 
	ORDER BY h.rating desc;
END;
$$ LANGUAGE plpgsql;




---
CREATE OR REPLACE FUNCTION GET_ALL_Candidate_ROOMS_OF_HOTEL(H_ID INTEGER,price_min integer, price_max integer,check_in Date,check_out Date, persons integer)	
RETURNS TABLE(
	Q_ROOM_TYPE TEXT,
	price INTEGER,
	capacity integer,
	faci TEXT,
	how_many integer
	) AS $$

DECLARE 

BEGIN 
	RETURN QUERY 
	
	select RR.room_type, RR.price, RR.capacity, RR.facilities, cast (count(*) AS INTEGER)

	from room_type_table RR join rooms R 
	ON(RR.room_type = R.room_type)
	where R.hotel_ID = H_ID and RR.price >= price_min and RR.price <= price_max and get_room_collide_with_how_many_reservation(R.room_ID, check_in, check_out) = 0 
	group by RR.room_type, RR.price, RR.capacity, RR.facilities;

	/* select R.room_type, cast ( count(*) AS INTEGER ) 
	from rooms R 
	where 0 = 
 	(select count(*)
		from ROOM_RESERVE RR join RESERVATIONS res 
		ON (RR.RESERVATION_ID = res.RESERVATION_ID)
		where (RR.ROOM_ID = R.room_ID) and ( ( res.checkin_date >= check_in and check_in <= res.checkout_date ) 
		or ( res.checkout_date >= check_out and check_out <= res.checkout_date ) )
	)
	group by R.room_type; */
 	
END;
$$ LANGUAGE PLpgSQL;




create or replace FUNCTION checker_get_room_collide_with_how_many_reservation(r_ID integer, check_in date, check_out date)
	returns TABLE(
        res_id integer,
        in_date date,
        out_date date
    ) AS $$
DECLARE 
       ret integer;
begin 
    return query

	select res.reservation_id, res.checkin_date, res.checkout_date 
	from ROOM_RESERVE RM join reservations res 
	ON(RM.RESERVATION_ID = res.reservation_id )
	where RM.ROOM_ID = r_ID and ( ( res.checkin_date >= check_in and check_in <= res.checkout_date ) 
		or ( res.checkout_date >= check_out and check_out <= res.checkout_date ) );
        
	

end;
$$ LANGUAGE PLpgSQL;








create or replace FUNCTION reservation_insert
(hid integer,checkin date, checkout date,
	c_id integer,prc integer)
returns integer AS $$

DECLARE
	cur_id integer;
BEGIN
	INSERT INTO public.reservations
		(
		hotel_id, checkin_date, checkout_date, client_id,
		price)
	VALUES
		( hid, checkin, checkout, c_id,
			prc)
	returning RESERVATION_ID into cur_id;

return cur_id;

END;
$$ LANGUAGE PLpgSQL;



create or replace FUNCTION room_reserve_room_id_insert(res_id integer,r_type text,how_many integer,check_in date, check_out date,h_id integer)
	returns integer AS $$
DECLARE
	counter integer ;
	rec rooms%rowtype;
	rid integer;
BEGIN
	counter := 0;
	rid := 0;
	for rec IN 
 	select * from rooms 	
	LOOP 
	
	if counter < how_many and rec.room_type = r_type and rec.hotel_id = h_id and  get_room_collide_with_how_many_reservation( rec.ROOM_ID,check_in,check_out) = 0 then
		INSERT INTO public.room_reserve(room_id, reservation_id) values(rec.room_ID, res_id); 
			counter := counter + 1;
			rid := rid + 1;
	end if;
	END LOOP;
	return rid;

END;
$$ LANGUAGE PLpgSQL;	


---
INSERT INTO public.rooms(room_id, hotel_id, room_type) VALUES(DEFAULT, 3,'king');
INSERT INTO public.rooms(room_id, hotel_id, room_type) VALUES(DEFAULT, 3,'twin');
INSERT INTO public.rooms(room_id, hotel_id, room_type) VALUES(DEFAULT, 3,'economy');
INSERT INTO public.rooms(room_id, hotel_id, room_type) VALUES(DEFAULT, 3,'king');
INSERT INTO public.rooms(room_id, hotel_id, room_type) VALUES(DEFAULT, 3,'double bed');
INSERT INTO public.rooms(room_id, hotel_id, room_type) VALUES(DEFAULT, 3,'king');
INSERT INTO public.rooms(room_id, hotel_id, room_type) VALUES(DEFAULT, 3,'economy');
INSERT INTO public.rooms(room_id, hotel_id, room_type) VALUES(DEFAULT, 3,'twin');
INSERT INTO public.rooms(room_id, hotel_id, room_type) VALUES(DEFAULT, 3,'twin');
INSERT INTO public.rooms(room_id, hotel_id, room_type) VALUES(DEFAULT, 3,'twin');
INSERT INTO public.rooms(room_id, hotel_id, room_type) VALUES(DEFAULT, 3,'double bed');
INSERT INTO public.rooms(room_id, hotel_id, room_type) VALUES(DEFAULT, 3,'double bed');
INSERT INTO public.rooms(room_id, hotel_id, room_type) VALUES(DEFAULT, 3,'double bed');
INSERT INTO public.rooms(room_id, hotel_id, room_type) VALUES(DEFAULT, 3,'king');





create or replace FUNCTION get_hotel_name(h_id integer)
RETURNS text AS $$
DECLARE 
	name text;
BEGIN
	select hotel_name into name 
	from hotel 
	where hotel_id = h_id;
	return name;
END;
$$ LANGUAGE PLpgSQL;

---
create or replace FUNCTION get_reservations_of_a_client(c_id integer)
RETURNS TABLE
(
	checkin_date date,
	checkout_date date,
	hotel_name text,
	total_cost integer
) AS $$
DECLARE

BEGIN
	RETURN QUERY
	select res.checkin_date, res.checkout_date, get_hotel_name(res.hotel_id), CAST (SUM(RTT.price) AS integer)
	from reservations res join room_reserve RR ON(res.reservation_id = RR.reservation_id)
		join rooms R ON(RR.room_id = R.room_id)
		join room_type_table RTT ON(RTT.room_type = R.room_type)
	where res.client_id = c_id
	GROUP BY res.reservation_id, res.checkin_date, res.checkout_date;

END;
$$ LANGUAGE PLpgSQL;