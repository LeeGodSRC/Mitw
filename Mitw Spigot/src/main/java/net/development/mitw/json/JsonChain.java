package net.development.mitw.json;

import com.google.gson.*;

public class JsonChain
{
    private JsonObject json;

    public JsonChain() {
        this.json = new JsonObject();
    }

    public JsonChain addProperty(final String property, final String value) {
        this.json.addProperty(property, value);
        return this;
    }

    public JsonChain addProperty(final String property, final Number value) {
        this.json.addProperty(property, value);
        return this;
    }

    public JsonChain addProperty(final String property, final Boolean value) {
        this.json.addProperty(property, value);
        return this;
    }

    public JsonChain addProperty(final String property, final Character value) {
        this.json.addProperty(property, value);
        return this;
    }

    public JsonChain add(final String property, final JsonElement element) {
        this.json.add(property, element);
        return this;
    }

    public JsonObject get() {
        return this.json;
    }
}
