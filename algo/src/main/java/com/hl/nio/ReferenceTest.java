package com.hl.nio;

import java.lang.ref.PhantomReference;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;

public class ReferenceTest {

    public static void main(String[] args) {
        SoftReference<Book> softBook = new SoftReference<>(new Book("softBook"));  // will dispose obj before heap overflow
        WeakReference<Book> weakBook = new WeakReference<>(new Book("weakBook")); // will dispose obj after GC
        PhantomReference<Book> phantomBook = new PhantomReference<>(new Book("phantomBook"), null); // will always return null


        System.out.println("bSoft=" + softBook.get());
        System.out.println("bWeak=" + weakBook.get());
        System.out.println("bPhantom=" + phantomBook.get());

        System.out.println("---------- GC ------------");
        System.gc();

        System.out.println("bSoft=" + softBook.get());
        System.out.println("bWeak=" + weakBook.get());
        System.out.println("bPhantom=" + phantomBook.get());

    }

    static class Book {
        String name;

        public Book() {
        }

        public Book(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public void closeBook() {
            System.out.println("close book " + name);
        }
    }
}
