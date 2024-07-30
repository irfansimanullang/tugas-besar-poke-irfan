import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Pokemon {
    public int id;
    public String name;
    public String url;
    public List<Type> types;
    public Sprites sprites;

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Type {
        public TypeInfo type;

        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class TypeInfo {
            public String name;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Sprites {
        public Other other;

        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class Other {
            public OfficialArtwork official_artwork;

            @JsonIgnoreProperties(ignoreUnknown = true)
            public static class OfficialArtwork {
                public String front_default;
            }
        }
    }
}
