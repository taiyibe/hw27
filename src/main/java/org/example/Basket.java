package org.example;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.io.*;

public class Basket implements Serializable {
    @JsonProperty("crs")
    private final int[] card;
    @JsonProperty("prics")
    private final int[] prices;
    @JsonProperty("prods")
    private final String[] products;

    public Basket(String[] prods, int[] prc) {
        products = prods;
        prices = prc;
        card = new int[prc.length];
    }

    @JsonCreator
    private Basket(@JsonProperty("prods") String[] prods, @JsonProperty("prics") int[] prics, @JsonProperty("crs") int[] crs) {
        products = prods;
        prices = prics;
        card = crs;
    }

    public boolean addToCart(int productNum, int amount) {
        if (productNum >= 0 && productNum < prices.length) {
            card[productNum] += amount;
            return true;
        } else {
            return false;
        }
    }

    public void printCart() {
        int cardSumm = 0;
        for (int j : card) {
            cardSumm += j;
        }
        if (cardSumm > 0) {
            int cardFinSumm = 0;
            System.out.println("Ваша корзина: ");
            for (int i = 0; i < card.length; i++) {
                if (card[i] > 0) {
                    cardFinSumm += card[i] * prices[i];
                    System.out.printf("%s %d шт. %d руб/шт. %d рублей в сумме \n", products[i], card[i], prices[i], card[i] * prices[i]);
                }
            }
            System.out.printf("Итог: %d руб. \n", cardFinSumm);
        } else {
            System.out.println("Ваша корзина пуста.");
        }
    }

    public boolean saveTxt(File textFile) {
        try (PrintWriter out = new PrintWriter(textFile)) {
            for (String prd : products) {
                out.print(prd + ' ');
            }
            out.print('\n');
            for (int prc : prices) {
                out.print(Integer.toString(prc) + ' ');
            }
            out.print('\n');
            for (int cr : card) {
                out.print(Integer.toString(cr) + ' ');
            }
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean saveBin(File binFile) {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(binFile))) {
            out.writeObject(this);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static Basket loadFromTxtFile(File textFile) {
        try (BufferedReader fileReader = new BufferedReader(new FileReader(textFile))) {
            String[] prd = fileReader.readLine().split(" ");
            String[] prc_buf = fileReader.readLine().split(" ");
            String[] cr_buf = fileReader.readLine().split(" ");

            int[] prc = new int[prc_buf.length];
            for (int i = 0; i < prc_buf.length; i++) {
                prc[i] = Integer.parseInt(prc_buf[i]);
            }

            int[] cr = new int[cr_buf.length];
            for (int i = 0; i < cr_buf.length; i++) {
                cr[i] = Integer.parseInt(cr_buf[i]);
            }

            Basket rs = new Basket(prd, prc);

            for (int i = 0; i < cr.length; i++) {
                if (cr[i] > 0) {
                    rs.addToCart(i, cr[i]);
                }
            }

            return rs;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Basket loadFromBinFile(File binFile) {
        try (ObjectInputStream inp = new ObjectInputStream(new FileInputStream(binFile))) {
            return (Basket) inp.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public int[] getPrices() {
        return prices;
    }

    public String[] getProducts() {
        return products;
    }
}
