import java.util.ArrayList;
import java.util.List;

public class HabitManajer {
    private List<Trackable> habits;

    public HabitManajer(){
        habits = new ArrayList<>();
    }

    public void addHabit(Trackable h){
        habits.add(h);
    }

    public void resetScore(int index){
        habits.get(index).resetScore();
    }

    public void markHabit(int index){
        habits.get(index).markDone();
    }

    public List<Trackable> getAllHabits(){
        return habits;
    }

    public int getTotalScore() {
    int totalScore = 0;
    for (Trackable h : habits) {
        totalScore += ((Habit) h).getScore();  
    }
    return totalScore;
    }

    public void removeHabit(int index) {
    if (index >= 0 && index < habits.size()) {
        habits.remove(index);
        System.out.println("Habit berhasil dihapus.");
    } else {
        System.out.println("Index tidak valid.");
    }
    }
    public boolean isHabitExists(String name) {
    for (Trackable h : habits) {
        Habit habit = (Habit) h;
        if (habit.getName().equalsIgnoreCase(name)) {
            return true;
        }
    }
    return false;
    }
    public List<Habit> getHabits() {
    List<Habit> list = new ArrayList<>();
    for (Trackable t : habits) {
        if (t instanceof Habit) {
            list.add((Habit) t);
        }
    }
    return list;
}



}
