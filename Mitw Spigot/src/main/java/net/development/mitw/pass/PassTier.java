package net.development.mitw.pass;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import lombok.Getter;
import lombok.Setter;
import net.development.mitw.Mitw;
import net.development.mitw.json.JsonChain;

import java.util.List;
import java.util.Map;

@Getter
@Setter
public class PassTier {

    private final int tier;
    private List<String> freeCommand;
    private List<String> premiumCommand;

    public PassTier(int tier) {
        this.tier = tier;
    }

    public PassTier(JsonObject jsonObject) {
        tier = jsonObject.get("tier").getAsInt();

        JsonArray array = jsonObject.get("freeCommands").getAsJsonArray();

        for (int i = 0; i < array.size(); i++) {
            freeCommand.add(array.get(i).getAsString());
        }

        array = jsonObject.get("premiumCommand").getAsJsonArray();

        for (int i = 0; i < array.size(); i++) {
            premiumCommand.add(array.get(i).getAsString());
        }
    }

    public JsonObject toJsonObject() {

        JsonElement freeCommands = Mitw.getInstance().getGson().toJsonTree(freeCommand);
        JsonElement premiumCommands = Mitw.getInstance().getGson().toJsonTree(premiumCommand);

        return new JsonChain().addProperty("tier",tier).add("freeCommands", freeCommands).add("premiumCommands", premiumCommands).get();
    }

}
