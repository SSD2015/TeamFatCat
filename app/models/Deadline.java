package models;

import play.data.validation.Constraints;
import play.db.ebean.Model;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class Deadline extends Model {

    @Id
    private long id;

//    @Constraints.Required
//    private Date date;

    @Constraints.Required
    private int year;

    @Constraints.Required
    private int month;

    @Constraints.Required
    private int day;

    @Constraints.Required
    private int hour;

    @Constraints.Required
    private int min;

    @Constraints.Required
    private int sec;

    private static Finder<Long, Deadline> find = new Finder<Long, Deadline>(Long.class, Deadline.class);

    public static Deadline getDeadline() {
        if (find.all().size() == 0) {
            Deadline.create(2020, 1, 1, 0, 0, 0);
        }
        return find.all().get(0);
    }

    public static Deadline create(int year, int month, int day, int hour, int min, int sec) {
        if (find.all().size() <= 0) {
            Deadline deadline = new Deadline(year, month, day, hour, min, sec);
            deadline.save();
            return deadline;
        }

        Deadline deadline = find.all().get(0);
        deadline.setYear(year);
        deadline.setMonth(month);
        deadline.setDay(day);
        deadline.setHour(hour);
        deadline.setMin(min);
        deadline.setSec(sec);
        deadline.update();
        return deadline;
    }

    public Deadline(int year, int month, int day, int hour, int min, int sec) {
        this.year = year;
        this.month = month;
        this.day = day;
        this.hour = hour;
        this.min = min;
        this.sec = sec;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMin() {
        return min;
    }

    public void setMin(int min) {
        this.min = min;
    }

    public int getSec() {
        return sec;
    }

    public void setSec(int sec) {
        this.sec = sec;
    }

    public Long getId() {
        return this.id;
    }

}