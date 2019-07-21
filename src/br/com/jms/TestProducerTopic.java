package br.com.jms;

import java.io.StringWriter;

import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.Message;
import javax.jms.MessageProducer;
import javax.jms.Session;
import javax.naming.InitialContext;
import javax.xml.bind.JAXB;

import br.com.caelum.modelo.Pedido;
import br.com.caelum.modelo.PedidoFactory;

public class TestProducerTopic {

	
	public static void main(String[] args) throws Exception{
		
		InitialContext context = new InitialContext();
		
		ConnectionFactory connectionFactory = (ConnectionFactory) context.lookup("ConnectionFactory");
		
		Connection connection = connectionFactory.createConnection();
		connection.start();
		
		Session session = connection.createSession(Boolean.FALSE, Session.AUTO_ACKNOWLEDGE);
		
		Destination destination = (Destination) context.lookup("loja");
		
		MessageProducer producer = session.createProducer(destination);
		
		Pedido pedido = new PedidoFactory().geraPedidoComValores();
		
		StringWriter writer = new StringWriter();
		JAXB.marshal(pedido, writer);
		
		String xml = writer.toString();
		
		System.out.println("XML:"+ xml);
		
		Message message = session.createTextMessage(xml);
		
		message.setBooleanProperty("ebook", Boolean.FALSE);
		
		producer.send(message);
		
		context.close();
		connection.close();
		session.close();
	}
}
