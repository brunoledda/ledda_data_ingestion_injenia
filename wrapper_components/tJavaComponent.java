import com.google.cloud.pubsub.v1.AckReplyConsumer;
import com.google.cloud.pubsub.v1.MessageReceiver;
import com.google.cloud.pubsub.v1.Subscriber;
import com.google.pubsub.v1.ProjectSubscriptionName;
import com.google.pubsub.v1.PubsubMessage;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.ConcurrentLinkedQueue;

String projectId = "project-id";
String subscriptionId = "subscription-id";
ProjectSubscriptionName subscriptionName = ProjectSubscriptionName.of(projectId, subscriptionId);
//Istanziare una coda di JsonObject
ConcurrentLinkedQueue<JsonObject> jsonArray = new ConcurrentLinkedQueue<>();
//Istanziare un ricevitore di messaggi asincrono
MessageReceiver receiver =
        (PubsubMessage message, AckReplyConsumer consumer) -> {
    String jsonString =
    message.getData().toStringUtf8();
    //Aggiungere virgolette intorno ai nomi delle chiavi e dei valori della stringa
    jsonString = jsonString.replaceAll("(\\{|,)(\\s*)([^:\\s]+)(\\s*):", "$1\"$3\":");
    jsonString = jsonString.replaceAll(":([^,}\\]]+)", ":\"$1\"");
    //Serializzazione della stringa in jsonObject
    Gson gson = new Gson();
    JsonObject jsonObject = gson.fromJson(jsonString,JsonObject.class);
    jsonArray.add(jsonObject);
    System.out.println("JSON Object: " + jsonObject);
    consumer.ack();
};
//Instanziare un sottoscrittore asincrono che invia la conferma di ricezione del messaggio
Subscriber subscriber = null;
try {
      subscriber = Subscriber.newBuilder(subscriptionName, receiver).build();
      subscriber.startAsync().awaitRunning();
      System.out.printf("Listening for messages on %s:\n", subscriptionName.toString());
      //Consentire al subscriber di ascoltare messaggi per 10s a meno che non si verifichi un errore
      subscriber.awaitTerminated(10, TimeUnit.SECONDS);
} catch (TimeoutException timeoutException) {
      subscriber.stopAsync();
}
try{
Thread.sleep(13000); 
	JsonArray jsonArrayResult = new JsonArray();
    	for (JsonObject jsonObject : jsonArray) {
    		jsonArrayResult.add(jsonObject);
    	}
    	context.jsonArray= jsonArrayResult;
}catch (InterruptedException e){
	System.out.println("exception: "+ e);
}