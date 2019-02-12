select P.property_name Hotel, (	select Q.property_name
								from properties Q
							  	where Q.property_type = 2 and Q.city_id=P.city_id
							 ) Car_Company
from properties P 
where P.property_type=1 and P.city_id=1;