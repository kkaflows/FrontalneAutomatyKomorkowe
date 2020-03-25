package sample;

import javafx.collections.FXCollections;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.paint.Color;

import java.util.*;


public class Controller {

    private static final double GRAIN_SIZE = 2.0;
    int canvasWidth = 400;
    int canvasHeight = 400;

    @FXML
    Canvas canvas;
    @FXML
    Button start;
    @FXML
    javafx.scene.control.TextField textField;
    @FXML
    ChoiceBox choiceBox;

    private static final int count = 400;
    private GraphicsContext graphicsContext;

    public void initialize() {
        initializeBoard();
        buttonsListeners();
    }

    private void buttonsListeners() {

        start.setOnMouseClicked(event -> startProgram());

    }

    private void initializeBoard() {
        graphicsContext = canvas.getGraphicsContext2D();
        choiceBox.setItems(FXCollections.observableArrayList(
                "Sync",
                "Async",
                "Async Random initialize",
                "Async Random every iteration"));
        choiceBox.setValue("Sync");
    }


    public void startProgram() {

        int[] tableState = new int[count];
        for (int i = 0; i < count; i++) {
            tableState[i] = 0;
        }
        tableState[count / 2] = 1;

        int numberOfPrinciple = Integer.valueOf(textField.getText());

        int[] tableOfResult = decimalToBinary(numberOfPrinciple);

        switch (choiceBox.getValue().toString()) {
            case "Sync": {
                printResultFromIteration(tableState);
                for (int i = 0; i < 400; i++) {
                    tableState = handlePrinciple(tableOfResult, tableState);
                    printResultFromIteration(tableState);
                    drawResult(tableState, i);
                }
                break;
            }
            case "Async": {
                List<Integer> indexList = new ArrayList<>();
                for (int i = 0; i < 400; i++) {
                    indexList.add(i);
                }
                for (int i = 0; i < 400; i++) {
                    asyncSolution(tableState, tableOfResult, indexList);
                    drawResult(tableState, i);
                    printResultFromIteration(tableState);

                }
                break;

            }
            case "Async Random initialize": {
                List<Integer> indexList = new ArrayList<>();
                for (int i = 0; i < 400; i++) {
                    indexList.add(i);
                }
                Collections.shuffle(indexList);
                for (int i = 0; i < 400; i++) {
                    asyncSolution(tableState, tableOfResult, indexList);
                    drawResult(tableState, i);
                    printResultFromIteration(tableState);

                }
                break;
            }
            case "Async Random every iteration": {
                List<Integer> indexList = new ArrayList<>();
                for (int i = 0; i < 400; i++) {
                    indexList.add(i);
                }
                for (int i = 0; i < 400; i++) {
                    Collections.shuffle(indexList);
                    asyncSolution(tableState, tableOfResult, indexList);
                    drawResult(tableState, i);
                    printResultFromIteration(tableState);

                }
                break;
            }
        }


    }

    private void asyncSolution(int[] tableState, int[] tableOfResult, List<Integer> indexList) {

        for (int i : indexList) {
            tableState = handleSingleChange(tableState, tableOfResult, i);
        }

    }

    private int[] handleSingleChange(int[] tableState, int[] tableOfResults, int i) {

        int endOfTableState;
        if (i - 1 == -1)
            endOfTableState = count - 1;
        else
            endOfTableState = i - 1;
        if (tableState[endOfTableState] == 1 && tableState[i] == 1 && tableState[(i + 1) % count] == 1) {
            tableState[i] = tableOfResults[7];
        }
        if (tableState[endOfTableState] == 1 && tableState[i] == 1 && tableState[(i + 1) % count] == 0) {
            tableState[i] = tableOfResults[6];
        }
        if (tableState[endOfTableState] == 1 && tableState[i] == 0 && tableState[(i + 1) % count] == 1) {
            tableState[i] = tableOfResults[5];
        }
        if (tableState[endOfTableState] == 1 && tableState[i] == 0 && tableState[(i + 1) % count] == 0) {
            tableState[i] = tableOfResults[4];
        }
        if (tableState[endOfTableState] == 0 && tableState[i] == 1 && tableState[(i + 1) % count] == 1) {
            tableState[i] = tableOfResults[3];
        }
        if (tableState[endOfTableState] == 0 && tableState[i] == 1 && tableState[(i + 1) % count] == 0) {
            tableState[i] = tableOfResults[2];
        }
        if (tableState[endOfTableState] == 0 && tableState[i] == 0 && tableState[(i + 1) % count] == 1) {
            tableState[i] = tableOfResults[1];
        }
        if (tableState[endOfTableState] == 0 && tableState[i] == 0 && tableState[(i + 1) % count] == 0) {
            tableState[i] = tableOfResults[0];
        }

        return tableState;

    }

    private void randomFill(int[] tableState) {
        Random random = new Random();
        int probability = 75;
        int sum = 0;
        for (int i = 0; i < tableState.length; i++) {
            int x = Math.abs(random.nextInt() % 100);
            if (x < probability) {
                System.out.println(x);
                tableState[i] = 1;
                sum++;
            }
        }
        System.out.println(sum);
    }

    private void drawResult(int[] tableState, int i) {

        for (int j = 0; j < tableState.length; j++) {
            if (tableState[j] == 1) {
                graphicsContext.setFill(Color.BLACK);
            } else {
                graphicsContext.setFill(Color.WHITE);
            }

            graphicsContext.fillRect(j * GRAIN_SIZE, i * GRAIN_SIZE, GRAIN_SIZE, GRAIN_SIZE);
        }

    }

    private static void printResultFromIteration(int[] tableState) {

        for (int i = 0; i < count; i++) {
//            System.out.print("[");
            if (tableState[i] == 1) {
                System.out.print("X");
            } else {
                System.out.print(" ");
            }
//            System.out.print("]");
            System.out.print("  ");
        }
        System.out.println();
    }

    static int[] decimalToBinary(int decimal) {
        int[] result = new int[8];
        int power = 7;
        for (int i = power; i >= 0; i--) {
            if (decimal >= Math.pow(2, i)) {
                result[i] = 1;
                decimal = (int) (decimal - Math.pow(2, i));
            } else {
                result[i] = 0;
            }
        }

        return result;
    }

    static int[] handlePrinciple(int[] tableOfResults, int[] tableState) {
        int[] newTableState = new int[count];
        int endOfTableState;
        for (int i = 0; i < count; i++) {

            if (i - 1 == -1)
                endOfTableState = count - 1;
            else
                endOfTableState = i - 1;
            if (tableState[endOfTableState] == 1 && tableState[i] == 1 && tableState[(i + 1) % count] == 1) {
                newTableState[i] = tableOfResults[7];
            }
            if (tableState[endOfTableState] == 1 && tableState[i] == 1 && tableState[(i + 1) % count] == 0) {
                newTableState[i] = tableOfResults[6];
            }
            if (tableState[endOfTableState] == 1 && tableState[i] == 0 && tableState[(i + 1) % count] == 1) {
                newTableState[i] = tableOfResults[5];
            }
            if (tableState[endOfTableState] == 1 && tableState[i] == 0 && tableState[(i + 1) % count] == 0) {
                newTableState[i] = tableOfResults[4];
            }
            if (tableState[endOfTableState] == 0 && tableState[i] == 1 && tableState[(i + 1) % count] == 1) {
                newTableState[i] = tableOfResults[3];
            }
            if (tableState[endOfTableState] == 0 && tableState[i] == 1 && tableState[(i + 1) % count] == 0) {
                newTableState[i] = tableOfResults[2];
            }
            if (tableState[endOfTableState] == 0 && tableState[i] == 0 && tableState[(i + 1) % count] == 1) {
                newTableState[i] = tableOfResults[1];
            }
            if (tableState[endOfTableState] == 0 && tableState[i] == 0 && tableState[(i + 1) % count] == 0) {
                newTableState[i] = tableOfResults[0];
            }
        }
        return newTableState;
    }
}
