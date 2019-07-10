package PubSub;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;

public class Publisher
{

    //URL of the JMS server. DEFAULT_BROKER_URL will just mean that JMS server is on localhost
    private static String url = ActiveMQConnection.DEFAULT_BROKER_URL;


    public static void main(String[] args) throws Exception
    {


        // Getting JMS connection from the server and starting it
        TopicConnectionFactory connectionFactory = new ActiveMQConnectionFactory(url);
        TopicConnection topicConn = connectionFactory.createTopicConnection();
        topicConn.start();

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
        message.setText("Hello World ");

        // publish the messages
        topicPublisher.publish(message);

        // print what we did
        System.out.println("Message published: " + message.getText());

        // close the topic connection
        topicConn.close();
    }
}
