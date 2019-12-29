package net.development.mitw.database;

import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoDatabase;
import lombok.Getter;
import net.development.mitw.config.Mongo;

import java.util.Collections;

@Getter
public class MongoDB {

    private MongoClient client;
    private MongoDatabase database;

    public MongoDB(String database) {

        System.out.println("Connecting to MongoDB " + Mongo.IP + ":" + Mongo.PORT + "...");

       if (Mongo.AUTH_ENABLED) {
           MongoCredential credential = MongoCredential.createCredential(
                   Mongo.AUTH_USERNAME,
                   Mongo.AUTH_DATABASE,
                   Mongo.AUTH_PASSWORD.toCharArray()
           );

           this.client = new MongoClient(new ServerAddress(Mongo.IP, Mongo.PORT), Collections.singletonList(credential));
       } else {
           this.client = new MongoClient(new ServerAddress(Mongo.IP, Mongo.PORT));
       }

       this.database = this.client.getDatabase(database);
    }

}
