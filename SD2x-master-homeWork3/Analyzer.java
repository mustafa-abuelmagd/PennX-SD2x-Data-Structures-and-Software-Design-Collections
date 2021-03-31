import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.stream.Collectors;
import java.util.stream.Stream;


/*
 * SD2x Homework #3
 * Implement the methods below according to the specification in the assignment description.
 * Please be sure not to change the method signatures!
 */
public class Analyzer {


	public static boolean isWellFormatted(String line) {
		if (line.equals(null))return false;
		String[] words = line.split(line); 

		if (words[0].matches("-?\\d+(\\.\\d+)?")) { 
			int score =Integer.parseInt(words[0]);  
			if ( score  >= -2 && score <= 2){
				return true;
			}
		}
		return false;
	}

	

	public static boolean isInteger(String s) {
	    try { 
	        Integer.parseInt(s); 
	    } catch(NumberFormatException e) { 
	        return false; 
	    } catch(NullPointerException e) {
	        return false;
	    }
	    return true;
	}

	public static Sentence makeASentence(String line) {


		if (line == null || line.length() == 0)return null;
		String[] words = line.split(" ", line.length()); 

		if (isInteger(words[0])) { 			
			int score = 0; 
			String text = "";
			Scanner scanner = new Scanner(words[0]); 
			if (scanner.hasNextInt()) {
				score=  scanner.nextInt();}
			if ( score  >= -2 && score <= 2){

				for (int i = 1 ; i < words.length ; i++) {
					text += words[i]+ " " ;
				}
				text = text.trim();
				if (text == "" )return null;
				return new Sentence(score , text) ;
			}
			else {
				System.out.println("score is:" +score);
			}
		}
		return null ;
	}




	public static boolean IsValidWord(String s) {
		for (char c : s.toCharArray()) 
			if (!Character.isLetterOrDigit(c)) return false;
		return true;
	}


	public static String validate(String word) {
		if (!IsValidWord(word)) 
			return null; 


		return word.toLowerCase();
	}


	public static List<Sentence> readFile(String filename) throws FileNotFoundException {
		List<Sentence> Sentences = new ArrayList<Sentence>();
		List<String> lines = new ArrayList<>();
		List<String> added = new ArrayList<>();

		//System.out.println("lines.size(): "+lines.size() +"       ");




		try {
			lines = Files.lines(Paths.get(filename)).collect(Collectors.toList());
		} catch (IOException e) {

			return new ArrayList<Sentence>(); 
		}
		catch (NullPointerException e ) {
			return new ArrayList<Sentence>(); 

		}


		try {		

			File file = new File(filename);
			String text = "";
			int score = 0;
			Scanner scnr = new Scanner(System.in);
			scnr = new Scanner(file);

			for (String line : lines){
				if (!added.contains(line)) {
					if (!(makeASentence(line)== null)) {

						added.add(line);
						Sentences.add(makeASentence(line));

					}
				}
				else continue; 
			}
		}
		catch (IndexOutOfBoundsException e) {

			return Sentences;

		}
		catch (NullPointerException e ) {
			return new ArrayList<Sentence>(); 
		}
		return Sentences; 
	}

	/*
	 * Implement this method in Part 2
	 */
	public static Set<Word> allWords(List<Sentence> sentences) {

		List<Word> WORDS = new ArrayList<Word>();

		try {

			if(sentences.get(0) != null && !sentences.isEmpty() && sentences.size() !=0) {
				for (Sentence sentence: sentences) {
					if (sentence != null  ) {
						String[] tokens = sentence.getText().toLowerCase().split(" ");
						for (String token : tokens) {

							if (IsValidWord(token)){

								String newWord = validate(token);

								Word WordObject = new Word(newWord);
								WordObject.increaseTotal(sentence.getScore());

								if (!WORDS.contains(WordObject)) {

									WORDS.add(WordObject);
								}		

								else {
									WORDS.get(WORDS.indexOf(WordObject)).increaseTotal(WordObject.getTotal());
								}
							}
						}
					}
				}
			}
		}
		catch (IndexOutOfBoundsException e) {
			WORDS.clear();
			return new HashSet<Word>(WORDS);
		}

		catch (NullPointerException e ) {
			WORDS.clear();
			return new HashSet<Word>(WORDS);
		}

		return new HashSet<Word>(WORDS);
	}

	/*
	 * Implement this method in Part 3
	 */
	public static Map<String, Double> calculateScores(Set<Word> words) {

		Map<String, Double> map = new HashMap<String, Double>();
		try {
			if (words != null || !words.isEmpty()) {
				for (Word word : words ) {
					if (word != null) {
						if (   IsValidWord(word.getText()  )) {
							double score = word.calculateScore();
							map.put(word.getText(), score);
						}
					}
				}
			}


		}
		catch (IndexOutOfBoundsException e) {
			map.clear();
			return map;

		}
		catch (NullPointerException e ) {
			map.clear();
			return map;
		}
		return map; 

	}

	/*
	 * Implement this method in Part 4
	 */
	public static double calculateSentenceScore(Map<String, Double> wordScores, String sentence) {
		double score= 0.0;
		double total= 0.0; 
		int size =0; 
		double SCORE= 0.0;
		try {
			String[] tokens = sentence.toLowerCase().split(" ");
			for (String word : tokens) {
				if (word != null ) {
					if (IsValidWord(word) ) {

						if (wordScores.containsKey(word)) {
							score = wordScores.get(word);
							total+= score; 
						}
						else {
							score =0.0;
							total+= score; 
							wordScores.put(word, score);
						}
						size++;


					}
				}

			}

			if (size ==0 ) return 0;

			SCORE = total / ((double) size) ; 

		}
		catch (IndexOutOfBoundsException e) {

			return 0;

		}
		catch (NullPointerException e ) {
			return 0;

		}
		return SCORE; 
	}

	/*
	 * This method is here to help you run your program. Y
	 * You may modify it as needed.
	 */
	public static void main(String[] args) throws FileNotFoundException {
		if (args.length == 0) {
			System.out.println("Please specify the name of the input file");
			System.exit(0);
		}
		String filename = args[0];
		System.out.print("Please enter a sentence: ");
		Scanner in = new Scanner(System.in);
		String sentence = in.nextLine();
		in.close();
		List<Sentence> sentences = Analyzer.readFile(filename);
		Set<Word> words = Analyzer.allWords(sentences);
		Map<String, Double> wordScores = Analyzer.calculateScores(words);
		double score = Analyzer.calculateSentenceScore(wordScores, sentence);
		System.out.println("The sentiment score is " + score);
	}
}
