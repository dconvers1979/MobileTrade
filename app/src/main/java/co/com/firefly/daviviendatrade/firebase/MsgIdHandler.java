package co.com.firefly.daviviendatrade.firebase;

public class MsgIdHandler {

    private Double messageId = Math.random()*100+1000;

    private static MsgIdHandler ourInstance = new MsgIdHandler();

    public static MsgIdHandler getInstance() {
        return ourInstance;
    }

    private MsgIdHandler() {
    }

    public String getMessageId(){
        return ""+messageId++;
    }
}
