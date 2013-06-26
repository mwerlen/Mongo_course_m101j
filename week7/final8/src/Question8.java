import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.MongoURI;
import com.mongodb.Mongo;
import com.mongodb.DB;

import java.io.IOException;
import java.lang.management.OperatingSystemMXBean;

public class Question8 {

    public static void main(String[] args) throws IOException {
        Mongo c =  new Mongo(new MongoURI("mongodb://localhost"));
        DB db = c.getDB("test");
        DBCollection animals = db.getCollection("animals");

        BasicDBObject animal = new BasicDBObject("animal", "monkey");

        animals.insert(animal);
        System.out.println(db.getLastError());
        animal.removeField("animal");
        animal.append("animal", "cat");
        animals.insert(animal);
        System.out.println(db.getLastError());
        animal.removeField("animal");
        animal.append("animal", "lion");
        animals.insert(animal);
        System.out.println(db.getLastError());

    }

}
