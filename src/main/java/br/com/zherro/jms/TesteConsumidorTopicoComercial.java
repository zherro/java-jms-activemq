package br.com.zherro.jms;

import br.com.zherro.modelo.Pedido;
import org.apache.activemq.ActiveMQConnectionFactory;

import javax.jms.*;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import java.util.Scanner;

public class TesteConsumidorTopicoComercial {


    @SuppressWarnings("resource")
    public static void main(String[] args) throws NamingException, JMSException {
        System.out.println("Iniciando");

        InitialContext context = new InitialContext();
//        ConnectionFactory factory = (ConnectionFactory) context.lookup("ConnectionFactory");
//       por
        ActiveMQConnectionFactory factory = (ActiveMQConnectionFactory) context.lookup("ConnectionFactory");
        factory.setTrustAllPackages(true);

        Connection connection = factory.createConnection();
        connection.setClientID("estoque");

        connection.start();
        Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);

        Topic topico = (Topic) context.lookup("loja");
        MessageConsumer consumer = session.createDurableSubscriber(topico, "assinatura", "book is null", true);

        consumer.setMessageListener(new MessageListener() {
            @Override
            public void onMessage(Message message) {


                ObjectMessage objectMessage = (ObjectMessage)message;

                try {
                    Pedido pedido = (Pedido) objectMessage.getObject();
                    System.out.println(pedido.getCodigo());
                } catch (JMSException e) {
                    e.printStackTrace();
                }
                try {
                    session.commit();
                } catch (JMSException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        });

        new Scanner(System.in).nextLine();

        session.close();
        connection.close();
        context.close();
    }
}
