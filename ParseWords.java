import org.jsoup.Jsoup;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.*;
import java.util.*;
import java.net.URLEncoder;

public class ParseWords{
	
	public static PrintWriter pw;

	public static void main(String[] args) throws Exception{
		if(args.length == 0){
			System.out.println("Incorrect Number of Parameters");
			return;
		}
		ArrayList<String> out = parseFile(args[0]);
		pw = new PrintWriter(new File(args[0] + ".out"));

		String koreanRoot = "http://endic.naver.com/search.nhn?" +
		"sLn=kr&searchOption=all&query=";
		for(String word : out){
			String url = koreanRoot + URLEncoder.encode(word, "UTF-8");
			Document doc = Jsoup.connect(url).get();
			parseDoc(doc, word);
			// System.out.println(doc.outerHtml());
		}
		pw.close();
	}

	public static void parseDoc(Document doc, String word) throws Exception{
		String hanj = checkHanJa(doc, word);
		System.out.println(word);
		pw.println(word);
		System.out.println("\n*****DEFINITION*****");
		pw.println("\n*****DEFINITION*****");
		Elements content = doc.select(".align_right");
		if(content.size() == 0){
			System.out.println("WORD DOESN'T HAVE DEFINITION");
			System.out.println("-----------------------------------");
			pw.println("-----------------------------------");
			return;
		}
		Element block = content.get(0);
		System.out.println(block.text());
		pw.println(block.text());
		if(hanj.length() != 0){
			System.out.println("");
			if(hanj.contains("-"))
				hanj = hanj.substring(0, hanj.indexOf("-"));
			if(hanj.contains("―"))
				hanj = hanj.substring(0, hanj.indexOf("―"));
			if(hanj.contains("-"))
				hanj = hanj.substring(0, hanj.indexOf("-"));
			if(hanj.contains("·"))
				hanj = hanj.substring(0, hanj.indexOf("·"));
			if(hanj.contains("ㆍ"))
				hanj = hanj.substring(0, hanj.indexOf("ㆍ"));
			if(hanj.contains("("))
				hanj = hanj.substring(0, hanj.indexOf("("));
			findPINYIN(hanj);
			findHanJa(hanj);
		}
		System.out.println("");
		pw.println("");
		System.out.println("-----------------------------------");
		pw.println("-----------------------------------");
	}

	public static void findPINYIN(String hanj) throws Exception{
		String c = hanj;
		System.out.println(c);
		pw.println(c);
		String query = "https://www.mdbg.net/chindict/chindict.php?page=worddict&wdrst=0&wdqb=";
		String url = query + URLEncoder.encode(c, "UTF-8");
		Document doc = Jsoup.connect(url).get();
		Element result = doc.select(".wordresults").first();
		if(result == null)
			return;
		Elements row = result.select(".row");
		for(Element r: row){
			System.out.println(r.text());
			pw.println(r.text());
		}
	}

	public static void findHanJa(String hanj) throws Exception{
		for(int i = 0; i < hanj.length(); i++){
			String c = "" + hanj.charAt(i);
			System.out.println(c);
			pw.println(c);
			String query = "http://hanja.naver.com/search?query=";
			String url = query + URLEncoder.encode(c, "UTF-8");
			Document doc = Jsoup.connect(url).get();
			Elements dl = doc.select("dl");
			Element firstList = dl.get(3);
			Elements dd = firstList.select("dd");
			Element def = dd.get(0);
			System.out.println(def.text());
			pw.println(def.text());
			//Go through all related words
			Element related = dl.get(4);
			Elements words = related.select("dt");
			Elements definition = related.select("dd");
			for(int j = 0; j < words.size(); j++){
				System.out.println(words.get(j).text());
				System.out.println(definition.get(2*j).text());
				System.out.println(definition.get(2*j + 1).text());
				pw.println(words.get(j).text());
				pw.println(definition.get(2*j).text());
				pw.println(definition.get(2*j + 1).text());
			}
			System.out.println("");
			pw.println("");
		}

	}

	public static String checkHanJa(Document doc, String word){
		Elements content = doc.select(".first");
		Element block = content.get(0);
		Elements lines = block.select(".fnt_e30");
		if(lines.size() == 0)
			return "";
		Element line = lines.get(0);
		System.out.println(line.text());
		String text = line.text();
		String out = "";
		System.out.println("text: " + text);
		String[] words = text.split(" ");
		if(text.length() != word.length() && text.split(" ").length != 1 
			&& ! alpha(text.split(" ")[1])){
			if(alpha(text.split(" ")[0])){
				return "";
			}
			out = text.split(" ")[1];			
		}
		return out;
	}

	public static ArrayList<String> parseFile(String path) throws Exception{
		BufferedReader br = new BufferedReader(new InputStreamReader(
			new FileInputStream(path), "UTF-8"));
		String line = "";
		ArrayList<String> out = new ArrayList<String>();
		while((line = br.readLine()) != null)
			out.add(line);
		return out;
	}

	public static boolean alpha(String s){
		return s.matches("[a-zA-Z]+");
	}

}