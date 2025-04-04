update fcb_credit_app_report, credit, borrower, personal_data
set fcb_credit_app_report.iin=personal_data.passport_iin
where fcb_credit_app_report.credit_id = credit.id
  and credit.borrower_id = borrower.id
  and borrower.personal_data_id = personal_data.id