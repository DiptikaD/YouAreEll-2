package youareell;

import java.util.List;

import controllers.*;
import models.Id;
import models.Message;

public class YouAreEll {
    private TransactionController tt;

    public YouAreEll (TransactionController t) {
        this.tt = t;
    }

    public static void main(String[] args) {
        // hmm: is this Dependency Injection?
        YouAreEll urlhandler = new YouAreEll(
            new TransactionController(
                new MessageController(ServerController.shared()), 
                new IdController(ServerController.shared())
        ));
    }

    public String get_ids() {
        List<models.Id> allIds = tt.getIds();
        StringBuilder sb = new StringBuilder();
        for (models.Id id : allIds) {
            sb.append(id.toString()+"\n");
        }
        return sb.toString();
    }

    public String get_messages(String githubId) {
        if (githubId == null) {
            List<models.Message> latestMessages = tt.getMessages();
            StringBuilder sb = new StringBuilder();
            for (models.Message msg : latestMessages) {
                sb.append(msg.toString() + "\n");
            }
            return sb.toString();
        }

        Id currentPerson = checkExistingGithubId(githubId);
        if (currentPerson !=null){
            List<models.Message> updatedMessages = tt.getMessagesForId(currentPerson);
            StringBuilder sb = new StringBuilder();
            for (models.Message msg : updatedMessages) {
                sb.append(msg.toString() + "\n");
            }
            return sb.toString();
        } else {
            return null;
        }
    }

    public String postMessage(String fromGithubId, String toGithubId, String message){
        Id checkedToId = checkExistingGithubId(toGithubId);
        Id checkedFromId = checkExistingGithubId(fromGithubId);
        if (checkedFromId !=null && checkedToId==null){
            Message postMessageToAll = new Message(message, fromGithubId, "");
            return tt.postMessage(postMessageToAll);
        } else if (checkedFromId !=null && checkedToId!=null){
            Message postMessage = new Message(message, fromGithubId, toGithubId);
            return tt.postMessage(postMessage);
        }
        return "message not posted, users not registered";
    }


    public String postOrPutId(String name, String githubId) {

        Id checkedId = checkExistingGithubId(githubId);

        if (checkedId != null){

            if (!name.equals(checkedId.getName())){
                checkedId.setName(name);
                return putId(checkedId);
            }
            return "Name is same on the server for this github id.";
        } else {
            //else if id doesnt exist, create new entry === post
            Id newId = new Id(name, githubId);
            return postId(newId);
        }

    }

    public String putId(Id id) {
        return tt.putId(id);
    }

    public String postId(Id id){
        return tt.postId(id.getName(), id.getGithub());
        //else if id doesnt exist, create new entry === post
    }

    public Id checkExistingGithubId(String checkID){
        List<models.Id> allIds = tt.getIds();
        for (Id element : allIds){
            if (element.getGithub().equals(checkID)){
                return element;
            }
        }
        return null;
    }


}
