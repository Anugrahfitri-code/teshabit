public class PositiveHabit extends Habit {
    public PositiveHabit(String name) {
        super(name);
    }

    @Override
    public void markDone() {
        this.jumlahDilakukan++;
        System.out.println(getName() + " berhasil dilakukan. Skor bertambah menjadi " + getScore());
    }

    @Override
    public int getScore() {
        int total = 0;
        for (int i = 1; i <= jumlahDilakukan; i++) {
            total += 10 + (i - 1) * 5;
        }
        return total;
    }
}
