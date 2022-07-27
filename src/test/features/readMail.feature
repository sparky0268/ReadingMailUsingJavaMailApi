Feature: Test Email Attachments
  Scenario:Login into mailBox and read recent unread mails and validate attachments
    Given I login into the mailbox using generic credentails
    When I open the Inbox and get the latest mails
    Then I validate the mail contents