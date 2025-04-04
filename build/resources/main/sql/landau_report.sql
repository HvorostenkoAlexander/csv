update landau_report, credit, borrower, personal_data
set landau_report.iin=personal_data.passport_iin
where landau_report.credit_id = credit.id
  and credit.borrower_id = borrower.id
  and borrower.personal_data_id = personal_data.id