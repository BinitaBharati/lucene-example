package bharati.binita.apache.lucene.example;

import java.io.File;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

public class SimpleSearcher {
    
    public static void main(String[] args) throws Exception {
        
        File indexDir = new File("/Users/bbharati/personal/projects/eclipseWS/lucene-example/index");
        String query = "drools";
        int hits = 100;
        
        SimpleSearcher searcher = new SimpleSearcher();
        searcher.searchIndex(indexDir, query, hits);
        
    }
    
    private void searchIndex(File indexDir, String queryStr, int maxHits) 
            throws Exception {
        
        Directory directory = FSDirectory.open(indexDir);
        DirectoryReader indexReader = DirectoryReader.open(directory);
        
        IndexSearcher indexSearcher = new IndexSearcher(indexReader);
        
        Term term = new Term("contents");
        Query termQuery = new TermQuery(term);
        
       // indexSearcher.search(query, results)
        

        
        /*TopDocs topDocs = searcher.search(query, maxHits);
        
        ScoreDoc[] hits = topDocs.scoreDocs;
        for (int i = 0; i < hits.length; i++) {
            int docId = hits[i].doc;
            Document d = searcher.doc(docId);
            System.out.println(d.get("filename"));
        }
        
        System.out.println("Found " + hits.length);*/
        
    }

}

