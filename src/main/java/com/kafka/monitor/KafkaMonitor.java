package com.kafka.monitor;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class KafkaMonitor {
	public static BethuConsumer consumer;
	
	public static void main(String[] args) throws InterruptedException {
		
		Runnable consumerWake = new Runnable() {
			public void run() {
				consumer.shutdown();
				System.out.println("shutdown the consumer thread");
			}
		};
		ExecutorService executor =  Executors.newFixedThreadPool(2);
		ScheduledExecutorService sexecutor = Executors.newScheduledThreadPool(1);
		BethuProducer producer = new BethuProducer();
		consumer = new BethuConsumer();
		
		try{
			
			executor.execute(producer);
			executor.execute(consumer);
			executor.shutdown();
			executor.awaitTermination(30, TimeUnit.SECONDS);
			
			sexecutor.schedule(consumerWake, 5, TimeUnit.SECONDS);
		} catch (InterruptedException e) {
            e.printStackTrace();
      }
		finally{
			executor.shutdownNow();
			sexecutor.shutdown();
		}

	}

}
