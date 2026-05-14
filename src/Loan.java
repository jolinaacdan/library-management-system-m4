import java.time.LocalDate;

public class Loan {
    private Member member;
    private LibraryItem item;
    private LocalDate borrowDate;
    private LocalDate dueDate;
    private boolean isReturned;

    public Loan(Member member, LibraryItem item) {
        this.member = member;
        this.item = item;
        this.borrowDate = LocalDate.now();
        this.dueDate = borrowDate.plusDays(7);
        this.isReturned = false;
    }

    public LibraryItem getItem() { return item; }
    public Member getMember() { return member; }
    public boolean isReturned() { return isReturned; }
    public LocalDate getDueDate() { return dueDate; }

    public void returnItem() {
        this.isReturned = true;
        this.item.setAvailable(true);
    }
}