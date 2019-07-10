package PubSubWeb;

import javax.jms.*;
import javax.naming.Context;
import javax.naming.InitialContext;
import java.util.Properties;

public class Publisher
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
        TopicConnectionFactory connFactory = (TopicConnectionFactory) ctx.lookup("ConnectionFactory");

        // create a topic connection
        TopicConnection topicConn = connFactory.createTopicConnection();

        // create a topic session
        TopicSession topicSession = topicConn.createTopicSession(false,
                Session.AUTO_ACKNOWLEDGE);

        //Create Topic
        Topic topic = topicSession.createTopic("EUREKA_TOPIC");

        // lookup the topic object
        //Topic topic = (Topic) ctx.lookup("EUREKA_TOPIC");

        // create a topic publisher
        TopicPublisher topicPublisher = topicSession.createPublisher(topic);
        topicPublisher.setDeliveryMode(DeliveryMode.NON_PERSISTENT);

        // create the "Hello World" message
        TextMessage message = topicSession.createTextMessage();
        message.setText("Hello World");

        // publish the messages
        topicPublisher.publish(message);

        // print what we did
        System.out.println("Message published: " + message.getText());

        // close the topic connection
        topicConn.close();
    }
}
