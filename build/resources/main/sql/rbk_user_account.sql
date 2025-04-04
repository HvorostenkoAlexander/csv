update rbk_user_account, borrower, personal_data
set rbk_user_account.iin=personal_data.passport_iin
where rbk_user_account.borrower_id = borrower.id
  and borrower.personal_data_id = personal_data.id