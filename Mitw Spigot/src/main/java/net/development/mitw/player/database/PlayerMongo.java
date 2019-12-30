package net.development.mitw.player.database;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.ReplaceOptions;
import net.development.mitw.database.MongoDB;
import org.bson.Document;

import java.util.UUID;

public class PlayerMongo extends MongoDB {

    private MongoCollection<Document> players;

    public PlayerMongo() {
        super("core");
        this.players = this.getDatabase().getCollection("players");
    }

    public Document getPlayer(UUID uuid) {
        return this.players.find(Filters.eq("uuid", uuid.toString())).first();
    }

    public void replacePlayer(UUID uuid, Document document) {
        this.players.replaceOne(Filters.eq("uuid", uuid.toString()), document, new ReplaceOptions().upsert(true));
    }

}
