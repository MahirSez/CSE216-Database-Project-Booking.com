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
	where RM.ROOM_ID = r_ID and ( ( res.checkin_date >= check_in and check_in <= res.checkout_date ) 
		or ( res.checkout_date >= check_out and check_out <= res.checkout_date ) );
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
