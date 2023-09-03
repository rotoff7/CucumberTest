package testing.data;

import java.util.HashMap;
import java.util.Map;

public class DataManager {

    private static DataManager dataManager = new DataManager();

    private DataManager() {
    }

    public static DataManager getDataManager() {
        return dataManager;
    }

    private static HashMap<String, String> map = new HashMap<>();

    public String getDataValue(String key) {
        return map.get(key);
    }

    public void saveDataValue(String key, String value) {
        this.map.put(key, value);
    }
    public Map<String, String> getMap(){
        return map;
    }
}
