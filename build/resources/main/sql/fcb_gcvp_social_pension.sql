update fcb_gcvp_social_pension, credit, borrower, personal_data
set fcb_gcvp_social_pension.request_iin=personal_data.passport_iin
where fcb_gcvp_social_pension.credit_id = credit.id
  and credit.borrower_id = borrower.id
  and borrower.personal_data_id = personal_data.id