update rbk_fee_debtors, credit, borrower, personal_data
set rbk_fee_debtors.iin=personal_data.passport_iin
where rbk_fee_debtors.credit_id = credit.id
  and credit.borrower_id = borrower.id
  and borrower.personal_data_id = personal_data.id