package br.com.bjbraz.mensageria;

import java.io.IOException;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

public class ReceiveTask {

	private final static String QUEUE_NAME = "task";

	public static void main(String[] argv) throws Exception, InterruptedException {
	    ConnectionFactory factory = new ConnectionFactory();
	    factory.setHost("172.17.0.3");
	    Connection connection = factory.newConnection();
	    Channel channel = connection.createChannel();

	    channel.queueDeclare(QUEUE_NAME, false, false, false, null);
	    System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

	    Consumer consumer = new DefaultConsumer(channel) {
	      @Override
	      public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body)
	          throws IOException {
	        String message = new String(body, "UTF-8");
	        System.out.println(" [x] Received '" + message + "'");
	        
	        try {
	            doWork(message);
	        } catch (InterruptedException e) {
				e.printStackTrace();
			} finally {
	            System.out.println(" [x] Done");
	        }
	        
	      }
	    };
	    
	    boolean autoAck = true; // acknowledgment is covered below
	    channel.basicConsume(QUEUE_NAME, true, consumer);
	}
	
	private static void doWork(String task) throws InterruptedException {
		Thread.sleep(1000);
		
	    for (char ch: task.toCharArray()) {
	        if (ch == '.') Thread.sleep(1000);
	    }
	}


}
