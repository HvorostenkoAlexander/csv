update fcb_gcvp, credit, borrower, personal_data
set fcb_gcvp.request_iin=personal_data.passport_iin
where fcb_gcvp.credit_id = credit.id
  and credit.borrower_id = borrower.id
  and borrower.personal_data_id = personal_data.id