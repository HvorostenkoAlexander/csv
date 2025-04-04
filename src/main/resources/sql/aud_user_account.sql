update aud_user_account aua, user_account ua
set aua.phone=ua.phone
where aua.id = ua.id