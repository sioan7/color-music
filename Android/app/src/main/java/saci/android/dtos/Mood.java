package saci.android.dtos;

/**
 * Created by Corina on 5/20/2017.
 */
public class Mood {

    private String color;
    private String mood;

    public Mood() {
    }

    public Mood(String color, String mood) {
        this.color = color;
        this.mood = mood;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getMood() {
        return mood;
    }

    public void setMood(String mood) {
        this.mood = mood;
    }
}
