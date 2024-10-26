import com.google.gson.Gson;
import java.awt.Insets;
import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;
import java.util.function.Consumer;

public class DisplayView {
    HttpClient client;
    private JFrame frame;
    private JPanel mainWindow, listPanel,mainPanel,imagePanel, breedInfoPanel;
    private JList<String> catJList;
    private Map<String, Cat> catMap;
    JLabel imageLbl, catNameInfoLbl, catImpWeightInfoLbl, catMetWeightInfoLbl, dogFriendlyInfoLbl, groomingInfoLbl;
    private Set<String> catBreedNames;
    private SearchView searchView;
    private Cat cat;
    private Gson gson;
    private Map<String, ImageIcon> images;
    private ImageIcon loadingGif = new ImageIcon("./catLoading.gif");
    public DisplayView(Map<String,Cat> _catMap, Map<String, ImageIcon> _images, HttpClient _client){
        this.client = _client;
        this.cat = new Cat();
        this.images = _images;
        this.gson = new Gson();
        this.frame = new JFrame();
        this.searchView = new SearchView();
        this.mainWindow = new JPanel();
        this.listPanel = new JPanel();
        this.mainPanel = new JPanel();
        this.imagePanel = new JPanel();
        this.breedInfoPanel = new JPanel();
        this.catMap = _catMap;
        this.imageLbl = new JLabel(loadingGif);
        this.catBreedNames = new HashSet<>();
        this.catJList = new JList<>(getBreedNames());
        this.catNameInfoLbl = new JLabel("");
        this.catImpWeightInfoLbl = new JLabel("");
        this.catMetWeightInfoLbl = new JLabel("");
        this.groomingInfoLbl = new JLabel("");
    }

    private void breedImage(String catID, Consumer<ImageIcon> callback){
        this.imageLbl.setIcon(loadingGif);
        HttpRequest imageRequest = CatApiPractice.sendRequest("images/" + catID);
        client.sendAsync(imageRequest, HttpResponse.BodyHandlers.ofString())
                .thenApply(response -> {
                    try {
                        CatImages catImages = gson.fromJson(response.body(), CatImages.class);
                        BufferedImage bufferedImage = ImageIO.read(catImages.url());
                        return new ImageIcon(bufferedImage.getScaledInstance(400, 600, Image.SCALE_SMOOTH));
                    } catch (Exception e) {
                        System.out.println("Error on getting response: \n" + e.getMessage());
                        return null;
                    }
                })
                .thenAccept(callback)
                .exceptionally(ex -> {
                    System.out.println("Error getting image: " + ex.getMessage());
                    return null;
                });
    }

    private void getImage(){
        this.cat = this.catMap.get(this.catJList.getSelectedValue());
        breedImage(this.cat.getReference_image_id(), imageIcon -> {
            this.imageLbl.setIcon(imageIcon);
        });
    }

    public void display(){

        this.frame.setPreferredSize(new Dimension(1200, 600));
        this.mainWindow.setPreferredSize(new Dimension(1200, 600));
        this.mainWindow.setLayout(new BorderLayout());
        this.listPanel.setPreferredSize(new Dimension(190, 600));
        this.mainPanel.setPreferredSize(new Dimension(390, 600));

        this.breedInfoPanel.add(catInfoPanel());
        this.breedInfoPanel.setPreferredSize(new Dimension(595, 580));
        this.breedInfoPanel.setBorder(new TitledBorder("Cat Information"));

        this.catJList.clearSelection();
        this.catJList.setSelectedIndex(0);
        getImage();
        this.catJList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        this.catJList.addListSelectionListener(e -> {
            getImage();
            this.imageLbl.revalidate();
            this.imageLbl.repaint();
            updateCatInfo();
        });

        JScrollPane scrollPane = new JScrollPane(this.catJList);

        this.listPanel.setBorder(new TitledBorder("Select a Cat"));
        this.listPanel.setLayout(new BorderLayout());
        this.listPanel.add(scrollPane, BorderLayout.CENTER);

        //JLabel imageLbl = new JLabel(getImage(this.cat));
        this.imagePanel.setLayout(new BorderLayout());
        this.imagePanel.setPreferredSize(new Dimension(395, 580));
        this.imagePanel.setBorder(new TitledBorder("Cat Image"));
        this.imagePanel.add(this.imageLbl);

        this.mainPanel.setLayout(new BorderLayout());
        this.mainPanel.add(this.breedInfoPanel, BorderLayout.WEST);
        this.mainPanel.add(this.imagePanel, BorderLayout.EAST);

        this.mainWindow.add(this.mainPanel, BorderLayout.CENTER);
        this.mainWindow.add(this.listPanel, BorderLayout.WEST);
        this.frame.setVisible(true);
        this.frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.frame.setLocationRelativeTo(null);
        this.frame.setContentPane(this.mainWindow);
        this.frame.pack();
    }

    private String[] getBreedNames(){
        ArrayList<String> namesList = new ArrayList<>();
        for(Cat cat: this.catMap.values()){
            namesList.add(cat.getName());
        }
        return namesList.toArray(new String[0]);
    }

    public void updateCatInfo(){
        this.catNameInfoLbl.setText(this.cat.getName());
        this.catMetWeightInfoLbl.setText(this.cat.getMetricWeight());
        this.catImpWeightInfoLbl.setText(this.cat.getImperialWeight());
    }

    /**
     * A method used for adding the information of a cat breed to the display screen.
     * @return JPanel
     */
    public JPanel catInfoPanel(){
        // Create main panel, layout and constraints
        JPanel infoPanel = new JPanel();
        GridBagLayout grid = new GridBagLayout();
        GridBagConstraints constraints = new GridBagConstraints();

        // Set up panel
        infoPanel.setLayout(grid);
        infoPanel.setPreferredSize(new Dimension(595, 580));

        // Add components to panel
        infoPanel.add(infoDisplay(generalInfo(), null, grid, constraints, 0, 0));
        infoPanel.add(infoDisplay(socialInfo(), null, grid, constraints, 0, 1));
        infoPanel.add(infoDisplay(new JPanel(), null, grid, constraints, 0, 2));
        infoPanel.add(infoDisplay(additionalInfo(), null, grid, constraints, 1, 0));
        infoPanel.add(infoDisplay(new JPanel(), null, grid, constraints, 1, 1));


        updateCatInfo();
        return infoPanel;
    }

    /**
     * A method to display breed general information within it's own border in the display
     * @return - JPanel
     */
    private JPanel generalInfo(){
        // Create labels for information titles
        JLabel catNameLbl = new JLabel("Cat Breed Name:    ");
        JLabel catImpWeightLbl = new JLabel("Breed Imperial Weight Range:    ");
        JLabel catMetWeightLbl = new JLabel("Breed Metric Weight Range:    ");

        // Set up the Panel
        JPanel generalInfoPanel = new JPanel();
        generalInfoPanel.setBorder(BorderFactory.createTitledBorder("General Breed Information"));
        generalInfoPanel.setPreferredSize(new Dimension(250, 200));
        GridBagLayout grid = new GridBagLayout();
        GridBagConstraints constraints = new GridBagConstraints();
        generalInfoPanel.setLayout(grid);


        // Assign information to the labels and position components

        generalInfoPanel.add(infoDisplay(catNameLbl, this.catNameInfoLbl, grid, constraints, 0, 0));
        generalInfoPanel.add(infoDisplay(catMetWeightLbl, this.catMetWeightInfoLbl, grid, constraints, 0, 1));
        generalInfoPanel.add(infoDisplay(catImpWeightLbl, this.catImpWeightInfoLbl, grid, constraints, 0, 2));
        generalInfoPanel.add(infoDisplay(new JLabel(), null, grid, constraints, 0, 3));

        return generalInfoPanel;
    }

    /**
     * A method to display the social behaviour ratings of the breed
     * @return - A JPanel
     */
    private JPanel socialInfo(){
        GridBagLayout grid = new GridBagLayout();
        GridBagConstraints constraints = new GridBagConstraints();
        JLabel dogFriendlyLabel = new JLabel("Dog Friendly:   ");
        JPanel socialPanel = new JPanel();
        socialPanel.setBorder(BorderFactory.createTitledBorder("Breed Social Information"));
        socialPanel.setPreferredSize(new Dimension(250, 300));
        socialPanel.setLayout(grid);

        socialPanel.add(infoDisplay(dogFriendlyLabel, this.dogFriendlyInfoLbl, grid, constraints, 0, 0));
        socialPanel.add(infoDisplay(new Panel(), null, grid, constraints, 0, 1));
        return socialPanel;
    }

    /**
     * A method to display additional information about the breed in a panel
     * @return - a JPanel
     */
    private JPanel additionalInfo(){
        GridBagLayout grid = new GridBagLayout();
        GridBagConstraints constraints = new GridBagConstraints();
        JLabel grooming = new JLabel("Grooming:  ");
        JPanel additionalInfoPanel = new JPanel();
        additionalInfoPanel.setBorder(BorderFactory.createTitledBorder("Additional Breed Information"));
        additionalInfoPanel.setPreferredSize(new Dimension(250, 550));
        additionalInfoPanel.setLayout(grid);

        additionalInfoPanel.add(infoDisplay(grooming, this.groomingInfoLbl, grid, constraints, 0, 0));
        additionalInfoPanel.add(infoDisplay(new Panel(), null, grid, constraints, 0, 1));

        return additionalInfoPanel;
    }

    /**
     * A method for set the GridBagConstraints
     * @param x - int
     * @param y - int
     * @return - GridBagConstraints
     */
    private GridBagConstraints setConstraints(int x, int y){
        GridBagConstraints c = new GridBagConstraints();

        c.gridx = x;
        c.gridy = y;
        c.anchor = GridBagConstraints.WEST;
        c.gridwidth = 1;
        c.gridheight = 1;
        c.fill = GridBagConstraints.BOTH;
        c.insets = new Insets(1, 1, 1, 10);
        c.weightx = 0.5;
        c.weighty = 0.5;
        c.ipadx = 1;

        return c;
    }

    /**
     * A method for setting up a panel in the correct format required for the display view
     * @param title - JLabel containing title text
     * @param data - JLabel that shows the breed info
     * @param grid - GridBagLayout
     * @param constraints - GridBagConstraint
     * @param x - int
     * @param y - int
     * @return - JPanel
     */
    private JPanel infoDisplay(Component title, Component data, GridBagLayout grid, GridBagConstraints constraints, int x, int y){
        // Create new panel
        JPanel panel = new JPanel();
        //set constraints
        constraints = setConstraints(x, y);
        // Set layout for panel
        panel.setLayout(new BorderLayout());
        // Add components to panel
        panel.add(title, BorderLayout.WEST);
        if(data != null) panel.add(data);
        // Assign panel and constraints to grid
        grid.setConstraints(panel, constraints);
        return panel;
    }
}
