import java.awt.*;
import javax.swing.*;
import javax.swing.border.Border;

public class HabitHeroSwingGUI {
    private JFrame frame;
    private DefaultListModel<Habit> habitListModel;
    private JList<Habit> habitJList;
    private HabitManajer manajer;
    private JLabel totalScoreLabel;
    private JButton showScoreButton;
    private boolean scoreVisible = false;

    public HabitHeroSwingGUI() {
        manajer = new HabitManajer();
        frame = new JFrame("HabitHero - Swing Edition");
        habitListModel = new DefaultListModel<>();
        habitJList = new JList<>(habitListModel);
        totalScoreLabel = new JLabel("Total Skor: ");
        totalScoreLabel.setVisible(false);

        habitJList.setCellRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
                Component c = super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                if (value instanceof Habit) {
                    Habit h = (Habit) value;
                    setText(h.getName() + " - Skor: " + h.getScore());
                    if (h instanceof PositiveHabit) {
                        setForeground(isSelected ? Color.WHITE : new Color(0, 128, 0));
                    } else {
                        setForeground(isSelected ? Color.WHITE : Color.RED.darker());
                    }
                }
                return c;
            }
        });

        JPanel detailPanel = new JPanel();
        detailPanel.setLayout(new GridLayout(3, 1));
        JLabel nameDetail = new JLabel("Nama: -");
        JLabel typeDetail = new JLabel("Tipe: -");
        JLabel scoreDetail = new JLabel("Skor saat ini: -");
        detailPanel.setBorder(BorderFactory.createTitledBorder("Detail Habit"));
        detailPanel.add(nameDetail);
        detailPanel.add(typeDetail);
        detailPanel.add(scoreDetail);

        JTextField inputField = new JTextField(15);
        Border defaultBorder = inputField.getBorder();

        habitJList.addListSelectionListener(e -> {
            int index = habitJList.getSelectedIndex();
            if (index >= 0) {
                Habit h = habitListModel.get(index);
                nameDetail.setText("Nama: " + h.getName());
                typeDetail.setText("Tipe: " + (h instanceof PositiveHabit ? "Positif" : "Negatif"));
                scoreDetail.setText("Skor saat ini: " + h.getScore());
            } else {
                nameDetail.setText("Nama: -");
                typeDetail.setText("Tipe: -");
                scoreDetail.setText("Skor saat ini: -");
            }
        });

        frame.add(detailPanel, BorderLayout.EAST);

        JRadioButton positiveButton = new JRadioButton("Positif", true);
        JRadioButton negativeButton = new JRadioButton("Negatif");
        ButtonGroup group = new ButtonGroup();
        group.add(positiveButton);
        group.add(negativeButton);

        JButton addButton = new JButton("Tambah Habit");
        JButton markButton = new JButton("Tandai Selesai");
        JButton resetButton = new JButton("Reset Skor");
        JButton deleteButton = new JButton("Hapus Habit");
        showScoreButton = new JButton("Tampilkan Hasil Skor");

        addButton.addActionListener(e -> {
            String nama = inputField.getText().trim();
            if (nama.isEmpty() || manajer.isHabitExists(nama)) {
                inputField.setBorder(BorderFactory.createLineBorder(Color.RED, 2));
                inputField.setToolTipText(nama.isEmpty() ? "Nama habit tidak boleh kosong" : "Nama habit sudah ada");
                JOptionPane.showMessageDialog(frame, "Nama habit kosong atau sudah ada.");
            } else {
                Habit habit = positiveButton.isSelected() ? new PositiveHabit(nama) : new NegativeHabit(nama);
                manajer.addHabit(habit);
                habitListModel.addElement(habit);
                inputField.setText("");
                inputField.setBorder(defaultBorder);
                inputField.setToolTipText(null);
                updateTotalScore();
                hideScore(); 
            }
        });

        markButton.addActionListener(e -> {
            int index = habitJList.getSelectedIndex();
            if (index >= 0) {
                manajer.markHabit(index);
                habitJList.repaint();
                updateTotalScore();
                hideScore();
            }
        });

        resetButton.addActionListener(e -> {
            int index = habitJList.getSelectedIndex();
            if (index >= 0) {
                manajer.resetScore(index);
                habitJList.repaint();
                updateTotalScore();
                hideScore();
            }
        });

        deleteButton.addActionListener(e -> {
            int index = habitJList.getSelectedIndex();
            if (index >= 0) {
                manajer.removeHabit(index);
                habitListModel.remove(index);
                updateTotalScore();
                hideScore();
            }
        });

        showScoreButton.addActionListener(e -> {
            scoreVisible = !scoreVisible;
            totalScoreLabel.setVisible(scoreVisible);
            showScoreButton.setText(scoreVisible ? "Sembunyikan Skor" : "Tampilkan Hasil Skor");

            if (scoreVisible) {
                updateTotalScore(); 
            }
        });

        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.Y_AXIS));
        JPanel topRow = new JPanel();
        topRow.add(new JLabel("Nama Habit:"));
        topRow.add(inputField);
        topRow.add(positiveButton);
        topRow.add(negativeButton);
        topRow.add(addButton);
        inputPanel.add(topRow);
        inputPanel.add(showScoreButton);
        inputPanel.add(totalScoreLabel);

        JPanel controlPanel = new JPanel();
        controlPanel.add(markButton);
        controlPanel.add(resetButton);
        controlPanel.add(deleteButton);

        frame.setLayout(new BorderLayout());
        frame.add(inputPanel, BorderLayout.NORTH);
        frame.add(new JScrollPane(habitJList), BorderLayout.CENTER);
        frame.add(controlPanel, BorderLayout.SOUTH);

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600, 400);
        frame.setVisible(true);
    }

    private void updateTotalScore() {
        int total = 0;
        for (Habit h : manajer.getHabits()) {
            total += h.getScore();
        }

        totalScoreLabel.setText("Total Skor: " + total);

        String feedbackMessage = "";
        if (total < 30) {
            feedbackMessage = "Pengguna diharapkan agar sering melakukan kegiatan positif, hindari melakukan kebiasaan negatif terlalu sering.";
        } else if (total <= 40) {
            feedbackMessage = "Pengguna sudah lumayan melakukan kegiatan positif namun masih ada kebiasaan negatifnya yang perlu dikurangi.";
        } else {
            feedbackMessage = "Pengguna sudah konsisten melakukan kebiasaan baik.";
        }

        if (scoreVisible) {
            JOptionPane.showMessageDialog(frame, feedbackMessage);
        }
    }

    private void hideScore() {
        scoreVisible = false;
        totalScoreLabel.setVisible(false);
        showScoreButton.setText("Tampilkan Hasil Skor");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(HabitHeroSwingGUI::new);
    }
}
