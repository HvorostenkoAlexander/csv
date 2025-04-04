update user_account ua, (select id
                         from user_account
                         where role_id = 71
                         limit 1) ff
set ua.email='ver_llp@solva.kz'
where ua.id = ff.id;