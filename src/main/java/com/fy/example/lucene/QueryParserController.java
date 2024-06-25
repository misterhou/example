package com.fy.example.lucene;

import org.apache.lucene.analysis.Analyzer;
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
import org.apache.lucene.search.similarities.BM25Similarity;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;


@RestController
@RequestMapping("/lucene")
public class QueryParserController {

    @GetMapping("/query-parser")
    public String queryParser() {

        try {
            // 使用Lucene的BM25相似度评分模型
            BM25Similarity similarity = new BM25Similarity();

            // 创建一个IndexSearcher对象
            IndexReader reader = DirectoryReader.open(FSDirectory.open(Paths.get("E:\\index")));
            IndexSearcher searcher = new IndexSearcher(reader);
            searcher.setSimilarity(similarity);

            // 创建QueryParser对象
            QueryParser parser = new QueryParser("substationName", new MyIKAnalyzer());

            // 设置模糊查询的最大编辑距离
//            parser.setFuzzyMinSim(0.5f); // 0.5是模糊查询时相似度的最小值

            // 进行模糊查询
            Query query = parser.parse("substationName:110^2.0 OR 打开剧村站联络图"); // "~"表示启用模糊查询
            ScoreDoc[] hits = searcher.search(query, 10).scoreDocs;

            // 输出查询结果
            for (ScoreDoc hit : hits) {
                int docId = hit.doc;
                Document document = searcher.doc(docId);
                System.out.println(document.get("substationName"));
            }

            // 关闭reader
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "query-parser";
    }

    @GetMapping("/index")
    public String index() {
        String indexPath = "E:\\index";
        try {
            Map<String, String> data = new HashMap<>();
            data.put("打开剧村站联络图", "01121300001000");
            data.put("打开剧村站接线图", "01121300001001");
            data.put("关闭剧村站接线图", "01121300001002");
            data.put("关闭剧村站联络图", "01121300001003");
            data.put("关闭110kV剧村站联络图", "01121300001004");
            Directory dir = FSDirectory.open(Paths.get(indexPath));
            Analyzer analyzer = new MyIKAnalyzer();
            IndexWriterConfig iwc = new IndexWriterConfig(analyzer);
            iwc.setOpenMode(IndexWriterConfig.OpenMode.CREATE);
            IndexWriter writer = new IndexWriter(dir, iwc);
            indexDoc(writer, data);
            writer.commit();
            writer.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return "index";
    }

    static void indexDoc(IndexWriter writer, Map<String, String> data) throws IOException {
        for (String key : data.keySet()) {
            Document doc = new Document();
            Field substationName = new TextField("substationName", key, Field.Store.YES);
            doc.add(substationName);
            doc.add(new StringField("substationCode", data.get(key), Field.Store.YES));

            writer.addDocument(doc);
        }
    }
}
