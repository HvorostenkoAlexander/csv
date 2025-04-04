update user_account ua, (select id
                         from user_account
                         where role_id = 12
                         limit 1) ff
set ua.email='credadmin@solva.kz'
where ua.id = ff.id;