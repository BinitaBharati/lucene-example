package bharati.binita.apache.lucene.query;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.Version;

/**
 * 
 * @author binita.bharati@gmail.com
 * Lucene query samples.
 * 
 * http://lucene.apache.org/core/4_10_0/queryparser/org/apache/lucene/queryparser/classic/package-summary.html#package_description
 * 
 * Lucene query can be done in 2 ways:
 * 1)Through Lucene query language (which uses JavaCC). And using a org.apache.lucene.queryparser.classic.QueryParser class.
 * QueryParser syntax :http://lucene.apache.org/core/4_10_0/queryparser/org/apache/lucene/queryparser/classic/package-summary.html#package_description
 * 2)Through Lucene APIs.org.apache.lucene.search.Query instances.
 *
 */

public class QuerySample {
	
	public static void main(String[] args) throws Exception {
	     
	    StandardAnalyzer analyzer = new StandardAnalyzer();

	    // Create the index
	    Directory index = new RAMDirectory();

	    //Build the index.
	    buildIndex(analyzer, index);
	    
	    //Try different combinations of search query string using Lucene's query language - start
	    //QueryParser syntax :http://lucene.apache.org/core/4_10_0/queryparser/org/apache/lucene/queryparser/classic/package-summary.html#package_description

	    /**
	     * Search employees whose skills has Java.
	     */
	    search(analyzer, "dumb", index, "Skills:Java", Arrays.asList(new String[]{"Name", "Skills"}) );
	    
	    /**
	     * Search employees whose skills include both Java and Lucene.
	     */
	    search(analyzer, "dumb", index, "Skills:(Java AND Lucene)", Arrays.asList(new String[]{"Name", "Skills"}));

	    /**
	     * Search employees whose skills include .NET or Lucene.
	     */
	    search(analyzer, "dumb", index, "Skills:.NET OR Skills:Lucene", Arrays.asList(new String[]{"Name", "Skills"}));
	    
	    /**
	     * Wild card search
	     * Search employees whose names start with S and end with N.
	     */
	    search(analyzer, "dumb", index, "Name:S*n", Arrays.asList(new String[]{"Name", "Age"}));
	    
	    /**
	     * Wild card search
	     * Search employees whose names starts with Ste.
	     */
	    search(analyzer, "dumb", index, "Name:Ste*", Arrays.asList(new String[]{"Name", "Age"}));
	    /**
	     * Wild card search
	     * Search employees whose names starts with Steven.
	     */
	    search(analyzer, "dumb", index, "Name:Steven*", Arrays.asList(new String[]{"Name", "Age"}));
	    /**
	     * Wild card search
	     * Search employees whose names are like B?nita.
	     */
	    search(analyzer, "dumb", index, "Name:B?nita", Arrays.asList(new String[]{"Name", "Age"}));
	    /**
	     * Search employees whose age is between 20 to 35.(20 and 35 inclusive)
	     */
	    search(analyzer, "dumb", index, "Age:[20 TO 35]", Arrays.asList(new String[]{"Name", "Age"}));
	    /**
	     * Search employees whose age is between 20 to 35.(20 and 35 exclusive)
	     */
	    search(analyzer, "dumb", index, "Age:{20 TO 35}", Arrays.asList(new String[]{"Name", "Age"}));
	    /**
	     * Search employees whose skills include either .NET or Java and Designation is Software Engineer - 1
	     */
	    search(analyzer, "dumb", index, "(Skills:.NET OR Skills:Java) AND Designation:\"Software Engineer - 1\"", Arrays.asList(new String[]{"Name", "Designation"}));
	    
	    //Try different combinations of search query string using Lucene's query language - end

	   
	  }
	
	/**
	 * 
	 * @param analyzer
	 * @param index
	 * @throws Exception
	 * Builds the index.
	 * 
	 * Lucene is able to achieve fast search responses because, instead of searching the text directly, it searches an index instead. 
	 * This would be the equivalent of retrieving pages in a book related to a keyword by searching the index at the back of a book, 
	 * as opposed to searching the words in each page of the book.
	 */
	
	private static void buildIndex(StandardAnalyzer analyzer, Directory index) throws Exception
	{
		IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_4_10_0, analyzer);

	    IndexWriter w = new IndexWriter(index, config);
	    addDoc(w, "238119","Sandy Peter", "20", "10000", "Software Engineer - 1", "Java, Lucene, XML Parsers");
	    addDoc(w, "1000245","Bonita Zuckberg", "44", "60000", "Architect", "Big Data, Hadoop, Storm, Trident, Kafka");
	    addDoc(w, "900876","Teena Stephan", "25", "10000", "Software Engineer - 1", "C++, Java, Scala, Clojure, Kafka, Tibco, Lucene");
	    addDoc(w, "6667565","Jackie Ling", "27", "15000", "Software Engineer - 2", "Scala, Clojure, Kafka, Storm, Java");
	    addDoc(w, "887123","Ashwini Lal", "27", "20000", "Software Engineer - 2", "Kafka, Storm, Pig, Ansible, JQuery, .NET, Lucene");
	    addDoc(w, "887123","Ashwini Sharma", "27", "10000", "Software Engineer - 1", ".NET, Java, C++");
	    addDoc(w, "7700009","Teena Stevens", "35", "20000", "Software Engineer - 2", "Selenium, Cucumber, Ruby, Kafka");
	    addDoc(w, "110988","Steven Gomes", "36", "40000", "Software Engineer - 2", "Kafka, Spark, Scala, Akka");
	    addDoc(w, "888990","Mathew Onjus", "22", "20000", "Software Engineer - 1", "Jquery, Javascript, Underscrore, Angular, Ember, Backbone");
	    addDoc(w, "2381178","Binita George", "40", "70000", "Networking Engineer - 2", "SNMP, QoS Policing, Perl, Shell scripting");
	    
	    w.close();
	    
	}
	
	private static void addDoc(IndexWriter w, String id, String name, String age, 
			String salary, String designation, String skills) throws IOException {
	    Document doc = new Document();
	    /**
	     * TextField doesn't mean that value part has to be String alone. The value part can be :
	     * java.io.Reader and org.apache.lucene.analysis.TokenStream
	     */
	    doc.add(new TextField("Name", name, Field.Store.YES));
	    doc.add(new TextField("Age", age, Field.Store.YES));
	    doc.add(new TextField("Salary", salary, Field.Store.YES));
	    doc.add(new TextField("Designation", designation, Field.Store.YES));
	    doc.add(new TextField("Skills", skills, Field.Store.YES));

	    w.addDocument(doc);
	  }
	
	/**
	 * 
	 * @param analyzer
	 * @param defaultField
	 * @param index
	 * @param queryStr
	 * @throws Exception
	 * Search using Lucene query language.
	 */
	  private static void search(StandardAnalyzer analyzer, String defaultField,
			  Directory index, String queryStr,
			  List<String> printFields) throws Exception
	  {
		  
		    //defaultField - the default field for query terms. Need not be used
		    Query q = new QueryParser(defaultField, analyzer).parse(queryStr);

		  
		    //search
		    int hitsPerPage = 10;
		    IndexReader reader = DirectoryReader.open(index);
		    IndexSearcher searcher = new IndexSearcher(reader);
		    TopScoreDocCollector collector = TopScoreDocCollector.create(hitsPerPage, true);
		    
		    System.out.println("Searching for " + queryStr);
		    searcher.search(q, collector);
		    ScoreDoc[] hits = collector.topDocs().scoreDocs;
		    
		    //display results
		    System.out.println("Found " + hits.length + " hits.");
		    for(int i=0;i<hits.length;++i) {
		      int docId = hits[i].doc;
		      Document d = searcher.doc(docId);
		      System.out.print((i + 1) + ". ");
		      String temp = "";
		      for(String eachPrintField : printFields)
		      {
		    	  	temp = temp + eachPrintField + " : "+d.get(eachPrintField)+",";
		      }
	    	  System.out.println(temp);


		    }

		    // reader can only be closed when there
		    // is no need to access the documents any more.
		    reader.close();
		  
	  }

	  

}
