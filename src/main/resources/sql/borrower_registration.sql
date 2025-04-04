update borrower_registration br,
    (select row_number() over () ind, id from borrower_registration) br_tmp,
    (select row_number() over () ind, ua.phone
    from user_account ua
    left join borrower b on ua.id = b.user_account_id
    where ua.role_id = 100
    and b.user_account_id is null) ua
set br.mobile_number=ua.phone
where br.id = br_tmp.id
  and br_tmp.ind = ua.ind;