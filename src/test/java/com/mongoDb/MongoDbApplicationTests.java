package com.mongoDb;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoIterable;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MongoDbApplicationTests {

	@Test
	public void mongoDbConnectionTest(){
		MongoClient mongoClient = new MongoClient(/* local default host, port */"localhost", 27017);
		mongoClient.getDatabase("test");

		//Display all database
		MongoIterable<String> dbs = mongoClient.listDatabaseNames();
		for(String db : dbs){
			System.out.println(db);
		}

	}




}
