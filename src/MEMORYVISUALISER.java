import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

class MemoryBlock {
    int start;
    int size;
    boolean allocated;

    MemoryBlock(int start, int size) {
        this.start = start;
        this.size = size;
        this.allocated = false;
    }
}

public class MEMORYVISUALISER {
    private static final int MEMORY_SIZE = 1000;
    private static MemoryBlock[] memory = {new MemoryBlock(0, MEMORY_SIZE)};
    private static JFrame frame;
    private static JPanel memoryPanel;
    private static JTextArea logArea;

    public static void main(String[] args) {
        frame = new JFrame("Memory Management Visualizer");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        memoryPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                for (MemoryBlock block : memory) {
                    if (block.allocated) {
                        g.setColor(Color.RED);
                    } else {
                        g.setColor(Color.GREEN);
                    }
                    g.fillRect(block.start, 50, block.size, 50);
                    g.setColor(Color.BLACK);
                    g.drawRect(block.start, 50, block.size, 50);
                }
            }
        };

        frame.add(memoryPanel, BorderLayout.CENTER);

        JPanel controlPanel = new JPanel();
        JButton allocateButton = new JButton("Allocate Memory");
        JButton deallocateButton = new JButton("Deallocate Memory");

        allocateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                allocateMemory(100); // Allocate 100 units of memory
                memoryPanel.repaint();
                logArea.append("Allocated 100 units of memory\n");
            }
        });

        deallocateButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deallocateMemory();
                memoryPanel.repaint();
                logArea.append("Deallocated memory\n");
            }
        });

        controlPanel.add(allocateButton);
        controlPanel.add(deallocateButton);
        frame.add(controlPanel, BorderLayout.NORTH);

        logArea = new JTextArea(10, 30);
        logArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(logArea);
        frame.add(scrollPane, BorderLayout.SOUTH);

        frame.setSize(800, 200);
        frame.setVisible(true);
    }

    private static void allocateMemory(int size) {
        for (MemoryBlock block : memory) {
            if (!block.allocated && block.size >= size) {
                block.allocated = true;
                if (block.size > size) {
                    MemoryBlock newBlock = new MemoryBlock(block.start + size, block.size - size);
                    int index = findBlockIndex(block);
                    memory[index] = new MemoryBlock(block.start, size);
                    insertBlockAtIndex(index + 1, newBlock);
                }
                return;
            }
        }
        logArea.append("Not enough memory to allocate " + size + " units\n");
    }

    private static void deallocateMemory() {
        for (MemoryBlock block : memory) {
            if (block.allocated) {
                block.allocated = false;
                return;
            }
        }
        logArea.append("No allocated memory to deallocate\n");
    }

    private static int findBlockIndex(MemoryBlock block) {
        for (int i = 0; i < memory.length; i++) {
            if (memory[i] == block) {
                return i;
            }
        }
        return -1;
    }

    private static void insertBlockAtIndex(int index, MemoryBlock block) {
        MemoryBlock[] newMemory = new MemoryBlock[memory.length + 1];
        System.arraycopy(memory, 0, newMemory, 0, index);
        newMemory[index] = block;
        System.arraycopy(memory, index, newMemory, index + 1, memory.length - index);
        memory = newMemory;
    }
}