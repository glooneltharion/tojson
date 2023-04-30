import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.LinkedHashMap;
import java.util.Map;

public class Main {
    public static void main(String[] args) throws IOException {
        File inputFile = new File("src/input.html");
        Map<String, String> result = convert(inputFile);

        String outputFilePath = "sk.json";
        writeToJsonFile(result, outputFilePath);

    }


    public static Map<String, String> convert(File input) throws IOException {
        String html = new String(Files.readAllBytes(input.toPath()));
        Document doc = Jsoup.parse(html);

        Map<String, String> result = new LinkedHashMap<>();
        int counter = 1;

        for (Node node : doc.childNodes()) {
            processNode(node, result, counter);
        }

        return result;
    }

    private static int processNode(Node node, Map<String, String> result, int counter) {
        if (node instanceof TextNode) {
            String text = ((TextNode) node).getWholeText().trim();
            if (!text.isEmpty()) {
                result.put("text" + counter, text);
                counter++;
            }
        } else if (node instanceof Element) {
            for (Node child : ((Element) node).childNodes()) {
                counter = processNode(child, result, counter);
            }
        }
        return counter;
    }

    public static void writeToJsonFile(Map<String, String> map, String outputFilePath) throws IOException {
        StringBuilder builder = new StringBuilder();
        builder.append("{\n");
        for (Map.Entry<String, String> entry : map.entrySet()) {
            builder.append("  \"" + entry.getKey() + "\": \"" + entry.getValue() + "\",\n");
        }
        builder.deleteCharAt(builder.length() - 2);
        builder.append("}");

        FileWriter writer = new FileWriter(outputFilePath);
        writer.write(builder.toString());
        writer.close();
    }


}