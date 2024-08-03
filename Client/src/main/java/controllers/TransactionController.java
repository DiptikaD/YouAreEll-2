package controllers;

import models.Id;
import models.Message;

import java.util.List;

public class TransactionController {
    private String rootURL = "http://zipcode.rocks:8085";
    private MessageController msgCtrl;
    private IdController idCtrl;

    public TransactionController(MessageController m, IdController j) {
        msgCtrl = m;
        idCtrl = j;
    }

    public List<Id> getIds() {
        return idCtrl.getIds();
    }

    public String getId(String id) {
        return null;
    }
    public String putId(Id id) {
        return idCtrl.putId(id).toString();
    }

    public String deleteId(String id) {
        return null;
    }

    public String postId(String name, String githubName) {
         Id tid = new Id(name, githubName);
        System.out.println("Id registered");
         return idCtrl.postId(tid);
    }

    public List<Message> getMessages() {
        return msgCtrl.getMessages();
    }
}
