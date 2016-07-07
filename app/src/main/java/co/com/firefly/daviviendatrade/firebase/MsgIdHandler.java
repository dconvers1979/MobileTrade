package co.com.firefly.daviviendatrade.firebase;

/**
 * Created by toshiba on 06/07/2016.
 */
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
