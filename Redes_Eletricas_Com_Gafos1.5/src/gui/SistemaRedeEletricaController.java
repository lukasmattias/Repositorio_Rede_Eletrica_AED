package gui;

import java.util.List;
import java.util.Scanner;

import com.sun.javafx.collections.MappingChange.Map;

import exception.OperacaoInvalidaException;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.shape.StrokeLineJoin;
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
    private GraphicsContext gc;
    private double escala = 1.0;
    
    // -----> ELEMENTOS DA TELA <---------
    
    @FXML
    private Button AdcNo;

    @FXML
    private Button AdcAresta;

    @FXML
    private Button RmvAresta;
    
    @FXML
    private Button CalAGM;
    
    @FXML
    private Button btnBusca;
    
    @FXML
    private Button Relatorio;
    
    @FXML
    private Button RmvDepartamento;
    
    @FXML
    private Canvas canvas;
    
    //-----> INICIALIZANDO O CANVAS <---------
    
    public void initialize() {
    	gc = canvas.getGraphicsContext2D();
    	
        canvas.addEventHandler(MouseEvent.MOUSE_CLICKED, event -> {      
            // Obtendo as coordenadas do clique
            double x = event.getX();
            double y = event.getY();
            boolean localOcupado = false;
            for (Departamento departamento : grafo.getDepartamentos()) {
                if (Math.abs(event.getX() - departamento.getX()) <= 15 && Math.abs(event.getY() - departamento.getY()) <= 15) {
                    // Acessando as informaÃ§Ãµes do nÃ³ caso corresponda Ã  coordenada do clique
                	mostrarDetalhesDepartamento(departamento);
                    localOcupado = true;
                }
            }
            if (!localOcupado) {
            	criarNo(x, y);	
            }
            });
        
    }
    
    //-----> MOSTRA DETALHES SOBRE OS DEP <---------
    
    private void mostrarDetalhesDepartamento(Departamento dep) {
    	Stage no = new Stage();
        no.initModality(Modality.APPLICATION_MODAL); // Bloqueia a janela principal
        no.setTitle("Informacoes do Departamento");
        
        // TextArea para exibir o relatorio
        TextArea detalhesTextArea = new TextArea();
        detalhesTextArea.setEditable(false); // Torna o TextArea somente leitura
        detalhesTextArea.setWrapText(true); // Habilita a quebra de texto automÃ¡tica

        // BotÃ£o para fechar a janela de relatÃ³rio
        Button closeButton = new Button("Fechar");
        closeButton.setOnAction(e -> no.close());

        // ConteÃºdo da janela do relatÃ³rio
        VBox vbox = new VBox(10);
        vbox.getChildren().addAll(detalhesTextArea, closeButton);
        vbox.setPadding(new Insets(10));

        // CenÃ¡rio da janela do relatÃ³rio
        Scene dialogScene = new Scene(vbox, 400, 300);
        no.setScene(dialogScene);

        // ConstruÃ§Ã£o do relatÃ³rio
        String relatorio = dep.toString();

        // Exibindo o relatÃ³rio no TextArea
        detalhesTextArea.setText(relatorio);
        
        // Exiba a janela de relatÃ³rio
        no.showAndWait(); // Exibe a janela de relatÃ³rio e aguarda sua conclusÃ£o
		
	}
    
    //-----> CRIA OS NOS <---------
    
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
        Label numPessoas = new Label("Numero de pessoas:");
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
        		atualizarCanvas();
        	}
        	catch (OperacaoInvalidaException a) {
        		Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Departamento nao adicionado.");
                alert.setHeaderText(a.getMessage());
                alert.showAndWait();
        	}
        	catch (NumberFormatException er) {
        		Alert alert = new Alert(Alert.AlertType.ERROR);
        	    alert.setTitle("Erro");
        	    alert.setHeaderText("Numero de pessoas invalido");
        	    alert.setContentText("Por favor, insira um numero valido para o numero de pessoas.");
        	    alert.showAndWait();
        	}
            
            no.close(); // Fecha a janela de diÃ¡logo
        });

        no.showAndWait(); // Exibe a janela de diÃ¡logo e aguarda sua conclusÃ£o
    }
    
    //-----> GERA ORELATORIO DE CUSTO <---------
    
    @FXML
    public void gerarRelatorio() {
    	
        if (grafo.getDepartamentos().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erro");
            alert.setHeaderText("Grafo vazio");
            alert.setContentText("O grafo esta vazio. Adicione departamentos e arestas para gerar o relatorio!");
            alert.showAndWait();
            return; // Retorna sem fazer nada se o grafo estiver vazio
        }
        
        Stage no = new Stage();
        no.initModality(Modality.APPLICATION_MODAL); // Bloqueia a janela principal
        no.setTitle("Relatorio de Custo e Pessoas Atendidas");

        // TextArea para exibir o relatÃ³rio
        TextArea relatorioTextArea = new TextArea();
        relatorioTextArea.setEditable(false); // Torna o TextArea somente leitura
        relatorioTextArea.setWrapText(true); // Habilita a quebra de texto automÃ¡tica

        // BotÃ£o para fechar a janela de relatÃ³rio
        Button closeButton = new Button("Fechar");
        closeButton.setOnAction(e -> no.close());

        // ConteÃºdo da janela do relatÃ³rio
        VBox vbox = new VBox(10);
        vbox.getChildren().addAll(relatorioTextArea, closeButton);
        vbox.setPadding(new Insets(10));

        // CenÃ¡rio da janela do relatÃ³rio
        Scene dialogScene = new Scene(vbox, 400, 300);
        no.setScene(dialogScene);

        // Calculando o custo total e as pessoas atendidas
        double custoTotal = grafo.calcularCustoTotal();
        int pessoasAtendidas = grafo.calcularPessoasAtendidas();

        // ConstruÃ§Ã£o do relatÃ³rio
        String relatorio = "Custo Total: " + custoTotal + " km\n";
        relatorio += "Pessoas Atendidas: " + pessoasAtendidas + " pessoas\n";

        // Exibindo o relatÃ³rio no TextArea
        relatorioTextArea.setText(relatorio);

        // Exiba a janela de relatÃ³rio
        no.showAndWait(); // Exibe a janela de relatÃ³rio e aguarda sua conclusÃ£o
    }

    //-----> REMOVE O DEPARTAMENTO <---------
    
    @FXML
    void removerDepartamento() {
    	
        if (grafo.getDepartamentos().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erro");
            alert.setHeaderText("Grafo vazio");
            alert.setContentText("O grafo esta vazio. Adicione departamentos.");
            alert.showAndWait();
            return; // Retorna sem fazer nada se o grafo estiver vazio
        }
        
        Stage no = new Stage();
        no.initModality(Modality.APPLICATION_MODAL); // Bloqueia a janela principal
        no.setTitle("Remover Departamento");

        GridPane grid = new GridPane(); // Organizando as labels e textfield em um Gridpane
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(10, 10, 10, 10));

        Label indiceDep = new Label("indice do departamento: ");
        TextField indiceDepartamento = new TextField();

        Button okButton = new Button("OK");

        grid.add(indiceDep, 0, 0); // Adicionando as labels e textfield no Gridpane
        grid.add(indiceDepartamento, 1, 0);

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
                int indiceDepartamentoParaExcluir = Integer.parseInt(indiceDepartamento.getText());

                // Remova o departamento do grafo
                grafo.removerDepartamento(indiceDepartamentoParaExcluir);
                grafo.atualizarIndices();
                atualizarCanvas();
                // Atualize o desenho no Canvas
                atualizarCanvas();
            } catch (NumberFormatException er) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erro");
                alert.setHeaderText("indice invalido");
                alert.setContentText("Por favor, insira um idice valido para o departamento.");
                alert.showAndWait();
            }

            no.close(); // Fecha a janela de diÃ¡logo
        });

        no.showAndWait(); // Exibe a janela de diÃ¡logo e aguarda sua conclusÃ£o
    }

    
    //-----> CALCULA AGM <---------
    
    @FXML
    public void calcularAGM() {
        if (grafo.getDepartamentos().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erro");
            alert.setHeaderText("Grafo vazio!!");
            alert.setContentText("O grafo está vazio. Adicione departamentos e arestas antes de calcular a Árvore Geradora Mínima.");
            alert.showAndWait();
            return; // Retorna sem fazer nada se o grafo estiver vazio
        }

        Stage no = new Stage();
        no.initModality(Modality.APPLICATION_MODAL);
        no.setTitle("Árvore Geradora Mínima");

        // Calcular a Árvore Geradora Mínima (AGM)
        Grafo agm = grafo.calcularAGM(); // Você pode escolher um departamento de início (índice 0 neste exemplo)

        GridPane grid = new GridPane(); // Organizando as labels e textfield em um Gridpane
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(10, 10, 10, 10));

        Label indiceDep = new Label("Índice do departamento inicial: ");
        TextField indiceDepartamento = new TextField();
        Button okButton = new Button("OK");

        grid.add(indiceDep, 0, 0); // Adicionando as labels e textfield no Gridpane
        grid.add(indiceDepartamento, 1, 0);
        grid.add(okButton, 2, 0);

        VBox vbox = new VBox(10);
        vbox.getChildren().addAll(grid);
        vbox.setPadding(new Insets(10, 10, 10, 10));

        Scene dialogScene = new Scene(vbox, 400, 150);
        no.setScene(dialogScene);

        okButton.setOnAction(e -> {
            try {
                gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
                List<Aresta> arestasForaAGM = grafo.getArestas();

                for (Aresta aresta : grafo.getArestas()) {
                    gc = canvas.getGraphicsContext2D();
                    gc.setStroke(Color.GREY);
                    gc.setLineWidth(1.0);
                    gc.strokeLine(aresta.getOrigem().getX(), aresta.getOrigem().getY(), aresta.getDestino().getX(), aresta.getDestino().getY());
                }
                System.out.println(agm.getArestas().size());
                for (Aresta aresta : agm.getArestas()) {
                    gc = canvas.getGraphicsContext2D();
                    gc.setStroke(Color.PURPLE);
                    gc.setLineWidth(2.0);
                    gc.strokeLine(aresta.getOrigem().getX(), aresta.getOrigem().getY(), aresta.getDestino().getX(), aresta.getDestino().getY());
                    arestasForaAGM.remove(aresta);

                    gc.setStroke(Color.BLACK); // Cor do contorno
                    gc.setLineWidth(3.5); // Largura do contorno
                    gc.setLineDashes(0); // Padrão de traço (0 significa contínuo)
                    gc.setLineCap(StrokeLineCap.ROUND); // Extremidades do traço arredondadas
                    gc.setLineJoin(StrokeLineJoin.ROUND); // Uniões do traço arredondadas

                    // Desenhar o contorno do texto
                    gc.strokeText(String.valueOf(aresta.getDistancia() + " km"), ((aresta.getOrigem().getX() + aresta.getDestino().getX()) / 2), (aresta.getOrigem().getY() + aresta.getDestino().getY()) / 2);

                    // Posicionando a informação do peso da aresta no canvas
                    gc.setFill(Color.WHITE);
                    gc.fillText(String.valueOf(aresta.getDistancia() + " km"), ((aresta.getOrigem().getX() + aresta.getDestino().getX()) / 2), (aresta.getOrigem().getY() + aresta.getDestino().getY()) / 2);
                    gc.setTextAlign(TextAlignment.CENTER);
                    gc.setTextBaseline(javafx.geometry.VPos.CENTER);
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
                    String texto = String.valueOf(dep.getId());
                    gc.fillText(texto, dep.getX(), dep.getY());
                    gc.setFill(Color.BLACK);
                }

                // Atualize o desenho no Canvas

            } catch (NumberFormatException er) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erro");
                alert.setHeaderText("Índice inválido");
                alert.setContentText("Por favor, insira um índice válido para o departamento.");
                alert.showAndWait();
            }

            no.close(); // Fecha a janela de diálogo
        });

        no.showAndWait(); // Exibe a janela de diálogo e aguarda sua conclusão
    }

    // -----> EXIBE RESULTADO DA BUSCA <---------
    
    @FXML
    void exibirResultadoDaBusca() {
        if (grafo.getDepartamentos().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erro");
            alert.setHeaderText("Grafo Vazio!!");
            alert.setContentText("Adicione Departamentos e crie Arestas!");
            alert.showAndWait();
            return; // Retorna sem fazer nada se o grafo estiver vazio
        }

        Stage no = new Stage();
        no.initModality(Modality.APPLICATION_MODAL); // Bloqueia a janela principal
        no.setTitle("Resutado da busca!");

        // TextArea para exibir o relatório
        TextArea relatorioTextArea = new TextArea();
        relatorioTextArea.setEditable(false); // Torna o TextArea somente leitura
        relatorioTextArea.setWrapText(true); // Habilita a quebra de texto automática

        // Botão para fechar a janela de relatório
        Button closeButton = new Button("Fechar");
        closeButton.setOnAction(e -> no.close());

        // Conteúdo da janela do relatório
        VBox vbox = new VBox(10);
        vbox.getChildren().addAll(relatorioTextArea, closeButton);
        vbox.setPadding(new Insets(10));

        // Cenário da janela do relatório
        Scene dialogScene = new Scene(vbox, 400, 300);
        no.setScene(dialogScene);

        // Exiba a janela de relatório
        no.showAndWait();
    }

    //-----> REMOVE ARESTAS <---------
    
    @FXML
    public void removerAresta() {
        if (grafo.getArestas().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erro");
            alert.setHeaderText("Sem Arestas!!");
            alert.setContentText("Adicione Departamentos e crie Arestas!");
            alert.showAndWait();
            return; // Retorna sem fazer nada se o grafo estiver vazio
        }
        
    	Stage no = new Stage();
        no.initModality(Modality.APPLICATION_MODAL); // Bloqueia a janela principal
        no.setTitle("Remover aresta");
   
        GridPane grid = new GridPane(); // Organizando as labels e textfield em um Gridpane
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(10, 10, 10, 10));

        Label origem = new Label("Idice do primeiro no: ");
        TextField origemNo = new TextField();
        Label destino = new Label("Indice do segundo no: ");
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

        okButton.setOnAction(e -> {       	
        	try {
                int indiceOrigem = Integer.parseInt(origemNo.getText());
                int indiceDestino = Integer.parseInt(destinoNo.getText());
                
                if (grafo.existeAresta(indiceOrigem, indiceDestino)) {
        			grafo.removerAresta(indiceOrigem, indiceDestino);
        			 atualizarCanvas();
        		}
                else {
                	throw new OperacaoInvalidaException("Nao ha aresta entre os dois nos!");
                }
                }
        	catch (OperacaoInvalidaException a) {
        		Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Aresta nao adicionada!");
                alert.setHeaderText(a.getMessage());
                alert.showAndWait();
        	}
        	catch (NumberFormatException er) {
        		Alert alert = new Alert(Alert.AlertType.ERROR);
        	    alert.setTitle("Erro");
        	    alert.setHeaderText("Numero de pessoas invalido");
        	    alert.setContentText("Por favor, insira um numero valido para o numero de pessoas.");
        	    alert.showAndWait();
        	}
            
            no.close(); // Fecha a janela de diÃ¡logo
        });

        no.showAndWait(); // Exibe a janela de diÃ¡logo e aguarda sua conclusÃ£o
    }
    
    //-----> ADICIONA ARESTAS <---------
    
    @FXML
    public void adicionarAresta() {
    	
        if (grafo.getDepartamentos().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Erro");
            alert.setHeaderText("Grafo vazio!!");
            alert.setContentText("O grafo esta vazio. Adicione departamentos.");
            alert.showAndWait();
            return; // Retorna sem fazer nada se o grafo estiver vazio
        }
        
    	Stage no = new Stage();
        no.initModality(Modality.APPLICATION_MODAL); // Bloqueia a janela principal
        no.setTitle("Nova aresta");
        
        GridPane grid = new GridPane(); // Organizando as labels e textfield em um Gridpane
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(10, 10, 10, 10));

        Label origem = new Label("Ã­ndice de origem: ");
        TextField origemAresta = new TextField();
        Label destino = new Label("Ã�ndice de destino: ");
        TextField destinoAresta = new TextField();
        Label distancia = new Label("DistÃ¢ncia entre origem e destino: ");
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
        			throw new OperacaoInvalidaException("JÃ¡ existe uma aresta entre os dois nÃ³s.");
        		}
                grafo.adicionarAresta(indiceOrigem, indiceDestino, pesoArestaDouble);
                atualizarCanvas();
                }
        	catch (OperacaoInvalidaException a) {
        		Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Aresta nÃ£o adicionada.");
                alert.setHeaderText(a.getMessage());
                alert.showAndWait();
        	}
        	catch (NumberFormatException er) {
        		Alert alert = new Alert(Alert.AlertType.ERROR);
        	    alert.setTitle("Erro");
        	    alert.setHeaderText("Apenas nÃºmeros inteiros sÃ£o permitidos.");
        	    alert.setContentText("Por favor, insira um nÃºmero inteiro vÃ¡lido.");
        	    alert.showAndWait();
        	}
            
            no.close(); // Fecha a janela de diÃ¡logo
        });

        no.showAndWait(); // Exibe a janela de diÃ¡logo e aguarda sua conclusÃ£o
        
        

    }
    
    //-----> ATUALIZA CANVAS <---------
    
    private void atualizarCanvas() {
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

        for (Aresta aresta : grafo.getArestas()) {
        	gc = canvas.getGraphicsContext2D();
            gc.setStroke(Color.BLACK);
            gc.setLineWidth(1.0);
            gc.strokeLine(aresta.getOrigem().getX(), aresta.getOrigem().getY(), aresta.getDestino().getX(), aresta.getDestino().getY());
            
            gc.setStroke(Color.BLACK); // Cor do contorno
            gc.setLineWidth(3.5); // Largura do contorno
            gc.setLineDashes(0); // PadrÃ£o de traÃ§o (0 significa contÃ­nuo)
            gc.setLineCap(StrokeLineCap.ROUND); // Extremidades do traÃ§o arredondadas
            gc.setLineJoin(StrokeLineJoin.ROUND); // UniÃµes do traÃ§o arredondadas

            // Desenhar o contorno do texto
            gc.strokeText(String.valueOf(aresta.getDistancia() + " km"), ((aresta.getOrigem().getX() + aresta.getDestino().getX())/2), (aresta.getOrigem().getY() + aresta.getDestino().getY())/2);
            
            // Posicionando a informaÃ§Ã£o do peso da aresta no canvas
            gc.setFill(Color.WHITE);
            gc.fillText(String.valueOf(aresta.getDistancia() + " km"), ((aresta.getOrigem().getX() + aresta.getDestino().getX())/2), (aresta.getOrigem().getY() + aresta.getDestino().getY())/2);
            gc.setTextAlign(TextAlignment.CENTER);
            gc.setTextBaseline(javafx.geometry.VPos.CENTER);
        }
        
        for (Departamento dep : grafo.getDepartamentos()) {
        	gc = canvas.getGraphicsContext2D();
            gc.setFill(Color.BLACK);
            // Configurando o alinhamento do texto para centralizar no ponto
            gc.setTextAlign(TextAlignment.CENTER);
            gc.setTextBaseline(javafx.geometry.VPos.CENTER);
    		// Desenhando um cÃ­rculo no local do clique
            double raio = 10; // Tamanho do ponto 
            gc.fillOval(dep.getX() - raio, dep.getY() - raio, raio * 2, raio * 2);
            gc.setFill(Color.WHITE);
            // Escreva texto dentro do ponto criado
            String texto =  String.valueOf(dep.getId());
            gc.fillText(texto, dep.getX(), dep.getY());
            gc.setFill(Color.BLACK);
        }
    }
    
    //-----> REINICIA O GRAFO <---------
    
    @FXML
    void restartGrafo() {
        // Limpa a lista de departamentos e arestas
        grafo.limparDepartamentos();
        grafo.limparArestas();
        
        // Reinicia o contador de departamentos
        grafo.setControleIndice(0);

        // Limpa o canvas e atualiza os Ã­ndices dos departamentos
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
        atualizarCanvas();
    }
    

    
}
