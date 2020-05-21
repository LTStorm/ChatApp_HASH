
import java.io.Serializable;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * container for any type of object to be sent
 *  
 */
public class VoiceCallMessage implements Serializable{

    private final Object data; //can carry any type of object. in this program, i used a sound packet, but it could be a string, a chunk of video, ...

    
    public VoiceCallMessage(Object data) {
        this.data = data;
    }

    public Object getData() {
        return data;
    }

}
