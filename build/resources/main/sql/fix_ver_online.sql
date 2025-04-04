update user_account ua, (select id
                         from user_account
                         where role_id = 70
                         limit 1) ff
set ua.email='ver_online@solva.kz'
where ua.id = ff.id;