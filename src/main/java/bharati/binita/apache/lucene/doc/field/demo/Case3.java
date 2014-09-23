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
 * Demonstration of the un-tokenized org.apache.lucene.document.Field Subclasses like the StringField. 
 * (org.apache.lucene.document.TextField is tokenized and org.apache.lucene.document.StringField is un-tokenized).
 * 
 * BTW, un-tokenized is useful only when the field text is a single word. Then tokenization is anyway not required.
 * So, to eliminate the overhead of tokenization , StringField can be used, instead of TextField.
 * 
 *
 * 
 *
 */

public class Case3 {
	
	public static void main(String[] args) throws Exception {
		  
	    /** http://stackoverflow.com/questions/650643/lucene-indexing-store-and-indexing-modes-explained
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
	    addDoc(w, "abc", "193398817");
	  
	    w.close();

	    // query
	    String querystr = args.length > 0 ? args[0] : "Managing Gigabytes";

	    System.out.println("queryStr1 = "+querystr);
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
	    doc.add(new StringField("title", title, Field.Store.YES));

	    // use a string field for isbn because we don't want it tokenized
	    doc.add(new StringField("isbn", isbn, Field.Store.YES));
	    w.addDocument(doc);
	  }
}
