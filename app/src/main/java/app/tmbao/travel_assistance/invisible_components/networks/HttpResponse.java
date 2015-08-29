package app.tmbao.travel_assistance.invisible_components.networks;

/**
 * Created by tmbao on 8/24/2015.
 */
public class HttpResponse {
    private String content;
    private int code;

    public HttpResponse(String content, int code) {
        this.content = content;
        this.code = code;
    }

    public String getContent() {
        return content;
    }

    public int getCode() {
        return code;
    }
}
