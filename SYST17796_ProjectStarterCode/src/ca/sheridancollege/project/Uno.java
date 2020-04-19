/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import static java.lang.Thread.sleep;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.Random;

/**
 *
 * @author omerm
 */
public class Uno {
    static Random rn = new Random();
    static deal play1 = new deal();
    static deal comp1 = new deal();
    static deal comp2 = new deal();
    static deal comp3 = new deal(); 
    static cardHandler deck = new cardHandler();
    static Scanner s = new Scanner(System.in);
    static Scanner si = new Scanner(System.in);
    static deal discardPile = new deal();
    static int numComp = 0;
    static boolean reverse = false; 
    static boolean skip = false; 
    static boolean gameEnded = false; 
    static boolean draw2 = false; 
    static boolean draw4 = false; 

    public static void main(String[] args) throws InterruptedException {
        deck.shuffleDeck();
        deck.shuffleDeck();
        boolean compNumGot = false;
        do {
            try {
                System.out.print("How many computers do you want to play against?(1-3): ");
                numComp = si.nextInt();
                si.nextLine();
            } catch (InputMismatchException e) {
                System.out.println("Invalid Input");
                si.nextLine();
            }
            if (numComp <= 3 && numComp >= 1) {
                compNumGot = true;
            } else {
                System.out.println("Please enter a number between 1 and 3");
                System.out.println();
                compNumGot = false;
            }
        } while (!compNumGot);
        if (numComp == 1) {
            for (int i = 0; i <= 6; i++) { 
                play1.addCard(deck);
                comp1.addCard(deck);
            }
        } else if (numComp == 2) {
            for (int i = 0; i <= 6; i++) {
                play1.addCard(deck);
                comp1.addCard(deck);
                comp2.addCard(deck);
            }
        } else if (numComp == 3) {
            for (int i = 0; i <= 6; i++) {
                play1.addCard(deck);
                comp1.addCard(deck);
                comp2.addCard(deck);
                comp3.addCard(deck);
            }
        }
        discardPile.addCard(deck);
        if (Card.getCardNumber(discardPile.getLast()) == 14) { 
            discardPile.addCard(wildColor(14));
        } else if (Card.getCardNumber(discardPile.getLast()) == 13) {
            discardPile.addCard(wildColor(13));
        }
        int currentPlayer = 1; 
        boolean uno = false; 
        do { 
            while (currentPlayer == 1) { 
                skip = false;  
                draw2 = false; 
                draw4 = false; 
                sleep(1500);
                boolean unoCalled = false; 
                int cardPlayed = 0; 
                System.out.println(); 
                int choice = 0; 
                Boolean drawCard = false; 
                do {
                    uno = checkUno(play1); 
                    System.out.println(); 
                    printHand(play1, drawCard, uno, unoCalled); 
                    System.out.println(); 
                    printDiscard();
                    choice = getCardNumber();
                    while (choice > play1.getSize() + 2 || choice < 0) { 
                        System.out.println("Invalid input, please try again");
                        choice = getCardNumber();
                    }
                    int elem = choice - 1; 
                    if (choice == (play1.getSize() + 1)) {
                        if (!drawCard) { 
                            play1.addCard(deck);
                            drawCard = true;
                        } else if (drawCard) {
                            cardPlayed = 1;
                        }
                    } else if (choice == (play1.getSize() + 2)) {
                        if (!unoCalled) { 
                            System.out.println();
                            System.out.println("Player 1 Calls Uno");
                            unoCalled = true;
                        } else if (unoCalled || !uno) {
                            System.out.println();
                            System.out.println("Please Select a Valid Card");
                        }
                    } else if (Card.getCardColor(play1.getCard(elem)) == 'a' && Card.getCardNumber(play1.getCard(elem)) == 13) {  
                        discardPile.addCard(wildColor(13)); 
                        play1.removeCard(elem);
                        cardPlayed = 1;
                    } else if (Card.getCardColor(play1.getCard(elem)) == 'a' && Card.getCardNumber(play1.getCard(elem)) == 14) { 
                        discardPile.addCard(wildColor(14));
                        draw4 = true;
                        play1.removeCard(elem);
                        cardPlayed = 1;
                    } else if (Card.getCardColor(play1.getCard(elem)) == Card.getCardColor(discardPile.getLast()) || Card.getCardNumber(play1.getCard(elem)) == Card.getCardNumber(discardPile.getLast())) {
                        discardPile.addCard(play1.getCard(elem));
                        switch (Card.getCardNumber(play1.getCard((elem)))) {
                            case 10:
                                skip = true;
                                break;
                            case 11:
                                draw2 = true;
                                break;
                            case 12:
                                if (reverse) {
                                    reverse = false;
                                } else if (!reverse) {
                                    reverse = true;
                                }
                                break;
                            default:
                                break;
                        }
                        play1.removeCard(elem);
                        cardPlayed = 1;
                    } else {
                        System.out.println();
                        System.out.println("Please Select a valid card");
                    }
                } while (cardPlayed == 0);
                if (play1.getSize() == 1 && !unoCalled) {
                    System.out.println("Player 1 did not call uno. +2");
                    play1.addCard(deck);
                    play1.addCard(deck);
                }
                if (play1.getSize() > 1 && unoCalled) {
                    System.out.println("Player 1 falsely called uno. +2");
                    play1.addCard(deck);
                    play1.addCard(deck);
                }
                if (play1.getSize() == 0) {
                    System.out.println();
                    System.out.println("Player 1 won");
                    gameEnded = true;
                    System.exit(0);
                }
                if (numComp == 1) {
                    if (reverse && skip || !reverse && skip) {
                        currentPlayer = 1;
                    } else if (reverse && !skip || !reverse && !skip) {
                        currentPlayer = 2;
                        if (draw2) {
                            draw2(comp1);
                        }
                        if (draw4) {
                            draw4(comp1);
                        }
                    }
                } else if (numComp == 2) { 
                    if (reverse && skip) {
                        currentPlayer = 2;
                    } else if (!reverse && skip) {
                        currentPlayer = 3;
                    } else if (reverse && !skip) {
                        currentPlayer = 3;
                        if (draw2) {
                            draw2(comp2);
                        }
                        if (draw4) {
                            draw4(comp2);
                        }
                    } else if (!reverse && !skip) {
                        currentPlayer = 2;
                        if (draw2) {
                            draw2(comp1);
                        }
                        if (draw4) {
                            draw4(comp1);
                        }
                    }
                } else if (numComp == 3) { 
                    if (reverse && skip || !reverse && skip) {
                        currentPlayer = 3;
                    } else if (!reverse && !skip) {
                        currentPlayer = 2;
                        if (draw2) {
                            draw2(comp1);
                        }
                        if (draw4) {
                            draw4(comp1);
                        }
                    } else if (reverse && !skip) {
                        currentPlayer = 4;
                        if (draw2) {
                            draw2(comp3);
                        }
                        if (draw4) {
                            draw4(comp3);
                        }
                    } else {
                        System.out.println("ERROR");
                    }
                }
                checkDraw(deck, discardPile);
                System.out.println();
                break;
            }
            while (currentPlayer == 2) {
                int compNumber = 1;
                computerPlay(comp1, skip, draw2, draw4, uno, reverse, gameEnded, compNumber);
                if (numComp == 1) { 
                    if (reverse && skip || !reverse && skip) {
                        currentPlayer = 2;
                    } else if (reverse && !skip || !reverse && !skip) {
                        currentPlayer = 1;
                        if (draw2) {
                            draw2(play1);
                        }
                        if (draw4) {
                            draw4(play1);
                        }
                    }
                } else if (numComp == 2) { 
                    if (reverse && skip) {
                        currentPlayer = 3;
                    } else if (!reverse && skip) {
                        currentPlayer = 1;
                    } else if (reverse && !skip) {
                        currentPlayer = 1;
                        if (draw2) {
                            draw2(play1);
                        }
                        if (draw4) {
                            draw4(play1);
                        }
                    } else if (!reverse && !skip) {
                        currentPlayer = 3;
                        if (draw2) {
                            draw2(comp2);
                        }
                        if (draw4) {
                            draw4(comp2);
                        }
                    }
                } else if (numComp == 3) {
                    if (reverse && skip || !reverse && skip) {
                        currentPlayer = 4;
                    } else if (!reverse && !skip) {
                        currentPlayer = 3;
                        if (draw2) {
                            draw2(comp2);
                        }
                        if (draw4) {
                            draw4(comp2);
                        }
                    } else if (reverse && !skip) {
                        currentPlayer = 1;
                        if (draw2) {
                            draw2(play1);
                        }
                        if (draw4) {
                            draw4(play1);
                        }
                    }
                }
                checkDraw(deck, discardPile);
                break;

            }
            while (currentPlayer == 3) {
                int compNumber = 2;
                computerPlay(comp2, skip, draw2, draw4, uno, reverse, gameEnded, compNumber);
                if (numComp == 2) { 
                    if (reverse && skip) {
                        currentPlayer = 3;
                    } else if (!reverse && skip) {
                        currentPlayer = 1;
                    } else if (reverse && !skip) {
                        currentPlayer = 2;
                        if (draw2) {
                            draw2(comp1);
                        }
                        if (draw4) {
                            draw4(comp1);
                        }
                    } else if (!reverse && !skip) {
                        currentPlayer = 1;
                        if (draw2) {
                            draw2(comp3);
                        }
                        if (draw4) {
                            draw4(comp3);
                        }
                    }
                } else if (numComp == 3) { 
                    if (reverse && skip || !reverse && skip) {
                        currentPlayer = 1;
                    } else if (!reverse && !skip) {
                        currentPlayer = 4;
                        if (draw2) {
                            draw2(comp3);
                        }
                        if (draw4) {
                            draw4(comp3);
                        }
                    } else if (reverse && !skip) {
                        currentPlayer = 2;
                        if (draw2) {
                            draw2(comp1);
                        }
                        if (draw4) {
                            draw4(comp1);
                        }
                    }
                }
                checkDraw(deck, discardPile);
                break;

            }
            while (currentPlayer == 4) {
                int compNumber = 3;
                computerPlay(comp3, skip, draw2, draw4, uno, reverse, gameEnded, compNumber);
                if (reverse && skip || !reverse && skip) {
                    currentPlayer = 2;
                } else if (!reverse && !skip) {
                    currentPlayer = 1;
                    if (draw2) {
                        draw2(comp1);
                    }
                    if (draw4) {
                        draw4(comp1);
                    }
                } else if (reverse && !skip) {
                    currentPlayer = 3;
                    if (draw2) {
                        draw2(comp3);
                    }
                    if (draw4) {
                        draw4(comp3);
                    }
                }
                checkDraw(deck, discardPile);
                break;

            }
        } while (!gameEnded);
    }

    public static void printHand(deal play1, boolean drawCard, boolean uno, boolean unoCalled) {
        int display = 0;
        for (int x = 0; x < play1.getSize(); x++) {
            display = x + 1;
            System.out.println(display + ". " + play1.getCard(x));
        }
        display++;
        if (!drawCard) {
            System.out.println(display + ". Draw Card");
        } else if (drawCard) {
            System.out.println(display + ". End Turn");
        }
        display++;
        if (uno && !unoCalled) {
            System.out.println(display + ". Uno");
        } else if (uno && unoCalled) {
            System.out.println("Uno Called");
        }
    }

    public static boolean checkUno(deal play) {
        boolean uno = false;
        if (play.getSize() == 2) {
            uno = true;
        } else {
            uno = false;
        }
        return uno;
    }

    public static Card wildColor(int cardNumber) {
        System.out.print("What color do you want the deck to be?: ");
        String input = s.nextLine();
        char color = 'a';
        input = input.toLowerCase();
        switch (input.charAt(0)) {
            case 'b':
                color = 'b';
                break;
            case 'r':
                color = 'r';
                break;
            case 'g':
                color = 'g';
                break;
            case 'y':
                color = 'y';
                break;
            default:
                System.out.println("Type: (b)lue, (g)reen, (r)ed or (y)ellow");
                wildColor(cardNumber);
                break;
        }
        return new Card(cardNumber, color);
    }

    public static void draw2(deal play) {
        for (int i = 0; i <= 1; i++) {
            play.addCard(deck);
        }
    }

    public static void draw4(deal play) {
        for (int i = 0; i <= 3; i++) {
            play.addCard(deck);
        }
    }

    public static void printDiscard() {
        System.out.print("Discard Pile: ");
        System.out.println(discardPile.getLast());
    }

    public static void checkDraw(cardHandler deck, deal discardPile) {
        if (deck.getSize() <= 4) {
            System.out.println();
            System.out.println("Shuffling Deck");
            System.out.println();
            for (int i = 0; i < discardPile.getSize(); i++) {
                if (Card.getCardNumber(discardPile.getLast()) == 13 && Card.getCardColor(discardPile.getLast()) != 'a') {
                    discardPile.removeCard(discardPile.getSize() - 1);
                    discardPile.addCard(new Card(13, 'a'));
                } else if (Card.getCardNumber(discardPile.getLast()) == 14 && Card.getCardColor(discardPile.getLast()) != 'a') {
                    discardPile.removeCard(discardPile.getSize() - 1);
                    discardPile.addCard(new Card(14, 'a'));
                } else {
                    deck.addCard(discardPile.getLast());
                    discardPile.removeCard(discardPile.getSize() - 1);
                }
            }
            deck.shuffleDeck();
        } else {

        }

    }

    public static int getComputerChoice(Card dCard, deal Computer, boolean unoCalled) {
        int choice = 0;
        boolean hWild = false;
        boolean hSkip = false;
        boolean hReverse = false;
        boolean hDTwo = false;
        boolean hDFour = false;
        boolean hPlayable = false;
        hWild = hasWild(dCard, Computer);
        hSkip = hasSkip(dCard, Computer);
        hReverse = hasReverse(dCard, Computer);
        hDTwo = hasDrawTwo(dCard, Computer);
        hDFour = hasDrawFour(dCard, Computer);
        hPlayable = hasPlayable(dCard, Computer);
        if (Computer.getSize() == 2 && !unoCalled) {
            choice = Computer.getSize() + 2;
        } else if (hDTwo) {
            choice = findDTwo(dCard, Computer);
            choice++;
        } else if (hSkip) {
            choice = findSkip(Computer, dCard);
            choice++;
        } else if (hReverse) {
            choice = findReverse(dCard, Computer);
            choice++;
        } else if (hPlayable) {
            choice = findPlayable(dCard, Computer);
            choice++;
        } else if (hWild && !hDTwo && !hSkip && !hReverse && !hPlayable) {
            choice = findWild(Computer);
            choice++;
        } else if (hDFour && !hWild && !hDTwo && !hSkip && !hReverse && !hPlayable) {
            choice = findDrawFour(Computer);
            choice++;
        } else {
            choice = Computer.getSize() + 1;
        }
        return choice;
    }

    public static int findWild(deal Computer) {
        int elem = 0;
        for (int i = 0; i < Computer.getSize(); i++) {
            if (Card.getCardNumber(Computer.getCard(i)) == 13) {
                elem = i;
            }
        }
        return elem;
    }

    public static int findDTwo(Card dCard, deal Computer) {
        int elem = 0;
        for (int i = 0; i < Computer.getSize(); i++) {
            if (Card.getCardNumber(Computer.getCard(i)) == 11 && Card.getCardColor(Computer.getCard(i)) == Card.getCardColor(dCard)) {
                elem = i;
            }
        }
        return elem;
    }

    public static int findSkip(deal Computer, Card dCard) {
        int elem = 0;
        for (int i = 0; i < Computer.getSize(); i++) {
            if (Card.getCardNumber(Computer.getCard(i)) == 10 && Card.getCardColor(Computer.getCard(i)) == Card.getCardColor(dCard)) {
                elem = i;
            }
        }
        return elem;
    }

    public static int findReverse(Card dCard, deal Computer) {
        int elem = 0;
        for (int i = 0; i < Computer.getSize(); i++) {
            if (Card.getCardNumber(Computer.getCard(i)) == 12 && Card.getCardColor(Computer.getCard(i)) == Card.getCardColor(dCard)) {
                elem = i;
            }
        }
        return elem;
    }

    public static int findDrawFour(deal Computer) {
        int elem = 0;
        for (int i = 0; i < Computer.getSize(); i++) {
            if (Card.getCardNumber(Computer.getCard(i)) == 14) {
                elem = i;
            }
        }
        return elem;
    }

    public static int findPlayable(Card dCard, deal Computer) {
        int elem = 0;
        for (int i = 0; i < Computer.getSize(); i++) {
            if (Card.getCardNumber(Computer.getCard(i)) < 10 && Card.getCardColor(Computer.getCard(i)) == Card.getCardColor(dCard)) {
                elem = i;
            }
        }
        return elem;
    }

    public static boolean hasWild(Card dCard, deal Computer) {
        boolean hWild = false;
        for (int i = 0; i < Computer.getSize(); i++) {
            if (Card.getCardNumber(Computer.getCard(i)) == 13) {
                hWild = true;
            }
        }
        return hWild;
    }

    public static boolean hasSkip(Card dCard, deal Computer) {
        boolean hSkip = false;
        for (int i = 0; i < Computer.getSize(); i++) {
            if (Card.getCardNumber(Computer.getCard(i)) == 10 && Card.getCardColor(Computer.getCard(i)) == Card.getCardColor(dCard)) {
                hSkip = true;
            }
        }
        return hSkip;
    }

    public static boolean hasReverse(Card dCard, deal Computer) {
        boolean hReverse = false;
        for (int i = 0; i < Computer.getSize(); i++) {
            if (Card.getCardNumber(Computer.getCard(i)) == 12 && Card.getCardColor(Computer.getCard(i)) == Card.getCardColor(dCard)) {
                hReverse = true;
            }
        }
        return hReverse;
    }

    public static boolean hasDrawTwo(Card dCard, deal Computer) {
        boolean hDTwo = false;
        for (int i = 0; i < Computer.getSize(); i++) {
            if (Card.getCardNumber(Computer.getCard(i)) == 11 && Card.getCardColor(Computer.getCard(i)) == Card.getCardColor(dCard)) {
                hDTwo = true;
            }
        }
        return hDTwo;
    }

    public static boolean hasDrawFour(Card dCard, deal Computer) {
        boolean hDrawFour = false;
        for (int i = 0; i < Computer.getSize(); i++) {
            if (Card.getCardNumber(Computer.getCard(i)) == 14) {
                hDrawFour = true;
            }
        }
        return hDrawFour;
    }

    public static boolean hasPlayable(Card dCard, deal Computer) {
        boolean hPlayable = false;
        for (int i = 0; i < Computer.getSize(); i++) {
            if (Card.getCardNumber(Computer.getCard(i)) < 10 && Card.getCardColor(Computer.getCard(i)) == Card.getCardColor(dCard)) {
                hPlayable = true;
            }
        }
        return hPlayable;
    }

    public static Card wildComputerColor(int cardNumber, deal computer) {
        int blue = 0;
        int red = 0;
        int yellow = 0;
        int green = 0;
        char cColor = 'a';
        for (int i = 0; i < computer.getSize(); i++) {
            if (Card.getCardColor(computer.getCard(i)) == 'b') {
                blue++;
            } else if (Card.getCardColor(computer.getCard(i)) == 'r') {
                red++;
            } else if (Card.getCardColor(computer.getCard(i)) == 'y') {
                yellow++;
            } else if (Card.getCardColor(computer.getCard(i)) == 'g') {
                green++;
            }
        }
        if (blue > green && blue > yellow && blue > red) {
            cColor = 'b';
        } else if (green > blue && green > yellow && green > red) {
            cColor = 'g';
        } else if (yellow > green && yellow > blue && yellow > red) {
            cColor = 'y';
        } else if (red > green && red > yellow && red > blue) {
            cColor = 'r';
        } else {
            cColor = 'b';
        }
        return new Card(cardNumber, cColor);
    }

    public static void computerPlay(deal comp, boolean skip, boolean draw2, boolean draw4, boolean uno, boolean reverse, boolean gameEnded, int compNumber) throws InterruptedException {
        skip = false;
        int special = 0;
        draw2 = false;
        draw4 = false;
        int choice = 0;
        int cardPlayed = 0; //this is similar to a boolean. 1's and 0's ya know. 
        boolean drawCard = false;
        boolean unoCalled = false;
        do {//do this while card played = 0
            sleep(2000);
            uno = checkUno(comp);
            Card dCard = discardPile.getLast();
            choice = getComputerChoice(dCard, comp, unoCalled);
            int elem = choice - 1;
            if (choice == (comp.getSize() + 1)) {
                if (!drawCard) {
                    System.out.println("Computer " + compNumber + " has drawn a card");
                    comp.addCard(deck);
                    drawCard = true;
                } else if (drawCard) {
                    System.out.println("Computer " + compNumber + " has ended their turn without playing a card");
                    cardPlayed = 1;
                }
            } else if (choice == (comp.getSize() + 2)) {
                if (!unoCalled) {
                    System.out.println("Computer " + compNumber + " Calls Uno");
                    unoCalled = true;
                }
            } else if (Card.getCardNumber(comp.getCard(elem)) == 13) {
                discardPile.addCard(wildComputerColor(13, comp));
                System.out.println("Computer " + compNumber + " played " + discardPile.getLast());
                comp.removeCard(elem);//remove the card from the player deck. it's no longer needed there
                cardPlayed = 1;
            } else if (Card.getCardNumber(comp.getCard(elem)) == 14) {
                discardPile.addCard(wildComputerColor(14, comp));
                System.out.println("Computer " + compNumber + " played " + discardPile.getLast());
                draw4 = true;
                comp.removeCard(choice - 1);
                cardPlayed = 1;
            } else if (Card.getCardColor(comp.getCard(elem)) == Card.getCardColor(discardPile.getLast()) || Card.getCardNumber(comp.getCard(choice - 1)) == Card.getCardNumber(discardPile.getLast())) {
                discardPile.addCard(comp.getCard(elem));
                switch (Card.getCardNumber(comp.getCard(elem))) {
                    case 10:
                        skip = true;
                        break;
                    case 11:
                        draw2 = true;
                        break;
                    case 12:
                        if (reverse) {
                            reverse = false;
                        } else if (!reverse) {
                            reverse = true;
                        } else {

                        }
                        break;
                    default:
                        break;
                }
                System.out.println("Computer " + compNumber + " played " + discardPile.getLast());
                comp.removeCard(choice - 1);
                cardPlayed = 1;
            }
        } while (cardPlayed == 0);
        if (comp.getSize() == 1 && !unoCalled) {
            System.out.println("Computer " + compNumber + " did not call uno. +2");
            comp.addCard(deck);
            comp.addCard(deck);
        }
        if (comp.getSize() == 0) {
            System.out.println();
            System.out.println("Computer " + compNumber + " won");
            gameEnded = true;
            System.exit(0);
        }
        if (reverse && skip) {
            special = 1;
        } else if (!reverse && skip) {
            special = 2;
        } else if (reverse && !skip) {
            if (draw2) {
                special = 4;
            } else if (draw4) {
                special = 5;
            } else {
                special = 3;
            }
        } else if (!reverse && !skip) {
            if (draw2) {
                special = 7;
            } else if (draw4) {
                special = 8;
            } else {
                special = 6;
            }
        }
    }

    public static int getCardNumber() {
        int choice = 0;
        do {
            try {
                System.out.print("Which card do you want to play? Play one card and then end your turn: ");
                choice = si.nextInt();
                si.nextLine();
            } catch (InputMismatchException e) {
                System.out.println("Invalid Input");
                si.nextLine();
            }
        } while (choice == 0);
        return choice;
    }
}
