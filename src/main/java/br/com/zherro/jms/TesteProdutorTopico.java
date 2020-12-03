package br.com.zherro.jms;

import br.com.zherro.modelo.Pedido;
import br.com.zherro.modelo.PedidoFactory;

import javax.jms.*;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.xml.bind.JAXB;
import java.io.StringWriter;
import java.util.Scanner;

public class TesteProdutorTopico {


    @SuppressWarnings("resource")
    public static void main(String[] args) throws NamingException, JMSException {
        System.out.println("Iniciando");

        InitialContext context = new InitialContext();

        System.setProperty("org.apache.activemq.SERIALIZABLE_PACKAGES","*");
        ConnectionFactory factory = (ConnectionFactory) context.lookup("ConnectionFactory");
        Connection connection = factory.createConnection();
        connection.start();

        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

        Destination fila = (Destination) context.lookup("loja");
        MessageProducer producer = session.createProducer(fila);

        Pedido pedido = new PedidoFactory().geraPedidoComValores();

        StringWriter writer = new StringWriter();
        JAXB.marshal(pedido, writer);
        String xml = writer.toString();
        System.out.println(xml);


        Message message= session.createObjectMessage(pedido);
//        Message message= session.createTextMessage(xml);
//        message.setBooleanProperty("book", true);
        producer.send(message);

//        new Scanner(System.in).nextLine();

        session.close();
        connection.close();
        context.close();
    }
}
