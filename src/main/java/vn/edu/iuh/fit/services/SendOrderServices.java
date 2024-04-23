package vn.edu.iuh.fit.services;

import jakarta.jms.Destination;
import jakarta.jms.JMSException;
import jakarta.jms.Message;
import jakarta.jms.Session;
import org.apache.activemq.command.ActiveMQDestination;
import org.apache.activemq.command.ActiveMQTopic;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import vn.edu.iuh.fit.utils.Base64EncodingText;
import vn.edu.iuh.fit.utils.EncodingText;

@Service
public class SendOrderServices {

    @Autowired
    private JmsTemplate template;

    public void sendOrder(String jsonDocs) throws Exception {
        // Mã hóa chuỗi JSON thành Base64
//        String base64EncodedJson = encodeToBase64(jsonDocs);
            EncodingText encodingText = new Base64EncodingText();
            String encryptedCartJson = encodingText.encrypt(jsonDocs);

        Destination destination = new ActiveMQTopic("order_products");
        MessageCreator msg=new MessageCreator() {
            @Override
            public Message createMessage(Session session) throws JMSException {
                return session.createTextMessage(encryptedCartJson);
            }
        };
        template.send(destination,msg);
    }
}


