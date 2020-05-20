package io.pipin.example;

import io.pipin.core.convert.FlatAndFilterConverter;
import io.pipin.core.ext.AbstractConverter;
import io.pipin.core.ext.Converter;
import io.pipin.core.settings.ConvertSettings;
import org.bson.Document;
import org.bson.types.ObjectId;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by libin on 2020/4/21.
 */
public class BooksConverter extends AbstractConverter {
    private Document partner = new Document();
    private Document shop = new Document();
    private String entity = "";
    public BooksConverter(ConvertSettings convertSettings) {
        super(convertSettings);
        partner.put("$ref","akstore_partner");
        partner.put("$id",new ObjectId("58054ade44f4307b19a89f4f"));
        shop.put("$ref","akstore_shop");
        shop.put("$id",new ObjectId("5e9c148f60b29a96b59dbdfa"));
        entity = convertSettings.defaultEntity();
    }

    @Override
    public Map<String, Map<String, Object>> convert(Map<String, Object> doc) {


        Map<String, Object> baseDoc = new Document();
        baseDoc.put("partner",partner);
        baseDoc.put("shop",shop);
        baseDoc.put("name",doc.get("图书名称"));

        baseDoc.put("stock",doc.get("库存数"));
        baseDoc.put("model",doc.get("ISBN"));
        baseDoc.put("sn",doc.get("条码"));
        baseDoc.put("fullPrice",doc.get("定价"));
        baseDoc.put("cat0",new Document("name",doc.get("类别名称")));


        baseDoc.put("shortDescription",doc.get("出版社"));
        baseDoc.put("remark",doc.get("作者"));
        baseDoc.put("attr0",doc.get("开本"));
        baseDoc.put("attr1",doc.get("出版年月"));
        baseDoc.put("attr3",doc.get("印次"));
        baseDoc.put("attr4",doc.get("版次"));



        baseDoc.put("stockUpdateTime",new Date());
        baseDoc.put("ISBN",doc.get("ISBN"));

        HashMap<String, Map<String, Object>> result = new HashMap<String, Map<String, Object>>();
        result.put(entity, baseDoc);
        return result;
    }
}
