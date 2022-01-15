package lilja.kiiski.doitlist;

import java.util.HashMap;
import java.util.Map;

/* DATA CLASS
- Just for use with Json/Gson
- Helper Tool
 */
public class Data {
    private Map<String, Map<String, String>> map;

    public Data (){
        map = new HashMap<>();
    }

    public Map<String, Map<String, String>> getMap() {
        return map;
    }
}
