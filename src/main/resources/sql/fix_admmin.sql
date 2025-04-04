update user_account ua, (select id
                         from user_account
                         where role_id = 10
                         limit 1) ff
set login='admmin', password='5QKMJByHhlGa5rFqBlaPK9RjuoO358M8dMD0Uc7elqk='
where ua.id = ff.id;