package app.tmbao.travel_assistance.invisible_components.models;

/**
 * Created by tmbao on 8/24/2015.
 */
public class LandscapeResource {
    public enum ResourceType {
        IMAGE, YOUTUBE, WEBSITE, WIKIPEDIA;

        public String toString() {
            switch (this) {
                case IMAGE:
                    return "Image";
                case YOUTUBE:
                    return "Youtube";
                case WEBSITE:
                    return "Website";
                case WIKIPEDIA:
                    return "Wikipedia";
                default:
                    return null;
            }
        }

        public static ResourceType parse(String stringType) {
            switch (stringType) {
                case "Image":
                    return IMAGE;
                case "Youtube":
                    return YOUTUBE;
                case "Website":
                    return WEBSITE;
                case "Wikipedia":
                    return WIKIPEDIA;
                default:
                    return null;
            }
        }
    }

    private ResourceType type;
    private String content;

    public LandscapeResource(ResourceType type, String content) {
        this.type = type;
        this.content = content;
    }

    public ResourceType getType() {
        return type;
    }

    public String getContent() {
        return content;
    }
}
