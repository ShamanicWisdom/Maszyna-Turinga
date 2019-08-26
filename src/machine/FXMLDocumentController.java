/*
 * Lingwistyka Matematyczna Zadanie 3 - Maszyna Turinga - zwiekszanie liczby binarnej o 3.
 * Szymon Zawadzki 221515.
 */

package machine;

import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

/**
 *
 * @author Szaman
 */
public class FXMLDocumentController implements Initializable 
{
    //Pictures Stuff
    Image exit = new Image("machine/pictures/Exit.png");
    ImageView exitImage = new ImageView(exit);
    Image accept = new Image("machine/pictures/Accept.png");
    ImageView acceptImage = new ImageView(accept);
    Image start = new Image("machine/pictures/Start.png");
    ImageView startImage = new ImageView(start);
    
    //FXML Stuff
    @FXML
    private Button loadButton;
    @FXML
    private Button startButton;
    @FXML
    private Button exitButton;
    @FXML 
    private Button autoStartButton;
    @FXML 
    private Button manualStartButton;
    @FXML
    private TextField userInputField;
    @FXML
    private Label catchedNumber;
    @FXML
    private Label catchedNumberLength;
    @FXML
    private Label iterationsLeftLabel;
    @FXML
    private Label iterationLabel;
    @FXML
    private Label machineStateLabel;
    @FXML
    private Label tapeStateLabel;
    @FXML
    private Label moveMachineHeadLabel;
    @FXML
    private Label resultLabel;
    
    //Static Labels:
    @FXML
    private Label iterationStaticLabel;
    @FXML
    private Label machineStateStaticLabel;
    @FXML
    private Label tapeStateStaticLabel;
    @FXML
    private Label moveMachineHeadStaticLabel;
    
    //Other Stuff
    private Main mainApp; 
    
    private String userInput;
    private String newBinaryNumber;
        
    private String iteration;
    private String currentMachineState;
    private String currentTapeState;
    private String currentMove;
    
    private int userInputLength;
    private int iterationsLeft;
    private int currentIteration;
        
    ArrayList<ArrayList<String>> mainStatesList = new ArrayList<>(); //List with all states.
    ArrayList<String> q0StateList = new ArrayList<>();
    ArrayList<String> q1StateList = new ArrayList<>();
    ArrayList<String> q2StateList = new ArrayList<>();
    ArrayList<String> q3StateList = new ArrayList<>();
    ArrayList<String> q4StateList = new ArrayList<>();
    
    ArrayList<ArrayList<String>> mainMovesList = new ArrayList<>(); //List with all moves.
    ArrayList<String> q0MoveList = new ArrayList<>();
    ArrayList<String> q1MoveList = new ArrayList<>();
    ArrayList<String> q2MoveList = new ArrayList<>();
    ArrayList<String> q3MoveList = new ArrayList<>();
    ArrayList<String> q4MoveList = new ArrayList<>();
    
    ArrayList<ArrayList<String>> mainChangesList = new ArrayList<>(); //List with all changes.
    ArrayList<String> q0ChangeList = new ArrayList<>();
    ArrayList<String> q1ChangeList = new ArrayList<>();
    ArrayList<String> q2ChangeList = new ArrayList<>();
    ArrayList<String> q3ChangeList = new ArrayList<>();
    ArrayList<String> q4ChangeList = new ArrayList<>();
    
    ArrayList<Character> alphabetList = new ArrayList<>();
    
    ArrayList<String> currentStateList = new ArrayList<>();
    ArrayList<String> currentMoveList = new ArrayList<>();
    ArrayList<String> currentChangeList = new ArrayList<>();
    
    public void setMainApp(Main mainApp)
    {
        this.mainApp = mainApp;
              
    }
    
    //Load Script
    @FXML
    private void handleLoad(ActionEvent event)
    {
        hideInterface();
        reset();
        
        userInput = userInputField.getText();
        boolean isInputCorrect = inputValidation(userInput);
        if(isInputCorrect == true)
        {
            int inputtedNumber = Integer.parseInt(userInput.replaceAll("[^0-1]", ""), 2); //Clear string from terminator and convert to decimal format.
            userInputLength = userInput.length();
            catchedNumber.setText("Podana liczba: " + inputtedNumber);
            catchedNumberLength.setText("Długość ciągu: " + userInputLength);  //Wyswietlenie długości znaków przekazanych przez uzytkownika.
            catchedNumber.setOpacity(1);
            catchedNumberLength.setOpacity(1);
            startButton.setOpacity(1);
            startButton.setDisable(false);
            
        }       
    }
    
    //Start Script.
    @FXML
    private void handleStart(ActionEvent event)
    {
        iterationStaticLabel.setOpacity(1);
        machineStateStaticLabel.setOpacity(1);
        tapeStateStaticLabel.setOpacity(1);
        moveMachineHeadStaticLabel.setOpacity(1);
        autoStartButton.setOpacity(1);
        autoStartButton.setDisable(false);
        manualStartButton.setOpacity(1);
        manualStartButton.setDisable(false);
        reset();
        
        iterationsLeft = userInputLength - 1;
        moveMachine(iterationsLeft);
        iterationsLeftLabel.setText("Pozostało " + iterationsLeft + " iteracji.");
        System.out.println(newBinaryNumber);
    }
    
    //ManualStart
    @FXML
    private void handleManualStart(ActionEvent event)
    {
        iterationsLeft--;
        moveMachine(iterationsLeft);  
        iterationsLeftLabel.setText("Pozostało " + iterationsLeft + " iteracji.");
        System.out.println("left: " + iterationsLeft + " iter: " + iteration);
        System.out.println(newBinaryNumber);
    }
    
    //AutoStart
    @FXML
    private void handleAutoStart() throws InterruptedException
    {
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.seconds(1), event -> 
                {
                    iterationsLeft--;
                    moveMachine(iterationsLeft);
                })
        );
        timeline.setCycleCount(iterationsLeft);
        timeline.play();
    }
    
    //Exit Script.
    @FXML
    public void handleExit()
    {        
        ButtonType okButton = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        ButtonType cancelButton = new ButtonType("Anuluj", ButtonBar.ButtonData.CANCEL_CLOSE);
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "", okButton, cancelButton);
        alert.setTitle("Wyjście");
        alert.setHeaderText(null);
        alert.setContentText("Na pewno?");
        
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == okButton)
        {
            System.exit(0);
        } 
        else 
        {
            alert.close();
        }
    }
    
    //Machine Script
    void moveMachine(int iterationsLeft)
    {
        char currentCharacter = userInput.charAt(iterationsLeft);
        int currentCharacterNumber = alphabetList.indexOf(currentCharacter);
        int currentMachineStateNumber = Integer.parseInt(currentMachineState.substring(1));
        System.out.println("Machine State: " + currentMachineState);
        System.out.println("Number: " + currentCharacterNumber);
        System.out.println("Char: " + currentCharacter);
        
        currentStateList.clear(); //clear temporary state list for new use.
        currentStateList.addAll(mainStatesList.get(currentMachineStateNumber)); //Copy possible current states pathes list.
        currentMachineState = currentStateList.get(currentCharacterNumber);
        
        currentMoveList.clear(); //clear temporary state list for new use.
        currentMoveList.addAll(mainMovesList.get(currentMachineStateNumber)); //Copy possible current states pathes list.
        currentMove = currentMoveList.get(currentCharacterNumber);
        
        currentChangeList.clear(); //clear temporary state list for new use.
        currentChangeList.addAll(mainChangesList.get(currentMachineStateNumber)); //Copy possible current states pathes list.
        currentTapeState = currentChangeList.get(currentCharacterNumber);
        
        if(iterationLabel.getText().equals(""))
        {
            iterationLabel.setText("" + currentIteration);
            machineStateLabel.setText(currentMachineState);
            tapeStateLabel.setText(currentTapeState);
            moveMachineHeadLabel.setText(currentMove);
        }
        else
        {
            iterationLabel.setText(currentIteration + "\t" + iterationLabel.getText());
            machineStateLabel.setText(currentMachineState + "\t" + machineStateLabel.getText());
            tapeStateLabel.setText(currentTapeState + "\t" + tapeStateLabel.getText());
            moveMachineHeadLabel.setText(currentMove + "\t" + moveMachineHeadLabel.getText());
        }
        if(!currentTapeState.equals("-"))
        {
            newBinaryNumber = currentTapeState + newBinaryNumber;
        }
        currentIteration++;
                
        if(iterationsLeft == 0)
        {

            manualStartButton.setDisable(true);
            autoStartButton.setDisable(true);
            resultLabel.setText("Stan taśmy: " + newBinaryNumber + ", liczba: " + Integer.parseInt(newBinaryNumber, 2));
        }
    }
    
    //Reset 
    void reset()
    {
        iterationsLeftLabel.setText("");
        iterationLabel.setText("");
        machineStateLabel.setText("");
        tapeStateLabel.setText("");
        moveMachineHeadLabel.setText("");
        resultLabel.setText("");
        currentIteration = 1;
        currentMachineState = "Q0";
        newBinaryNumber = "";
    }
    
    void hideInterface()
    {
        iterationsLeftLabel.setText("");
        iterationLabel.setText("");
        machineStateLabel.setText("");
        tapeStateLabel.setText("");
        moveMachineHeadLabel.setText("");
        resultLabel.setText("");
        
        autoStartButton.setOpacity(0);
        autoStartButton.setDisable(true);
        manualStartButton.setOpacity(0);
        manualStartButton.setDisable(true);
        
        iterationStaticLabel.setOpacity(0);
        machineStateStaticLabel.setOpacity(0);
        tapeStateStaticLabel.setOpacity(0);
        moveMachineHeadStaticLabel.setOpacity(0);
    }
    
    //Validator
    boolean inputValidation(String userInput)
    {
        String errorMessage = "";
        Pattern onlyBinaryPattern = Pattern.compile("^[#][0-1]*$");
        Matcher onlyBinaryMatcher = onlyBinaryPattern.matcher(userInput);
        boolean isInputCorrect = onlyBinaryMatcher.find();
        if(userInput.length() == 0)
        {
            errorMessage += "Nie podano liczby!" + "\n";
        }
        else
        {
            if(userInput.length() == 1)
            {
                errorMessage += "Liczba jest niepoprawna!" + "\n";
            }
            else
            {
                if(isInputCorrect == false)
                {
                    errorMessage += "Liczba jest niepoprawna!" + "\n";
                }
            }
        }
        
        if(errorMessage.length() != 0) 
        {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setHeaderText("Błąd!");
            alert.setContentText(errorMessage);
            alert.showAndWait();
            return false;
        }
        else 
        {
            return true;
        }
    }
        
    @Override
    public void initialize(URL url, ResourceBundle rb) 
    {
        //Accept initialization
        acceptImage.setFitHeight(50);
        acceptImage.setFitWidth(50);
        loadButton.setGraphic(acceptImage);
        loadButton.setMaxHeight(50);
        loadButton.setMaxWidth(50);
        
        //Start initialization
        startImage.setFitHeight(75);
        startImage.setFitWidth(75);
        startButton.setGraphic(startImage);
        startButton.setMaxHeight(75);
        startButton.setMaxWidth(75);
        
        //Exit initialization
        exitImage.setFitHeight(75);
        exitImage.setFitWidth(75);
        exitButton.setGraphic(exitImage);
        exitButton.setMaxHeight(75);
        exitButton.setMaxWidth(75);
        
        //Lists initialization
        
        //States lists:
        q0StateList.addAll(Arrays.asList("Q1","Q2","Q1"));
        q1StateList.addAll(Arrays.asList("Q3","Q4","Q3"));
        q2StateList.addAll(Arrays.asList("Q4","Q4","Q4"));
        q3StateList.addAll(Arrays.asList("Q3","Q3","Q3"));
        q4StateList.addAll(Arrays.asList("Q3","Q4","Q3"));
        
        mainStatesList.add(q0StateList);
        mainStatesList.add(q1StateList);
        mainStatesList.add(q2StateList);
        mainStatesList.add(q3StateList);
        mainStatesList.add(q4StateList);
        
        //Moves lists:
        q0MoveList.addAll(Arrays.asList("L","L","L"));
        q1MoveList.addAll(Arrays.asList("L","L","-"));
        q2MoveList.addAll(Arrays.asList("L","L","L"));
        q3MoveList.addAll(Arrays.asList("L","L","-"));
        q4MoveList.addAll(Arrays.asList("L","L","-"));
        
        mainMovesList.add(q0MoveList);
        mainMovesList.add(q1MoveList);
        mainMovesList.add(q2MoveList);
        mainMovesList.add(q3MoveList);
        mainMovesList.add(q4MoveList);
        
        //Changes lists:
        q0ChangeList.addAll(Arrays.asList("1","0","1"));
        q1ChangeList.addAll(Arrays.asList("1","0","1"));
        q2ChangeList.addAll(Arrays.asList("0","1","0"));
        q3ChangeList.addAll(Arrays.asList("0","1","-"));
        q4ChangeList.addAll(Arrays.asList("1","0","1"));
        
        mainChangesList.add(q0ChangeList);
        mainChangesList.add(q1ChangeList);
        mainChangesList.add(q2ChangeList);
        mainChangesList.add(q3ChangeList);
        mainChangesList.add(q4ChangeList);
        
        //Alphabet lists:
        alphabetList.addAll(Arrays.asList('0','1','#'));
        
        //Visibility & accessibility:
        catchedNumber.setOpacity(0);
        catchedNumberLength.setOpacity(0);
        iterationStaticLabel.setOpacity(0);
        machineStateStaticLabel.setOpacity(0);
        tapeStateStaticLabel.setOpacity(0);
        moveMachineHeadStaticLabel.setOpacity(0);
        startButton.setOpacity(0);
        startButton.setDisable(true);
        autoStartButton.setOpacity(0);
        autoStartButton.setDisable(true);
        manualStartButton.setOpacity(0);
        manualStartButton.setDisable(true);
        
        iterationsLeftLabel.setText("");
        iterationLabel.setText("");
        machineStateLabel.setText("");
        tapeStateLabel.setText("");
        moveMachineHeadLabel.setText("");
        
        //Utilities:
        currentIteration = 1;
    }    
    
}
