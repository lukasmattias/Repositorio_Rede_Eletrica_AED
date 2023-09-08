package gui;

import java.util.Scanner;
import exception.OperacaoInvalidaException;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.TextAlignment;
import negocio.beans.Departamento;
import negocio.beans.Aresta;
import negocio.beans.Grafo;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class SistemaRedeEletricaController {
	private Stage stage;
    private Scene scene;
    private Parent root;
    Grafo grafo = Grafo.getInstance();
    
    @FXML
    private Button AdcNo;

    @FXML
    private Button AdcAresta;

    @FXML
    private Button RmvAresta;
    
    @FXML
    private Button CalAGM;
    
    @FXML
    private Button Relatorio;
    
    @FXML
    private Canvas canvas;
    
    private GraphicsContext gc;
    private double escala = 1.0;
    
    public void initialize() {
        
        canvas.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {      
            // Obtendo as coordenadas do clique
            double x = event.getX();
            double y = event.getY();
            boolean localOcupado = false;
            for (Departamento departamento : grafo.getDepartamentos()) {
                if (Math.abs(event.getX() - departamento.getX()) <= 15 && Math.abs(event.getY() - departamento.getY()) <= 15) {
                    // Clique no Departamento (nó), você pode acessar as informações diretamente
                    System.out.println("Clicou no Departamento: " + departamento.getNome() + " (ID: " + departamento.getId() + ")");
                    System.out.println("Número de Pessoas: " + departamento.getNumPessoas());
                    localOcupado = true;
                }
            }
            if (!localOcupado) {
            	criarNo(x, y);	
            }
            });
        
    }
    
    private void criarNo(double x, double y) {
        Stage no = new Stage();
        no.initModality(Modality.APPLICATION_MODAL); // Bloqueia a janela principal
        no.setTitle("Novo departamento");
   
        GridPane grid = new GridPane(); // Organizando as labels e textfield em um Gridpane
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(10, 10, 10, 10));

        Label nome = new Label("Nome do departamento:");
        TextField nomeDepartamento = new TextField();
        Label numPessoas = new Label("Número de pessoas:");
        TextField pessoasDepartamento = new TextField();
        Button okButton = new Button("OK");
        
        grid.add(nome, 0, 0); // Adicionando as labels e textfield no Gridpane
        grid.add(nomeDepartamento, 1, 0);
        grid.add(numPessoas, 0, 1);
        grid.add(pessoasDepartamento, 1, 1);

        HBox buttonBox = new HBox(10);
        buttonBox.getChildren().add(okButton);
        HBox.setMargin(okButton, new Insets(0, 0, 0, 10));
        
        VBox vbox = new VBox(10);
        vbox.getChildren().addAll(grid, buttonBox);
        vbox.setPadding(new Insets(10, 10, 10, 10));
        
        Scene dialogScene = new Scene(vbox, 400, 150);
        no.setScene(dialogScene);   
        
        okButton.setOnAction(e -> {       	
        	try {	
        		int numero = Integer.parseInt(pessoasDepartamento.getText());
        		grafo.adicionarDepartamento(nomeDepartamento.getText(), numero, x, y);
        		gc = canvas.getGraphicsContext2D();
                gc.setFill(Color.BLACK);
                // Configurando o alinhamento do texto para centralizar no ponto
                gc.setTextAlign(TextAlignment.CENTER);
                gc.setTextBaseline(javafx.geometry.VPos.CENTER);
        		// Desenhando um círculo no local do clique
                double raio = 10; // Tamanho do ponto 
                gc.fillOval(x - raio, y - raio, raio * 2, raio * 2);
                gc.setFill(Color.WHITE);
                // Escreva texto dentro do ponto criado
                Departamento aux = grafo.getDepartamentos().get(grafo.getDepartamentos().size()-1);
                String texto =  String.valueOf(aux.getId());
                gc.fillText(texto, x, y);
                gc.setFill(Color.BLACK);
        	}
        	catch (OperacaoInvalidaException a) {
        		Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Departamento não adicionado.");
                alert.setHeaderText(a.getMessage());
                alert.showAndWait();
        	}
        	catch (NumberFormatException er) {
        		Alert alert = new Alert(Alert.AlertType.ERROR);
        	    alert.setTitle("Erro");
        	    alert.setHeaderText("Número de pessoas inválido");
        	    alert.setContentText("Por favor, insira um número válido para o número de pessoas.");
        	    alert.showAndWait();
        	}
            
            no.close(); // Fecha a janela de diálogo
        });

        no.showAndWait(); // Exibe a janela de diálogo e aguarda sua conclusão
    }
    
    @FXML
    public void gerarRelatorio() {
    	
    }
    
    @FXML
    public void calcularAGM() {
    	
    }
    
    @FXML
    public void removerAresta() {
    	Stage no = new Stage();
        no.initModality(Modality.APPLICATION_MODAL); // Bloqueia a janela principal
        no.setTitle("Remover aresta");
   
        GridPane grid = new GridPane(); // Organizando as labels e textfield em um Gridpane
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(10, 10, 10, 10));

        Label origem = new Label("índice do primeiro nó: ");
        TextField origemNo = new TextField();
        Label destino = new Label("Índice do segundo nó: ");
        TextField destinoNo = new TextField();
        
        Button okButton = new Button("OK");
        
        grid.add(origem, 0, 0); // Adicionando as labels e textfield no Gridpane
        grid.add(origemNo, 1, 0);
        grid.add(destino, 0, 1);
        grid.add(destinoNo, 1, 1);

        HBox buttonBox = new HBox(10);
        buttonBox.getChildren().add(okButton);
        HBox.setMargin(okButton, new Insets(0, 0, 0, 10));
        
        VBox vbox = new VBox(10);
        vbox.getChildren().addAll(grid, buttonBox);
        vbox.setPadding(new Insets(10, 10, 10, 10));
        
        Scene dialogScene = new Scene(vbox, 400, 150);
        no.setScene(dialogScene);  
        // Consumir a quebra de linha ap�s o n�mero
        
        okButton.setOnAction(e -> {       	
        	try {
                int indiceOrigem = Integer.parseInt(origemNo.getText());
                int indiceDestino = Integer.parseInt(destinoNo.getText());
                
                if (grafo.existeAresta(indiceOrigem, indiceDestino)) {
        			grafo.removerAresta(indiceOrigem, indiceDestino);
        			 atualizarCanvas();
        		}
                else {
                	throw new OperacaoInvalidaException("Não há aresta entre os dois nós.");
                }
                }
        	catch (OperacaoInvalidaException a) {
        		Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Aresta não adicionada.");
                alert.setHeaderText(a.getMessage());
                alert.showAndWait();
        	}
        	catch (NumberFormatException er) {
        		Alert alert = new Alert(Alert.AlertType.ERROR);
        	    alert.setTitle("Erro");
        	    alert.setHeaderText("Número de pessoas inválido");
        	    alert.setContentText("Por favor, insira um número válido para o número de pessoas.");
        	    alert.showAndWait();
        	}
            
            no.close(); // Fecha a janela de diálogo
        });

        no.showAndWait(); // Exibe a janela de diálogo e aguarda sua conclusão
    }
    
    
    
    @FXML
    public void adicionarAresta() {
    	Stage no = new Stage();
        no.initModality(Modality.APPLICATION_MODAL); // Bloqueia a janela principal
        no.setTitle("Nova aresta");
        
        GridPane grid = new GridPane(); // Organizando as labels e textfield em um Gridpane
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(10, 10, 10, 10));

        Label origem = new Label("índice de origem: ");
        TextField origemAresta = new TextField();
        Label destino = new Label("Índice de destino: ");
        TextField destinoAresta = new TextField();
        Label distancia = new Label("Distância entre origem e destino: ");
        TextField pesoAresta = new TextField();
        Button okButton = new Button("OK");
        
        grid.add(origem, 0, 0); // Adicionando as labels e textfield no Gridpane
        grid.add(origemAresta, 1, 0);
        grid.add(destino, 0, 1);
        grid.add(destinoAresta, 1, 1);
        grid.add(distancia, 0, 2);
        grid.add(pesoAresta, 1, 2);

        HBox buttonBox = new HBox(10);
        buttonBox.getChildren().add(okButton);
        HBox.setMargin(okButton, new Insets(0, 0, 0, 10));
        
        VBox vbox = new VBox(10);
        vbox.getChildren().addAll(grid, buttonBox);
        vbox.setPadding(new Insets(10, 10, 10, 10));
        
        Scene dialogScene = new Scene(vbox, 400, 200);
        no.setScene(dialogScene);
        
        okButton.setOnAction(e -> {       	
        	try {
                int indiceOrigem = Integer.parseInt(origemAresta.getText());
                int indiceDestino = Integer.parseInt(destinoAresta.getText());
                double pesoArestaDouble = Double.parseDouble(pesoAresta.getText());
                if (grafo.existeAresta(indiceOrigem, indiceDestino)) {
        			throw new OperacaoInvalidaException("Já existe uma aresta entre os dois nós.");
        		}
                grafo.adicionarAresta(indiceOrigem, indiceDestino, pesoArestaDouble);
                
                gc = canvas.getGraphicsContext2D();
                gc.setStroke(Color.BLACK);
                gc.setLineWidth(1);
                gc.strokeLine(grafo.getDepartamentos().get(indiceOrigem).getX(), grafo.getDepartamentos().get(indiceOrigem).getY(), 
                grafo.getDepartamentos().get(indiceDestino).getX(), grafo.getDepartamentos().get(indiceDestino).getY());
                }
        	catch (OperacaoInvalidaException a) {
        		Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Aresta não adicionada.");
                alert.setHeaderText(a.getMessage());
                alert.showAndWait();
        	}
        	catch (NumberFormatException er) {
        		Alert alert = new Alert(Alert.AlertType.ERROR);
        	    alert.setTitle("Erro");
        	    alert.setHeaderText("Apenas números inteiros são permitidos.");
        	    alert.setContentText("Por favor, insira um número inteiro válido.");
        	    alert.showAndWait();
        	}
            
            no.close(); // Fecha a janela de diálogo
        });

        no.showAndWait(); // Exibe a janela de diálogo e aguarda sua conclusão
        
        

    }
    
    private void atualizarCanvas() {
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

        for (Aresta aresta : grafo.getArestas()) {
        	gc = canvas.getGraphicsContext2D();
            gc.setStroke(Color.BLACK);
            gc.setLineWidth(1.0);
            gc.strokeLine(aresta.getOrigem().getX(), aresta.getOrigem().getY(), aresta.getDestino().getX(), aresta.getDestino().getY());
        }
        
        for (Departamento dep : grafo.getDepartamentos()) {
        	gc = canvas.getGraphicsContext2D();
            gc.setFill(Color.BLACK);
            // Configurando o alinhamento do texto para centralizar no ponto
            gc.setTextAlign(TextAlignment.CENTER);
            gc.setTextBaseline(javafx.geometry.VPos.CENTER);
    		// Desenhando um círculo no local do clique
            double raio = 10; // Tamanho do ponto 
            gc.fillOval(dep.getX() - raio, dep.getY() - raio, raio * 2, raio * 2);
            gc.setFill(Color.WHITE);
            // Escreva texto dentro do ponto criado
            String texto =  String.valueOf(dep.getId());
            gc.fillText(texto, dep.getX(), dep.getY());
            gc.setFill(Color.BLACK);
        }
    }
    
}
