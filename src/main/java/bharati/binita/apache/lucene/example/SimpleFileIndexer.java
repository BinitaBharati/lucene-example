package bharati.binita.apache.lucene.example;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.apache.lucene.analysis.core.SimpleAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.index.IndexableField;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

public class SimpleFileIndexer {
    
    public static void main(String[] args) throws Exception {
        
        File indexDir = new File("/Users/bbharati/personal/projects/eclipseWS/lucene-example/index");
        File dataDir = new File("/Users/bbharati/personal/projects/eclipseWS/binita-drools-storm");
        String suffix = "java";
        
        SimpleFileIndexer indexer = new SimpleFileIndexer();
        
        int numIndex = indexer.index(indexDir, dataDir, suffix);
        
        System.out.println("Total files indexed " + numIndex);
        
    }
    
    private int index(File indexDir, File dataDir, String suffix) throws Exception {
        
        /** Lucene40 
         * Whats a Analyzer ?
         * Input text needs to be analyzed before it can be indexed.
         * One eg of analysis would be too eliminate stop words like "the", "a" etc.
         * Such words do no add any value during search, and hence should not be indexed.
         * StandardAnalyzer by default eliminates stop words.
         * 
         */
    	IndexWriterConfig indexWriterConfig = new IndexWriterConfig(Version.LUCENE_4_10_0, new StandardAnalyzer());
    	indexWriterConfig.setOpenMode(OpenMode.CREATE_OR_APPEND);
    	IndexWriter indexWriter = new IndexWriter(FSDirectory.open(indexDir), indexWriterConfig);
        
        indexDirectory(indexWriter, dataDir, suffix);
        
        int numIndexed = indexWriter.maxDoc();
        indexWriter.close();
        
        return numIndexed;
        
    }
    
    private void indexDirectory(IndexWriter indexWriter, File dataDir, 
           String suffix) throws IOException {

        File[] files = dataDir.listFiles();
        for (int i = 0; i < files.length; i++) {
            File f = files[i];
            if (f.isDirectory()) {
                indexDirectory(indexWriter, f, suffix);
            }
            else {
                indexFileWithIndexWriter(indexWriter, f, suffix);
            }
        }

    }
    
    private void indexFileWithIndexWriter(IndexWriter indexWriter, File f, 
            String suffix) throws IOException {

        if (f.isHidden() || f.isDirectory() || !f.canRead() || !f.exists()) {
            return;
        }
        if (suffix!=null && !f.getName().endsWith(suffix)) {
            return;
        }
        System.out.println("Indexing file " + f.getCanonicalPath());
        
        Document doc = new Document();
        doc.add(new TextField("contents", new FileReader(f)));
        doc.add(new Field("filename", f.getCanonicalPath(), 
           Field.Store.YES, Field.Index.ANALYZED));
        
        indexWriter.addDocument(doc);

    }

}