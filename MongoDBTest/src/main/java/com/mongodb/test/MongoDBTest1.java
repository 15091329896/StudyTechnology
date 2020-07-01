package com.mongodb.test;

import org.bson.Document;
import org.bson.conversions.Bson;

import com.mongodb.MongoClient;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;

@SuppressWarnings({ "unchecked", "rawtypes", "resource" })
public class MongoDBTest1 {

	private static MongoClient mongoClient = null;
	
	public static void main(String[] args) {

		try {
			MongoDatabase database = Connect("myDB2");
			// 切换至集合
			MongoCollection mySet = database.getCollection("mySet");
			MongoCollection mySet2 = database.getCollection("mySet2");
			MongoCollection user = database.getCollection("user");

			// 插入文档
			// Document document = new Document("_id", 1001)
			// .append("name", "王昌龄")
			// .append("age","30");
			// mySet2.insertOne(document);

			// 插入多条
			// List<Document> documents = new ArrayList<Document>();
			// for (int i = 1; i < 6; i++) {
			// Document documentTmp = new Document("_id", 1001 + i)
			// .append("name", "王昌龄" + i)
			// .append("age","30");
			// Set<String> keySet = documentTmp.keySet();
			// for (String string : keySet) {
			// Object object = documentTmp.get(string);
			// System.out.println(string + " : " + object.toString());
			// }
			// documents.add(documentTmp);
			// }
			////
			// mySet2.insertMany(documents);

			// 删除一条
			// Bson filter = Filters.eq("_id",1001);
			// find(mySet2);
			// System.out.println("******************");
			// mySet2.deleteOne(filter);

			// Bson filter = Filters.eq("_id",1001);

			// Bson gte = Filters.gte("_id", 1010);
			//// Bson lte = Filters.lte("_id", 1006);
			//// Bson and = Filters.and(gte, lte);
			//// find(mySet2);
			// mySet.deleteMany(gte);

			// 修改
			// Bson filter = Filters.eq("name", "王昌龄1");
			// Document document = new Document("$set", new Document("age",
			// 100).append("address", "陕西省咸阳市"));
			// UpdateResult updateOne = mySet2.updateOne(filter, document);
			// System.out.println(updateOne);

			find(mySet2);
			// 修改多条

			Bson gt = Filters.gt("_id", 1001);
			mySet2.updateMany(gt, new Document("$set", new Document("age", 15)));

			
			
			// find(mySet);

			find(mySet2);
			// find(user);

			
			mongoClient.close();
			
		} catch (Exception e) {
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
		}

	}

	private static void find(MongoCollection<Document> collection) {
		// 查找集合中的所有文档
		FindIterable<Document> findIterable = collection.find();
		MongoCursor cursor = findIterable.iterator();
		System.out.println(collection.getNamespace());
		while (cursor.hasNext()) {
			System.out.println(cursor.next());
		}

	}

	private static MongoDatabase Connect(String databaseName) {
		// 连接MongoDB服务器，端口号为27017
		mongoClient = new MongoClient("192.168.0.170", 27017);
		// 连接数据库
		MongoDatabase database = mongoClient.getDatabase(databaseName); // test可选
		System.out.println("Connect successfully!");
		System.out.println("MongoDatabase inof is : " + database.getName());
		return database;
	}
}
