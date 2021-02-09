package aritra.code.chatters.Models;

public class MessagesModel {

    String userId, message , messageId;
    Long messageTime;
    Boolean hasSeen ;
    int feeling = -1;

    public MessagesModel(String userId, String message, Long messageTime) {
        this.userId = userId;
        this.message = message;
        this.messageTime = messageTime;
    }

    public MessagesModel(String userId, String message) {
        this.userId = userId;
        this.message = message;
    }

    public MessagesModel() {
    }

    public int getFeeling() {
        return feeling;
    }

    public void setFeeling(int feeling) {
        this.feeling = feeling;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Long getMessageTime() {
        return messageTime;
    }

    public void setMessageTime(Long messageTime) {
        this.messageTime = messageTime;
    }

    public Boolean getHasSeen() {
        return hasSeen;
    }

    public void setHasSeen(Boolean hasSeen) {
        this.hasSeen = hasSeen;
    }

}
