package stepDefinition;

import constants.StoreFields;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import cucumber.api.java.Before;
import jakarta.mail.*;
import jakarta.mail.search.FlagTerm;
import util.ReusableMethods;

import java.util.Arrays;
import java.util.Properties;

public class StepDefinitions {

    private static Properties props;
    private static Session session;
    private static Store store;
    private static Folder emailFolder;
    private static Message[] messages;



    @Before
    public void before() {
        // Get a Properties object
        props = System.getProperties();
        props.setProperty("mail.imap.ssl.enable", "true");
        // Get a Session object
        session = Session.getInstance(props);
        session.setDebug(StoreFields.debug);
    }


    @Given("^I login into the mailbox using generic credentails$")
    public static  void i_login_into_the_mailbox_using_generic_credentails()  {
        // Get a Store object
        try {
            store = session.getStore(StoreFields.storeProtocol);
        } catch (NoSuchProviderException e) {
            e.printStackTrace();
        }
        // Connect
        try {
            store.connect(StoreFields.host, StoreFields.port, StoreFields.user, StoreFields.password);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    @When("^I open the Inbox and get the latest mails$")
    public static void i_open_the_inbox_and_get_the_latest_mails() {
        // Open the Folder
        try {
            emailFolder = store.getFolder("Inbox");
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        System.out.println("Reading Mails From - " + emailFolder.getFullName());
        // try to open read/write and if that fails try read-only
        try {
            emailFolder.open(Folder.READ_WRITE);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    @Then("^I validate the mail contents$")
    public static void i_validate_the_mail_contents()  {
        int totalMessages = 0;
        try {
            totalMessages = emailFolder.getMessageCount();
        } catch (MessagingException e) {
            e.printStackTrace();
        }
        if (totalMessages == 0) {
            System.out.println("Empty folder");
            try {
                emailFolder.close(false);
            } catch (MessagingException e) {
                e.printStackTrace();
            }
            try {
                store.close();
            } catch (MessagingException e) {
                e.printStackTrace();
            }
            System.exit(1);
        }
        if (StoreFields.readEnable) {
            int newMessages = 0;
            try {
                newMessages = emailFolder.getNewMessageCount();
            } catch (MessagingException e) {
                e.printStackTrace();
            }
            System.out.println("Total messages = " + totalMessages);
            System.out.println("New messages = " + newMessages);
            messages = new Message[0];
            try {
                messages = emailFolder.search(new FlagTerm(new Flags(Flags.Flag.SEEN), false));
            } catch (MessagingException e) {
                e.printStackTrace();
            }


            // Sort messages from recent to oldest
            Arrays.sort(messages, (m1, m2) -> {
                try {
                    return m2.getSentDate().compareTo(m1.getSentDate());
                } catch (MessagingException e) {
                    throw new RuntimeException(e);
                }
            });
            for (int i = 0; i < messages.length; i++) {
                Message message = messages[i];
                System.out.println("---------------------------------");
                System.out.println("Email Number " + (i + 1));
                try {
                    System.out.println("Subject: " + message.getSubject());
                } catch (MessagingException e) {
                    e.printStackTrace();
                }
                try {
                    System.out.println("From: " + message.getFrom()[0]);
                } catch (MessagingException e) {
                    e.printStackTrace();
                }
                System.out.println("body: \n");
                try {
                    ReusableMethods.getTextFromMessage(message);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        // closing files and connections
        //emailFolder.close(true);
        try {
            store.close();
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
