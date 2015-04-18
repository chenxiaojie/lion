package com.chenxiaojie.lion.test.thread;

import com.google.common.collect.Lists;

import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by xiaojie.chen on 2015-03-27 10:23:56.
 */
public class LockTest {
    private ExecutorService executorService = Executors.newFixedThreadPool(20);

    private Lock lock = new ReentrantLock();

    private Condition condition = lock.newCondition();

    private Producer producer = new Producer();

    private LinkedList<Integer> productList = Lists.newLinkedList();

    public static void main(String[] args) {
        final LockTest lockTest = new LockTest();
        lockTest.executorService.execute(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    lockTest.createProduct();
                    try {
                        Thread.sleep(1);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        for (int i = 1; i <= 10; i++) {
            final Consumer consumer = new Consumer(i + "号消费者");
            lockTest.executorService.execute(new Runnable() {
                @Override
                public void run() {
                    while (true) {
                        lockTest.consumeProduct(consumer);
                    }
                }
            });
        }
    }

    public void createProduct() {
        productList.add(producer.createProduct());
        lock.lock();
        condition.signal();
        lock.unlock();
    }

    public void consumeProduct(Consumer consumer) {
        lock.lock();
        try {
            //一定要用while
            while (productList.isEmpty()) {
                condition.await();
            }
            consumer.consumeProduct(productList.removeFirst());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            lock.unlock();
        }
    }


//    public void createProduct() {
//        productList.add(producer.createProduct());
//    }
//
//    public void consumeProduct(Consumer consumer) {
//        if (!productList.isEmpty()) {
//            consumer.consumeProduct(productList.removeFirst());
//        }
//    }


//    public void createProduct() {
//        productList.add(producer.createProduct());
//    }
//
//    public synchronized void consumeProduct(Consumer consumer) {
//        if (!productList.isEmpty()) {
//            consumer.consumeProduct(productList.removeFirst());
//        }
//    }


//    public void createProduct() {
//        productList.add(producer.createProduct());
//    }
//
//    public void consumeProduct(Consumer consumer) {
//        synchronized (productList) {
//            if (!productList.isEmpty()) {
//                consumer.consumeProduct(productList.removeFirst());
//            }
//        }
//    }

//    public void createProduct() {
//        productList.add(producer.createProduct());
//        synchronized (productList) {
//            productList.notify();
//        }
//    }
//
//    public synchronized void consumeProduct(Consumer consumer) {
//        synchronized (productList) {
//            while (productList.isEmpty()) {
//                try {
//                    productList.wait();
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//            consumer.consumeProduct(productList.removeFirst());
//        }
//    }

}

class Producer {
    private static int product = 1;

    public int createProduct() {
//        System.out.println("count:" + product);
        return product++;
    }
}

class Consumer {
    private String name;

    public Consumer(String name) {
        this.name = name;
    }

    public void consumeProduct(int product) {
        System.out.println(name + "消费了:" + product);
    }
}