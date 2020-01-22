package mitw.bungee.config.impl;

public class Mongo extends Config {

    public static String IP, DATABASE, AUTH_USERNAME, AUTH_PASSWORD, AUTH_DATABASE;
    public static int PORT;
    public static boolean AUTH_ENABLED;

    private Mongo(final String fileName) {
        super(fileName);
    }

    public void loadVars() {
        ConfigCursor cursor = new ConfigCursor(this, "mongo");

        this.IP = cursor.getString("ip");
        this.PORT = cursor.getInt("port");
        this.DATABASE = cursor.getString("database");

        this.AUTH_ENABLED = cursor.getBoolean("authentication.enabled");
        this.AUTH_USERNAME = cursor.getString("authentication.username");
        this.AUTH_PASSWORD = cursor.getString("authentication.password");
        this.AUTH_DATABASE = cursor.getString("authentication.database");

        save();
    }

    public static void init() {
        new Mongo("mongo").loadVars();
    }

}
