update refinancing_param, credit, borrower, personal_data
set refinancing_param.iin=personal_data.passport_iin
where refinancing_param.credit_id = credit.id
  and credit.borrower_id = borrower.id
  and borrower.personal_data_id = personal_data.id