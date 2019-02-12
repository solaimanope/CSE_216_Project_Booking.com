select user_id, count(*) from room_booking
group by user_id
order by count(*) desc
limit 2