package org.travelex.poc.talend.rest;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import javax.jms.ConnectionFactory;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.UriInfo;

import org.travelex.poc.talend.messaging.JMSHelper;

@Path("/bookstore")
@Produces("application/json")
@Consumes("application/json")
public class BookStore {
    private Map<Long, Book> books = new HashMap<Long, Book>();
    @Context 
    private UriInfo ui;

	private ConnectionFactory jmsConnectionFactory;
	
    public void setJmsConnectionFactory(ConnectionFactory jmsConnectionFactory) {
		this.jmsConnectionFactory = jmsConnectionFactory;
	}

	public BookStore() {
        init();
    }
    
    @GET
    @Path("/books/{id}")
    public Response getBookRoot(@PathParam("id") Long id) {
		System.out.println(ui.getAbsolutePath());
		JMSHelper.publishMessageToQueue(jmsConnectionFactory, id);
        Book b = books.get(id);
        if (b == null) {
            return Response.status(Status.NOT_FOUND).build();
        }
        return Response.ok(b).build();
    }

    @PUT
    @Path("/books/{id}")
    public Response updateBook(@PathParam("id") Long id, Book book) {
        Book b = books.get(id);

        if (b == null) {
            return Response.status(Status.NOT_FOUND).build();
        } else {
            b.setName(book.getName());
            return Response.ok().build();
        }
    }
    
	@POST
	@Path("/books")
	public Response createBook(Book book) {
		Book b = books.get(book.getId());

		if (b != null) {
			return Response.status(Status.CONFLICT).build();
		} else {
			books.put(book.getId(), book);
			UriBuilder ub = ui.getAbsolutePathBuilder();
			URI uri = ub.path(
					Long.toString(book.getId())).build();
			return Response.created(uri).link(uri, "new book").build();
		}
	}

    @DELETE
    @Path("/books/{id}")
    public Response removeBook(@PathParam("id") Long id) {
        Book b = books.get(id);

        if (b == null) {
            return Response.status(Status.NOT_FOUND).build();
        } else {
            books.remove(id);
            return Response.ok().build();
        }
    }

    private void init() {
        books.clear();
        
        Book book = new Book();
        book.setId(123);
        book.setName("CXF in Action");
        books.put(book.getId(), book);
    }
    
}
