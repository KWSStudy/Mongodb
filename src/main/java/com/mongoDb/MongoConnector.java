package com.mongoDb;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Iterator;

import org.bson.BsonDocument;
import org.bson.BsonObjectId;
import org.bson.Document;
import org.bson.types.ObjectId;

import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSBuckets;
import com.mongodb.client.gridfs.GridFSDownloadStream;
import com.mongodb.client.gridfs.model.GridFSUploadOptions;
import com.mongodb.client.model.Filters;

public class MongoConnector {
	public static void main(String[] args) throws FileNotFoundException {
		MongoClient client = new MongoClient("localhost");
		MongoDatabase database = client.getDatabase("tutorial");
		MongoCollection<Document> collection =  database.getCollection("numbers");
		
		if(collection.count() == 0){
			database.createCollection("numbers");
			for(int i = 0 ; i < 200000 ; i ++){
				collection.insertOne(new Document("num", i));
			}
		}else{
			FindIterable<Document> numbers = collection.find();
			Iterator<Document> numbersItor = numbers.iterator(); 
			while(numbersItor.hasNext()){
				Document docNum = numbersItor.next();
				System.out.println(docNum);
			}
		}
		
		final String bucket = "test";
		final String filename = "test.JPG";
		 
		MongoCollection<Document> testColl = database.getCollection(bucket+".files");
		
		if(testColl.count() == 0){
			GridFSBucket gridFSBucket = GridFSBuckets.create(database,bucket);

	        //add etag to metadata
	        ObjectId etag = new ObjectId();
	        BsonDocument metadata = new BsonDocument();
	        metadata.put("_etag", new BsonObjectId(etag));
	 
	        GridFSUploadOptions options = new GridFSUploadOptions()
	                .metadata(Document.parse(metadata.toJson()));
	 
	        InputStream sourceStream = MongoConnector.class.getResourceAsStream(filename);
	         
	        ObjectId _id = gridFSBucket.uploadFromStream(
	                filename, 
	                sourceStream, 
	                options);
	        
	        System.out.println(_id);
	        
		}else{
			GridFSBucket downloadGridFs = GridFSBuckets.create(database,bucket);
			downloadGridFs.downloadToStream(downloadGridFs.find(Filters.eq("filename", filename)).first().getObjectId(), new FileOutputStream(new File("d:/mongo.JPG")));
		
			/*GridFSDownloadStream gds = downloadGridFs.openDownloadStream(downloadGridFs.find(Filters.eq("filename", filename)).first().getObjectId());
			OutputStream os =  new FileOutputStream(new File("d:/mongo.JPG"));
			byte [] b = new byte[256];
			int len = 0;
			while((len = gds.read(b)) > 0){
				try {
					os.write(b, 0, len);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			try {
				gds.close();
				os.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}*/
		}
        
		client.close();
	}
}
