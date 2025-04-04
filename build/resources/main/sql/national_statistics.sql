update national_statistics, credit, borrower, personal_data
set national_statistics.iin=personal_data.passport_iin
where national_statistics.credit_id = credit.id
  and credit.borrower_id = borrower.id
  and borrower.personal_data_id = personal_data.id