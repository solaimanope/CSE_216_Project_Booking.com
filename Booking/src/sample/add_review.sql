create trigger add_review
  after insert
  on review
  for each row
  execute procedure changeRating();