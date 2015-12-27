package group20.flashcards.flashcards.CardStructure;


public class FlashCard extends Base {
    private Subject subject;
    private String content, answer;

    /**
     * Basic Constructor
     */
    public FlashCard() {
    }

    /**
     * Constructor
     */
    public FlashCard(Subject subject, String title, String content, String answer) {
        super(title);
        this.subject = subject;
        this.content = content;
        this.answer = answer;
    }

    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
}
