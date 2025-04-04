update fcb_kdn, credit, borrower, personal_data
set fcb_kdn.first_name=personal_data.first_name,
    fcb_kdn.iin=personal_data.passport_iin,
    fcb_kdn.last_name=personal_data.last_name,
    fcb_kdn.middle_name=personal_data.patronymic
where fcb_kdn.credit_id = credit.id
  and credit.borrower_id = borrower.id
  and borrower.personal_data_id = personal_data.id