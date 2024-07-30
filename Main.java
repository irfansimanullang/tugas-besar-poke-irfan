import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class Main {
    private final JFrame frame;
    private final JList<String> pokemonList;
    private final JLabel statusBar;
    private List<Pokemon> pokemons;

    public Main() {
        frame = new JFrame("Pencarian Pokemons");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);

        pokemonList = new JList<>();
        pokemonList.setBackground(Color.CYAN);
        pokemonList.setFont(new Font("Arial", Font.PLAIN, 16));
        frame.add(new JScrollPane(pokemonList), BorderLayout.CENTER);

        statusBar = new JLabel("pilih sebuah pokemon untuk melihat detilnya");
        frame.add(statusBar, BorderLayout.SOUTH);

        JMenuBar menuBar = new JMenuBar();
        JMenu menu = new JMenu("Options");
        JMenuItem exitItem = new JMenuItem("Exit");
        exitItem.addActionListener(e -> System.exit(0));
        menu.add(exitItem);
        menuBar.add(menu);
        frame.setJMenuBar(menuBar);

        pokemonList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int selectedIndex = pokemonList.getSelectedIndex();
                if (selectedIndex != -1) {
                    Pokemon selectedPokemon = pokemons.get(selectedIndex);
                    showPokemonDetails(selectedPokemon);
                    statusBar.setText("Selected Pokémon: " + selectedPokemon.name);
                }
            }
        });
    }

    public void show() {
        frame.setVisible(true);
    }

    public void updatePokemonList(List<Pokemon> pokemons) {
        this.pokemons = pokemons;
        DefaultListModel<String> listModel = new DefaultListModel<>();
        for (Pokemon pokemon : pokemons) {
            listModel.addElement(pokemon.name);
        }
        pokemonList.setModel(listModel);
    }

    private void showPokemonDetails(Pokemon pokemon) {
        try {
            PokemonDetailsApiClient detailsClient = new PokemonDetailsApiClient();
            Pokemon detailedPokemon = detailsClient.fetchPokemonDetails(pokemon.url);

            StringBuilder types = new StringBuilder();
            if (detailedPokemon.types != null) {
                for (Pokemon.Type type : detailedPokemon.types) {
                    types.append(type.type.name).append(" ");
                }
            }

            String imageUrl = "https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/other/official-artwork/"
                    + detailedPokemon.id + ".png";

            Icon icon = null;
            if (imageUrl != null) {
                try {
                    icon = new ImageIcon(new ImageIcon(new URL(imageUrl))
                            .getImage().getScaledInstance(200, 200, Image.SCALE_SMOOTH));
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
            }

            JDialog detailsDialog = new JDialog(frame, "Pokémon Details", true);
            detailsDialog.setLayout(new BorderLayout());
            JLabel nameLabel = new JLabel("Name: " + detailedPokemon.name);
            JLabel typesLabel = new JLabel("Types: " + types.toString().trim());
            JLabel urlLabel = new JLabel("URL: " + pokemon.url);
            detailsDialog.add(nameLabel, BorderLayout.NORTH);
            detailsDialog.add(typesLabel, BorderLayout.CENTER);
            detailsDialog.add(urlLabel, BorderLayout.SOUTH);

            if (icon != null) {
                detailsDialog.add(new JLabel(icon), BorderLayout.EAST);
            }

            detailsDialog.setSize(400, 300);
            detailsDialog.setVisible(true);
        } catch (IOException e) {
            JOptionPane.showMessageDialog(frame, "gagal mengambil data, error cuk", JOptionPane.ERROR_MESSAGE);
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Main app = new Main();
            app.show();

            PokemonApiClient fetcher = new PokemonApiClient();
            try {
                List<Pokemon> pokemons = fetcher.fetchPokemon();
                app.updatePokemonList(pokemons);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(app.frame, "gagal mengambil data, error cuk", JOptionPane.ERROR_MESSAGE);
                e.printStackTrace();
            }
        });
    }
}
