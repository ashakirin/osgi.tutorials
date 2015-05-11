package org.travelex.poc.talend.soap;

import javax.jms.ConnectionFactory;
import javax.ws.rs.core.Response;

import org.apache.camel.ProducerTemplate;
import org.apache.hello_world_soap_http.Greeter;
import org.travelex.poc.talend.camel.FileCamelRoute;
import org.travelex.poc.talend.messaging.JMSHelper;
import org.travelex.poc.talend.rest.Book;
import org.travelex.poc.talend.rest.BookStore;

public class GreeterImpl implements Greeter {

	private ProducerTemplate producer;
	private BookStore bookStore;
	
	public GreeterImpl(ConnectionFactory jmsConnectionFactory, ProducerTemplate producer, BookStore bookStore) {
		JMSHelper.consumeMessageFromQueue(jmsConnectionFactory);
		this.producer = producer;
		this.bookStore = bookStore;
	}

	@Override
	public String greetMe(String requestType) {
		producer.sendBody(FileCamelRoute.ENDPOINT, requestType);	

//		BookStore store = JAXRSClientFactory.create("http://localhost:8040/services/bookstore", BookStore.class);
		Response response = bookStore.getBookRoot(123L);
		System.out.println("Book: " + response.readEntity(Book.class).getId());
		
		return "Hi Travelex !!!";
	}

}
