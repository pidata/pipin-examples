package io.pipin.example;

import akka.actor.ActorSystem;
import io.pipin.core.poll.SimpleTraversal;
import io.pipin.core.settings.PollSettings;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.Logger;

import java.util.Map;

/**
 * Created by libin on 2020/5/20.
 */
public class WechatReportTraversal extends SimpleTraversal{
    public WechatReportTraversal(String uri, String pageParameter, int pageStartFrom, PollSettings pollSettings, ActorSystem actorSystem, Logger log) {
        super(uri, pageParameter, pageStartFrom, pollSettings, actorSystem, log);
    }

    @Override
    public Map<String, String> extraParamsMap() {
        Map<String, String> params = super.extraParamsMap();
        params.put("access_token","33_xxx");
        return super.extraParamsMap();
    }

    @Override
    public String getBody(Map<String, String> extraParams) {
        DateTimeFormatter dateTimeFormatter = DateTimeFormat.forPattern("yyyy-MM-dd");
        DateTime dateEnd = DateTime.now().withTimeAtStartOfDay().minusDays(1);
        DateTime dateStart = dateEnd.minus(7);

        return "{ \n" +
                "    \"begin_date\": \""+ dateEnd.toString(dateTimeFormatter) +"\", \n" +
                "    \"end_date\": \""+ dateEnd.toString(dateTimeFormatter) +"\"\n" +
                "}";
    }
}
