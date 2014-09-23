package bharati.binita.apache.lucene.doc.field.demo;

import java.io.IOException;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
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
 * 
 * Demonstration of Field.Store.NO. 
 * This means that the value associated to a Field wont be stored in index.
 * Thus when you search this field, its value part will be obtained as null,
 * as the value part hasn't been stored while adding the doc to index.
 * Note: Search can only happen for what you store in the Document's Field text.
 * Search doesn't happen in the Document's Field value.
 *
 */

public class Case2 {
	
	public static void main(String[] args) throws Exception {
		  
	    /** http://stackoverflow.com/questions/650643/lucene-indexing-store-and-indexing-modes-explained
	     *  Specify the analyzer for tokenizing text. The same analyzer should be used for indexing and searching.
	     *   * What's a Analyzer ?
	         * Input text needs to be analyzed before it can be indexed and searched
	         * Note: Same analyzer instance should be used for indexing and searching!.
	         * One e.g of analysis would be to eliminate stop words like "the", "a" etc.
	         * Such words do not add any value during search, and hence should not be indexed.
	         * StandardAnalyzer by default eliminates stop words.
	     */
	     
	    StandardAnalyzer analyzer = new StandardAnalyzer();

	    // Create the index
	    Directory index = new RAMDirectory();

	    IndexWriterConfig config = new IndexWriterConfig(Version.LUCENE_4_10_0, analyzer);

	    IndexWriter w = new IndexWriter(index, config);
	    addDoc(w, "Lucene in Action", "193398817");
	    addDoc(w, "Lucene for Dummies", "55320055Z");
	    addDoc(w, "Managing Gigabytes", "55063554A");
	    addDoc(w, "The Art of Computer Science", "9900333X");
	    w.close();

	    // query
	    String querystr = args.length > 0 ? args[0] : "lucene";

	    
	    Query q = new QueryParser("title", analyzer).parse(querystr);
	    

	    // search
	    int hitsPerPage = 10;
	    IndexReader reader = DirectoryReader.open(index);
	    IndexSearcher searcher = new IndexSearcher(reader);
	    TopScoreDocCollector collector = TopScoreDocCollector.create(hitsPerPage, true);
	    searcher.search(q, collector);
	    ScoreDoc[] hits = collector.topDocs().scoreDocs;
	    
	    // 4. display results
	    System.out.println("Found " + hits.length + " hits.");
	    for(int i=0;i<hits.length;++i) {
	      int docId = hits[i].doc;
	      Document d = searcher.doc(docId);
	      System.out.println((i + 1) + ". " + d.get("isbn") + "\t" + d.get("title"));
	    }

	    // reader can only be closed when there
	    // is no need to access the documents any more.
	    reader.close();
	  }

	  private static void addDoc(IndexWriter w, String title, String isbn) throws IOException {
	    Document doc = new Document();
	    /**
	     * TextField doesn't mean that value part has to be String alone. The value part can be :
	     * java.io.Redaer and org.apache.lucene.analysis.TokenStream
	     */
	    doc.add(new TextField("title", title, Field.Store.YES));

	    // use a string field for isbn because we don't want it tokenized
	    doc.add(new TextField("isbn", isbn, Field.Store.NO));
	    w.addDocument(doc);
	  }
}
