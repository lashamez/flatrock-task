package com.flatrock.recommendation.data;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class TestDataGenerator {

    private static final int NUM_USERS = 1000;
    private static final int NUM_PRODUCTS = 100;
    private static final int MAX_PRODUCT_VIEWS = 10;
    private static final int MAX_PURCHASES = 5;

    private TestDataGenerator() {

    }

    public static void main(String[] args) {
        try {
            FileWriter writer = new FileWriter("test.csv");
            writer.write("user_id,product_id,view_count,purchase_count\n");

            Random rand = new Random();
            for (int i = 1; i <= NUM_USERS; i++) {
                for (int j = 1; j <= NUM_PRODUCTS; j++) {
                    int viewCount = rand.nextInt(MAX_PRODUCT_VIEWS + 1);
                    int purchaseCount = rand.nextInt(MAX_PURCHASES + 1);
                    writer.write(i + "," + j + "," + viewCount + "," + purchaseCount + "\n");
                }
            }

            writer.close();
            System.out.println("Test data generated successfully!");

        } catch (IOException e) {
            System.out.println("An error occurred while generating test data.");
            e.printStackTrace();
        }
    }
}
