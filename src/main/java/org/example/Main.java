package org.example;

import java.io.File;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Basket basket;
        File bu = new File("basket.bin");
        if (bu.exists() && !bu.isDirectory()) {
            basket = Basket.loadFromBinFile(bu);
            if (basket == null) {
                System.out.println("Ошибка загрузки корзины");
                String[] products = {"Молоко", "Яйца", "Сахар", "Соль", "Мука", "Ванилин"};
                int[] prices = {125, 85, 45, 25, 90, 5};
                basket = new Basket(products, prices);
            }
        } else {
            String[] products = {"Молоко", "Яйца", "Сахар", "Соль", "Мука", "Ванилин"};
            int[] prices = {125, 85, 45, 25, 90, 5};
            basket = new Basket(products, prices);
        }

        boolean isRunning = true;
        Scanner scanner = new Scanner(System.in);
        while (isRunning) {
            for (int i = 0; i < basket.getPrices().length; i++) {
                System.out.printf("[%d] %s - %d руб \n", i + 1, basket.getProducts()[i], basket.getPrices()[i]);
            }
            System.out.println("Введите номер продукта и его кол-во. \n Для завершения введите 'end'.");
            String buff = scanner.nextLine();
            String[] splBuff = buff.split("\\s+");
            if (splBuff.length > 1) {
                int prodNumber;
                int amountProd;
                try {
                    prodNumber = Integer.parseInt(splBuff[0]) - 1;
                    amountProd = Integer.parseInt(splBuff[1]);
                } catch (NumberFormatException e) {
                    System.out.println("Введен некоректный номер товара или его количество!\n");
                    continue;
                }
                if (prodNumber < 0 || prodNumber >= basket.getProducts().length) {
                    System.out.println("Введен некоректный номер товара!\n");
                    continue;
                }
                if (amountProd < 1) {
                    System.out.println("Введенно неверное количество товара!\n");
                    continue;
                }
                if (basket.addToCart(prodNumber, amountProd)) {
                    if (!basket.saveBin(new File("basket.bin"))) {
                        System.out.println("Ошибка сохранения");
                    }
                } else {
                    System.out.println("Ошибка добавления");
                }

            } else {
                if (splBuff[0].equals("end")) {
                    isRunning = false;
                }
            }
        }
        basket.printCart();
    }
}