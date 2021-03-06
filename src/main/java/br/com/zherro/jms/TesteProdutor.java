package br.com.zherro.jms;

import javax.jms.*;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.Scanner;

public class TesteProdutor {


    @SuppressWarnings("resource")
    public static void main(String[] args) throws NamingException, JMSException {
        System.out.println("Iniciando");

        InitialContext context = new InitialContext();

        ConnectionFactory factory = (ConnectionFactory) context.lookup("ConnectionFactory");
        Connection connection = factory.createConnection();
        connection.start();

        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

        Destination fila = (Destination) context.lookup("financeiro");
        MessageProducer producer = session.createProducer(fila);

        Message message= session.createTextMessage("teste+112+kjdd");
        producer.send(message);

        new Scanner(System.in).nextLine();

        session.close();
        connection.close();
        context.close();
    }
}
