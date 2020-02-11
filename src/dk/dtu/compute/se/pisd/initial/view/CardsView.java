/*
 *  This file is part of the initial project provided for the
 *  course "Project in Software Development (02362)" held at
 *  DTU Compute at the Technical University of Denmark.
 *
 *  Copyright (C) 2019, 2020: Ekkart Kindler, ekki@dtu.dk
 *
 *  This software is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; version 2 of the License.
 *
 *  This project is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this project; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 */
package dk.dtu.compute.se.pisd.initial.view;


import com.sun.istack.internal.NotNull;

import dk.dtu.compute.se.pisd.designpatterns.observer.Subject;
import dk.dtu.compute.se.pisd.initial.controller.GameController;
import dk.dtu.compute.se.pisd.initial.model.CommandCardField;
import dk.dtu.compute.se.pisd.initial.model.Player;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

/**
 * ...
 *
 * @author Ekkart Kindler, ekki@dtu.dk
 *
 */
public class CardsView extends VBox implements ViewObserver {

    private Player player;

    private GameController gameController;

    private Label playerLabel;
    private Label programLabel;
    private GridPane programPane;
    private Label cardsLabel;
    private GridPane cardsPane;

    private CardFieldView[] programCardViews;
    private CardFieldView[] cardViews;

    private Button finishButton;
    private Button executeButton;
    private Button stepButton;

    public CardsView(@NotNull GameController gameController, @NotNull Player player) {
        this.gameController = gameController;
        this.player = player;

        playerLabel = new Label(player.getName());
        playerLabel.setTextFill(Color.valueOf(player.getColor()));
        playerLabel.setFont((Font.font(null, FontWeight.BOLD, 24)));
        programLabel = new Label("Program");

        programPane = new GridPane();
        programPane.setVgap(2.0);
        programPane.setHgap(2.0);
        programCardViews = new CardFieldView[Player.NO_REGISTERS];
        for (int i = 0; i < Player.NO_REGISTERS; i++) {
            CommandCardField cardField = player.getProgramField(i);
            if (cardField != null) {
                programCardViews[i] = new CardFieldView(gameController, cardField);
                programPane.add(programCardViews[i], i, 0);
            }
        }

        finishButton = new Button("Finish Programming");
        finishButton.setOnAction( e -> {
            gameController.finishProgrammingPhase();
        });

        executeButton = new Button("Execute Program");
        executeButton.setOnAction( e-> {
            gameController.executePrograms();
        });

        stepButton = new Button("Execute Current Register");
        stepButton.setOnAction( e-> {
            gameController.executeStep();
        });

        VBox buttonPanel = new VBox(finishButton, executeButton, stepButton);
        buttonPanel.setAlignment(Pos.CENTER_LEFT);
        buttonPanel.setSpacing(3.0);
        programPane.add(buttonPanel, Player.NO_REGISTERS, 0);

        cardsLabel = new Label("Command Cards");
        cardsPane = new GridPane();
        cardsPane.setVgap(2.0);
        cardsPane.setHgap(2.0);
        cardViews = new CardFieldView[Player.NO_CARDS];
        for (int i = 0; i < Player.NO_CARDS; i++) {
            CommandCardField cardField = player.getCardField(i);
            if (cardField != null) {
                cardViews[i] = new CardFieldView(gameController, cardField);
                cardsPane.add(cardViews[i], i, 0);
            }
        }

        this.getChildren().add(playerLabel);
        this.getChildren().add(programLabel);
        this.getChildren().add(programPane);
        this.getChildren().add(cardsLabel);
        this.getChildren().add(cardsPane);


        // player.attach(this);
        if (player.board != null) {
            update(player.board);
            player.board.attach(this);
        }

        updateView(player.board);
    }

    @Override
    public void updateView(Subject subject) {
        if (subject == player.board) {
            switch (player.board.getPhase()) {
                case INITIALISATION:
                    finishButton.setDisable(true);
                    // XXX just to make sure that there is a way for the player to get
                    //     from the initialization phase to the programming phase some ow!
                    executeButton.setDisable(false);
                    stepButton.setDisable(true);
                    break;

                case PROGRAMMING:
                    finishButton.setDisable(false);
                    executeButton.setDisable(true);
                    stepButton.setDisable(true);
                    break;

                case ACTIVATION:
                    finishButton.setDisable(true);
                    executeButton.setDisable(false);
                    stepButton.setDisable(false);
                    break;

                default:
                    finishButton.setDisable(true);
                    executeButton.setDisable(true);
                    stepButton.setDisable(true);
            }
        }
    }

}
