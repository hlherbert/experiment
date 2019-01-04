package com.hl.nio;

public class AutoClose {
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

    static class AutoClosableBook extends ReferenceTest.Book implements AutoCloseable {
        ReferenceTest.Book book;

        AutoClosableBook(ReferenceTest.Book book) {
            this.book = book;
        }

        @Override
        public String getName() {
            return this.book.getName();
        }

        @Override
        public void closeBook() {
            this.book.closeBook();
        }

        @Override
        public void close() {
            this.closeBook();
        }
    }

    public static void main(String[] args) {
        // auto close object in try() block
        try (
                AutoClosableBook myBook = new AutoClosableBook(new ReferenceTest.Book("autoCloseBook1"));
                AutoClosableBook myBook2 = new AutoClosableBook(new ReferenceTest.Book("autoCloseBook2"))
        ) {
            System.out.println("read book:" + myBook.getName());
            System.out.println("read book:" + myBook2.getName());
        }
    }
}
