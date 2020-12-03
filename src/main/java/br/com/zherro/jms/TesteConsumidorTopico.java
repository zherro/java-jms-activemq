package br.com.zherro.jms;

import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.Scanner;

public class TesteConsumidorTopico {


    @SuppressWarnings("resource")
    public static void main(String[] args) throws NamingException, JMSException {
        System.out.println("Iniciando");
        System.setProperty("org.apache.activemq.SERIALIZABLE_PACKAGES","*");

        InitialContext context = new InitialContext();
        ConnectionFactory factory = (ConnectionFactory) context.lookup("ConnectionFactory");
        Connection connection = factory.createConnection();
        connection.setClientID("estoque");

        connection.start();
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

        Topic topico = (Topic) context.lookup("loja");
        MessageConsumer consumer = session.createDurableSubscriber(topico, "assinatura");

        consumer.setMessageListener(new MessageListener() {
            @Override
            public void onMessage(Message message) {
                System.out.println("Recebendo mensagem " + message);
            }
        });

        new Scanner(System.in).nextLine();

        session.close();
        connection.close();
        context.close();
    }
}
