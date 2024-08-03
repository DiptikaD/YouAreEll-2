package controllers;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import models.Id;
import models.Message;
import youareell.URLShell;

public class MessageController {
    ServerController sc;

    private HashSet<Message> messagesSeen;
    // why a HashSet??

    public MessageController(ServerController shared) {
        sc = shared;
        messagesSeen = new HashSet<Message>();
    }
    public ArrayList<Message> getMessages(Id id) {
        if (id == null) {
            String jsonInput = sc.getMessages();
            // convert json to array of Ids
            ObjectMapper mapper = new ObjectMapper();
            List<Message> msgs;
            try {
                msgs = mapper.readValue(jsonInput, mapper.getTypeFactory().constructCollectionType(List.class, Message.class));

                ArrayList<Message> msgList = new ArrayList<>(msgs);
                // return array of Ids
                return msgList;
            } catch (JsonMappingException e) {
                System.out.println("Error processing JSON from response: " + e.getMessage());
            } catch (JsonProcessingException e) {
                System.out.println("Error processing JSON from response: " + e.getMessage());
            }
            return null;
        } else {
            String jsonInput = sc.getUserMessages(id);
            // convert json to array of Ids
            ObjectMapper mapper = new ObjectMapper();
            List<Message> msgs;

            try {
                msgs = mapper.readValue(jsonInput, mapper.getTypeFactory().constructCollectionType(List.class, Message.class));

                try {
                    ArrayList<Message> msgList = new ArrayList<>(msgs);
                    return msgList;
                }
                catch (NullPointerException e){
                    System.out.println("You dont have any messages \uD83D\uDC94");
                }
                // return array of Ids
            } catch (JsonMappingException e) {
                System.out.println("Error processing JSON from response: " + e.getMessage());
            } catch (JsonProcessingException e) {
                System.out.println("Error processing JSON from response: " + e.getMessage());
            }
            return null;}
    }
    public Message getMessageForSequence(String seq) {
        return null;
    }
    public ArrayList<Message> getMessagesFromFriend(Id myId, Id friendId) {
        return null;
    }

    public String postMessage(Message msg) {
        return sc.postUserMessage(msg);

    }
 
}