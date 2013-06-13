package com.tengen.week3.homework1;

import com.mongodb.*;

import java.net.UnknownHostException;
import java.util.ArrayList;

public class Main {

    public static void main(String[] args) throws UnknownHostException {
        final MongoClient mongoClient = new MongoClient(new MongoClientURI("mongodb://localhost"));
        final DB blogDatabase = mongoClient.getDB("school");
        DBCollection students = blogDatabase.getCollection("students");

        for (DBObject student : students.find()) {
            ArrayList<DBObject> scores = (ArrayList<DBObject>) student.get("scores");
            DBObject lowestHomeworkScore = null;
            for (DBObject score : scores) {
                if(score.get("type").equals("homework")) {
                    if (lowestHomeworkScore == null) {
                        lowestHomeworkScore = score;
                    } else if (((Double) score.get("score")) < ((Double) lowestHomeworkScore.get("score"))) {
                        lowestHomeworkScore = score;
                    }
                }
            }
            scores.remove(lowestHomeworkScore
            );
            students.update(QueryBuilder.start("_id").is(student.get("_id")).get(), new BasicDBObject("$set", new BasicDBObject("scores", scores)));
        }
    }
}
