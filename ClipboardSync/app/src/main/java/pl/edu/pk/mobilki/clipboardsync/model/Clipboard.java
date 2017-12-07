package pl.edu.pk.mobilki.clipboardsync.model;


/**
 * Created by mati on 2017-12-04.
 */

public class Clipboard {
    private long id;
    private String text;
    private String data_time;

    public Clipboard(long id, String text, String data_time){
        this.id = id;
        this.text = text;
        this.data_time = data_time;
    }

    public long getId(){
        return id;
    }

    public String getText(){
        return text;
    }

    public String getData_time(){
        return data_time;
    }
}
