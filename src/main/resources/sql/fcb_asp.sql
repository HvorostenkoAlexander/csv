update fcb_asp, credit, borrower, personal_data
set fcb_asp.birth_date=personal_data.birthday,
    fcb_asp.firstname=personal_data.first_name,
    fcb_asp.iin=personal_data.passport_iin,
    fcb_asp.secondname=personal_data.last_name,
    fcb_asp.surname=personal_data.patronymic,
    fcb_asp.sex=personal_data.sex
where fcb_asp.credit_id = credit.id
  and credit.borrower_id = borrower.id
  and borrower.personal_data_id = personal_data.id