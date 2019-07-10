package PubSubWeb;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.jms.Topic;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.jms.TopicSession;
import javax.jms.TopicSubscriber;
import javax.jms.TopicConnection;
import javax.jms.TopicConnectionFactory;
import java.util.Properties;

public class Subscriber
{
    public static void main(String[] args) throws Exception
    {
        // get the initial context
        InitialContext ctx = new InitialContext();


        Properties jndiParameters = new Properties() ;
        jndiParameters.put(Context.INITIAL_CONTEXT_FACTORY, "org.apache.activemq.jndi.ActiveMQInitialContextFactory");
        jndiParameters.put(Context.PROVIDER_URL, "tcp://localhost:61616");
        Context context = new InitialContext(jndiParameters);
        ctx = (InitialContext)context;


        // lookup the topic connection factory
        TopicConnectionFactory connFactory = (TopicConnectionFactory) ctx.
                lookup("ConnectionFactory");

        // create a topic connection
        TopicConnection topicConn = connFactory.createTopicConnection();

        // create a topic session
        TopicSession topicSession = topicConn.createTopicSession(false,
                Session.AUTO_ACKNOWLEDGE);

        // lookup the topic object
        Topic topic = (Topic) ctx.lookup("topic/EUREKA_TOPIC");
        TopicSubscriber topicSubscriber;

        try{
            // create a topic subscriber
         topicSubscriber = topicSession.createSubscriber(topic);

        // start the connection
        topicConn.start();

        // receive the message
        TextMessage message = (TextMessage) topicSubscriber.receive();

        // print the message
        System.out.println("Message received: " + message.getText());

        // close the topic connection
        topicConn.close();
        } catch (Exception e) {
            System.out.println("JNDI API for dest lookup failed: " +
                    e.toString());
            e.printStackTrace();
            System.exit(1);
        }
    }
}