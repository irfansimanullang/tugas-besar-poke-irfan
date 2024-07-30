import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;

public class PokemonDetailsApiClient {
    private final OkHttpClient client;
    private final ObjectMapper objectMapper;

    public PokemonDetailsApiClient() {
        client = new OkHttpClient();
        objectMapper = new ObjectMapper();
    }

    public Pokemon fetchPokemonDetails(String url) throws IOException {
        Request request = new Request.Builder().url(url).build();
        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful() && response.body() != null) {
                String jsonData = response.body().string();
                return objectMapper.readValue(jsonData, Pokemon.class);
            } else {
                throw new IOException("Unexpected code " + response);
            }
        }
    }
}
