package org.example;

import java.io.FileWriter;
import java.io.IOException;

public class CreateCSV {
    public static void main(String[] args) {
        String csvContent = "1,John,Smith,USA,25\n2,Ivan,Petrov,RU,23";

        try (FileWriter writer = new FileWriter("data.csv")) {
            writer.write(csvContent);
            System.out.println("CSV файл успешно создан!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}