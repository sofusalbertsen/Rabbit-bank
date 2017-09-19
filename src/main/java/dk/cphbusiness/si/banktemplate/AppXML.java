package dk.cphbusiness.si.banktemplate;

import java.io.IOException;
import org.joda.time.DateTime;
import org.joda.time.Months;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;
import com.rabbitmq.client.AMQP.BasicProperties;

import dk.cphbusiness.si.banktemplate.XMLDTO.XMLBankLoanDTO;
import dk.cphbusiness.si.banktemplate.XMLDTO.XMLWrapper;

/**
 * Hello world!
 *
 */
public class AppXML 
{
	 
	 final String  EXCHANGE_NAME= "cphbusiness.bankXML";
	 public void run() {
		 ConnectionFactory factory = new ConnectionFactory();
		 factory.setHost("datdb.cphbusiness.dk");
		    Connection connection;
			try {
				connection = factory.newConnection();
		    Channel channel = connection.createChannel();
		    channel.exchangeDeclare(EXCHANGE_NAME,"fanout");
		    String queue= channel.queueDeclare().getQueue();
		    
		    channel.queueBind(queue, EXCHANGE_NAME, "");	    
		    System.out.println(" [*] Waiting for messages. To exit press CTRL+C");
		    channel.basicQos(1); //Only hold one
		    
		    QueueingConsumer consumer = new QueueingConsumer(channel);
		    channel.basicConsume(queue, false, consumer);
		    
		    while (true) {
		      QueueingConsumer.Delivery delivery = consumer.nextDelivery();
		      String message = new String(delivery.getBody());
		      
		      System.out.println(" [x] Received '" + message + "'");
		      String reply =doWork(message);
		      System.out.println(" [x] Done");

		      BasicProperties bp = delivery.getProperties();
				//Unpack the reply queue
				String replyQueue = bp.getReplyTo();
				
				System.out.print("\tReply Message: "+reply);
				System.out.print("\t. Replying to: "+replyQueue+"\n");
				
				
				channel.basicPublish("", replyQueue, bp,reply.getBytes());
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
	    String out="";
	    try {
			XMLBankLoanDTO dto = XMLWrapper.XMLTODTO(task);
			BLDTOResponse response = new BLDTOResponse();
			double baseInterest=1.3;
			//Determine the loan amount
			if(dto.getLoanAmount()>500000){
				//do nothing
			}else{
				baseInterest+=1.1;
			}
			DateTime start = new DateTime(0);
			DateTime end = new DateTime(dto.getLoanDuration());
			System.out.println(Months.monthsBetween(start, end).getMonths());
			//if it is a short term loan, then intrest is higher
			if(Months.monthsBetween(start, end).getMonths()<48){
				baseInterest=baseInterest*1.9;
			}
			
			//if RKI,then double the interest
			if (dto.getCreditScore()<600){
				baseInterest=baseInterest*2.8;
			}
			response.setInterestRate(baseInterest);
			response.setSsn(dto.getSsn());
			out=XMLWrapper.responseToXML(response);
		} catch (Exception e) {
			out = "Exception: Something went wrong. Data should be sent like: <LoanRequest>   <ssn>12345678</ssn>   <creditScore>685</creditScore>   <loanAmount>10.0</loanAmount>   <loanDuration>1970-01-01 01:00:00.0 CET</loanDuration> </LoanRequest> \t";
			out +=e.getMessage();
		}
	    return out;
	  }
    public static void main( String[] args )
    {
     
     AppXML app = new AppXML();
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
