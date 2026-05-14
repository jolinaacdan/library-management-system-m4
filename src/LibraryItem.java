public class LibraryItem {
    private String itemId;
    private String title;
    private String category;
    private boolean isAvailable;

    public LibraryItem(String itemId, String title, String category) {
        this.itemId = itemId;
        this.title = title;
        this.category = category;
        this.isAvailable = true;
    }

    public String getItemId() { return itemId; }
    public String getTitle() { return title; }
    public String getCategory() { return category; }
    public boolean isAvailable() { return isAvailable; }
    public void setAvailable(boolean status) { this.isAvailable = status; }
}