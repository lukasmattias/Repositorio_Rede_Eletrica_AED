package gui;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;

public class SistemaRedeEletricaController {
    private Controller controller;
    private JFrame window = new JFrame();
    private Canvas canvas;

    private final int WINDOW_WIDTH_OFFSET = 18; //18
    private final int WINDOW_HEIGHT_OFFSET = 47; //47

    public Window(Controller controller, Canvas canvas) {
        this.controller = controller;
        this.canvas = canvas;

        ImageIcon imageIcon = new ImageIcon("images/icon.png");

        window.setIconImage(imageIcon.getImage());
        window.setSize(500+WINDOW_WIDTH_OFFSET, 300+WINDOW_HEIGHT_OFFSET);
        window.setTitle("Graph visualization");
        window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        window.add(canvas);
        window.setVisible(true);
        controller.setWindow(window);
    @FXML
    private BorderPane root;

    // Adicione os elementos de interface conforme definidos no arquivo FXML

    @FXML
    private void initialize() {
        // Inicializa��o da interface, configura��es, etc.
    }

    // Adicione m�todos para manipular eventos da interface
}
