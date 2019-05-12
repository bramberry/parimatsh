import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.Iterator;
import java.util.List;

public class PMparser {
    private final static String BASE_URL = "https://pm.by";

    public static void main(String[] args) throws IOException {
        Document page = Jsoup.connect("https://pm.by/live.html").get();
        Element element = page.body().getElementById("footballSItem");
        List<Node> nodes = element.childNodes();
        nodes.forEach(node -> {
            List<Node> items = node.childNodes();
            items.forEach(item->{
                if(item.childNodeSize() < 6){
                    return;
                }
                String url = item.childNode(6).childNode(0).childNode(1).childNode(0).attr("href");
                try {
                    getValue(BASE_URL + url);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        });

    }

    private static Double getValue(String url) throws IOException {
        Document page = Jsoup.connect(url).get();
        Elements elements = page.body().getElementsByClass("dyn");

        Node element = findElement(elements);
        Double value = 0d;
        if (element != null) {
            Node node = element.childNode(0);
            value = Double.parseDouble(node.toString());
        }
        if (value >= 2.6d){
            System.out.println(url + " coef = " + value);
        }
        return value;
    }

    private static Node findElement(Elements elements) {
        Iterator<Element> elementIterator = elements.iterator();

        while (elementIterator.hasNext()){
            Element element = elementIterator.next();
            if(element.getElementsByClass("p2r").first().childNode(0).toString().equals("Гол после 80-й минуты:")){
                return element.childNode(3).childNode(1).childNode(0);
            }

        }

        return null;

    }
}
