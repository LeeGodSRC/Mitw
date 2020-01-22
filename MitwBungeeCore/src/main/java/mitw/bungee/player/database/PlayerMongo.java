package mitw.bungee.player.database;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import mitw.bungee.database.MongoDB;
import org.bson.Document;

import java.util.UUID;

public class PlayerMongo extends MongoDB {

    private MongoCollection<Document> players;

    public PlayerMongo() {
        super("core");
        this.players = this.getDatabase().getCollection("player_languages");
    }

    public Document getPlayer(UUID uuid) {
        return this.players.find(Filters.eq("uuid", uuid.toString())).first();
    }

    public void replacePlayer(UUID uuid, Document document) {
        this.players.replaceOne(Filters.eq("uuid", uuid.toString()), document, this.replaceOptions());
    }

}
