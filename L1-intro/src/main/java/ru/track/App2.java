package ru.track;


import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.apache.commons.lang3.StringUtils;

public class App2 {

    public static void main(String[] args) throws UnirestException {
        HttpResponse<JsonNode> r = Unirest.post("https://guarded-mesa-31536.herokuapp.com/track")
                .header("accept", "application/json")
                .field("name", "Иван")
                .field("github", "https://github.com/MiloMilk")
                .field("email", "nyno4ek.olo@gmail.com")
                .asJson();


        r.getBody().getObject().get("success");
// Должен венруть true
    }
}
