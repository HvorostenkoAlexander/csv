update datalab_action, borrower, personal_data
set datalab_action.iin=personal_data.passport_iin
where datalab_action.borrower_id = borrower.id
  and borrower.personal_data_id = personal_data.id