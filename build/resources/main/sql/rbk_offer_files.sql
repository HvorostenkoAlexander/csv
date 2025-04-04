update rbk_offer_files, credit, borrower, personal_data
set rbk_offer_files.iin=personal_data.passport_iin
where rbk_offer_files.credit_id = credit.id
  and credit.borrower_id = borrower.id
  and borrower.personal_data_id = personal_data.id