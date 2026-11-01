package service;

public class TransactionBuffer {

    private final Runnable[] buffer;
    private int in = 0;
    private int out = 0;
    private int count = 0;

    public static final Runnable STOP = () -> {};

    public TransactionBuffer(int capacity) {
        buffer = new Runnable[capacity];
    }

    // Producer adds a transaction
    public synchronized void put(Runnable transaction)
            throws InterruptedException {

        while (count == buffer.length) {
            wait();
        }

        buffer[in] = transaction;
        in = (in + 1) % buffer.length;
        count++;

        System.out.println("Producer added transaction. Buffer size: " + count);

        notifyAll();
    }

    // Consumer removes a transaction
    public synchronized Runnable take()
            throws InterruptedException {

        while (count == 0) {
            wait();
        }

        Runnable transaction = buffer[out];
        buffer[out] = null;
        out = (out + 1) % buffer.length;
        count--;

        System.out.println("Consumer removed transaction. Buffer size: " + count);

        notifyAll();

        return transaction;
    }
}
