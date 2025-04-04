update rbk_monthly_loan_repayment, credit, borrower, personal_data
set rbk_monthly_loan_repayment.iin=personal_data.passport_iin
where rbk_monthly_loan_repayment.credit_id = credit.id
  and credit.borrower_id = borrower.id
  and borrower.personal_data_id = personal_data.id