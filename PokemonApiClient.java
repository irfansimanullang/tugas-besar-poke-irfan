import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.List;

public class PokemonApiClient {
    private static final String API_URL = "https://pokeapi.co/api/v2/pokemon?limit=30";
    private final OkHttpClient client;
    private final ObjectMapper objectMapper;

    public PokemonApiClient() {
        client = new OkHttpClient();
        objectMapper = new ObjectMapper();
    }

    public List<Pokemon> fetchPokemon() throws IOException {
        Request request = new Request.Builder().url(API_URL).build();
        try (Response response = client.newCall(request).execute()) {
            if (response.isSuccessful() && response.body() != null) {
                String jsonData = response.body().string();
                PokemonApiResponse pokemonResponse = objectMapper.readValue(jsonData, PokemonApiResponse.class);
                return pokemonResponse.results;
            } else {
                throw new IOException("Unexpected code " + response);
            }
        }
    }
}
