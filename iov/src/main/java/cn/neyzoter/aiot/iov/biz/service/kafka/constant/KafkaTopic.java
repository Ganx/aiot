package cn.neyzoter.aiot.iov.biz.service.kafka.constant;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * kafka topic
 * @author Neyzoter Song
 * @date 2020-2-8
 */
@Component
public class KafkaTopic {
    public final static String TOPIC_VEHICLE_HTTP_PACKET_NAME = "VehicleHttpPack";

    @Bean
    public NewTopic TOPIC_VEHICLE_HTTP_PACKET () {
        return new NewTopic(KafkaTopic.TOPIC_VEHICLE_HTTP_PACKET_NAME,3,(short)2);
    }
}
