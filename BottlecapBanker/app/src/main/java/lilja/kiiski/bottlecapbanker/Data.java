package lilja.kiiski.bottlecapbanker;

import java.util.HashMap;
import java.util.Map;

public class Data {
    private Map<String, Map<String, String>> map;

    public Data (){
        map = new HashMap<>();
    }

    public Data(Map<String, Map<String, String>> map) {
        this.map = map;
    }

    public Map<String, Map<String, String>> getMap() {
        return map;
    }

    public void setMap(Map<String, Map<String, String>> map) {
        this.map = map;
    }

}
