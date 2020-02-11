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


import dk.dtu.compute.se.pisd.designpatterns.observer.Observer;
import dk.dtu.compute.se.pisd.designpatterns.observer.Subject;
import dk.dtu.compute.se.pisd.initial.model.Player;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;

/**
 * ...
 *
 * @author Ekkart Kindler, ekki@dtu.dk
 *
 */
public class PlayerView extends GridPane implements ViewObserver {

    private Player player;

    private Label playerLabel;
    private GridPane programPane;
    private Label cardsLabel;
    private GridPane cardsPane;

    public PlayerView(Player player) {
        this.player = player;

        playerLabel = new Label(player.getName());
        programPane = new GridPane();
        cardsLabel = new Label("Command Cards");
        cardsPane = new GridPane();

        this.add(playerLabel, 0,0);
        this.add(programPane, 0,1);
        this.add(cardsLabel, 0,2);
        this.add(cardsPane, 0, 3);
    }

    @Override
    public void updateView(Subject subject) {
        // XXX nothing to upatde yet at this level
    }

}
