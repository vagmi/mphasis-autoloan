Feature: Pay Auto Loan
  As a Chase User with Auto loan
  In order to avoid the late charges
  I decide to pay the loan amount due within the due date

Background:
  Given today is "25-Oct-2013"
  And I have logged in as a Chase user
  And I have a checking account with the following details
    | account_number    | 12345   |
    | available_balance | $ 25000 |


Scenario: Schedule a Loan Payment before due date
  Given I have a Chase User with Auto loan
    | loan_account_number | 123456     |
    | principal_balance   | $ 10000    |
    | amount_due          | $ 2000     |
    | next_payment_date   | 1-Nov-2013 |
  When I make a payment of $ 2000 against the loan 123456
  Then my account balance should be $ 23000
  When I view my payment activity for loan 123456
  Then I should see the following details
    | principal_balance | $ 8000     |
    | amount_due        | $ 2000     |
    | next_payment_date | 1-Dec-2013 |

Scenario: Schedule  Payment after due date
  Given I have a Chase User with Auto loan
    | loan_account_number | 123457      |
    | principal_balance   | $ 10000     |
    | amount_due          | $ 2000      |
    | late_fee            | $ 20        |
    | additional_interest | $ 5         |
    | next_payment_date   | 20-Oct-2013 |
  When I make a payment of $ 2025 against the loan 123457
  Then my account balance should be $ 22975
  When I view my payment activity for loan 123457
  Then I should see the following details
    | principal_balance | $ 8000     |
    | amount_due        | $ 2000     |
    | next_payment_date | 20-Nov-2013 |