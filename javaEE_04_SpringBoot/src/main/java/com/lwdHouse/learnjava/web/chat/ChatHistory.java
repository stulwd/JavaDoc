package com.lwdHouse.learnjava.web.chat;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

@Component
public class ChatHistory {
    final int maxMessages = 100;
    final List<ChatMessage> chatHistory = new ArrayList<>(100);
    final Lock readLock;
    final Lock writeLock;

    public ChatHistory() {
        ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
        this.readLock = lock.readLock();
        this.writeLock = lock.writeLock();
    }

    public List<ChatMessage> getHistory() {
        this.readLock.lock();
        try{
            return List.copyOf(chatHistory);
        }finally {
            this.readLock.unlock();
        }
    }

    public void addToHistory(ChatMessage message){
        this.writeLock.lock();
        try {
            this.chatHistory.add(message);
            if (this.chatHistory.size() > maxMessages){
                this.chatHistory.remove(0);
            }
        }finally {
            this.writeLock.unlock();
        }
    }
}
