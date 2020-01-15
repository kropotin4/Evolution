package model;

import org.jetbrains.annotations.Contract;

import java.io.Serializable;

/******************************************
 * This class define Evolutions game cards.
 * There are two ways to use the card:
 *  1. Add new creature
 *  2. Add new trait to exist creature
 * May be one or two traits on one card.
 *
 * @author akropotin
 *
 * @see Trait
 * @see Creature
 ******************************************/
public class Card implements Serializable {

    private static int idCounter = 0;
    private int id = idCounter;

    private boolean fat = false;    // show the presence of fat on card
    private boolean used = false;   // show the use of trait on current phase (when card trait already add to creature)

    private Trait up;
    private Trait down;
    private boolean isUp;   // indicate active trait when card have got two trait and card trait add to creature

    /**********************
     * Card class constructor. One trait.
     * @param trait trait of this card.
     * @see Trait
     **********************/
    public Card(Trait trait){
        idCounter++;
        up = trait;
        down = trait;
    }

    /**********************
     * Card class constructor. Two trait.
     * @param upTrait   first trait of this card.
     * @param downTrait second trait of this card.
     * @see Trait
     **********************/
    public Card(Trait upTrait, Trait downTrait){
        idCounter++;
        up = upTrait;
        down = downTrait;
    }

    /**********************
     * Get active trait of card.
     * @return active trait
     * @see Trait
     **********************/
    public Trait getTrait(){
        return isUp ? up : down;
    }

    /**********************
     * Get first or second trait of card.
     * @param isUp the side (up or down) of card.
     * @return trait
     * @see Trait
     **********************/
    public Trait getTrait(boolean isUp){
        return isUp ? up : down;
    }

    /**********************
     * Get active side of card.
     * @return true -- if up side is active
     **********************/
    public boolean isUp(){
        return isUp;
    }

    /**********************
     * Turn active side of card.
     **********************/
    public void turnCard(){
        isUp = !isUp;
    }

    /**********************
     * Set fat trait of card (used if available).
     * @param fat fat value.
     * @see Trait
     **********************/
    public void setFat(boolean fat) {
        this.fat = fat;
    }

    /**********************
     * Get fat trait value (used if available).
     * @return value of fat
     **********************/
    public boolean isFat() {
        return fat;
    }

    /**********************
     * Get value of used active trait (when card trait add to creature).
     * @return used value
     **********************/
    public boolean isUsed() {
        return used;
    }

    /**********************
     * Set value of used active trait (when card trait add to creature).
     * @param used value of used.
     **********************/
    public void setUsed(boolean used) {
        this.used = used;
    }

    /**********************
     * Return card id.
     * @return id
     **********************/
    public int getId(){
        return id;
    }

    @Contract(value = "null -> false", pure = true)
    @Override
    public boolean equals(Object obj) {
        if(this == obj) return true;

        if(!(obj instanceof Card)) return false;

        Card card = (Card) obj;

        return (card.up == up && card.down == down)
                || (card.up == down && card.down == up);
    }

    @Override
    public String toString() {
        if(up == down)
            return "Card{" +
                    "id=" + id +
                    ", trait=" + up +
                    '}';
        else
            return "Card{" +
                    "id=" + id +
                    ", up=" + up +
                    ", down=" + down +
                    '}';
    }
}
