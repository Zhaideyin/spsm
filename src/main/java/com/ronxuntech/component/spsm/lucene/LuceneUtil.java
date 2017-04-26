package com.ronxuntech.component.spsm.lucene;

import com.ronxuntech.entity.Page;
import com.ronxuntech.util.PageData;
import com.ronxuntech.util.PathUtil;
import org.apache.lucene.analysis.TokenStream;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.search.highlight.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.store.SimpleFSDirectory;
import org.apache.lucene.util.QueryBuilder;
import org.wltea.analyzer.lucene.IKAnalyzer;

import java.io.File;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

/**
 * luceneutil 创建索引工具类
 *
 * @author angrl
 */

public class LuceneUtil {
    // 默认index_dir 目录
    private static String INDEX_DIR = PathUtil.getClasspath()+ "/index";// 索引存放目录
//    private static String INDEX_DIR =PathUtil.getClasspath()+"\\index";
    private static Directory directory = null;

    /**
     * 初始化存放索引目录
     *
     * @return
     * @throws Exception
     */
    public static Directory initLuceneDirctory() throws Exception {
        if (directory == null) {
            /* 这里放索引文件的位置 */
            File indexDir = new File(INDEX_DIR);
            //目录如果不存在创建目录
            if(indexDir.exists()){
                indexDir.mkdirs();
            }

            directory = FSDirectory.open(indexDir.toPath());
        }
        return directory;
    }

    /**
     * 创建索引
     *
     * @param pagedataList  创建索引的数据对象集
     * @param fieldNameList 创建索引属性
     * @throws Exception
     */
    public static void createIndex(List<PageData> pagedataList, List<String> fieldNameList) throws Exception {
        // 创建索引写入器
        IndexWriter indexWriter = new IndexWriter(initLuceneDirctory(), new IndexWriterConfig(new IKAnalyzer()));
        for (int i = 0; i < pagedataList.size(); i++) {
            // 创建document 对象。
            Document doc = new Document();
            // 通过传递来的字段 来对 对应的值进行索引
            for (int j = 0; j < fieldNameList.size(); j++) {
                // 将一条记录放入ｄｏｃ对象中
                doc.add(new Field(fieldNameList.get(j), pagedataList.get(i).getString(fieldNameList.get(j)),
                        TextField.TYPE_STORED));
                // 将添加的索引提交
            }
            indexWriter.addDocument(doc);
//            indexWriter.commit();
        }
        indexWriter.commit();
        indexWriter.close();
    }


    /**
     * 检索索引
     *
     * @param queryString   关键字
     * @param fieldNameList 搜索字段列表
     * @param page
     * @return
     * @throws Exception
     */

    public static List<PageData> search(String queryString, List<String> fieldNameList, Page page) throws Exception {
        // 指定查询索引的路径
        Directory dir = new SimpleFSDirectory(new File(INDEX_DIR).toPath());
//        Directory dir = new SimpleFSDirectory(new File("/usr/idea/workspace/spsm_console/target/spsm/index").toPath());
        // dir读取器
        DirectoryReader irReader = DirectoryReader.open(dir);
        // 创建索引查询器
        IndexSearcher indexSearcher = new IndexSearcher(irReader);
        // 创建查询解析器
        QueryBuilder parser = new QueryBuilder(new IKAnalyzer(true));

        // 设置高亮格式
        Formatter formatter = new SimpleHTMLFormatter("<font color='red'>", "</font>");
        // 用于去重
        List<Integer> idList = new ArrayList<>();
        // 用于存放高亮结果
        List<PageData> resultList = new ArrayList<>();
        for (int i = 0; i < fieldNameList.size(); i++) {
            Query query = parser.createPhraseQuery(fieldNameList.get(i), queryString);

            Scorer scorer = new QueryScorer(query);
            Highlighter highlighter = new Highlighter(formatter, scorer);
            //关键字附近的总字数
            highlighter.setTextFragmenter(new SimpleFragmenter(150));

            // 取出查询结果 search 中的 第二个int 参数是指，查询结果中取前面的多少条，应该大于0 ，如果是1， 则只会取第一条，
            TopDocs topDocs = indexSearcher.search(query, page.getTotalResult());

            if (topDocs.totalHits <= 0) {
                continue;
            }
            //设置总条数
            if(topDocs.totalHits <= page.getTotalResult()){
                page.setTotalResult(topDocs.totalHits);
            }
            int begin = page.getShowCount() * (page.getCurrentPage() - 1);
            // 取命中的长度和传递的得到的条数中最小的值作为结束
            int end = Math.min(page.getCurrentPage() * page.getShowCount(), topDocs.scoreDocs.length);
            ScoreDoc[] docs = topDocs.scoreDocs;
            //当取得结果 大于或者等于 分页总数
            if (resultList.size() >= page.getShowCount()) {
                break;
            }
            // 遍历
            for (int k = begin; k < end; k++) {
                // 得到查询到的信息 的序号
                int docId = docs[k].doc;
                // 将id 放入集合中，如果已经存在了，则执行下一个。
                if (idList.contains(docId)) {
                    continue;
                } else {
                    idList.add(docId);
                }
                // 通过indexSeacher 用id 查对应的document 对象。
                Document doc = indexSearcher.doc(docId);

                PageData pd = new PageData();
                // 循环将每个对应的字段的值传递过来。
                for (int j = 0; j < fieldNameList.size(); j++) {
                    String temp = doc.get(fieldNameList.get(j));
                    String fieldName = fieldNameList.get(j);
                    TokenStream tokenStream = parser.getAnalyzer().tokenStream(fieldName, new StringReader(temp));
                    // 设置高亮
                    String newContent = highlighter.getBestFragment(tokenStream, temp);
                    if (newContent == null) {
                        newContent = doc.get(fieldName);
                    }
                    pd.put(fieldNameList.get(j), newContent);
                    // tokenStream.reset();
                }
                // 将转换成高亮的结果放入resultList中。
                resultList.add(pd);
            }
        }
        return resultList;
    }

    /**
     * 查询
     *
     * @param resultList
     * @param fieldNameList
     * @throws Exception
     */
  /*  public static void showDocContent(List<PageData> resultList, List<String> fieldNameList) throws Exception {
        for (int i = 0; i < resultList.size(); i++) {
            for (int j = 0; j < fieldNameList.size(); j++) {
                System.out.println(resultList.get(i).getString(fieldNameList.get(j)));
            }
        }
    }*/

    /**
     * 设置高亮
     *
//     * @param topDocsList
     * @throws Exception
     */
    /*public List<PageData> getDocInfo(List<String> fieldNameList, List<TopDocs> topDocsList) throws Exception {
        Directory dir = new SimpleFSDirectory(new File(INDEX_DIR).toPath());
        // dir读取器
        DirectoryReader irReader = DirectoryReader.open(dir);
        // 创建索引查询器
        IndexSearcher indexSearcher = new IndexSearcher(irReader);

        List<PageData> resultList = new ArrayList<>();
        for (int i = 0; i < topDocsList.size(); i++) {
            TopDocs topDocs = topDocsList.get(i);
            for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
                int docId = scoreDoc.doc;
                Document doc = indexSearcher.doc(docId);
                PageData pd = new PageData();
                // 循环将每个对应的字段的值传递过来。
                for (int j = 0; j < fieldNameList.size(); j++) {
                    pd.put(fieldNameList.get(j), doc.get(fieldNameList.get(j)));
                }
                System.out.println("pd:" + pd.toString());
                resultList.add(pd);
            }
        }
        return resultList;
    }*/

   /* public static void main(String[] args) throws Exception {
        // LuceneUtil luceneUtil = new LuceneUtil();
        List<PageData> pagedataList = new ArrayList<>();
        PageData pd = new PageData();
        pd.put("id", "5");
        pd.put("title", "百度一下");
        pd.put("content", "<html> <body><a href='http://baidu.com' >百度一下你就知道了1百度百度1百度百度百度一</a></body></html>");
        pagedataList.add(pd);

        PageData pd2 = new PageData();
        pd2.put("id", "6");
        pd2.put("title", "百度一下");
        pd2.put("content", "<html> <body><a href='http://baidu.com' >爱的一天永恒还是瞬间，让我们平凡相恋</a></body></html>");
        pagedataList.add(pd2);

        PageData pd3 = new PageData();
        pd3.put("id", "7");
        pd3.put("title", "百度一下");
        pd3.put("content", "<html> <body><a href='http://baidu.com' >我期待有一天，我独自站在海边</a></body></html>");
        pagedataList.add(pd3);

        List<String> fieldNameList = new ArrayList<>();
        fieldNameList.add("id");
        fieldNameList.add("title");
        fieldNameList.add("content");

//         LuceneUtil.createIndex(pagedataList, fieldNameList);

        String queryString = "热研";

        Page page = new Page(3, 1);
//		page.setCurrentPage(1);
        page.setTotalPage(5);
        page.setTotalResult(10);
        List<PageData> resultList = LuceneUtil.search(queryString, fieldNameList, page);
        System.out.println(resultList.size());
//        LuceneUtil.showDocContent(resultList, fieldNameList);

    }*/

}
