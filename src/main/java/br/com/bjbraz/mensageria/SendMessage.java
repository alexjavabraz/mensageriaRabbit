package br.com.bjbraz.mensageria;

import java.text.SimpleDateFormat;
import java.util.Date;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class SendMessage {
	
	public static final String QUEUE_NAME = "fila2";
	
	public static void main(String[] args) throws Exception {
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost("172.17.0.3");
		Connection connection = factory.newConnection();
		Channel channel = connection.createChannel();
		
		for(int i = 0 ; i < 100; i++) {
			Date data = new Date();
			SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss");
			
			channel.queueDeclare(QUEUE_NAME, false, false, false, null);
			String message = "Mensagem Enviada " + sdf.format(data);
			channel.basicPublish("", QUEUE_NAME, null, message.getBytes());
			System.out.println(" [x] Sent '" + message + "'");
		}
		
		channel.close();
		connection.close();
	}

}
