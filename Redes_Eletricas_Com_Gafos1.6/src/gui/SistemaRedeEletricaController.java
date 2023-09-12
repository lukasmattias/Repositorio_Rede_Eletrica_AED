package gui;

import exception.OperacaoInvalidaException;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
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
	
    Grafo grafo = Grafo.getInstance();
    private GraphicsContext gc;
    
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

        // Conteudo da janela de detalhes
        VBox vbox = new VBox(10);
        vbox.getChildren().addAll(detalhesTextArea, closeButton);
        vbox.setPadding(new Insets(10));

        // Cenario da janela do relatorio
        Scene dialogScene = new Scene(vbox, 400, 300);
        no.setScene(dialogScene);

        // Construcao do relatorio
        String detalhes = dep.toString();

        // Exibindo o relatorio no TextArea
        detalhesTextArea.setText(detalhes);
        
        no.showAndWait();
		
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

        HBox buttonBox = new HBox(10); // Adicionando e posicionando o botao
        buttonBox.getChildren().add(okButton);
        HBox.setMargin(okButton, new Insets(0, 0, 0, 10));
        
        VBox vbox = new VBox(10); // Organizando o gridpane e o botao
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

        // TextArea para exibir o relatorio
        TextArea relatorioTextArea = new TextArea();
        relatorioTextArea.setEditable(false); // Torna o TextArea somente leitura
        relatorioTextArea.setWrapText(true); // Habilita a quebra de texto automÃ¡tica

        // Botao para fechar a janela de relatorio
        Button closeButton = new Button("Fechar");
        closeButton.setOnAction(e -> no.close());

        // Conteudo da janela do relatorio
        VBox vbox = new VBox(10);
        vbox.getChildren().addAll(relatorioTextArea, closeButton);
        vbox.setPadding(new Insets(10));

        // Cenario da janela do relatorio
        Scene dialogScene = new Scene(vbox, 400, 300);
        no.setScene(dialogScene);

        // Calculando o custo total e as pessoas atendidas
        double custoTotal = grafo.calcularCustoTotal();
        int pessoasAtendidas = grafo.calcularPessoasAtendidas();

        // Construcao do relatorio
        String relatorio = "Custo Total: " + custoTotal + " km\n";
        relatorio += "Pessoas Atendidas: " + pessoasAtendidas + " pessoas\n";

        // Exibindo o relatorio no TextArea
        relatorioTextArea.setText(relatorio);

        // Exiba a janela de relatorio
        no.showAndWait(); // Exibe a janela de relatorio e aguarda sua conclusao
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
        
        // TextArea para exibir dados da AGM
        TextArea AGMTextArea = new TextArea();
        AGMTextArea.setEditable(false); // Torna o TextArea somente leitura
        AGMTextArea.setWrapText(true);
        
     // Botão para fechar a janela da AGM
        Button closeButton = new Button("Fechar");
        
        VBox vbox = new VBox(10);
        vbox.getChildren().addAll(grid, AGMTextArea, closeButton);
        vbox.setPadding(new Insets(10, 10, 10, 10));
        
        
        // Cenário da janela da AGM
        Scene dialogScene = new Scene(vbox, 450, 200);
        no.setScene(dialogScene);
        
        okButton.setOnAction(e -> {
            try {
            	/*
            	 * limpa o canvas para então desenhar a AGM
            	 */
                gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
                
                // Calcular a Árvore Geradora Mínima (AGM)
                Grafo agm = grafo.calcularAGM(Integer.parseInt(indiceDepartamento.getText()));
                detalhesAGM(agm, AGMTextArea);
                // Muda para cinza a cor das arestas que nao estao presentes na AGM
                
                for (Aresta aresta : grafo.getArestas()) {
                    gc = canvas.getGraphicsContext2D();
                    gc.setStroke(Color.GREY);
                    gc.setLineWidth(1.0);
                    gc.strokeLine(aresta.getOrigem().getX(), aresta.getOrigem().getY(), aresta.getDestino().getX(), aresta.getDestino().getY());
                }
               
                // muda a cor das arestas presentes na AGM para roxo
                for (Aresta aresta : agm.getArestas()) {
                    gc = canvas.getGraphicsContext2D();
                    gc.setStroke(Color.PURPLE);
                    gc.setLineWidth(2.0);
                    gc.strokeLine(aresta.getOrigem().getX(), aresta.getOrigem().getY(), aresta.getDestino().getX(), aresta.getDestino().getY());

                
                // Configura um contorno para a escrita do peso das arestas
                    
                    gc.setStroke(Color.BLACK); // Cor do contorno
                    gc.setLineWidth(3.5); // Largura do contorno
                    gc.setLineDashes(0);
                    gc.setLineCap(StrokeLineCap.ROUND); // Extremidades do traço arredondadas
                    gc.setLineJoin(StrokeLineJoin.ROUND); // Uniões do traço arredondadas

                 // Desenhar o contorno do texto
                    gc.strokeText(String.valueOf(aresta.getDistancia() + " km"), ((aresta.getOrigem().getX() + aresta.getDestino().getX()) / 2), (aresta.getOrigem().getY() + aresta.getDestino().getY()) / 2);

                  // Redesenhando e posicionando a informação do peso da aresta no canvas
                    gc.setFill(Color.WHITE);
                    gc.fillText(String.valueOf(aresta.getDistancia() + " km"), ((aresta.getOrigem().getX() + aresta.getDestino().getX()) / 2), (aresta.getOrigem().getY() + aresta.getDestino().getY()) / 2);
                    gc.setTextAlign(TextAlignment.CENTER);
                    gc.setTextBaseline(javafx.geometry.VPos.CENTER);
                }
                
                // redesenha os nos do grafo
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

            } catch (NumberFormatException er) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erro");
                alert.setHeaderText("Índice inválido");
                alert.setContentText("Por favor, insira um índice válido para o departamento.");
                alert.showAndWait();
            }
            catch (IndexOutOfBoundsException a) {
            	Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erro");
                alert.setHeaderText("Índice inválido");
                alert.setContentText("Por favor, insira um índice válido para o departamento.");
                alert.showAndWait();	
            }
        });
        
        no.setOnCloseRequest(event -> {
            atualizarCanvas();
        });
        
        closeButton.setOnAction(event -> {
            atualizarCanvas();
            no.close();
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
        no.initModality(Modality.APPLICATION_MODAL);
        no.setTitle("Busca por menor caminho");

        GridPane grid = new GridPane(); // Organizando as labels e textfield em um Gridpane
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(10, 10, 10, 10));

        Label noOrigem = new Label("Indice de origem: ");
        TextField depOrigem = new TextField();
        Label noDestino = new Label("Indice de destino: ");
        TextField depDestino = new TextField();
        Button okButton = new Button("OK");
        
        grid.add(noOrigem, 0, 0); // Adicionando as labels e textfield no Gridpane
        grid.add(depOrigem, 1, 0);
        grid.add(noDestino, 0, 1);
        grid.add(depDestino, 1, 1);
        
        grid.add(okButton, 2, 1);
        
        // TextArea para exibir dados
        TextArea DijTextArea = new TextArea();
        DijTextArea.setEditable(false); // Torna o TextArea somente leitura
        DijTextArea.setWrapText(true);
        
     // Botão para fechar a janela
        Button closeButton = new Button("Fechar");
        
        VBox vbox = new VBox(10);
        vbox.getChildren().addAll(grid, DijTextArea, closeButton);
        vbox.setPadding(new Insets(10, 10, 10, 10));
        
        
        // Cenário da janela 
        Scene dialogScene = new Scene(vbox, 450, 200);
        no.setScene(dialogScene);
        
        okButton.setOnAction(e -> {
            try {
            	/*
            	 * limpa o canvas para então desenhar a AGM
            	 */
            	Grafo subGrafo = grafo.buscaDijkstra(grafo.getDepartamentos().get(Integer.parseInt(depOrigem.getText())),
            			grafo.getDepartamentos().get(Integer.parseInt(depDestino.getText())));
            	detalhesBusca(subGrafo, DijTextArea);
            	 gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());	
            	 
            	// Muda para cinza a cor das arestas do grafo original para cinza
                 
                 for (Aresta aresta : grafo.getArestas()) {
                     gc = canvas.getGraphicsContext2D();
                     gc.setStroke(Color.GREY);
                     gc.setLineWidth(1.0);
                     gc.strokeLine(aresta.getOrigem().getX(), aresta.getOrigem().getY(), aresta.getDestino().getX(), aresta.getDestino().getY());
                 }
                
                 // muda a cor das arestas presentes na AGM para roxo
                 for (Aresta aresta : subGrafo.getArestas()) {
                     gc = canvas.getGraphicsContext2D();
                     gc.setStroke(Color.PURPLE);
                     gc.setLineWidth(2.0);
                     gc.strokeLine(aresta.getOrigem().getX(), aresta.getOrigem().getY(), aresta.getDestino().getX(), aresta.getDestino().getY());

                 
                 // Configura um contorno para a escrita do peso das arestas
                     
                     gc.setStroke(Color.BLACK); // Cor do contorno
                     gc.setLineWidth(3.5); // Largura do contorno
                     gc.setLineDashes(0);
                     gc.setLineCap(StrokeLineCap.ROUND); // Extremidades do traço arredondadas
                     gc.setLineJoin(StrokeLineJoin.ROUND); // Uniões do traço arredondadas

                  // Desenhar o contorno do texto
                     gc.strokeText(String.valueOf(aresta.getDistancia() + " km"), ((aresta.getOrigem().getX() + aresta.getDestino().getX()) / 2), (aresta.getOrigem().getY() + aresta.getDestino().getY()) / 2);

                   // Redesenhando e posicionando a informação do peso da aresta no canvas
                     gc.setFill(Color.WHITE);
                     gc.fillText(String.valueOf(aresta.getDistancia() + " km"), ((aresta.getOrigem().getX() + aresta.getDestino().getX()) / 2), (aresta.getOrigem().getY() + aresta.getDestino().getY()) / 2);
                     gc.setTextAlign(TextAlignment.CENTER);
                     gc.setTextBaseline(javafx.geometry.VPos.CENTER);
                 }
                 
                 // redesenha os nos do grafo
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
            } catch (NumberFormatException er) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erro");
                alert.setHeaderText("Índice inválido");
                alert.setContentText("Por favor, insira um índice válido para o departamento.");
                alert.showAndWait();
            }
            catch (IndexOutOfBoundsException a) {
            	Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Erro");
                alert.setHeaderText("Índice inválido");
                alert.setContentText("Por favor, insira um índice válido para o departamento.");
                alert.showAndWait();	
            }
        });
        
        no.setOnCloseRequest(event -> {
            atualizarCanvas();
        });
        
        closeButton.setOnAction(event -> {
            atualizarCanvas();
            no.close();
        });
        
        no.showAndWait(); // Exibe a janela de diálogo e aguarda sua conclusão
    }

   
    /*adiciona à tela as informacoes sobre a
     Arvore Geradora Minima (AGM)*/
    
    	public void detalhesAGM(Grafo agm, TextArea textArea) {
    	    textArea.clear(); // Limpa o conteúdo existente na TextArea
    	    textArea.appendText("Custo Total da AGM: " + agm.calcularCustoTotal() + " km");
    	    textArea.appendText("\nPessoas Atendidas pela AGM: " + agm.calcularPessoasAtendidas());
    	}

    	public void detalhesBusca(Grafo subgrafo, TextArea textArea) {
    	    textArea.clear(); // Limpa o conteúdo existente na TextArea
    	    textArea.appendText("Custo Total: " + subgrafo.calcularCustoTotal() + " km");
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

        Label origem = new Label("Indice do primeiro no: ");
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

        Label origem = new Label("Indice de origem: ");
        TextField origemAresta = new TextField();
        Label destino = new Label("Indice de destino: ");
        TextField destinoAresta = new TextField();
        Label distancia = new Label("Distancia entre origem e destino: ");
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
        			throw new OperacaoInvalidaException("Ja existe uma aresta entre os dois nós.");
        		}
                grafo.adicionarAresta(indiceOrigem, indiceDestino, pesoArestaDouble);
                atualizarCanvas();
                }
        	catch (OperacaoInvalidaException a) {
        		Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Aresta naoo adicionada.");
                alert.setHeaderText(a.getMessage());
        	    alert.setContentText("Por favor, tente novamente.");
                alert.showAndWait();
        	}
        	catch (NumberFormatException er) {
        		Alert alert = new Alert(Alert.AlertType.ERROR);
        	    alert.setTitle("Erro");
        	    alert.setHeaderText("Apenas numeros inteiros sao permitidos.");
        	    alert.setContentText("Por favor, insira um numero inteiro valido.");
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
