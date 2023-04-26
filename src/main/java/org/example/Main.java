package org.example;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        try {
            Config cfg = new Config(new File("shop.xml"));

            ObjectMapper mapper = new ObjectMapper();
            Basket basket = null;
            ClientLog lg = new ClientLog();
            if (cfg.loadEnable) {
                if (cfg.loadFormat == FileFormat.JSON) {

                    File bu = new File(cfg.loadFile);
                    if (bu.exists() && !bu.isDirectory()) {
                        try {
                            basket = mapper.readValue(bu, new TypeReference<>() {
                            });
                        } catch (java.io.IOException e) {
                            System.out.println("Ошибка загрузки корзины");
                            e.printStackTrace();
                            String[] products = {"Молоко", "Яйца", "Сахар", "Соль", "Мука", "Ванилин"};
                            int[] prices = {125, 85, 45, 25, 90, 5};
                            basket = new Basket(products, prices);
                        }
                    } else {
                        String[] products = {"Молоко", "Яйца", "Сахар", "Соль", "Мука", "Ванилин"};
                        int[] prices = {125, 85, 45, 25, 90, 5};
                        basket = new Basket(products, prices);
                    }
                } else {
                    basket = Basket.loadFromTxtFile(new File(cfg.loadFile));
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
                        lg.log(prodNumber, amountProd);
                        try {
                            if (cfg.saveEnable) if (cfg.saveFormat == FileFormat.JSON)
                                mapper.writeValue(new File(cfg.saveFile), basket);
                            else basket.saveTxt(new File(cfg.saveFile));
                        } catch (IOException e) {
                            System.out.println("Ошибка сохранения");
                            e.printStackTrace();
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
            if (cfg.logEnable) lg.exportAsCSV(new File(cfg.logFile));
            basket.printCart();
        } catch (IOException | ParserConfigurationException | SAXException e) {
            System.out.println("Не удалось загрузить файл конфигурации");
        }
    }
}