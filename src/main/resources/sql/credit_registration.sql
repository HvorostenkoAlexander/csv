update credit_registration, borrower, user_account
set credit_registration.email=user_account.email,
    credit_registration.mobile_number=user_account.phone
where credit_registration.borrower_id = borrower.id
  and borrower.user_account_id = user_account.id