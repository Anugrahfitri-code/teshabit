public class NegativeHabit extends Habit {
    public NegativeHabit(String name) {
        super(name);
    }

    @Override
    public void markDone() {
        this.jumlahDilakukan++;
        System.out.println(getName() + " dilakukan, ini kebiasaan buruk. Skor turun menjadi " + getScore());
    }

    @Override
    public int getScore() {
        int total = 0;
        for (int i = 1; i <= jumlahDilakukan; i++) {
            total -= 5 * i;  
        }
        return total;
    }
}


