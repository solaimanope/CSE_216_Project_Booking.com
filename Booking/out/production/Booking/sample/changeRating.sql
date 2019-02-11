create or replace function changeRating()
	returns trigger AS
$BODY$

declare
	prop record;
	prop_id int;
	sumR numeric;
	cnt int;

begin
	prop_id := new.property_id;
	sumR := 0;
	cnt := 0;
	
	for prop in select * from review
	loop
		if prop.property_id = prop_id then
			sumR := sumR+prop.rating;
			cnt := cnt+1;
		end if;
	end loop;
	
	update properties
	set rating = sumR/cnt
	where property_id = prop_id;
 
 	return new;
end;

$BODY$
language 'plpgsql';