package org.example;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import org.w3c.dom.*;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.*;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        String[] columnMapping = {"id", "firstName", "lastName", "country", "age"};
        String csvFileName = "data.csv";
        String xmlFileName = "data.xml";
        String jsonFileName = "data.json";
        String outputJsonFileName = "data2.json";

        try {
            // Parse CSV
            List<Employee> csvEmployees = parseCSV(columnMapping, csvFileName);
            String csvToJson = listToJson(csvEmployees);
            writeString(csvToJson, jsonFileName);
            System.out.println("File '" + jsonFileName + "' created successfully!");

            // Parse XML
            List<Employee> xmlEmployees = parseXML(xmlFileName);
            String xmlToJson = listToJson(xmlEmployees);
            writeString(xmlToJson, outputJsonFileName);
            System.out.println("File '" + outputJsonFileName + "' created successfully!");

            // Read JSON and convert back to List<Employee>
            String json = readString(outputJsonFileName);
            List<Employee> employees = jsonToList(json);
            employees.forEach(System.out::println);
        } catch (Exception e) {
            System.err.println("An error occurred: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static List<Employee> parseCSV(String[] columnMapping, String fileName) {
        try (FileReader reader = new FileReader(fileName)) {
            ColumnPositionMappingStrategy<Employee> strategy = new ColumnPositionMappingStrategy<>();
            strategy.setType(Employee.class);
            strategy.setColumnMapping(columnMapping);

            CsvToBean<Employee> csvToBean = new CsvToBeanBuilder<Employee>(reader)
                    .withMappingStrategy(strategy)
                    .withSkipLines(0)
                    .withIgnoreLeadingWhiteSpace(true)
                    .build();

            return csvToBean.parse();
        } catch (IOException e) {
            throw new RuntimeException("Error reading CSV file: " + fileName, e);
        }
    }

    public static List<Employee> parseXML(String fileName) {
        List<Employee> employees = new ArrayList<>();
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document document = builder.parse(new File(fileName));
            document.getDocumentElement().normalize();

            NodeList nodeList = document.getElementsByTagName("employee");

            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);

                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;

                    long id = Long.parseLong(getTagValue("id", element));
                    String firstName = getTagValue("firstName", element);
                    String lastName = getTagValue("lastName", element);
                    String country = getTagValue("country", element);
                    int age = Integer.parseInt(getTagValue("age", element));

                    employees.add(new Employee(id, firstName, lastName, country, age));
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Error parsing XML file: " + fileName, e);
        }
        return employees;
    }

    private static String getTagValue(String tag, Element element) {
        NodeList nodeList = element.getElementsByTagName(tag);
        if (nodeList.getLength() == 0) {
            throw new RuntimeException("Missing tag: " + tag);
        }
        return nodeList.item(0).getTextContent();
    }

    public static String listToJson(List<Employee> list) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        Type listType = new TypeToken<List<Employee>>() {}.getType();
        return gson.toJson(list, listType);
    }

    public static void writeString(String json, String fileName) {
        try (FileWriter writer = new FileWriter(fileName)) {
            writer.write(json);
        } catch (IOException e) {
            throw new RuntimeException("Error writing to JSON file: " + fileName, e);
        }
    }

    public static String readString(String fileName) {
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            StringBuilder json = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                json.append(line);
            }
            return json.toString();
        } catch (IOException e) {
            throw new RuntimeException("Error reading JSON file: " + fileName, e);
        }
    }

    public static List<Employee> jsonToList(String json) {
        Gson gson = new GsonBuilder().create();
        Type listType = new TypeToken<List<Employee>>() {}.getType();
        return gson.fromJson(json, listType);
    }
}
