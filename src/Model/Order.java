package Model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.util.JSON;

import Commands.Command;

public class Order {

	private static final String COLLECTION_NAME = "orders";

	private static MongoCollection<Document> collection = null;

	public static HashMap<String, Object> create(HashMap<String, Object> atrributes) {

		MongoClientURI uri = new MongoClientURI(
				"mongodb://localhost");

		MongoClient mongoClient = new MongoClient(uri);
		MongoDatabase database = mongoClient.getDatabase("El-Menus");
//    	Method method =   Class.forName("PlatesService").getMethod("getDB", null);
//    	MongoDatabase database = (MongoDatabase) method.invoke(null, null);

		// Retrieving a collection
		MongoCollection<Document> collection = database.getCollection("orders");
		Document newOrder = new Document();

		for (String key : atrributes.keySet()) {
			newOrder.append(key, atrributes.get(key));
		}
		collection.insertOne(newOrder);

		return atrributes;
	}

	public static HashMap<String, Object> update(String id, HashMap<String, Object> atrributes) {
		MongoClientURI uri = new MongoClientURI(
				"mongodb://admin:admin@cluster0-shard-00-00-nvkqp.gcp.mongodb.net:27017,cluster0-shard-00-01-nvkqp.gcp.mongodb.net:27017,cluster0-shard-00-02-nvkqp.gcp.mongodb.net:27017/El-Menus?ssl=true&replicaSet=Cluster0-shard-0&authSource=admin&retryWrites=true");

		MongoClient mongoClient = new MongoClient(uri);
		MongoDatabase database = mongoClient.getDatabase("El-Menus");
//    	Method method =   Class.forName("PlatesService").getMethod("getDB", null);
//    	MongoDatabase database = (MongoDatabase) method.invoke(null, null);

		// Retrieving a collection
		MongoCollection<Document> collection = database.getCollection("orders");
		Document updatedOrder = new Document();
		Bson filter = new Document("_id", new ObjectId(id));
		System.out.println(filter.toString());
		for (String key : atrributes.keySet()) {
			updatedOrder.append(key, atrributes.get(key));
		}

		Document updateOperationDocument = new Document("$set", updatedOrder);
		collection.updateMany(filter, updateOperationDocument);
		
		return atrributes;
	}

	public static HashMap<String, Object> get(String messageId) {

		MongoClientURI uri = new MongoClientURI(
				"mongodb://localhost");

		MongoClient mongoClient = new MongoClient(uri);
		MongoDatabase database = mongoClient.getDatabase("El-Menus");
//    	Method method =   Class.forName("PlatesService").getMethod("getDB", null);
//    	MongoDatabase database = (MongoDatabase) method.invoke(null, null);

		// Retrieving a collection
		MongoCollection<Document> collection = database.getCollection("orders");
		System.out.println("Inside Get");
		BasicDBObject query = new BasicDBObject();
		query.put("_id", new ObjectId(messageId));

		System.out.println(query.toString());
		HashMap<String, Object> message = null;
		Document doc = collection.find(query).first();
		JSONParser parser = new JSONParser(); 
		try {
			JSONObject json = (JSONObject) parser.parse(doc.toJson());
		
			message = Command.jsonToMap(json);
			
			System.out.println(message.toString());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
		return message;
	}
	
	public static ArrayList<HashMap<String, Object>> getAll() {

		MongoClientURI uri = new MongoClientURI(
				"mongodb://localhost");

		MongoClient mongoClient = new MongoClient(uri);
		MongoDatabase database = mongoClient.getDatabase("El-Menus");
//    	Method method =   Class.forName("PlatesService").getMethod("getDB", null);
//    	MongoDatabase database = (MongoDatabase) method.invoke(null, null);

		// Retrieving a collection
		MongoCollection<Document> collection = database.getCollection("orders");
		System.out.println("Inside GetAll");
		FindIterable<Document> docs = collection.find();
		JSONParser parser = new JSONParser(); 
		ArrayList<HashMap<String, Object>> orders = new  ArrayList<HashMap<String, Object>>();
				
		for (Document doc : docs) {
			try {
				JSONObject json = (JSONObject) parser.parse(doc.toJson());
				HashMap<String, Object> message = null;
				message = Command.jsonToMap(json);
				orders.add(message);
				System.out.println(message.toString());
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return orders;
	}
}
