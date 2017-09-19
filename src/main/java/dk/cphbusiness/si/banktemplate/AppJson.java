package dk.cphbusiness.si.banktemplate;

import java.io.IOException;

import org.codehaus.jackson.map.ObjectMapper;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;
import com.rabbitmq.client.AMQP.BasicProperties;

import dk.cphbusiness.si.banktemplate.JsonDTO.BankLoanDTO;
import dk.cphbusiness.si.banktemplate.JsonDTO.JsonWrapper;

/**
 * Hello world!
 * 
 */
public class AppJson {

	final String EXCHANGE_NAME = "cphbusiness.bankJSON";

	public void run() {
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost("datdb.cphbusiness.dk");
		Connection connection;
		try {
			connection = factory.newConnection();
			Channel channel = connection.createChannel();
			channel.exchangeDeclare(EXCHANGE_NAME, "fanout");
			String queue = channel.queueDeclare().getQueue();
			channel.queueBind(queue, EXCHANGE_NAME, "");
			System.out
					.println(" [*] Waiting for messages. To exit press CTRL+C");
			channel.basicQos(1); // Only hold one

			QueueingConsumer consumer = new QueueingConsumer(channel);
			channel.basicConsume(queue, false, consumer);

			while (true) {
				QueueingConsumer.Delivery delivery = consumer.nextDelivery();
				String message = new String(delivery.getBody());

				System.out.println(" [x] Received '" + message + "'");
				String reply = doWork(message);
				System.out.println(" [x] Done");

				BasicProperties bp = delivery.getProperties();
				// Unpack the reply queue
				String replyQueue = bp.getReplyTo();

				System.out.print("\tReply Message: " + reply);
				System.out.print("\t. Replying to: " + replyQueue + "\n");

				channel.basicPublish("", replyQueue, bp, reply.getBytes());
				channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static String doWork(String task) throws InterruptedException {
		String out = "";
		try {
			BankLoanDTO bDto = JsonWrapper.jsonToBLDTO(task);
			BLDTOResponse response = new BLDTOResponse();
			double baseInterest = 1.5;
			// Determine the loan amount
			if (bDto.getLoanAmount() > 500000) {
				// do nothing
			} else {
				baseInterest += 1;
			}
			// if it is a short term loan, then intrest is higher
			if (bDto.getLoanDuration() < 48) {
				baseInterest = baseInterest * 1.7;
			}

			// if RKI,then double the interest
			if (bDto.getCreditScore() < 600) {
				baseInterest = baseInterest * 2.2;
			}
			response.setInterestRate(baseInterest);
			response.setSsn(bDto.getSsn());
			out = new ObjectMapper().writeValueAsString(response);
		} catch (Exception e) {
			out = "Exception: Something went wrong. Data should be sent like: {\"ssn\":1605789787,\"loanAmount\":10.0,\"loanDuration\":360,\"rki\":false}\t";
			out += e.getMessage();
		}
		return out;
	}

	public static void main(String[] args) {
		BankLoanDTO bDto = new BankLoanDTO();
		bDto.setLoanAmount(10d);
		bDto.setLoanDuration(30 * 12);
		bDto.setCreditScore(598);
		bDto.setSsn(1605789787);
		System.out.println(JsonWrapper.BLDTOToJson(bDto));
		AppJson app = new AppJson();
		while (true) {
			try {
				app.run();
				Thread.sleep(1000);
			} catch (Exception e) {
				System.out.println("Ended program loop, starting again");
				 e.printStackTrace();
			}
		}
	}
}
