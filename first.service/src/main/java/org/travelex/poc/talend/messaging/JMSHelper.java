package org.travelex.poc.talend.messaging;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.MessageProducer;
import javax.jms.Queue;
import javax.jms.Session;
import javax.jms.TextMessage;

public class JMSHelper {
	private static final String QUEUE_NAME = "SERVICE_TEST_QUEUE";
	
	public static void publishMessageToQueue(ConnectionFactory cf, Long id) {
	    Connection qcon = null;
	    Session session = null;
	    try {
			qcon = cf.createConnection();
			session = qcon.createSession(false, Session.AUTO_ACKNOWLEDGE);
			Queue queue = session.createQueue(QUEUE_NAME);
			MessageProducer producer = session.createProducer(queue);
			TextMessage msg = session.createTextMessage();
			msg.setText("Message is sent from REST service: " + id);
			producer.send(msg);
			System.out.println("*** Message for id " + id + " was sent to queue ***");
		} catch (JMSException e) {
			e.printStackTrace();
		} finally {
		    try {
				if(session != null) {
					session.close();
				}
				if (qcon != null) {
					qcon.close();
				}
			} catch (JMSException e) {
				e.printStackTrace();
			}
		}
	}

	public static void consumeMessageFromQueue(ConnectionFactory cf) {
	    try {
			Connection qcon = cf.createConnection();
			Session session = qcon.createSession(false, Session.AUTO_ACKNOWLEDGE);
			Queue queue = session.createQueue(QUEUE_NAME);
			MessageConsumer consumer = session.createConsumer(queue);
			consumer.setMessageListener(new MessageListener() {
				
				@Override
				public void onMessage(Message message) {
					try {
						System.out.println("*** Received message from queue: " + ((TextMessage)message).getText());
					} catch (JMSException e) {
						e.printStackTrace();
					}
				}
			});
			System.out.println("*** Listenning the queue ***");
			qcon.start();
		} catch (JMSException e) {
			e.printStackTrace();
		}
	}
}

