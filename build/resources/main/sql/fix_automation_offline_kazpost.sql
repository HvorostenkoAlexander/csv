update user_account ua, (select id
                         from user_account
                         where role_id = 13 and email not like '%@solva.kz'
                         limit 1) ff
set ua.email='automation_offline_kazpost@solva.kz'
where ua.id = ff.id;