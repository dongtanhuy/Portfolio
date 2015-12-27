package group20.flashcards.flashcards.CardStructure;

import java.util.ArrayList;

public class Subject extends Base {
    private ArrayList<FlashCard> flashCards; // list of flashcards belong to the subject

    /**
     * Basic Constructor
     */
    public Subject() {
    }

    /**
     * Constructor
     */
    public Subject(String subject, ArrayList<FlashCard> flashCards) {
        super(subject);
        this.flashCards = flashCards;
    }

    public ArrayList<FlashCard> getFlashCards() {
        return flashCards;
    }

    public void setFlashCards(ArrayList<FlashCard> flashCards) {
        this.flashCards = flashCards;
    }
}
