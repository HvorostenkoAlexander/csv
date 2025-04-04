update cp_fcb_additional_sources, credit, borrower, personal_data
set cp_fcb_additional_sources.iin=personal_data.passport_iin
where cp_fcb_additional_sources.credit_id = credit.id
  and credit.borrower_id = borrower.id
  and borrower.personal_data_id = personal_data.id