import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class ThreadPool {
  private int size = 4;
  private boolean isLive = true;
  private Queue<Runnable> tasks = new LinkedList<Runnable>();
  private ArrayList<Thread> pool = new ArrayList<Thread>();
  private final ReentrantLock lock = new ReentrantLock();
  Condition notEmpty = lock.newCondition();

  public ThreadPool() {
    this.initiateThreadPool();
  }

  public ThreadPool(int size) {
    this.size = size;
    this.initiateThreadPool();
  }

  public void shutdown() {
    lock.lock();
    try {
      isLive = false;
      notEmpty.signalAll();
    } finally {
      lock.unlock();
    }
    pool.stream().forEach((w) -> w.interrupt());
    pool.stream().forEach(w -> {
      try {
        w.join(5_000);
      } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
        return;
      }
    });

  }

  private void initiateThreadPool() {
    for (int i = 0; i < size; i++) {
      String name = "thread-" + i;
      Thread worker = new Thread(() -> {
        while (true) {
          Runnable task;
          lock.lock();
          try {
            while (tasks.isEmpty()) {
              if (!isLive)
                return;
              notEmpty.await();
            }
            task = tasks.poll();
          } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return;
          } finally {
            lock.unlock();
          }
          log("EXECUTED by: " + name);
          task.run();
        }
      }, name);
      pool.add(worker);
      worker.start();
    }
    log("Created ThreadPool with " + size + " workers");
  }

  private void log(final String str) {
    System.out.println(str);
  }

  public void execute(Runnable task) {
    lock.lock();
    try {
      if (isLive) {
        tasks.add(task);
      }
    } finally {
      notEmpty.signal();
      lock.unlock();
    }
  }

}
