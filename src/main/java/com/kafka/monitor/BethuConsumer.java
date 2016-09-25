package com.kafka.monitor;

import java.util.Arrays;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicBoolean;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.common.errors.WakeupException;

public class BethuConsumer implements Runnable {
	ConsumerRecords<String, String> records;
	private final AtomicBoolean closed = new AtomicBoolean(false);
//	private final KafkaConsumer consumer;
	private final KafkaConsumer<String, String> kafkaConsumer = new KafkaConsumer<>(readProperties());
	
	@Override
	public void run() {
		try{
//		KafkaConsumer<String, String> kafkaConsumer = new KafkaConsumer<>(props);
		System.out.println("consumer proped");
		kafkaConsumer.subscribe(Arrays.asList("iPhone"));
		System.out.println("subscripbed");
		long start = System.currentTimeMillis();
		records = kafkaConsumer.poll(1000);
		if(records.isEmpty()){
			System.out.println("is Empty");
		}
		for (ConsumerRecord<String, String> record : records) {
			System.out.printf("offset = %d, value = %s", record.offset(), record.value());
			System.out.println();
		}
		} catch (WakeupException e) {
            // Ignore exception if closing
            if (!closed.get()) throw e;
        } finally {
        	kafkaConsumer.close();
        }
//		
//		Runnable wakeupThread = new Runnable() {
//			public void run() {
//				System.out.println("Thread Running");
//				kafkaConsumer.wakeup();
//			}
//		};
//		Runnable pollThread = new Runnable() {
//			public void run() {
//				records = kafkaConsumer.poll(0);
//				for (ConsumerRecord<String, String> record : records) {
//					System.out.printf("offset = %d, value = %s", record.offset(), record.value());
//				}
//			}
//		};
//		
//		ExecutorService executor =  Executors.newFixedThreadPool(2);
//try{
//			
//			executor.execute(pollThread);
//			executor.execute(wakeupThread);
//			executor.shutdown();
//			executor.awaitTermination(10, TimeUnit.SECONDS);
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (WakeupException we) {
//			we.printStackTrace();
//		}
//		finally{
//			executor.shutdownNow();
			System.out.println("closed consumer");
//			kafkaConsumer.close();

//		}
		
//		try {
//			records = kafkaConsumer.poll(0);
//			wakeupThread.start();
//			for (ConsumerRecord<String, String> record : records) {
//				System.out.printf("offset = %d, value = %s", record.offset(), record.value());
//			}
//		} finally {
//			System.out.println("closed consumer");
//			kafkaConsumer.close();
//		}
		// wakeupThread.start();

		// do{
		// if( break;
		// System.out.println("before");
		// records = kafkaConsumer.poll(10);
		// System.out.println("polled");
		// for (ConsumerRecord<String, String> record : records) {
		// System.out.printf("offset = %d, value = %s", record.offset(),
		// record.value());
		// System.out.println();
		// }
		// if (!records.isEmpty() || (System.currentTimeMillis() - start) >
		// 3000) break;
		// } while (records.isEmpty() && (System.currentTimeMillis() - start) <
		// 3000) ;
		// System.out.println("closed consumer");
		// kafkaConsumer.close();
	}
	private Properties readProperties(){
		Properties props = new Properties();
		props.put("bootstrap.servers", "localhost:9092");
		props.put("group.id", "group-1");
		props.put("enable.auto.commit", "true");
		props.put("auto.commit.interval.ms", "10000");
		props.put("auto.offset.reset", "earliest");
		props.put("session.timeout.ms", "10000");
		props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
		props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");

		return props;
	}
	
	public void shutdown() {
        closed.set(true);
        kafkaConsumer.wakeup();
    }
}
