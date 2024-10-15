package mystery;
/*
 * Item class which holds attributes of items, such as name
 * if it was used as a murder weapon yet, and where it spawns
 * 
 * @author Colin Sullivan
 * @author Steven Chen
 */
public class Item {
  
    // The name of the item
    private String itemName;
    // Original room item spawns in
    private String roomName;
    // Has the murderer touched this item?
    private boolean marked;

    /*
     * Full Construtor for Item()
     * 
     * @param itemName The name of the item
     * @param room The name of the room the item spawns in
     */
    public Item(String itemName) {
        this.itemName = itemName;
        this.marked = false;
    }

    // GETTERS AND SETTERS
    public String getItemName(){return itemName;}
    public void setItemName(String itemName){this.itemName = itemName;}
    public boolean isMarked() {return this.marked;}
    public void setMarked(boolean toMark) {this.marked = toMark;}
    public String getRoomName() {return roomName;}
    public void setRoomName(String roomName) {this.roomName = roomName;}
}

