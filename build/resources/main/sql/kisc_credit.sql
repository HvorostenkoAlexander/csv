update kisc_credit, credit, borrower, personal_data
set kisc_credit.iin=personal_data.passport_iin
where kisc_credit.credit_id = credit.id
  and credit.borrower_id = borrower.id
  and borrower.personal_data_id = personal_data.id