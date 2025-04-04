update fcb_additional_sources, credit, borrower, personal_data
set fcb_additional_sources.iin=personal_data.passport_iin
where fcb_additional_sources.credit_id = credit.id
  and credit.borrower_id = borrower.id
  and borrower.personal_data_id = personal_data.id