import org.jdom.Document;
import org.jdom.Element;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Scanner;
import java.util.stream.IntStream;


public class AutoXML {

    private File inputFile;
    private File outPath;
    private XMLOutputter XMLOut;

    public AutoXML() {
        XMLOut = new XMLOutputter(FormatXML());
    }

    public AutoXML(File inputFile, File outPath) {
        this();
        this.inputFile = inputFile;
        this.outPath = outPath;
    }

    public AutoXML(String inputFile, String outPath) {
        this(new File(inputFile), new File(outPath));
    }

    private void BuildXML(String xmlInfo, String outPath) throws IOException {
        //创建根节点...
        Element root = new Element("annotation");
        Document doc = new Document(root);

        String[] ss = xmlInfo.split(" ");

        String inputFileName = ss[0];

        init(root, inputFileName);

        IntStream.range(1, ss.length).mapToObj(i -> add2Date(ss[i])).forEach(root::addContent);

        String out = inputFileName.substring(0, inputFileName.lastIndexOf('.') + 1) + "xml";

        XMLOut.output(doc, new FileOutputStream(outPath + "\\" + out));

    }

    private void init(Element root, String fileName) {
        root.addContent(new Element("folder").setText("p1"));
        root.addContent(new Element("filename").setText(fileName));
        String path = "/Data/1_Work/4_deep/p1/%s";
        root.addContent(new Element("path").setText(String.format(path, fileName)));
        Element source = new Element("source");
        source.addContent(new Element("database").setText("Unknown"));
        root.addContent(source);
        Element size = new Element("size");
        size.addContent(new Element("width").setText("224"));
        size.addContent(new Element("height").setText("224"));
        size.addContent(new Element("depth").setText("1"));
        root.addContent(size);
        root.addContent(new Element("segmented").setText("0"));
    }

    private Format FormatXML() {
        //格式化生成的xml文件
        Format format = Format.getCompactFormat();
        format.setEncoding("utf-8");
        format.setIndent("  ");
        return format;
    }

    private Element add2Date(String s) {
        String[] ss = s.split(",");
        Element object = new Element("object");
        object.addContent(new Element("name").setText("exception"));
        object.addContent(new Element("pose").setText("Unspecified"));
        object.addContent(new Element("truncated").setText("0"));
        object.addContent(new Element("difficult").setText("0"));

        Element bndbox = new Element("bndbox");

        bndbox.addContent(new Element("xmin").setText(ss[0]));
        bndbox.addContent(new Element("ymin").setText(ss[1]));
        bndbox.addContent(new Element("xmax").setText(ss[2]));
        bndbox.addContent(new Element("ymax").setText(ss[3]));

        object.addContent(bndbox);
        return object;
    }

    public void outPut() throws IOException {
        outPut(inputFile.getPath(), outPath.getPath());
    }

    public void outPut(File inputFile, File outPath) throws IOException {

        if (inputFile == null || outPath == null) {
            throw new FileNotFoundException();
        }

        Scanner scanner = new Scanner(inputFile);

        while (scanner.hasNextLine()) {
            BuildXML(scanner.nextLine(), outPath.getPath());
        }

        System.out.println("输出完毕");
    }

    public void outPut(String inputFile, String outPath) throws IOException {
        outPut(new File(inputFile), new File(outPath));
    }
}