//START CODE
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.Gson;
JsonArray jsonArray = new JsonArray();
Gson gson = new Gson();
//Ottengo l'array serializzandolo come stringa Json
String json =gson.toJson(context.jsonArray);
//Deserializzo la stringa nel tipo in cui era stata originariamente creata
jsonArray = gson.fromJson(json, JsonArray.class);

for(int i =0; i<jsonArray.size();i++){

//MAIN CODE
row1.onecmsid = jsonArray.get(i).getAsJsonObject().get("onecmsid").getAsString();
row1.path = jsonArray.get(i).getAsJsonObject().get("path").getAsString();
row1.section = jsonArray.get(i).getAsJsonObject().get("section").getAsString();
row1.url = jsonArray.get(i).getAsJsonObject().get("url").getAsString();

//END CODE
};