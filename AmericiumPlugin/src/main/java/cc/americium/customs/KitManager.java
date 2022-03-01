package cc.americium.customs;

import static com.mongodb.client.model.Filters.eq;

import com.mongodb.*;
import com.mongodb.client.*;
import com.mongodb.client.result.DeleteResult;
import org.bson.Document;
import org.bson.conversions.Bson;
import org.bson.types.ObjectId;

import java.util.logging.Level;

import java.util.Objects;

public class KitManager {

    static MongoClient mongoClient = MongoClients.create("mongodb+srv://americiumcodedb:jPt6uBz5egHpSiF@americiumdb.hab19.mongodb.net");
    static MongoDatabase database = mongoClient.getDatabase("Americium");


    public KitManager() throws ClassNotFoundException {
    }

    public static MongoCollection<Document> getKitCollection() {
        return database.getCollection("Kits");
    }

    public static String getKit(MongoCollection<Document> kit_collection, String kit_name) {
        try{
            return kit_collection.find(eq("kit_name", kit_name)).first().getString("kit");
        } catch (Exception e){
            return null;
        }
    }

    public static FindIterable<Document> getKits(MongoCollection<Document> kit_collection) {
        return kit_collection.find();
    }

    public static void saveKit(MongoCollection<Document> kit_collection, String kit_inventory, String creator_username, String kit_name, String creator_uuid) {
        Document kit = new Document("_id", new ObjectId());
        kit.append("kit", kit_inventory)
                .append("creator", creator_username)
                .append("kit_name", kit_name)
                .append("creator_uuid", creator_uuid);
        kit_collection.insertOne(kit);
    }

    public static boolean deleteKit(MongoCollection<Document> kit_collection, String kit_name, String creator_uuid) {
        Bson query = eq("kit_name", kit_name);
        try {
            String check_uuid = kit_collection.find(eq("kit_name", kit_name)).first().getString("creator_uuid");

            if (Objects.equals(creator_uuid, check_uuid)) {
                try {
                    DeleteResult result = kit_collection.deleteOne(query);
                    return true;
                } catch (MongoException me) {
                    return false;
                }
            } else {
                return false;
            }
        } catch (Exception e){
            return false;
        }
    }
}
