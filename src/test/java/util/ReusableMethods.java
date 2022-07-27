package util;

import jakarta.mail.Message;
import jakarta.mail.Multipart;
import jakarta.mail.Part;
import jakarta.mail.internet.MimeBodyPart;

public class ReusableMethods{
        public static void getTextFromMessage(Message message) throws Exception {
            if (message.isMimeType("text/plain")) {
                System.out.println("Text : " + message.getContent().toString());
            } else if (message.isMimeType("text/html")) {
                String html = (String) message.getContent();
                System.out.println(org.jsoup.Jsoup.parse(html).text());
            } else
                readPart(message);
        }

        private static void readPart(Part p) throws Exception {
            // check if the content has attachment
            if (p.isMimeType("multipart/*")) {
                Multipart mp = (Multipart) p.getContent();
                int count = mp.getCount();
                // iterate through each part identify the attachment
                for (int i = 0; i < count; i++) {
                    MimeBodyPart part = (MimeBodyPart) mp.getBodyPart(i);
                    if (Part.ATTACHMENT.equalsIgnoreCase(part.getDisposition())) {
                        System.out.println("Mail has an attachment");
                        System.out.println("attachment name: " + part.getFileName());
                    }
                    if (part.isMimeType("text/html")) {
                        String html = (String) part.getContent();
                        System.out.println(org.jsoup.Jsoup.parse(html).text());
                    }
                }
            }
        }
}
