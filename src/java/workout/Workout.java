package workout;

import java.util.Date;

public class Workout {
    
    private int number = 0;
    private static int totNumber = 0;
    private Date date = new Date();
    int year;
    private int duration;
    private String text;
    private String category;

    public Workout() {
        updateTotNumber();
    }

    public Workout(Date date, int duration, String text, String category) {
        this.date = date;
        this.duration = duration;
        this.text = text;
        this.category = category;
        updateTotNumber();
    }
    
    public void setNumber(int number){
        this.number = number;
    }
    
    public synchronized int getNumber(){
        return number;
    }

    public synchronized void updateTotNumber() {
        totNumber++;
    }

    public synchronized int getTotNumber() {
        return totNumber;
    }

    public synchronized void setCategory(String category) {
        try {
            this.category = category;
        } catch (IllegalArgumentException e) {
            System.out.println("Illegal value!");
        }
    }

    public synchronized String getCategory() {
        return category;
    }

    public synchronized Date getDate() {
        return date;
    }
    public synchronized void setDate(Date date) {
        this.date = date;
    }

    public synchronized String getText() {
        return text;
    }

    public synchronized void setText(String text) {
            this.text = text;
    }

    public synchronized int getDuration() {
        return duration;
    }

    public synchronized void setDuration(int duration) {
            this.duration = duration;
    }
   
    public synchronized void reset() {
        date = null;
        duration = 0;
        text = null;
        category = null;
    }
}
