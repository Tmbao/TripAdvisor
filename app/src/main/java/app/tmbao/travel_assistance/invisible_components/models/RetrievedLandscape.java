package app.tmbao.travel_assistance.invisible_components.models;

import java.util.List;

/**
 * Created by tmbao on 8/25/2015.
 */
public class RetrievedLandscape {

    public enum Status {
        PENDING, COMPLETED;

        @Override
        public String toString() {
            switch (this) {
                case PENDING:
                    return "Pending";
                case COMPLETED:
                    return "Completed";
                default:
                    return null;
            }
        }

        public static Status parse(String statusString) {
            switch (statusString) {
                case "Pending":
                    return PENDING;
                case "Completed":
                    return COMPLETED;
                default:
                    return null;
            }
        }
    }

    private int id;
    private Status status;
    private List<String> content;

    public RetrievedLandscape(int id, Status status, List<String> content) {
        this.id = id;
        this.status = status;
        this.content = content;
    }

    public int getId() {
        return id;
    }

    public Status getStatus() {
        return status;
    }

    public List<String> getContent() {
        return content;
    }
}
