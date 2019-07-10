package PubSub;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import javax.naming.Context;
import javax.naming.InitialContext;
import java.util.Properties;

public class Subscriber
{
    //URL of the JMS server. DEFAULT_BROKER_URL will just mean that JMS server is on localhost
    private static String url = ActiveMQConnection.DEFAULT_BROKER_URL;

    private static String TOPIC = "EUREKA_TOPIC";

    public static void main(String[] args) throws Exception
    {


        // Getting JMS connection from the server and starting it
        TopicConnectionFactory connectionFactory = new ActiveMQConnectionFactory(url);
        TopicConnection topicConn = connectionFactory.createTopicConnection();
        topicConn.start();


        // create a topic session
        TopicSession topicSession = topicConn.createTopicSession(false,
                Session.AUTO_ACKNOWLEDGE);

        // lookup the topic object
        Topic topic = topicSession.createTopic(TOPIC);
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