package it.polimi.ingsw.server.Lobby;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import it.polimi.ingsw.shared.JsonSupportClasses.JsonUrl;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Scanner;

public class LobbySupport {
    public String jsonCreate() throws FileNotFoundException {
        InputStream inputStream1 = this.getClass().getClassLoader().getResourceAsStream(JsonUrl.getUrl("netConfig"));
        if(inputStream1 == null) throw new FileNotFoundException();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream1));

        JsonObject jsonObject = new Gson().fromJson(bufferedReader, JsonObject.class);

        return jsonObject.get("serverIP").getAsString();
    }
}
