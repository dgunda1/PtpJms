package P2PWeb;

import javax.jms.*;
import javax.naming.Context;
import javax.naming.InitialContext;
import java.util.Properties;

public class Sender {
    public static void main(String[] args) throws Exception {
        // get the initial context
        // InitialContext context = new InitialContext();

        Properties props = new Properties();
        props.setProperty(Context.INITIAL_CONTEXT_FACTORY, "org.apache.activemq.jndi.ActiveMQInitialContextFactory");
        props.setProperty(Context.PROVIDER_URL, "tcp://localhost:61616");
        javax.naming.Context context = new InitialContext(props);
        // lookup the queue object
        Queue queue = (Queue) context.lookup("EUREKA_QUEUE");

        // lookup the queue connection factory
        QueueConnectionFactory conFactory = (QueueConnectionFactory) context.lookup("ConnectionFactory");

        // create a queue connection
        QueueConnection queConn = conFactory.createQueueConnection();

        // create a queue session
        QueueSession queSession = queConn.createQueueSession(false, Session.DUPS_OK_ACKNOWLEDGE);

        // create a queue sender
        QueueSender queSender = queSession.createSender(queue);
        // JMSProducer queueSender =queSender;
        queSender.setDeliveryMode(DeliveryMode.NON_PERSISTENT);

        // create a simple message to say "Hello World"
        TextMessage message = queSession.createTextMessage("Hello World");

        // send the message
        queSender.send(message);

        // print what we did
        System.out.println("Message Sent: " + message.getText());

        // close the queue connection
        queConn.close();
    }
}