package io.pipin.example;

import akka.actor.ActorSystem;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;
import io.pipin.core.poll.SimpleTraversal;
import io.pipin.core.poll.TokenAuthorizator;
import io.pipin.core.settings.PollSettings;
import org.bson.Document;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

/**
 * Created by libin on 2020/3/22.
 */
public class CustomPageableTraversal extends SimpleTraversal {

    private String pageToken = "";

    public CustomPageableTraversal(String uri, String pageParameter, int pageStartFrom, PollSettings pollSettings, ActorSystem actorSystem, Logger log) {
        super(uri, pageParameter, pageStartFrom, pollSettings, actorSystem, log);
    }

    /***
     * 组装POST请求中的BODY
     * @return POST请求中的BODY
     */
    @Override
    public String getBody(Map<String,String> extraParamsMap) {
        return "{\n" +
                "  \"_projectId\": \""+extraParamsMap.get("_projectId")+"\",\n" +
                "  \"pageToken\": \""+pageToken+"\",\n" +
                "  \"pageSize\": 1000\n" +
                "}";
    }

    /***
     * 额外的参数，比如分页参数
     * @return http参数
     */
    @Override
    public Map<String, String> extraParamsMap() {
        return super.extraParamsMap();
    }

    /***
     * 指定HTTP headers
     * @return headers
     */
    @Override
    public String[][] headers() {
        Map<String,String> extraSettings = extraParamsMap();
        return new String[][]{{"Content-Type","application/json"},
                {"X-Tenant-Id",extraSettings.get("tenantId")},
                {"X-Tenant-Type","organization"}};
    }

    /***
     * 生成请求中的所需要的token
     * @return TokenAuthorizator
     */
    @Override
    public TokenAuthorizator getTokenAuthorizator() {
        return new TokenAuthorizator(){
            public String getToken() {
                return "11111";
            }
        };
    }


    /***
     * 结果【列表】所在的字段
     * @return 字段名
     */
    @Override
    public String getContentField() {
        return "result";
    }


    /***
     * 判断是否为最后一页的依据
     * @return 是否为最后一页
     */
    @Override
    public boolean endPage(Document doc) {
        String nextPageToken = doc.getString("nextPageToken");
        return null==nextPageToken || "".equals(nextPageToken);
    }

    /***
     * 进入下一页前的回调
     *
     */
    @Override
    public void onPageNext(Document doc, Map<String, String> params) {
        pageToken = doc.getString("nextPageToken");
        params.put("pageToken", pageToken);
        super.onPageNext(doc, params);
    }

    @Override
    public Map<String, String>[] extraParamsBatch() {
        ArrayList<Map<String, String>> batch = new ArrayList<>();
        MongoClient mongoClient = new  MongoClient("localhost", 27017);
        MongoDatabase database = mongoClient.getDatabase("pipin");
        database.getCollection("project").find().forEach((Consumer<Document>) document -> {
            Map<String,String> result = new HashMap<>(1);
            result.putAll(extraParamsMap());
            result.put("_projectId",document.getString("projectId"));
            batch.add(result);
        });

        return (Map<String, String>[])batch.toArray(new Map[0]);
    }
}
