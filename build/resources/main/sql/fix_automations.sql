update user_account ua, (select id
                         from user_account
                         where role_id = 60
                         limit 1) ff
set ua.email='automationslkzkol@test.ru'
where ua.id = ff.id;