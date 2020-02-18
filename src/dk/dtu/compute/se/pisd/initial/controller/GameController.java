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
package dk.dtu.compute.se.pisd.initial.controller;

import com.sun.codemodel.internal.JCommentPart;
import com.sun.istack.internal.NotNull;

import dk.dtu.compute.se.pisd.initial.model.*;

import javax.swing.text.Position;
import java.util.List;

/**
 * ...
 *
 * @author Ekkart Kindler, ekki@dtu.dk
 *
 */
public class GameController {

    final public Board board;

    public GameController(Board board) {
        this.board = board;
    }

    public void moveForward(Player player) {
        if (player != null && player.board == board) {
            Space currentPosition = player.getSpace();

            int x = currentPosition.x;
            int y = currentPosition.y;

            // TODO compute x and y of the new position when
            //            //     the player moves forward!



            Heading heading = player.getHeading();
            switch (heading) {
                case EAST:
                    x = (x+1)%player.board.width;
                    break;
                case WEST:
                    x = (x+player.board.width-1)%player.board.width;
                    break;
                case NORTH:
                    y = (y+player.board.height-1)%player.board.height;
                    break;
                case SOUTH:
                    y = (y+1)%player.board.height;
                    break;
                default:
            }




            Space newPosition = board.getSpace(x, y);
            if (newPosition != null &&
                    newPosition.getPlayer() == null &&
                    newPosition != currentPosition) {
                newPosition.setPlayer(player);
            }
        }
    }

    public void fastForward(Player player) {
        for(int i = 0; i<2; i++)
            moveForward(player);
            }


    public void turnRight(Player player) {
        player.setHeading(player.getHeading().next());



    }

    public void turnLeft(Player player) {
        player.setHeading(player.getHeading().prev());

    }

    public boolean moveCards(@NotNull CommandCardField source, @NotNull CommandCardField target) {
        CommandCard sourceCard = source.getCard();
        CommandCard targetCard = target.getCard();
        if (sourceCard != null & targetCard == null) {
            target.setCard(sourceCard);
            source.setCard(null);
            return true;
        } else {
            return false;
        }

    }

    public void finishProgrammingPhase() {
        board.setPhase(Phase.ACTIVATION);
        board.setStep(0);
    }

    public void executeStep() {
        Player currentPlayer = board.getCurrentPlayer();
        if (board.getPhase() == Phase.ACTIVATION && currentPlayer != null) {
            int step = board.getStep();
            if (step >= 0 && step < Player.NO_REGISTERS) {
                executeCommandCard(currentPlayer, currentPlayer.getProgramField(step).getCard());
                step++;
                board.setStep(step);
            }
            if (step < 0 || step >= Player.NO_REGISTERS) {
                iniatilizePogrammingPhase();
            }
        }

        if (board.getPhase() == Phase.INITIALISATION) {
            iniatilizePogrammingPhase();
        }
    }

    public void executePrograms() {
        while (board.getPhase() == Phase.ACTIVATION) {
            executeStep();
        }
        iniatilizePogrammingPhase();
    }

    public void iniatilizePogrammingPhase() {
        board.setPhase(Phase.PROGRAMMING);
        board.setStep(0);

        Player currentPlayer = board.getCurrentPlayer();
        if (currentPlayer != null) {
            for (int i = 0; i < Player.NO_REGISTERS; i++) {
                currentPlayer.getProgramField(i).setCard(null);
            }
            for (int i = 0; i < Player.NO_CARDS; i++) {
                currentPlayer.getCardField(i).setCard(generateRandomCommandCard());
            }
        }
    }

    private CommandCard generateRandomCommandCard() {
        Command[] commands = Command.values();
        int random = (int) (Math.random() * commands.length);
        return new CommandCard(commands[random]);
    }

    private void executeCommandCard(@NotNull Player player, CommandCard card) {
        if (card != null && card.command != null) {
            // XXX This is an very simplistic way of dealing with some basic cards and
            // their execution. This should eventually be done in a much more elegant way
            // (this concerns the way cards are modelled as well, the way they are executed.
            switch (card.command) {
                case FORWARD:
                    this.moveForward(player);
                    break;
                case RIGHT:
                    this.turnRight(player);
                    break;
                case LEFT:
                    this.turnLeft(player);
                    break;
                case FAST_FORWARD:
                    this.fastForward(player);
                    break;
                default:
                    // DO NOTHING (for now)
            }
        }
    }

}
