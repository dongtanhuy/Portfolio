package group20.flashcards.flashcards.CardStructure;


// Base structure for FlashCard and Subject
public abstract class Base {
    private String title; // All cards and subjects must have title to display
    private long id; // id to save to DB


    /**
     * Basic Constructor
     */
    protected Base() {
    }

    /**
     * @param title
     *       The title for this Item
     */
    protected Base(String title) {
        this.title = title;
    }


    // STANDARD GETTERS AND SETTERS

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

}
